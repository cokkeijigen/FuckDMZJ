package ss.colytitse.fuckdmzj.hook.fucker;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import java.lang.reflect.Field;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class Others {

    // 获取字段
    public static Object getFieldByName(XC_MethodHook.MethodHookParam param, String name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    // 将阅读界面强制全屏
    public static void BrowseActivityAncestors(String PKGN, ClassLoader classLoader){

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override  @SuppressLint("NewApi")
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                try {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                } catch (Throwable t) {
                    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    activity.getWindow().setAttributes(lp);
                }
            }
        };
        try {
            if (PKGN.equals(DMZJ_PKGN)){
                Class<?> BrowseActivityAncestors = findClass("com.dmzj.manhua.ui.BrowseActivityAncestors", classLoader);
                findAndHookMethod(BrowseActivityAncestors, "onCreate", Bundle.class, xc_methodHook);
            }
            if(PKGN.equals(DMZJSQ_PKGN)){
                Class<?> BrowseActivityAncestors4 = findClass("com.dmzjsq.manhua.ui.abc.viewpager2.BrowseActivityAncestors4", classLoader);
                findAndHookMethod(BrowseActivityAncestors4,"publicFindViews", xc_methodHook);
            }
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> BrowseActivityAncestors： " + t);
        }
    }

    // 去除更新检测
    public static void AppUpDataHelper(String PKGN, ClassLoader classLoader){
        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(null);
            }
        };
        try{
            Class<?> AppUpDataHelper = findClass(PKGN + ".helper.AppUpDataHelper", classLoader);
            findAndHookMethod(AppUpDataHelper, "checkVersionInfo", Activity.class, Class.class, boolean.class, xc_methodHook);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> AppUpDataHelper： " + t);
        }
    }

    // 关闭青少年傻逼弹窗
    public static void TeenagerModeDialogActivity(String PKGN, ClassLoader classLoader){
        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                callMethod(param.thisObject, "finish");
                param.setResult(null);
                XposedBridge.log("FUDM_TeenagerMode: SUCCESS");
            }
        };
        try {
            Class<?> TeenagerModeDialogActivity = findClass(PKGN + "_kt.ui.TeenagerModeDialogActivity", classLoader);
            findAndHookMethod(TeenagerModeDialogActivity, "initView", xc_methodHook);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> TeenagerModeDialogActivity： " + t);
        }
    }

    // 阻止粘贴板被强○
    public static void DoNotFuckMyClipboard(){
        XC_MethodHook setPrimaryClip = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ClipData clipData = (ClipData) param.args[0];
                String inText = clipData.getItemAt(0).getText().toString().trim();
                int isReg = 0;
                for(String reg : new String[]{".*[A-Z]+.*", ".*[a-z]+.*",".*[~!@#$%^&*()_+|<>,.?/:;'\\\\[\\\\]{}\\\"]+.*"}){
                    if (inText.matches(reg)) ++isReg;
                }
                if (isReg == 0 || (isReg > 0 && inText.contains("http"))) return;
                param.args[0] = ClipData.newPlainText("","");
            }
        };
        try {
            findAndHookMethod(ClipboardManager.class, "setPrimaryClip", ClipData.class, setPrimaryClip);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> DoNotFuckMyClipboard： " + t);
        }
    }
}
