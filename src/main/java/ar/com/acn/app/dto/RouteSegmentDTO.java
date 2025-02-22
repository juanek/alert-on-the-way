package ar.com.acn.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteSegmentDTO {

    private String routeId;
    private double kmStart;
    private double kmEnd;
    private List<IncidentDTO> incidents;
    private List<IntersectionDTO> intersections;

    public RouteSegmentDTO() {

    }

    public RouteSegmentDTO(String routeId, double kmStart, double kmEnd,
                           List<IncidentDTO> incidents, List<IntersectionDTO> intersections) {
        this.routeId = routeId;
        this.kmStart = kmStart;
        this.kmEnd = kmEnd;
        this.incidents = incidents;
        this.intersections = intersections;
    }

}
