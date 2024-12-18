package exampel;

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

    protected static final int STATUS_OK = 200;
    protected static final int BAD_REQUEST = 400;
    protected static final int STATUS_METHOD_NOT_ALLOWED = 405;

    protected static final int NO_RESPONSE_LENGTH = -1;

    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final String METHOD_OPTIONS = "OPTIONS";

    DummyRLEnvironment dummyRLEnvironment;

    public EnvironmentHttpHandler(DummyRLEnvironment dummyRLEnvironment) {
        this.dummyRLEnvironment = dummyRLEnvironment;
    }

    protected static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    protected static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }

    protected static JSONObject inputStreamToJson(InputStream inputStream) throws UnsupportedEncodingException, IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        return new JSONObject(responseStrBuilder.toString());
    }
}
