package ar.com.acn.app.repository;


import ar.com.acn.app.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RouteRepository extends MongoRepository<Route, String> {
}
