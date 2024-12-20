package environment.domain;

import java.util.List;
import java.util.Map;

public interface RLEnvironment {
    public List<Double> getObservation();
    public double getReward();
    public boolean terminated();
    public boolean truncated();
    public Map<String, String> getInfo();
    public FirstObservation reset();
    public Transition step(int action);
}
