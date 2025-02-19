package ar.com.acn.app.service;

import ar.com.acn.app.dto.*;
import ar.com.acn.app.exception.BadRequestException;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.RouteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private RouteRepository routeRepository;

    @CacheEvict(value = "incidents", allEntries = true)
    public Incident registerIncident(Incident incident) {
        return incidentRepository.save(incident);
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
