package ar.com.acn.app.controller;

import ar.com.acn.app.dto.IncidentDTO;
import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.dto.RouteSegmentDTO;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.RouteRepository;
import ar.com.acn.app.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    @Autowired
    private IncidentService incidentService;

    @Autowired
    RouteRepository routeRepository;

    @PostMapping("/register")
    public Incident registerIncident(@RequestBody Incident incident) {
        return incidentService.registerIncident(incident);
    }

    @GetMapping("/consult")
    public List<IncidentDTO> consultIncidents(@RequestParam String name, @RequestParam double kmInit) {
        return incidentService.consultIncidents(name, kmInit);
    }

    @GetMapping("/incidents")
    public RouteSegmentDTO getIncidents(@RequestParam String name, @RequestParam double kmInit) {
        System.out.println("name --> "+name);
        Optional<Route> optionalRoute = routeRepository.findByName(name);

        Route route = optionalRoute.orElseThrow(() -> new RuntimeException("Invalid route name"));
        System.out.println("route "+route.getId());
        return incidentService.getRouteSegment(route.getId(), kmInit);
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
