package ar.com.acn.app;

import ar.com.acn.app.dto.IncidentDTO;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.RouteRepository;
import ar.com.acn.app.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("test")
@SpringBootTest
class AlertOnTheWayApplicationTests {

	@Autowired
	RouteRepository routeRepository;

	@Autowired
	IncidentRepository incidentRepository;

	@Autowired
	IncidentService incidentService;

	@Test
	void contextLoads() {
		System.out.println(routeRepository);
//		// Mostrar todas las rutas en la BD
		routeRepository.findAll().forEach(System.out::println);
//
//		// Mostrar todos los incidentes en la BD
		incidentRepository.findAll().forEach(System.out::println);
//
		// Buscar una ruta específica por nombre
		Optional<Route> optionalRoute = routeRepository.findByName("Route 1");
		if (optionalRoute.isEmpty()) {
			fail("Route not found");
		}
//
		Route route = optionalRoute.get();
//
//		// Buscar incidentes en un tramo de la ruta
		System.out.println("Repositorio");
		List<Incident> incidentList = incidentRepository.findIncidentsInRange(route.getId(), 50, 150);
		incidentList.forEach(System.out::println);
//
		// Consultar incidentes a través del servicio
		System.out.println("Service");
		List<IncidentDTO> incidentList1 = incidentService.consultIncidents(route.getName(), 50);
		incidentList1.forEach(System.out::println);
//
//		// Eliminar un incidente si la lista no está vacía
//		if (!incidentList.isEmpty()) {
//			incidentRepository.deleteById(incidentList.get(0).getId());
//			System.out.println("Deleted incident: " + incidentList.get(0).getId());
//		}
	}
}
