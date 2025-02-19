package ar.com.acn.app;

import ar.com.acn.app.dto.IntersectionDTO;
import ar.com.acn.app.dto.RouteSegmentDTO;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.IncidentType;
import ar.com.acn.app.model.Intersection;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.IncidentTypeRepository;
import ar.com.acn.app.repository.RouteRepository;
import ar.com.acn.app.service.IncidentService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
public class RouteServiceTest {

    @Autowired
    private IncidentService routeService;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentTypeRepository incidentTypeRepository;

    @BeforeEach
    public void setUp() {
        // Limpiar las colecciones antes de insertar nuevos datos
        routeRepository.deleteAll();
        incidentRepository.deleteAll();
        incidentTypeRepository.deleteAll();

        // Crear y guardar los tipos de incidentes
        IncidentType accident = incidentTypeRepository.save(new IncidentType(null, "Accidente", 3));
        IncidentType pothole = incidentTypeRepository.save(new IncidentType(null, "Bache", 1));
        IncidentType policeControl = incidentTypeRepository.save(new IncidentType(null, "Control Policial", 2));
        IncidentType fire = incidentTypeRepository.save(new IncidentType(null, "Incendio", 3));
        IncidentType fog = incidentTypeRepository.save(new IncidentType(null, "Neblina", 1));
        IncidentType picket = incidentTypeRepository.save(new IncidentType(null, "Piquete", 2));
        IncidentType animals = incidentTypeRepository.save(new IncidentType(null, "Animales", 2));
        IncidentType trafficCamera = incidentTypeRepository.save(new IncidentType(null, "Fotomulta", 1));


        // Crear y guardar rutas
        Route route1 = new Route(null, "Route 1", "City A", "City B", 300, new ArrayList<>());
        Route route2 = new Route(null, "Route 2", "City C", "City D", 150, new ArrayList<>());

        route1 = routeRepository.save(route1);
        route2 = routeRepository.save(route2);

        // Crear intersección en el Km 120
        route1.setIntersections(List.of(new Intersection(route2.getId(), 120)));
        routeRepository.save(route1);

        // Crear incidentes en el tramo
        Incident incident1 = new Incident(null, route1, 50, accident, LocalDateTime.now(), "Choque múltiple");
        Incident incident2 = new Incident(null, route1, 130, pothole, LocalDateTime.now(), "Bache grande en la derecha");

        incidentRepository.saveAll(List.of(incident1, incident2));
    }

    @Test
    public void testGetRouteSegment() {

        List<Route> allRoutes = routeRepository.findAll();
        allRoutes.forEach(System.out::println);

        List<Incident> allIncidents = incidentRepository.findAll();
        allIncidents.forEach(System.out::println);

        List<Incident> incidents = incidentRepository.findIncidentsInRange(
                new ObjectId(allIncidents.get(0).getRoute().getId()),
                50,
                150
        );

        System.out.println("incidents "+incidents);

        double kmStart = 50;
        double kmEnd = kmStart + 100;
        String routeId = allIncidents.get(0).getRoute().getId(); // O el ID de la ruta que estás probando
        System.out.println(routeId);
        ObjectId routeObjectId = new ObjectId(routeId);
        List<IntersectionDTO> intersections = routeRepository.findIntersectionsInRange1(routeId, kmStart, kmEnd);

        System.out.println("Intersections: " + intersections);



        RouteSegmentDTO segment = routeService.getRouteSegment(allIncidents.get(0).getRoute().getId(), kmStart);

        System.out.println(segment);

        assertNotNull(segment);
        //assertEquals("Route 1", segment.getRouteId());
        assertEquals(kmStart, segment.getKmStart(), 0.1);
        assertEquals(kmStart + 100, segment.getKmEnd(), 0.1);

        // Verificar incidentes
        assertEquals(2, segment.getIncidents().size());

        // Verificar intersecciones
        assertEquals(1, segment.getIntersections().size());
        assertEquals(120, segment.getIntersections().get(0).getIntersectionKm(), 0.1);
        assertEquals("Route 2", segment.getIntersections().get(0).getIntersectingRouteName());
    }
}

