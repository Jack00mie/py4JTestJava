package exampel;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FirstObservation implements Serializable {
    private final List<Double> observationVector;
    private final Map<String, String> info;

    public FirstObservation(List<Double> observationVector, Map<String, String> info) {
        this.observationVector = observationVector;
        this.info = info;
    }

    public List<Double> getObservationVector() {
        return observationVector;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public JSONObject toJson() {
        return new JSONObject(this);
    }

}
