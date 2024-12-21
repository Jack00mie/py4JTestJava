package environment.application.httpApplication;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestHttpApplication {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        HttpTest httpTestService = new HttpTest(100);

        httpTestService.startTest();
    }
}
