package ss.colytitse.fuckdmzj;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.AdLayout.*;
import static ss.colytitse.fuckdmzj.hook.AdService.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.hook.Others.*;
import android.app.Application;
import android.content.Context;
import java.lang.reflect.Field;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    // 动漫之家普通版
    public static final String DMZJ_PKGN = "com.dmzj.manhua";
    // 动漫之家社区版
    public static final String DMZJSQ_PKGN = "com.dmzjsq.manhua";
    /* hook所需字段：类加载器、进程包名*/
    public static ClassLoader APPLICATION_CLASS_LOADER = null;
    public static ClassLoader LPPARAM_CLASS_LOADER = null;
    public static String TARGET_PACKAGE_NAME = "";
    private static final String TAG = "test_";

    // 获取类
    public static Class<?> getClazz(String className){
        try {
            return findClass(className, APPLICATION_CLASS_LOADER);
        } catch (Throwable ignored) {}
        try {
            return findClass(className, LPPARAM_CLASS_LOADER);
        } catch (Throwable ignored) {}
        return FuckerHook.getClass(className);
    }

    // 获取字段
    public static Object getField(XC_MethodHook.MethodHookParam param, String name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!(lpparam.packageName.equals(DMZJ_PKGN) || lpparam.packageName.equals(DMZJSQ_PKGN))) return;
        LPPARAM_CLASS_LOADER = lpparam.classLoader;
        TARGET_PACKAGE_NAME = lpparam.packageName;
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                APPLICATION_CLASS_LOADER = ((Context) param.args[0]).getClassLoader();
                loadAdServiceInit();
                loadAdLayoutInit();
                loadOthersInit();
//                inClassLoaderFindAndHook(clazz -> XposedBridge.log("调用：" + clazz.getName()));
            }
        });
    }
}
