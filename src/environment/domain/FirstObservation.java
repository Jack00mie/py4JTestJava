package environment.domain;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class FirstObservation {
    private final List<Integer> observationVector;
    private final Map<String, String> info;

    public FirstObservation(List<Integer> observationVector, Map<String, String> info) {
        this.observationVector = observationVector;
        this.info = info;
    }

    public List<Integer> getObservationVector() {
        return observationVector;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public JSONObject toJson() {
        return new JSONObject(this);
    }

}
