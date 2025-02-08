package ar.com.acn.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;


@Document(collection = "routes")
public class Route implements Serializable {
    @Id
    private String id;
    private String name;
    private String origin;
    private String destination;
    private double distance;
    private List<String> intersections;

    public Route(String id, String name, String origin, String destination, double distancia, List<String> intersections) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.distance = distancia;
        this.intersections = intersections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<String> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<String> intersections) {
        this.intersections = intersections;
    }
}
