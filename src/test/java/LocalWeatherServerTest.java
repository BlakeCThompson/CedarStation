import WeatherStation.LocalWeatherServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LocalWeatherServerTest {
    PrintStream savedSystemOut = System.out;
    ByteArrayOutputStream testOut = new ByteArrayOutputStream();

    @Test
    public void connectionTest() throws IOException {

        LocalWeatherServer localWeatherServer = new LocalWeatherServer();
        assert(testOut.toString().contains("connecting to server"));
        restoreSystemOut();
        System.out.print(testOut.toString());
        restoreSystemOut();
    }

    @After
    public void restoreSystemOut(){
        System.setOut(savedSystemOut);
    }
    @Before
    public void redirectSystemOut(){
        PrintStream ps = new PrintStream(testOut);
        System.setOut(ps);
    }
}
