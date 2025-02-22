package ar.com.acn.app.dto;

import lombok.Data;

@Data
public class IntersectionDTO {
    private String intersectingRouteName;
    private double intersectionKm;

    public IntersectionDTO(String intersectingRouteName, double intersectionKm) {
        this.intersectingRouteName = intersectingRouteName;
        this.intersectionKm = intersectionKm;
    }

    public IntersectionDTO() {
    }
}

