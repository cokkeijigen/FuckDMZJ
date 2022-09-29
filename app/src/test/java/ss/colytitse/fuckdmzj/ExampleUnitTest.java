package ss.colytitse.fuckdmzj;


import android.util.Log;

import org.junit.Test;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    static final String RSA_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK8nNR1lTnIfIes6oRW" +
            "JNj3mB6OssDGx0uGMpgpbVCpf6+VwnuI2stmhZNoQcM417Iz7WqlPzbUmu9R4dEKmLGEEqOhOdVaeh9Xk2IPPjqIu5Tbk" +
            "LZRxkY3dJM1htbz57d/roesJLkZXqssfG5EJauNc+RcABTfLb4IiFjSMlTsnAgMBAAECgYEAiz/pi2hKOJKlvcTL4jpHJ" +
            "Gjn8+lL3wZX+LeAHkXDoTjHa47g0knYYQteCbv+YwMeAGupBWiLy5RyyhXFoGNKbbnvftMYK56hH+iqxjtDLnjSDKWnhc" +
            "B7089sNKaEM9Ilil6uxWMrMMBH9v2PLdYsqMBHqPutKu/SigeGPeiB7VECQQDizVlNv67go99QAIv2n/ga4e0wLizVuaN" +
            "BXE88AdOnaZ0LOTeniVEqvPtgUk63zbjl0P/pzQzyjitwe6HoCAIpAkEAxbOtnCm1uKEp5HsNaXEJTwE7WQf7PrLD4+Bp" +
            "GtNKkgja6f6F4ld4QZ2TQ6qvsCizSGJrjOpNdjVGJ7bgYMcczwJBALvJWPLmDi7ToFfGTB0EsNHZVKE66kZ/8Stx+ezue" +
            "ke4S556XplqOflQBjbnj2PigwBN/0afT+QZUOBOjWzoDJkCQClzo+oDQMvGVs9GEajS/32mJ3hiWQZrWvEzgzYRqSf3XV" +
            "cEe7PaXSd8z3y3lACeeACsShqQoc8wGlaHXIJOHTcCQQCZw5127ZGs8ZDTSrogrH73Kw/HvX55wGAeirKYcv28eauveCG" +
            "7iyFR0PFB/P/EDZnyb+ifvyEFlucPUI0+Y87F";

    @Test
    public void addition_isCorrect(){
        String data = "EcvVJ01JblNrJ50ur6H+4ddxxqVHrX8iw8Lfp60sj6MheByad0Eeqc9EKrFZd/++JAJBpd44gbGV3frVBEXNy0sorgub657W997YPzEFUpM3+BU9RpELXwdr+bQz474YzvSpO222qgmaUsC1OwVgiLNfWWQoCHIxlV8IGO/W9sc=";
        try {
            String decrypt = RSAUtils.decrypt(data, RSA_KEY);
            System.out.println(decrypt);
        } catch (Exception e) {
            System.out.println("喵咪喵？" + e);
        }
    }



}
