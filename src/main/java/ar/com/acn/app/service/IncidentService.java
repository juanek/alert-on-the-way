package ar.com.acn.app.service;

import ar.com.acn.app.dto.*;
import ar.com.acn.app.exception.BadRequestException;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.IncidentType;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.IncidentTypeRepository;
import ar.com.acn.app.repository.RouteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private IncidentTypeRepository incidentTypeRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int QUERY_THRESHOLD = 5;
    private static final Duration SHORT_TTL = Duration.ofMinutes(10);
    private static final Duration LONG_TTL = Duration.ofHours(1);


    @CacheEvict(value = "incidents", allEntries = true) // Invalida TODAS las entradas en incidents
    public Incident registerIncident(RequestIncidentBody requestIncidentBody) throws BadRequestException {
        Optional<Route> routeOptional = routeRepository.findByName(requestIncidentBody.getRouteName());
        Route route = routeOptional.orElseThrow(()-> new BadRequestException("Invalid route name"));

        Optional<IncidentType> optIncidentType = incidentTypeRepository.findByName(requestIncidentBody.getType());
        IncidentType incidentType = optIncidentType.orElseThrow(()->new BadRequestException("Invalid incident name"));

        if(requestIncidentBody.getKilometer() > route.getDistance()){
            throw new BadRequestException("Invalid distance");
        }

        Incident incident = mapear(requestIncidentBody,route,incidentType);

        Incident savedIncident = incidentRepository.save(incident);

        // Invalida cache solo para la ruta específica
        evictRouteCache(route.getId());

        return savedIncident;
    }

    private Incident mapear(RequestIncidentBody requestIncidentBody,Route route,IncidentType incidentType) {
        return new Incident(null,route, requestIncidentBody.getKilometer(), incidentType,LocalDateTime.now(),requestIncidentBody.getComments());
    }


    @CacheEvict(value = "incidents", allEntries = true)
    public boolean deleteIncident(String id) {
        Optional<Incident> incidentOpt = incidentRepository.findById(id);
        if (incidentOpt.isPresent()) {
            Incident incident = incidentOpt.get();
            incidentRepository.deleteById(id);

            // Invalida cache solo para la ruta específica
            evictRouteCache(incident.getRoute().getId());

            return true;
        }
        return false;
    }

    public RouteReportDTO getRouteReport(String routeName) throws BadRequestException {
        Optional<Route> routeOptional = routeRepository.findByName(routeName);
        Route route = routeOptional.orElseThrow(()-> new BadRequestException("Invalid route name"));
        List<RouteReport> reports = incidentRepository.getRouteReportByRouteId(route.getId());
        return new RouteReportDTO(routeName,reports);
    }

    private void evictRouteCache(String routeId) {
        String cacheKeyPattern = "incidents::" + routeId + "_*";
        Set<String> keys = redisTemplate.keys(cacheKeyPattern);
        if (keys != null) {
            for (String key : keys) {
                redisTemplate.delete(key);
            }
        }
        // También eliminar el contador de consultas
        redisTemplate.delete("route_query_count:" + routeId);
    }

    //@Cacheable(value = "incidents", key = "#routeId + '_' + #kmStart")
    public RouteSegmentDTO getRouteSegment(String routeId, double kmStart) {
        double kmEnd = kmStart + 100;
        String cacheKeyIncident = "incidents::" + routeId + "_"+kmStart;
        // Incrementa el contador de consultas en Redis antes de ir a la BD
        String counterKey = "route_query_count:" + routeId;

        Long expire = redisTemplate.getExpire(cacheKeyIncident, TimeUnit.SECONDS);
        System.out.println(" expire "+expire);

        Long queryCount = redisTemplate.opsForValue().increment(counterKey);

        if (queryCount == 1) {
            redisTemplate.expire(cacheKeyIncident, LONG_TTL); // Expirar en 1 hora
        }

        // Si supera el límite de consultas, cambiar TTL de la caché
        if (queryCount != null && queryCount > QUERY_THRESHOLD) {
            String cacheKeyPattern = "incidents::" + routeId + "_*";
            Set<String> keys = redisTemplate.keys(cacheKeyPattern);
            if (keys != null) {
                for (String key : keys) {
                    redisTemplate.expire(key, SHORT_TTL);
                }
            }
        }

        RouteSegmentDTO routeSegmentDTOCache = (RouteSegmentDTO) redisTemplate.opsForValue().get(cacheKeyIncident);
        if (routeSegmentDTOCache != null) {
            return routeSegmentDTOCache;
        }


        ObjectId routeObjectId = new ObjectId(routeId);
        List<IncidentDTO> incidents = incidentRepository.findIncidentsInRange(routeObjectId, kmStart, kmEnd)
                .stream().map(IncidentDTO::new).collect(Collectors.toList());

        List<IntersectionDTO> intersections = routeRepository.findIntersectionsInRange1(routeId, kmStart, kmEnd);

        RouteSegmentDTO routeSegmentDTO = new RouteSegmentDTO(routeId, kmStart, kmEnd, incidents, intersections);

        redisTemplate.opsForValue().set(cacheKeyIncident, routeSegmentDTO);

        return routeSegmentDTO;
    }


}
