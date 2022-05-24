package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import de.robv.android.xposed.XC_MethodHook;

public class MethodHook {

    public static final String TAG = "test_";

    public static XC_MethodHook beforeResultNull(){
        return new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(null);
            }
        };
    }

    public static XC_MethodHook onCallMethod(String methodName, boolean before){
        return before ? new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                callMethod(param.thisObject, methodName);
                param.setResult(null);
            }
        } : new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                callMethod(param.thisObject, methodName);
            }
        };
    }

    public static XC_MethodHook onSetResult(int value, boolean before){
        return before ? new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable  {
                super.beforeHookedMethod(param);
                param.setResult(value);
            }
        } : new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(value);
            }
        };
    }

    public static XC_MethodHook onActivityFinish(boolean before){
        return before ? new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                activity.finish();
                param.setResult(null);
            }
        } : new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                activity.finish();
            }
        };
    }

    @SuppressLint("NewApi")
    public static void setActivityFullscreen(Activity activity){
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        activity.getWindow().setAttributes(lp);
    }

    public static XC_MethodHook onSetActivityStatusBar(int Color){
        return new XC_MethodHook() {
            @Override @SuppressLint("InlinedApi")
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(Color);
            }
        };
    }

    // 在全部类加载器中查找并hook
    public static void inClassLoaderFindAndHook(Fucker fucker){
        hookAllMethods(ClassLoader.class, "loadClass", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (param.hasThrowable() || param.args.length != 1) return;
                fucker.hook((Class<?>) param.getResult());
            }
        });
    }

    public interface Fucker{
        void hook(Class<?> clazz);
    }
}
