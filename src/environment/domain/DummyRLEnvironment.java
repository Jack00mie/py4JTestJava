package environment.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class DummyRLEnvironment implements RLEnvironment {

    private int observationVectorSize;
    private Random random = new Random();

    public DummyRLEnvironment(int observationVectorSize) {
        this.observationVectorSize = observationVectorSize;
    }

    @Override
    public List<Integer> getObservation() {
        List<Integer> observationVector = random.ints(observationVectorSize, 0, 100).boxed().toList();
        return observationVector;
    }

    @Override
    public double getReward() {
        return random.nextDouble() * 100;
    }

    @Override
    public boolean terminated() {
        return false;
    }

    @Override
    public boolean truncated() {
        return false;
    }

    @Override
    public Map<String, String> getInfo() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("info", "info text");
        return hashMap;
    }

    public Transition step(int action) {
        return new Transition(getObservation(), getReward(), terminated(), truncated(), getInfo());
    }

    public FirstObservation reset() {
        return new FirstObservation(getObservation(), getInfo());
    }
}
