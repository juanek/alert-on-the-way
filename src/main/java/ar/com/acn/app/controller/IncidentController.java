package ar.com.acn.app.controller;

import ar.com.acn.app.model.Incident;
import ar.com.acn.app.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    @Autowired
    private IncidentService incidentService;

    @PostMapping("/register")
    public Incident registerIncident(@RequestBody Incident incident) {
        return incidentService.registerIncident(incident);
    }

    @GetMapping("/consult")
    public List<Incident> consultIncidents(@RequestParam String routeId, @RequestParam double kmInit) {
        return incidentService.consultIncidents(routeId, kmInit);
    }
}
