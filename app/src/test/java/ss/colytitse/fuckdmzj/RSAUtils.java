package ss.colytitse.fuckdmzj;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class RSAUtils {

    public static String decrypt(String content, String private_key) throws Exception {
        PrivateKey priKey = getPrivateKey(private_key);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        InputStream ins = new ByteArrayInputStream(Base64Util.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buf = new byte[128];
        int bufl;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block;
            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                System.arraycopy(buf, 0, block, 0, bufl);
            }
            writer.write(cipher.doFinal(block));
        }
        return new String(writer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64Util.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


}