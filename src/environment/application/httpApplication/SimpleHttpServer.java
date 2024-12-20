package environment.application.httpApplication;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import environment.domain.DummyRLEnvironment;
import environment.domain.FirstObservation;
import environment.domain.Transition;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


// Driver Class
public class SimpleHttpServer
{

    private long startTime = 0;
    private long finishTime = 0;

    // Main Method
    public void test() throws IOException, NullPointerException, URISyntaxException, InterruptedException {
        // Create an HttpServer instance
        HttpServer server = HttpServer.create(new InetSocketAddress(8094), 0);


        DummyRLEnvironment dummyRLEnvironment = new DummyRLEnvironment(200);

        // Create step context
        server.createContext("/step", new stepHttpHandler(dummyRLEnvironment));
        // Creat reset context
        server.createContext("/reset", new resetHttpHandler(dummyRLEnvironment));

        server.createContext("/testComplete", new testCompleteHttpHandler(this));

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server is running on port 8094");

        this.startTime = System.currentTimeMillis();
        startTest();
    }

    private static void startTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("http://127.0.0.1:8095/start");
        HttpRequest request  = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.noBody()).build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }

    private void testFinished() {
        this.finishTime = System.currentTimeMillis();
        long elapsedTime = this.finishTime - this.startTime;

        System.out.println(elapsedTime + " ms" + "; " + elapsedTime/1000.0 + " s");

        System.out.println("Test fished");
    }

    // step http handler POST request
    static class stepHttpHandler extends EnvironmentHttpHandler implements HttpHandler {

        DummyRLEnvironment dummyRLEnvironment;

        public stepHttpHandler(DummyRLEnvironment dummyRLEnvironment) {
            super();

            this.dummyRLEnvironment = dummyRLEnvironment;
        }

        private String invalidFieldsResponse() {
            JSONObject error = new JSONObject();
            error.put("error", "Invalid fields");
            JSONObject fields = new JSONObject();
            fields.put("action", "Integer required. Describes action to take.");
            error.put("details", fields);

            return error.toString();
        }

        private String invalidJson() {
            JSONObject error = new JSONObject();
            error.put("error:", "Invalid Json.");
            return error.toString();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            final Headers headers = exchange.getResponseHeaders();
            final String requestMethod = exchange.getRequestMethod().toUpperCase();
            switch (requestMethod) {
                case METHOD_POST:
                    String response;
                    int responseCode = STATUS_OK;

                    try {
                        // get request body
                        JSONObject requestBody = inputStreamToJson(exchange.getRequestBody());

                        try {
                            int action = requestBody.getInt("action");

                            // step in environment
                            Transition transition = this.dummyRLEnvironment.step(action);

                            // creat responds
                            response = transition.toJson().toString();
                        }
                        catch (JSONException exception) {
                            response = invalidFieldsResponse();
                            responseCode = BAD_REQUEST;
                        }
                    }
                    catch (Exception e) {
                        responseCode = BAD_REQUEST;
                        response = invalidJson();
                    }

                    headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                    final byte[] rawResponseBody = response.getBytes(CHARSET);
                    exchange.sendResponseHeaders(responseCode, rawResponseBody.length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(rawResponseBody);
                    outputStream.close();
                case METHOD_OPTIONS:
                    headers.set(HEADER_ALLOW, METHOD_POST);
                    exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                default:
                    headers.set(HEADER_ALLOW, METHOD_POST);
                    exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                    break;
            }
        }
    }

    // reset http handler
    static class resetHttpHandler extends EnvironmentHttpHandler implements HttpHandler {

        DummyRLEnvironment dummyRLEnvironment;

        public resetHttpHandler(DummyRLEnvironment dummyRLEnvironment) {
            super();
            this.dummyRLEnvironment = dummyRLEnvironment;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            final Headers headers = exchange.getResponseHeaders();
            final String requestMethod = exchange.getRequestMethod().toUpperCase();
            switch (requestMethod) {
                case METHOD_POST:
                    // reset environment
                    FirstObservation firstObservation = this.dummyRLEnvironment.reset();

                    // creat responds
                    String response = firstObservation.toJson().toString();

                    headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                    final byte[] rawResponseBody = response.getBytes(CHARSET);
                    exchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(rawResponseBody);
                    outputStream.close();
                case METHOD_OPTIONS:
                    headers.set(HEADER_ALLOW, METHOD_POST);
                    exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                default:
                    headers.set(HEADER_ALLOW, METHOD_POST);
                    exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                    break;
            }
        }
    }

    static class testCompleteHttpHandler extends EnvironmentHttpHandler implements HttpHandler {
            SimpleHttpServer simpleHttpServer;
        public testCompleteHttpHandler(SimpleHttpServer simpleHttpServer) {
            super();
            this.simpleHttpServer = simpleHttpServer;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            final Headers headers = exchange.getResponseHeaders();
            final String requestMethod = exchange.getRequestMethod().toUpperCase();
            switch (requestMethod) {
                case METHOD_POST:
                    this.simpleHttpServer.testFinished();
                    exchange.sendResponseHeaders(NO_CONTENT, NO_RESPONSE_LENGTH);
                case METHOD_OPTIONS:
                    headers.set(HEADER_ALLOW, METHOD_POST);
                    exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                default:
                    headers.set(HEADER_ALLOW, METHOD_POST);
                    exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                    break;
            }
        }
    }

}
