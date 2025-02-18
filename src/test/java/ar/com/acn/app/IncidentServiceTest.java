package ar.com.acn.app;

import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.IncidentType;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.RouteRepository;
import ar.com.acn.app.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {

    @InjectMocks
    private IncidentService incidentService;

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private RouteRepository routeRepository; // ✅ Se agrega el mock de RouteRepository

    @Test
    void testGetRouteReport() {
        // GIVEN: Creamos una ruta de prueba
        Route route = new Route("1", "Ruta 1", "Ciudad A", "Ciudad B", 300, new ArrayList<>());

        // Creamos distintos tipos de incidentes con severidad
        IncidentType accidente = new IncidentType("1", "Accidente", 5);
        IncidentType bache = new IncidentType("2", "Bache", 2);
        IncidentType incendio = new IncidentType("3", "Incendio", 4);
        IncidentType controlPolicial = new IncidentType("4", "Control Policial", 3);

        // Creamos incidentes con la nueva estructura
        List<Incident> mockIncidents = Arrays.asList(
                new Incident("1", route, 50, accidente, LocalDateTime.now(), "Colisión múltiple"),
                new Incident("2", route, 120, bache, LocalDateTime.now(), "Bache grande"),
                new Incident("3", route, 180, incendio, LocalDateTime.now(), "Fuego en la vía"),
                new Incident("4", route, 90, controlPolicial, LocalDateTime.now(), "Control de alcoholemia")
        );

        // ✅ Mockear la consulta de la ruta
        Mockito.when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));

        // ✅ Mockear la consulta de incidentes por ID de ruta
        Mockito.when(incidentRepository.findByRoute(route)).thenReturn(mockIncidents);

        // WHEN: Se ejecuta el método de servicio
        List<RouteReport> report = incidentService.getRouteReport(route.getId());

        System.out.println(report);

        // 📌 Verificamos que hay 2 tramos (0-99, 100-199)
        assertEquals(2, report.size());

        // 📌 Verificamos que los segmentos están ordenados de mayor a menor gravedad
        assertTrue(report.get(0).getTotalSeverity() >= report.get(1).getTotalSeverity());

        // 📌 Verificamos que la ruta devuelta es la correcta
        report.forEach(r -> assertEquals(route.getId(), r.getRouteId()));

        // 📌 Verificamos que el método del repositorio se llamó una vez
        Mockito.verify(incidentRepository, Mockito.times(1)).findByRoute(route);
    }
}
