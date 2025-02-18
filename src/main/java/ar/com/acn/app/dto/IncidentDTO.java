package ar.com.acn.app.dto;

import ar.com.acn.app.model.Incident;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class IncidentDTO implements Serializable {
    private String id;
    private String routeId;  // 🔄 Solo almacena el ID de la ruta en lugar del objeto completo
    private double kilometer;
    private String type;
    private LocalDateTime timestamp;
    private String description;

    public IncidentDTO(Incident incident) {
        this.id = incident.getId();
        this.routeId = incident.getRoute().getId(); // ✅ Guardamos solo el ID
        this.kilometer = incident.getKilometer();
        this.type = incident.getType().getName();
        this.timestamp = incident.getTimestamp();
        this.description = incident.getComments();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

