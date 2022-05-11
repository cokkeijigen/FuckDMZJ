package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import java.lang.reflect.Field;
import de.robv.android.xposed.XC_MethodHook;

public class Others {

    public static final String TAG = "test_";

    // 获取字段
    public static Object getFieldByName(XC_MethodHook.MethodHookParam param, String name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    // 获取状态高度
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }

    // 在全部类加载器中查找类并hook
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

    // 界面显示优化
    public static void ActivityOptimization(String appId, ClassLoader classLoader){

        XC_MethodHook onActivityFullscreen = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                setActivityFullscreen((Activity) param.thisObject);
            }
        };

        XC_MethodHook onActivityFullscreen2 = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                setActivityFullscreen((Activity) param.thisObject);
                FrameLayout framelayout = (FrameLayout) getFieldByName(param, "framelayout");
                framelayout.setPadding(framelayout.getPaddingStart(), getStatusBarHeight((Context) param.thisObject),
                        framelayout.getPaddingEnd(), framelayout.getPaddingBottom());
            }
        };

        // 普通版
        if (appId.equals(DMZJ_PKGN)){
            String LaunchInterceptorActivity = "com.dmzj.manhua.ui.LaunchInterceptorActivity";
            try { // 启动页
                findAndHookMethod(findClass(LaunchInterceptorActivity,classLoader),"onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){
                inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(LaunchInterceptorActivity)) return;
                    findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFullscreen);
                });
            }
            String BrowseActivityAncestors = "com.dmzj.manhua.ui.BrowseActivityAncestors";
            try { // 漫画阅读界面
                findAndHookMethod(findClass(BrowseActivityAncestors, classLoader), "onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){
                inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(BrowseActivityAncestors)) return;
                    findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFullscreen);
                });
            }
            String NovelBrowseActivity = "com.dmzj.manhua.ui.NovelBrowseActivity";
            try { // 小说阅读界面
                findAndHookMethod(findClass(NovelBrowseActivity, classLoader),"findViews", onActivityFullscreen2);
            }catch (Throwable ignored){
                inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(NovelBrowseActivity)) return;
                    findAndHookMethod(clazz, "findViews", onActivityFullscreen2);
                });
            }
        }

        // 社区版
        if(appId.equals(DMZJSQ_PKGN)){
            String BrowseActivityAncestors4 = "com.dmzjsq.manhua.ui.abc.viewpager2.BrowseActivityAncestors4";
            try { // 漫画阅读界面
                findAndHookMethod(findClass(BrowseActivityAncestors4, classLoader), "publicFindViews", onActivityFullscreen);
            }catch  (Throwable ignored){
                inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(BrowseActivityAncestors4)) return;
                    findAndHookMethod(clazz, "publicFindViews", onActivityFullscreen);
                });
            }
            String NovelBrowseActivity = "com.dmzjsq.manhua.ui.NovelBrowseActivity";
            try { // 小说阅读界面
                findAndHookMethod(findClass(NovelBrowseActivity, classLoader), "findViews", onActivityFullscreen2);
            } catch (Throwable ignored){
                inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(NovelBrowseActivity)) return;
                    findAndHookMethod(clazz, "findViews", onActivityFullscreen2);
                });
            }
        }
    }

    // 去除更新检测
    public static void AppUpDataHelper(String appId, ClassLoader classLoader){
        String AppUpDataHelper = appId + ".helper.AppUpDataHelper";
        try{
            findAndHookMethod(findClass(AppUpDataHelper, classLoader), "checkVersionInfo",
                    Activity.class, Class.class, boolean.class, beforeResultNull());
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(AppUpDataHelper)) return;
                findAndHookMethod(clazz, "checkVersionInfo", Activity.class, Class.class, boolean.class, beforeResultNull());
            });
        }
    }

    // 关闭傻逼青少年弹窗
    public static void TeenagerModeDialogActivity(String appId, ClassLoader classLoader){
        String TeenagerModeDialogActivity = appId + "_kt.ui.TeenagerModeDialogActivity";
        try {
            findAndHookMethod(findClass(TeenagerModeDialogActivity, classLoader), "initView", onActivityFinish(true));
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(TeenagerModeDialogActivity)) return;
                findAndHookMethod(clazz, "initView", onActivityFinish(true));
            });
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
                for(String reg : new String[]{".*[A-Z]+.*", ".*[a-z]+.*", ".*[~!@#$%^&*()_+|<>,.?/:;'\\\\[\\\\]{}\\\"]+.*"}){
                    if (inText.matches(reg)) ++isReg;
                }
                if (isReg == 0 || (isReg > 0 && inText.contains("http") &&
                        (inText.contains("muwai.com") || inText.contains("dmzj.com")))) return;
                param.args[0] = ClipData.newPlainText("","");
            }
        };
        try {
            findAndHookMethod(ClipboardManager.class, "setPrimaryClip", ClipData.class, setPrimaryClip);
        }catch  (Throwable ignored){}
    }
}
