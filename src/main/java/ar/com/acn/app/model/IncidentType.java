package ar.com.acn.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "incident_types")
public class IncidentType implements Serializable {
    @Id
    private String id;
    private String name;        // nombre → name
    private int severity;       // importancia → severity

    public IncidentType(String id, String name, int severity) {
        this.id = id;
        this.name = name;
        this.severity = severity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "IncidentType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", severity=" + severity +
                '}';
    }
}
