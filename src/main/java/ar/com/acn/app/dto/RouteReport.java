package ar.com.acn.app.dto;

import lombok.Data;

@Data
public class RouteReport {
    private String route;  // <-- Debería llamarse igual que en la agregación
    private int segment;
    private int totalSeverity;

    public RouteReport(String route, int segment, int totalSeverity) {
        this.route = route;
        this.segment = segment;
        this.totalSeverity = totalSeverity;
    }

    @Override
    public String toString() {
        return "RouteReport{" +
                "route='" + route + '\'' +
                ", segment=" + segment +
                ", totalSeverity=" + totalSeverity +
                '}';
    }
}
