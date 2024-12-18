package exampel;

public class DummyRLEnvironmentEntryPoint {
    public DummyRLEnvironment getEnvironment(int observationVectorSize) {
        return new DummyRLEnvironment(observationVectorSize);
    }
}
