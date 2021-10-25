package ss.colytitse.fuckdmzj.hook;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import java.lang.reflect.Field;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ss.colytitse.fuckdmzj.MainHook;

public class fucks {

    // 获取控件
    private static Object getView(XC_MethodHook.MethodHookParam param,String id_name) throws Throwable {
        Class clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(id_name);
        field.setAccessible(true);
        return  (Object) field.get(param.thisObject);
    }

    // 去除广告
    public static void fuck_AD(ClassLoader classLoader, String PKGN){
        
        {   // 规则一
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".bean.GuangGaoBean", classLoader),
                    "getCode",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(-1);
                        }
                    }
            );
        }

        {   // 规则二
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".ad.adv.LTUnionADPlatform", classLoader),
                    "LoadShowInfo", int.class, String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedHelpers.callMethod(param.thisObject, "onAdCloseView");
                            param.setResult(null);
                        }
                    }
            );
        }

        {   // 去除详细页的广告位
            String ActVeCla = (PKGN.equals(MainHook.DMZJ_PKGN)) ?
                    ".ui.CartoonInstructionActivity":
                    ".ui.NovelInstructionActivity";
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ActVeCla, classLoader),
                    "findViews",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            RelativeLayout rl = (RelativeLayout) getView(param,"layout_ad_layout");
                            rl.setVisibility(View.GONE);
                        }
                    }
            );
        }
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
