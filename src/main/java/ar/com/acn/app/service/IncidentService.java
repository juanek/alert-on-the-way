package ar.com.acn.app.service;

import ar.com.acn.app.model.Incident;
import ar.com.acn.app.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IncidentService {
    @Autowired
    private IncidentRepository incidentRepository;

    @CacheEvict(value = "incidents", allEntries = true)
    public Incident registerIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    @Cacheable(value = "incidents", key = "#routeId + #kmInit")
    public List<Incident> consultIncidents(String routeId, double kmInit) {
        return incidentRepository.findByRouteIdAndKilometerBetween(routeId, kmInit, kmInit + 100);
    }
}
