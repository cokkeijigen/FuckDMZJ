package ss.colytitse.fuckdmzj;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.app.Application;
import android.content.Context;
import android.content.res.XModuleResources;
import java.lang.reflect.Field;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.atsg.AutoSign;
import ss.colytitse.fuckdmzj.hook.*;

public class MainHook implements IXposedHookLoadPackage, IXposedHookInitPackageResources,IXposedHookZygoteInit {

    // 普通版包名
    public static final String DMZJ_PKGN = "com.dmzj.manhua";
    // 社区版包名
    public static final String DMZJSQ_PKGN = "com.dmzjsq.manhua";
    /* hook所需字段：类加载器、进程包名、模块路径 */
    public static ClassLoader APPLICATION_CLASS_LOADER = null;
    public static ClassLoader LPPARAM_CLASS_LOADER = null;
    public static String TARGET_PACKAGE_NAME = "";
    private static String MODULE_PATH = null;
    
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
        if (!lpparam.packageName.equals(lpparam.processName)) return;
        if (!(lpparam.packageName.equals(DMZJ_PKGN) || lpparam.packageName.equals(DMZJSQ_PKGN))) return;
        LPPARAM_CLASS_LOADER = lpparam.classLoader;
        TARGET_PACKAGE_NAME = lpparam.packageName;
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                APPLICATION_CLASS_LOADER = ((Context) param.args[0]).getClassLoader();
                 // inClassLoaderFindAndHook(clazz -> Log.d(TAG, "已加载：" + clazz.getName()));
                AdLayout.initClassHooks();
                AdService.initClassHooks();
                Others.initClassHooks();
                AutoSign.initStart();
                AutoSign.SignInView();
                AutoSign.clearSignButtonView();
            }
        });
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam)  {
        if (!(resparam.packageName.equals(DMZJ_PKGN) || resparam.packageName.equals(DMZJSQ_PKGN))) return;
        XModuleResources instance = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        resparam.res.setReplacement(resparam.packageName, "drawable", "img_lauch_bitch", instance.fwd(R.drawable.img_lauch_bitch));
    }
}
