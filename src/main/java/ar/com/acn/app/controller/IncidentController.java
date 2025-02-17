package ar.com.acn.app.controller;

import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Incident> consultIncidents(@RequestParam String name, @RequestParam double kmInit) {
       return incidentService.consultIncidents(name, kmInit);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIncident(@PathVariable String id) {
        boolean deleted = incidentService.deleteIncident(id);
        if (deleted) {
            return ResponseEntity.ok("Incident deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incident not found");
        }
    }

    @GetMapping("/route-report")
    public List<RouteReport> getRouteReport(@RequestParam String routeId) {
        return incidentService.getRouteReport(routeId);
    }
}
