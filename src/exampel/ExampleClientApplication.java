package exampel;

import py4j.GatewayServer;

public class ExampleClientApplication {

    public static void main(String[] args) {
        GatewayServer.turnLoggingOff();
        GatewayServer server = new GatewayServer(new DummyRLEnvironmentEntryPoint());
        server.start();
        Py4JTest py4JTest = (Py4JTest) server.getPythonServerEntryPoint(new Class[] { Py4JTest.class });
        try {
            long startTime = System.currentTimeMillis();
            String massage = py4JTest.test();
            long finishTime = System.currentTimeMillis();
            long elapsedTime = finishTime - startTime;
            System.out.println(elapsedTime + " ms" + "; " + elapsedTime/1000.0 + " s");

            System.out.println(massage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.shutdown();
    }
}

