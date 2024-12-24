package environment.application.httpApplication;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestHttpApplication {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        if(args[0] == null) args[0] = "100";
        System.out.println("observationVectorSize: " + args[0]);
        HttpTest httpTestService = new HttpTest(Integer.parseInt(args[0]));

        httpTestService.startTest();
    }
}
