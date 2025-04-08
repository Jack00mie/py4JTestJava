package environment.application.py4JApplication;

import py4j.GatewayServer;
import utils.Writer;

public class TestPy4JApplication {

    public static void main(String[] args) {
        GatewayServer.turnLoggingOff();
        // Java server
        GatewayServer server = new GatewayServer(new DummyRLEnvironmentEntryPoint());
        server.start();

        Py4JTest py4JTest = (Py4JTest) server.getPythonServerEntryPoint(new Class[] { Py4JTest.class });

        if(args[0] == null) args[0] = "100";
        System.out.println("observationVectorSize: " + args[0]);
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Py4J test round started.");
            String massage = py4JTest.test(Integer.parseInt(args[0]));
            long finishTime = System.currentTimeMillis();
            long elapsedTime = finishTime - startTime;

            System.out.println(elapsedTime + " ms" + "; " + elapsedTime/1000.0 + " s");
            Writer.store(elapsedTime, "Py4JTestResultsContinues" + args[0] + ".txt");

            System.out.println(massage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.shutdown();
    }
}

