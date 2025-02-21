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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private IncidentTypeRepository incidentTypeRepository;

    @CacheEvict(value = "incidents", allEntries = true)
    public Incident registerIncident(RequestIncidentBody requestIncidentBody) throws BadRequestException {
        Optional<Route> routeOptional = routeRepository.findByName(requestIncidentBody.getRouteName());
        Route route = routeOptional.orElseThrow(()-> new BadRequestException("Invalid route name"));

        Optional<IncidentType> optIncidentType = incidentTypeRepository.findByName(requestIncidentBody.getType());
        IncidentType incidentType = optIncidentType.orElseThrow(()->new BadRequestException("Invalid incident name"));

        if(requestIncidentBody.getKilometer() > route.getDistance()){
            throw new BadRequestException("Invalid distance");
        }

        Incident incident = mapear(requestIncidentBody,route,incidentType);

        return incidentRepository.save(incident);
    }

    private Incident mapear(RequestIncidentBody requestIncidentBody,Route route,IncidentType incidentType) {
        return new Incident(null,route, requestIncidentBody.getKilometer(), incidentType,LocalDateTime.now(),requestIncidentBody.getComments());
    }


    @CacheEvict(value = "incidents", allEntries = true)
    public boolean deleteIncident(String id) {
        if (incidentRepository.existsById(id)) {
            incidentRepository.deleteById(id);
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



    //@Cacheable(value = "incidents", key = "#routeName + #kmInit")
    public RouteSegmentDTO getRouteSegment(String routeId, double kmStart) {
        double kmEnd = kmStart + 100;

        ObjectId routeObjectId = new ObjectId(routeId); // Convertir routeId a ObjectId

        // Consultar incidentes directamente desde MongoDB
        List<IncidentDTO> incidents = incidentRepository.findIncidentsInRange(routeObjectId, kmStart, kmEnd)
                .stream().map(IncidentDTO::new).collect(Collectors.toList());

        // Consultar intersecciones directamente desde MongoDB
        List<IntersectionDTO> intersections = routeRepository.findIntersectionsInRange1(routeId, kmStart, kmEnd);

        return new RouteSegmentDTO(routeId, kmStart, kmEnd, incidents, intersections);
    }

}
