package ar.com.acn.app;

import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.IncidentType;
import ar.com.acn.app.model.Intersection;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.IncidentTypeRepository;
import ar.com.acn.app.repository.RouteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@EnableMongoRepositories
@EnableCaching
public class AlertOnTheWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertOnTheWayApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner initDatabase(RouteRepository routeRepository,
								   IncidentRepository incidentRepository,
								   IncidentTypeRepository incidentTypeRepository) {
		return args -> {
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

			// Crear y guardar rutas sin intersecciones
			Route route1 = new Route(null, "Route 1", "City A", "City B", 300, new ArrayList<>());
			Route route2 = new Route(null, "Route 2", "City C", "City D", 150, new ArrayList<>());
			Route route3 = new Route(null, "Route 3", "City E", "City F", 200, new ArrayList<>());
			Route route4 = new Route(null, "Route 4", "City G", "City H", 250, new ArrayList<>());
			Route route5 = new Route(null, "Route 5", "City I", "City J", 400, new ArrayList<>());

			// Guardar rutas en MongoDB
			route1 = routeRepository.save(route1);
			route2 = routeRepository.save(route2);
			route3 = routeRepository.save(route3);
			route4 = routeRepository.save(route4);
			route5 = routeRepository.save(route5);

			// Ahora creamos las intersecciones usando los IDs de las rutas guardadas
			route1.setIntersections(Arrays.asList(
					new Intersection(route2.getId(), 100),
					new Intersection(route3.getId(), 200)
			));
			route2.setIntersections(Collections.singletonList(
					new Intersection(route1.getId(), 100)
			));
			route3.setIntersections(Arrays.asList(
					new Intersection(route1.getId(), 200),
					new Intersection(route4.getId(), 50)
			));
			route4.setIntersections(Collections.singletonList(
					new Intersection(route3.getId(), 50)
			));
			route5.setIntersections(new ArrayList<>());

			// Guardamos las rutas nuevamente con las intersecciones actualizadas
			routeRepository.saveAll(Arrays.asList(route1, route2, route3, route4, route5));

			// Crear y guardar incidentes con referencias
			incidentRepository.saveAll(Arrays.asList(
					new Incident(null, route1, 50, accident, LocalDateTime.now(), "Multi-car collision"),
					new Incident(null, route2, 30, pothole, LocalDateTime.now(), "Large pothole in right lane"),
					new Incident(null, route3, 120, policeControl, LocalDateTime.now(), "Alcohol control checkpoint"),
					new Incident(null, route4, 200, fire, LocalDateTime.now(), "Fire on the roadside"),
					new Incident(null, route5, 90, fog, LocalDateTime.now(), "Dense fog reducing visibility")
			));
		};
	}
}
