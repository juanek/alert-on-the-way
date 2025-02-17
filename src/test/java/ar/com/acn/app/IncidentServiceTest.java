package ar.com.acn.app;

import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {

    @InjectMocks
    private IncidentService incidentService;

    @Mock
    private IncidentRepository incidentRepository;

    @Test
    void testGetRouteReport() {
        // GIVEN: Simulamos incidentes en una ruta
        String routeId = "Ruta 1";
        List<Incident> mockIncidents = Arrays.asList(
                new Incident("1", routeId, 50, "Accidente", LocalDateTime.now(), "Colisión múltiple"),
                new Incident("2", routeId, 120, "Bache", LocalDateTime.now(), "Bache grande"),
                new Incident("3", routeId, 180, "Incendio", LocalDateTime.now(), "Fuego en la vía"),
                new Incident("4", routeId, 90, "Control Policial", LocalDateTime.now(), "Control de alcoholemia")
        );

        // WHEN: Se mockea la consulta de incidentes
        Mockito.when(incidentRepository.findByRouteId(routeId)).thenReturn(mockIncidents);

        // THEN: Se ejecuta el método de servicio
        List<RouteReport> report = incidentService.getRouteReport(routeId);

        System.out.println(report);

        // 📌 Verificamos que hay 2 tramos (0-99, 100-199)
        assertEquals(2, report.size());

        // 📌 Verificamos que los segmentos están ordenados de mayor a menor gravedad
        assertTrue(report.get(0).getTotalSeverity() >= report.get(1).getTotalSeverity());

        // 📌 Verificamos que la ruta devuelta es la correcta
        report.forEach(r -> assertEquals(routeId, r.getRouteId()));

        // 📌 Verificamos que el método del repositorio se llamó una vez
        Mockito.verify(incidentRepository, Mockito.times(1)).findByRouteId(routeId);
    }
}

