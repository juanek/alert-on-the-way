package ar.com.acn.app.repository;


import ar.com.acn.app.model.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IncidentRepository extends MongoRepository<Incident, String> {
    @Query("{'routeId': ?0, 'kilometer': { $gte: ?1, $lte: ?2 }}")
    List<Incident> findIncidentsInRange(String routeId, double kmInit, double kmEnd);

    List<Incident> findByRouteId(String routeId);
}
