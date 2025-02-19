package ar.com.acn.app.dto;

import ar.com.acn.app.model.Incident;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class IncidentDTO implements Serializable {
    private String id;
    private double kilometer;
    private String type;
    private int severity;
    private String comments;
    private LocalDateTime timestamp;


    public IncidentDTO(String id, double kilometer, String type, int severity, String comments, LocalDateTime timestamp, String routeName) {
        this.id = id;
        this.kilometer = kilometer;
        this.type = type;
        this.severity = severity;
        this.comments = comments;
        this.timestamp = timestamp;

    }

    public IncidentDTO(Incident incident) {
        this.id = incident.getId();
        this.kilometer = incident.getKilometer();
        this.type = incident.getType().getName();
        this.severity = incident.getType().getSeverity();
        this.comments = incident.getComments();
        this.timestamp = incident.getTimestamp();

    }
}
