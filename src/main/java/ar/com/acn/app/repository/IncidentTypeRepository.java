package ar.com.acn.app.repository;

import ar.com.acn.app.model.IncidentType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IncidentTypeRepository extends MongoRepository<IncidentType, String> {
    Optional<IncidentType> findByName(String type);
}
