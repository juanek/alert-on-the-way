package ar.com.acn.app.service;

import ar.com.acn.app.dto.IntersectionDTO;
import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.dto.RouteSegmentDTO;
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
import ar.com.acn.app.dto.IncidentDTO;

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

    @Cacheable(value = "incidents", key = "#routeName + #kmInit")
    public List<IncidentDTO> consultIncidents(String routeName, double kmInit) {

        Route route = routeRepository.findByName(routeName)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada: " + routeName));
        ObjectId routeObjectId = new ObjectId(route.getId()); // Convertir routeId a ObjectId

        List<Incident> incidents = incidentRepository.findIncidentsInRange(routeObjectId, kmInit, kmInit + 100);
        return incidents.stream().map(IncidentDTO::new).collect(Collectors.toList());

    }

    @CacheEvict(value = "incidents", allEntries = true)
    public boolean deleteIncident(String id) {
        if (incidentRepository.existsById(id)) {
            incidentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RouteReport> getRouteReport1(String routeName) {
        Optional<Route> routeOptional = routeRepository.findByName(routeName);
        if (routeOptional.isEmpty()) {
            System.out.println("No encontró la ruta!!!");
            return Collections.emptyList();
        }

        Route route = routeOptional.get();
        List<RouteReport> reports = incidentRepository.getRouteReportByRouteId(route.getId());

        // Imprimir JSON antes de devolver
        reports.forEach(report -> System.out.println(report));

        return reports;
    }


    public List<RouteReport> getRouteReport(String name) {
        Optional<Route> routeOptional = routeRepository.findByName(name);
        if (routeOptional.isEmpty()) {
            return Collections.emptyList();
        }

        Route route = routeOptional.get();
        List<Incident> incidents = incidentRepository.findByRoute(route);

        // Mapa para agrupar incidentes por tramo de 100KM
        Map<Integer, Integer> severityBySegment = new HashMap<>();

        for (Incident incident : incidents) {
            int segment = (int) (incident.getKilometer() / 100) * 100;
            int severity = incident.getType().getSeverity(); // Se usa el objeto IncidentType

            severityBySegment.put(segment, severityBySegment.getOrDefault(segment, 0) + severity);
        }

        // Convertir a lista y ordenar de mayor a menor gravedad
        return severityBySegment.entrySet().stream()
                .map(entry -> new RouteReport(name, entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(RouteReport::getTotalSeverity).reversed())
                .collect(Collectors.toList());
    }




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
