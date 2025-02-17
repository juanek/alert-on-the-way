package ar.com.acn.app;

import ar.com.acn.app.model.Incident;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.RouteRepository;
import ar.com.acn.app.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

		routeRepository.findAll().forEach(System.out::println);

		incidentRepository.findAll().forEach(System.out::println);

		System.out.println("Repositorio");
		List<Incident> incidentList = incidentRepository.findIncidentsInRange("Ruta 1",50,150);
		incidentList.forEach(System.out::println);

		System.out.println("service");
		incidentList = incidentService.consultIncidents("Ruta 1",50);
		incidentList.forEach(System.out::println);

		incidentRepository.deleteById(incidentList.get(0).getId());
	}

}
