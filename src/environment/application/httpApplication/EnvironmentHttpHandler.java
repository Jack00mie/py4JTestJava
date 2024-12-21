package environment.application.httpApplication;

import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class EnvironmentHttpHandler {
    protected static final String HOSTNAME = "localhost";
    protected static final int PORT = 8080;
    protected static final int BACKLOG = 1;

    protected static final String HEADER_ALLOW = "Allow";
    protected static final String HEADER_CONTENT_TYPE = "Content-Type";

    protected static final Charset CHARSET = StandardCharsets.UTF_8;

    protected static final int NO_CONTENT = 204;
    protected static final int STATUS_OK = 200;
    protected static final int BAD_REQUEST = 400;
    protected static final int STATUS_METHOD_NOT_ALLOWED = 405;

    protected static final int NO_RESPONSE_LENGTH = -1;

    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final String METHOD_OPTIONS = "OPTIONS";

    public EnvironmentHttpHandler() {
    }

    protected static String invalidJson() {
        JSONObject error = new JSONObject();
        error.put("error:", "Invalid Json.");
        return error.toString();
    }

    protected static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        return responseStrBuilder.toString();
    }

    protected static JSONObject inputStreamToJson(InputStream inputStream) throws UnsupportedEncodingException, IOException {
        return new JSONObject(inputStreamToString(inputStream));
    }
}
