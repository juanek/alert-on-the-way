package ar.com.acn.app.service;

import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.repository.IncidentRepository;
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

    @CacheEvict(value = "incidents", allEntries = true)
    public Incident registerIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    @Cacheable(value = "incidents", key = "#name + #kmInit")
    public List<Incident> consultIncidents(String name, double kmInit) {
        return incidentRepository.findIncidentsInRange(name, kmInit, kmInit + 100);
    }

    @CacheEvict(value = "incidents", allEntries = true)
    public boolean deleteIncident(String id) {
        Optional<Incident> incident = incidentRepository.findById(id);
        if (incident.isPresent()) {
            incidentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RouteReport> getRouteReport(String routeId) {
        List<Incident> incidents = incidentRepository.findByRouteId(routeId);

        // Mapa para agrupar incidentes por tramo de 100KM
        Map<Integer, Integer> severityBySegment = new HashMap<>();

        for (Incident incident : incidents) {
            int segment = (int) (incident.getKilometer() / 100) * 100;
            int severity = getSeverity(incident.getType());

            severityBySegment.put(segment, severityBySegment.getOrDefault(segment, 0) + severity);
        }

        // Convertir a lista y ordenar de mayor a menor gravedad
        return severityBySegment.entrySet().stream()
                .map(entry -> new RouteReport(routeId, entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(RouteReport::getTotalSeverity).reversed())
                .collect(Collectors.toList());
    }

    // Método auxiliar para asignar gravedad a cada tipo de incidente
    private int getSeverity(String type) {
        return switch (type) {
            case "Accidente" -> 5;
            case "Incendio" -> 4;
            case "Control Policial" -> 3;
            case "Bache" -> 2;
            case "Neblina" -> 1;
            default -> 0;
        };
    }
}
