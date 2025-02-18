package ar.com.acn.app.service;

import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.RouteRepository;
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

        List<Incident> incidents = incidentRepository.findIncidentsInRange(route.getId(), kmInit, kmInit + 100);
        return incidents.stream().map(IncidentDTO::new).collect(Collectors.toList());
        //return incidentRepository.findIncidentsInRange(route.getId(), kmInit, kmInit + 100);
    }

    @CacheEvict(value = "incidents", allEntries = true)
    public boolean deleteIncident(String id) {
        if (incidentRepository.existsById(id)) {
            incidentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RouteReport> getRouteReport(String routeId) {
        Optional<Route> routeOptional = routeRepository.findById(routeId);
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
                .map(entry -> new RouteReport(routeId, entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(RouteReport::getTotalSeverity).reversed())
                .collect(Collectors.toList());
    }
}
