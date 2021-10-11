package ss.colytitse.fuckdmzj.hook;

import android.app.Activity;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class fucks {

    // 关闭广告
    public static void fuck_AD(ClassLoader classLoader, String PKGN){

        XposedHelpers.findAndHookMethod(
                XposedHelpers.findClass(PKGN + ".bean.GuangGaoBean", classLoader),
                "getCode",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(-1);
                    }
                }
        );

    }

    // 阻止更新检测
    public static void fuck_CheckVersionInfo(ClassLoader classLoader, String PKGN){

        XposedHelpers.findAndHookMethod(
                XposedHelpers.findClass(PKGN + ".helper.AppUpDataHelper",classLoader),
                "checkVersionInfo", Activity.class, Class.class, boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null);
                    }
                }
        );

    }

    // 关闭青少年傻逼弹窗
    public static void fuck_TeenagerMode(ClassLoader classLoader, String PKGN){

        XposedHelpers.findAndHookMethod(
                XposedHelpers.findClass(PKGN + "_kt.ui.TeenagerModeDialogActivity",classLoader),
                "initView",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedHelpers.callMethod(param.thisObject,"finish");
                        param.setResult(null);
                    }
                }
        );

    }

}
