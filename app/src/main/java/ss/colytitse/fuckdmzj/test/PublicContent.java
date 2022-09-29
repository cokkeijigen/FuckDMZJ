package ss.colytitse.fuckdmzj.test;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PublicContent extends printStackTrace {

    public static final String INFO = "definfo_";
    public static final String TAG = "test_";

    public static Object newMethodResult(MethodResult methodResult){
        return newMethodResult(methodResult, TAG);
    }

    public static Object newMethodResult(MethodResult methodResult, String _tag){
        try{
            return methodResult.method();
        }catch (Exception e){
            if (_tag == null) _tag = TAG;
            Log.d(_tag, "newMethodResult Err: " + e);
        }
        return null;
    }

    public interface MethodResult{
        Object method();
    }

    public interface HandleLoad{
        void load();
    }
    public static void showToast(Context mContext, String text){
        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(
                    mContext, text,
                    Toast.LENGTH_SHORT
            ).show();
            Looper.loop();
        }).start();
    }
}