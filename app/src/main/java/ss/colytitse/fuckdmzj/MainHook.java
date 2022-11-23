package ss.colytitse.fuckdmzj;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.AdLayout.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.app.Application;
import android.content.Context;
import android.content.res.XModuleResources;
import java.lang.reflect.Field;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.*;
import ss.colytitse.fuckdmzj.atsg.*;
import ss.colytitse.fuckdmzj.gdhd.*;
import ss.colytitse.fuckdmzj.hook.*;
import ss.colytitse.fuckdmzj.test.PublicContent;

public class MainHook extends PublicContent implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    // 普通版包名
    public static final String DMZJ_PKGN = "com.dmzj.manhua";
    // 社区版包名
    public static final String DMZJSQ_PKGN = "com.dmzjsq.manhua";
    /* hook所需字段：类加载器、进程包名、模块路径 */
    public static ClassLoader APPLICATION_CLASS_LOADER = null;
    public static ClassLoader LPPARAM_CLASS_LOADER = null;
    public static String TARGET_PACKAGE_NAME = "";
    private static String MODULE_PATH = "";
    public static ReplaceActivity.CreateResId CREATE_RES_ID;

    // 获取类
    public static Class<?> getClazz(String className){
        className = className.trim().replace("/",".");
        try {
            return findClass(className, APPLICATION_CLASS_LOADER);
        } catch (Throwable ignored) {}
        try {
            return findClass(className, LPPARAM_CLASS_LOADER);
        } catch (Throwable ignored) {}
        return FuckerHook.getClass(className);
    }

    // 获取当前进程包名下路径类
    public static Class<?> getThisPackgeClass(String name){
        if (!name.contains(":")) return getClazz(TARGET_PACKAGE_NAME + name);
        String[] split = name.split(":");
        if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN))
            return getClazz(TARGET_PACKAGE_NAME + split[0].trim());
        else
            return getClazz(TARGET_PACKAGE_NAME + split[1].trim());
    }

    // 获取字段
    public static Object getField(XC_MethodHook.MethodHookParam param, String name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    private void initApplicationHandleLoad(HandleLoad handleLoad){
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                APPLICATION_CLASS_LOADER = ((Context) param.args[0]).getClassLoader();
                handleLoad.load();
            }
        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals(lpparam.processName)) return;
        if (!(lpparam.packageName.equals(DMZJ_PKGN) || lpparam.packageName.equals(DMZJSQ_PKGN))) return;
        LPPARAM_CLASS_LOADER = lpparam.classLoader;
        TARGET_PACKAGE_NAME = lpparam.packageName;
        initApplicationHandleLoad(() -> {
            AdLayout.initClassHooks();
            AdService.initClassHooks();
            Others.initClassHooks();
            AutoSign.initStart();
            AutoSign.SignInView();
            AutoSign.clearSignButtonView();
//            test();
        });
    }

    private void test() {
        FilterKamiGakusi.Runnenr();
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam)  {
        if (!(resparam.packageName.equals(DMZJ_PKGN) || resparam.packageName.equals(DMZJSQ_PKGN))) return;
        XModuleResources instance = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        int img_lauch_bitch = resparam.res.addResource(instance, R.drawable.img_lauch_bitch);
        initApplicationHandleLoad(() -> LaunchInterceptorActivity(img_lauch_bitch));
        CREATE_RES_ID = new ReplaceActivity.CreateResId();
        CREATE_RES_ID.img_layout = resparam.res.addResource(instance, R.drawable.img_layout_neko);
        CREATE_RES_ID.img_icon_clock = resparam.res.addResource(instance, R.drawable.img_icon_clock);
        CREATE_RES_ID.img_icon_tag = resparam.res.addResource(instance, R.drawable.img_icon_tag);
        CREATE_RES_ID.img_icon_head = resparam.res.addResource(instance, R.drawable.img_icon_head);
    }
}
