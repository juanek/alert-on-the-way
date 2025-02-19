package ar.com.acn.app.dto;

import lombok.Data;

@Data
public class RouteReport {
    private int segment;
    private int totalSeverity;

    public RouteReport(int segment, int totalSeverity) {
        this.segment = segment;
        this.totalSeverity = totalSeverity;
    }

}
