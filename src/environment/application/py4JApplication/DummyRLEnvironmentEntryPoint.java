package environment.application.py4JApplication;

import environment.domain.DummyRLEnvironment;

public class DummyRLEnvironmentEntryPoint {
    public DummyRLEnvironment getEnvironment(int observationVectorSize) {
        return new DummyRLEnvironment(observationVectorSize);
    }
}
