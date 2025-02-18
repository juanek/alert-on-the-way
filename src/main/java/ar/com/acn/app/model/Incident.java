package ar.com.acn.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "incidents")
public class Incident implements Serializable {
    @Id
    private String id;

    @DBRef(lazy = true)
    private Route route; // Relación con la ruta donde ocurre el incidente

    private double kilometer;

    @DBRef
    private IncidentType type; // Relación con el tipo de incidente

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private String comments;

    public Incident(String id, Route route, double kilometer, IncidentType type, LocalDateTime timestamp, String comments) {
        this.id = id;
        this.route = route;
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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public IncidentType getType() {
        return type;
    }

    public double getKilometer() {
        return kilometer;
    }

    public void setKilometer(double kilometer) {
        this.kilometer = kilometer;
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
                ", route=" + route +
                ", kilometer=" + kilometer +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", comments='" + comments + '\'' +
                '}';
    }
}
