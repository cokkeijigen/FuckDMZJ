package ss.colytitse.fuckdmzj;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.hook.Others.*;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.hook.AdService;

public class MainHook implements IXposedHookLoadPackage {

    // 动漫之家普通版
    public static final String DMZJ_PKGN = "com.dmzj.manhua";
    // 动漫之家社区版
    public static final String DMZJSQ_PKGN = "com.dmzjsq.manhua";

    private static final String TAG = "test_";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!(lpparam.packageName.equals(DMZJ_PKGN) || lpparam.packageName.equals(DMZJSQ_PKGN))) return;
        try {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override @RequiresApi(api = Build.VERSION_CODES.N)
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
                    // test(classLoader);
                    // inClassLoaderFindAndHook(clazz -> Log.d(TAG, "调用：" + clazz.getName()));
                    new AdService(lpparam.packageName, classLoader);
                    allActivitySetStatusBar(lpparam.packageName);
                    ActivityOptimization(lpparam.packageName, classLoader);
                    AppUpDataHelper(lpparam.packageName, classLoader);
                    TeenagerModeDialogActivity(lpparam.packageName, classLoader);
                    DoNotFuckMyClipboard();
                }
            });
        }catch (Throwable ignored){}
    }
    
    private void test(ClassLoader classLoader) {
    }

}
