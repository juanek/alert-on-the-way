package ar.com.acn.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteReportDTO {
    private String routeName;
    private List<RouteReport> reports;

    public RouteReportDTO(String routeName, List<RouteReport> reports) {
        this.routeName = routeName;
        this.reports = reports;
    }
}


