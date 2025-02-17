package ar.com.acn.app.dto;

public class RouteReport {
    private String routeId;
    private int segment; // Tramo de 100KM
    private int totalSeverity; // Suma de la gravedad en el tramo

    public RouteReport(String routeId, int segment, int totalSeverity) {
        this.routeId = routeId;
        this.segment = segment;
        this.totalSeverity = totalSeverity;
    }

    public String getRouteId() {
        return routeId;
    }

    public int getSegment() {
        return segment;
    }

    public int getTotalSeverity() {
        return totalSeverity;
    }

    @Override
    public String toString() {
        return "RouteReport{" +
                "routeId='" + routeId + '\'' +
                ", segment=" + segment +
                ", totalSeverity=" + totalSeverity +
                '}';
    }
}
