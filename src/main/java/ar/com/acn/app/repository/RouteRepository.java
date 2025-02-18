package ar.com.acn.app.repository;


import ar.com.acn.app.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RouteRepository extends MongoRepository<Route, String> {

    Optional<Route> findByName(String name);
}
