package ss.colytitse.fuckdmzj;

import org.junit.Test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String test = new String(new byte[]{-26, -128, -69, -28, -71, -117, -26, -119, -109, -25, -126, -71, -27, -83, -105, -28, -72, -118, -27, -114, -69});
        System.out.println(test);
    }
}