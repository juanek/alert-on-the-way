package ar.com.acn.app.dto;

import lombok.Data;

@Data
public class RequestIncidentBody {
    private String routeName;
    private double kilometer;
    private String type;
    private String comments;
}
