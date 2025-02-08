package ar.com.acn.app.repository;


import ar.com.acn.app.model.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface IncidentRepository extends MongoRepository<Incident, String> {
    List<Incident> findByRouteIdAndKilometerBetween(String routeId, double kmInit, double kmEnd);
}
