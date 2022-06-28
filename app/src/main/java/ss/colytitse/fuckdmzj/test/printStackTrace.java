package ss.colytitse.fuckdmzj.test;

import static ss.colytitse.fuckdmzj.test.PublicContent.TAG;
import static ss.colytitse.fuckdmzj.MainHook.DMZJ_PKGN;
import android.annotation.SuppressLint;
import android.util.Log;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class printStackTrace {

    @SuppressLint("NewApi")
    public static void setStackTracePrint(){
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        Log.d(TAG, "#############################[start]#############################");
        Arrays.stream(stackTraceElement).forEach(stack -> Log.d(TAG,
                "at " + stack.getClassName() + "."
                        + stack.getMethodName() +
                        "(" + stack.getFileName() +
                        ":" + stack.getLineNumber() + ")"));
        Log.d(TAG, "#############################[end]#############################");
    }

    public static void setMethodInvokePrint(Class<?> clazz) {
        String clazzName = clazz.getName();
        if (!clazzName.contains(DMZJ_PKGN)) return;
        Method[] mds = clazz.getDeclaredMethods();
        for (final Method md : mds) {
            int mod = md.getModifiers();
            if (!Modifier.isAbstract(mod) && !Modifier.isNative(mod) && !Modifier.isInterface(mod)) {
                XposedBridge.hookMethod(md, new XC_MethodHook() {
                    public void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.d(TAG, clazzName + " -> " + md.getName() + " [args.length = " + param.args.length);
                    }
                });

            }
        }

    }
}
