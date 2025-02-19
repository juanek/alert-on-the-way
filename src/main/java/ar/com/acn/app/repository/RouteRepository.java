package ar.com.acn.app.repository;


import ar.com.acn.app.dto.IntersectionDTO;
import ar.com.acn.app.model.Route;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends MongoRepository<Route, String> {

    @Aggregation({
            "{ $match: { _id: ?0 } }",  // Asegúrate de que el ID está en formato ObjectId
            "{ $unwind: '$intersections' }",
            "{ $match: { 'intersections.kilometer': { $gte: ?1, $lte: ?2 } } }",
            "{ $set: { 'intersections.routeId': { $convert: { input: '$intersections.routeId', to: 'objectId' } } } }", // Cambiar a $convert
            "{ $lookup: { from: 'routes', localField: 'intersections.routeId', foreignField: '_id', as: 'intersectingRoute' } }",
            "{ $unwind: { path: '$intersectingRoute', preserveNullAndEmptyArrays: true } }",
            "{ $project: { _id: 0, intersectionKm: '$intersections.kilometer', intersectingRouteName: { $ifNull: ['$intersectingRoute.name', 'Unknown'] } } }"
    })
    List<IntersectionDTO> findIntersectionsInRange1(String routeId, double kmStart, double kmEnd);


    Optional<Route> findByName(String name);
}
