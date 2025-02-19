package ar.com.acn.app.controller;

import ar.com.acn.app.dto.*;
import ar.com.acn.app.exception.BadRequestException;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIncident(@PathVariable String id) {
        boolean deleted = incidentService.deleteIncident(id);
        if (deleted) {
            return ResponseEntity.ok("Incident deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incident not found");
        }
    }

    @GetMapping("/incidents")
    public ResponseEntity<?> getIncidents(@RequestParam String name, @RequestParam double kmInit) {

        try {
            Optional<Route> optionalRoute = routeRepository.findByName(name);
            Route route = optionalRoute.orElseThrow(() -> new BadRequestException("Invalid route name"));
            if((kmInit + 100 ) > route.getDistance()){
                throw new BadRequestException("Invalid distance");
            }
            return new ResponseEntity<>(incidentService.getRouteSegment(route.getId(), kmInit),HttpStatus.OK);
        } catch (BadRequestException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/route-report")
    public ResponseEntity<?> getRouteReport(@RequestParam String name) {
        try {
            RouteReportDTO body = incidentService.getRouteReport(name);
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (BadRequestException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
