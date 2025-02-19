package ar.com.acn.app;

import ar.com.acn.app.controller.IncidentController;
import ar.com.acn.app.dto.IncidentDTO;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.IncidentType;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentControllerTest {
    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private IncidentController incidentController;

    private Incident incident;
    private Route route;
    private IncidentType incidentType;

    @BeforeEach
    void setUp() {
        route = new Route("1", "Ruta 1", "Ciudad A", "Ciudad B", 300, Arrays.asList());
        incidentType = new IncidentType("1", "Accidente", 5);
        incident = new Incident("1", route, 50, incidentType, LocalDateTime.now(), "Colisión múltiple");
    }

    @Test
    void testRegisterIncident() {
        when(incidentService.registerIncident(any(Incident.class))).thenReturn(incident);

        Incident result = incidentController.registerIncident(incident);

        assertNotNull(result);
        assertEquals("Ruta 1", result.getRoute().getName());
        assertEquals("Accidente", result.getType().getName());
        verify(incidentService, times(1)).registerIncident(any(Incident.class));
    }

    @Test
    void testConsultIncidents() {
        List<Incident> incidents = Arrays.asList(incident);
        List<IncidentDTO> dtos = incidents.stream().map(IncidentDTO::new).collect(Collectors.toList());
        when(incidentService.consultIncidents("Ruta 1", 50)).thenReturn(dtos);

        List<IncidentDTO> result = incidentController.consultIncidents("Ruta 1", 50);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Accidente", result.get(0).getType());
        verify(incidentService, times(1)).consultIncidents("Ruta 1", 50);
    }

    @Test
    void testDeleteIncident_Success() {
        when(incidentService.deleteIncident("1")).thenReturn(true);

        ResponseEntity<String> response = incidentController.deleteIncident("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Incident deleted successfully", response.getBody());
        verify(incidentService, times(1)).deleteIncident("1");
    }

    @Test
    void testDeleteIncident_NotFound() {
        when(incidentService.deleteIncident("999")).thenReturn(false);

        ResponseEntity<String> response = incidentController.deleteIncident("999");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Incident not found", response.getBody());
        verify(incidentService, times(1)).deleteIncident("999");
    }
}

