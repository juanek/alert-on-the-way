package ar.com.acn.app.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


public class Intersection {

    private String routeId; // Guardamos solo el ID de la ruta intersectada
    private double kilometer;

    public Intersection(String routeId, double kilometer) {
        this.routeId = routeId;
        this.kilometer = kilometer;
    }

    public double getKilometer() {
        return kilometer;
    }

    public void setKilometer(double kilometer) {
        this.kilometer = kilometer;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}

