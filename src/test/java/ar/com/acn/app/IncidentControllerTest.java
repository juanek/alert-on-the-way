package ar.com.acn.app;

import ar.com.acn.app.controller.IncidentController;
import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidentControllerTest {

    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private IncidentController incidentController;

    private Incident incident;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        incident = new Incident("1", "Ruta 1", 50, "Accidente", LocalDateTime.now(), "Colisión múltiple");
    }

    @Test
    void testRegisterIncident() {
        when(incidentService.registerIncident(any(Incident.class))).thenReturn(incident);

        Incident result = incidentController.registerIncident(incident);

        assertNotNull(result);
        assertEquals("Ruta 1", result.getRouteId());
        verify(incidentService, times(1)).registerIncident(any(Incident.class));
    }

    @Test
    void testConsultIncidents() {
        List<Incident> incidents = Arrays.asList(incident);
        when(incidentService.consultIncidents("Ruta 1", 50)).thenReturn(incidents);

        List<Incident> result = incidentController.consultIncidents("Ruta 1", 50);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Ruta 1", result.get(0).getRouteId());
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

