package ar.com.acn.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "incidents")
public class Incident implements Serializable {
    @Id
    private String id;
    private String routeId;
    private double kilometer;
    private String type;
    private LocalDateTime timestamp;
    private String comments;

    public Incident(String id, String routeId, double kilometer, String type, LocalDateTime timestamp, String comments) {
        this.id = id;
        this.routeId = routeId;
        this.kilometer = kilometer;
        this.type = type;
        this.timestamp = timestamp;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public double getKilometer() {
        return kilometer;
    }

    public void setKilometer(double kilometer) {
        this.kilometer = kilometer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id='" + id + '\'' +
                ", routeId='" + routeId + '\'' +
                ", kilometer=" + kilometer +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", comments='" + comments + '\'' +
                '}';
    }
}
