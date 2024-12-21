package environment.application.py4JApplication;

import py4j.GatewayServer;
import utils.Writer;

import java.io.FileWriter;
import java.io.IOException;

public class TestPy4JApplication {

    public static void main(String[] args) {
        GatewayServer.turnLoggingOff();
        // Java server
        GatewayServer server = new GatewayServer(new DummyRLEnvironmentEntryPoint());
        server.start();

        Py4JTest py4JTest = (Py4JTest) server.getPythonServerEntryPoint(new Class[] { Py4JTest.class });

        try {
            long startTime = System.currentTimeMillis();
            String massage = py4JTest.test(100);
            long finishTime = System.currentTimeMillis();
            long elapsedTime = finishTime - startTime;

            System.out.println(elapsedTime + " ms" + "; " + elapsedTime/1000.0 + " s");
            Writer.store(elapsedTime, "Py4JTestResults.txt");

            System.out.println(massage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.shutdown();
    }
}

