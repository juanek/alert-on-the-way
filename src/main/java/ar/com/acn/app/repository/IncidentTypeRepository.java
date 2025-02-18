package ar.com.acn.app.repository;

import ar.com.acn.app.model.IncidentType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IncidentTypeRepository extends MongoRepository<IncidentType,String> {
}
