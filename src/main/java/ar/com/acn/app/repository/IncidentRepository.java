package ar.com.acn.app.repository;


import ar.com.acn.app.dto.IncidentDTO;
import ar.com.acn.app.dto.RouteReport;
import ar.com.acn.app.model.Incident;
import ar.com.acn.app.model.Route;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IncidentRepository extends MongoRepository<Incident, String> {


    @Aggregation({
            "{ $match: { 'route.$id': ?0, 'kilometer': { $gte: ?1, $lte: ?2 } } }",
            "{ $project: { '_id': 1, 'kilometer': 1, 'type': 1, 'comments': 1, 'timestamp': 1 } }"
    })
    List<Incident> findIncidentsInRange(ObjectId routeId, double kmStart, double kmEnd);

    @Aggregation(pipeline = {
            "{ '$match': { 'route.$id': ObjectId(?0) } }",
            "{ '$lookup': { 'from': 'incident_types', 'localField': 'type.$id', 'foreignField': '_id', 'as': 'incidentTypeDetails' } }",
            "{ '$unwind': '$incidentTypeDetails' }",
            "{ '$group': { '_id': { 'segment': { '$multiply': [ { '$floor': { '$divide': ['$kilometer', 100] } }, 100 ] } }, 'routeId': { '$first': { '$toString': '$route.$id' } }, 'totalSeverity': { '$sum': '$incidentTypeDetails.severity' } } }",
            "{ '$sort': { 'totalSeverity': -1 } }",
            "{ '$project': { 'segment': '$_id.segment', 'totalSeverity': 1, '_id': 0 } }"
    })
    List<RouteReport> getRouteReportByRouteId(String routeId);

    List<Incident> findByRoute(Route route);
}
