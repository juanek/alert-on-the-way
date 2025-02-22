package ar.com.acn.app.dto;

import ar.com.acn.app.model.Incident;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
    public IncidentDTO() {}
}
