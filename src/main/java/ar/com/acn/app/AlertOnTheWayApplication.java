package ar.com.acn.app;

import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.Route;
import ar.com.acn.app.repository.IncidentRepository;
import ar.com.acn.app.repository.RouteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
@EnableMongoRepositories
@EnableCaching
public class AlertOnTheWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertOnTheWayApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner initDatabase(RouteRepository rutaRepository, IncidentRepository incidenteRepository) {
		return args -> {
			rutaRepository.deleteAll();
			incidenteRepository.deleteAll();

			rutaRepository.saveAll(Arrays.asList(
					new Route("1", "Ruta 1", "Ciudad A", "Ciudad B", 300, Arrays.asList("Ruta 2", "Ruta 3")),
					new Route("2", "Ruta 2", "Ciudad C", "Ciudad D", 150, Arrays.asList("Ruta 1")),
					new Route("3", "Ruta 3", "Ciudad E", "Ciudad F", 200, Arrays.asList("Ruta 1", "Ruta 4")),
					new Route("4", "Ruta 4", "Ciudad G", "Ciudad H", 250, Arrays.asList("Ruta 3")),
					new Route("5", "Ruta 5", "Ciudad I", "Ciudad J", 400, Arrays.asList())
			));

			incidenteRepository.saveAll(Arrays.asList(
					new Incident("1", "Ruta 1", 50, "Accidente", LocalDateTime.now(), "Colisión múltiple"),
					new Incident("2", "Ruta 2", 30, "Bache", LocalDateTime.now(), "Bache grande en el carril derecho"),
					new Incident("3", "Ruta 3", 120, "Control Policial", LocalDateTime.now(), "Control de alcoholemia"),
					new Incident("4", "Ruta 4", 200, "Incendio", LocalDateTime.now(), "Fuego en el costado de la vía"),
					new Incident("5", "Ruta 5", 90, "Neblina", LocalDateTime.now(), "Visibilidad reducida por niebla densa")
			));
		};
	}
}
