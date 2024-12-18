package exampel;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Driver Class
public class SimpleHttpServer
{
    // Main Method
    public static void main(String[] args) throws IOException
    {
        // Create an HttpServer instance
        HttpServer server = HttpServer.create(new InetSocketAddress(8094), 0);


        DummyRLEnvironment dummyRLEnvironment = new DummyRLEnvironment(100);

        // Create step context
        server.createContext("/step", new stepHttpHandler(dummyRLEnvironment));
        // Creat reset context
        server.createContext("/reset", new resetHttpHandler(dummyRLEnvironment));

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        DummyRLEnvironment e = new DummyRLEnvironment(3);
        JSONObject j = e.step(1).toJson();
        System.out.println(j.toString());

        System.out.println("Server is running on port 8094");
    }

    // step http handler POST request
    static class stepHttpHandler extends EnvironmentHttpHandler implements HttpHandler {

        public stepHttpHandler(DummyRLEnvironment dummyRLEnvironment) {
            super(dummyRLEnvironment);
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
                            Transition transition = dummyRLEnvironment.step(action);

                            // creat responds body
                            response = transition.toJson().toString();
                        }
                        catch (JSONException exception) {
                            response = invalidFieldsResponse();
                            responseCode = BAD_REQUEST;
                        }

                        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                        final byte[] rawResponseBody = response.getBytes(CHARSET);
                        exchange.sendResponseHeaders(responseCode, rawResponseBody.length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(rawResponseBody);
                        outputStream.close();
                    }
                    catch (Exception e) {
                        responseCode = BAD_REQUEST;
                        response = invalidJson();
                        return;
                    }



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

        public resetHttpHandler(DummyRLEnvironment dummyRLEnvironment) {
            super(dummyRLEnvironment);
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            final Headers headers = exchange.getResponseHeaders();
            final String requestMethod = exchange.getRequestMethod().toUpperCase();
            switch (requestMethod) {
                case METHOD_POST:
                    // step in environment
                    FirstObservation firstObservation = dummyRLEnvironment.reset();

                    // creat responds body
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
}
