package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.RequiresApi;

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

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) return context.getResources().getDimensionPixelSize(resourceId);
        return 0;
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

    // 界面显示优化
    public static void ActivityOptimization(String appId, ClassLoader classLoader){

        XC_MethodHook onActivityFullscreen = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                setActivityFullscreen((Activity) param.thisObject);
            }
        };

        XC_MethodHook onNovelBrowseActivity = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                setActivityFullscreen(activity);
                View decorView = activity.getWindow().getDecorView();
                decorView.post(() -> {
                    if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) return;
                    if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) return;
                    if (decorView.getRootWindowInsets().getDisplayCutout() == null) return;
                    try {
                        FrameLayout framelayout = (FrameLayout) getFieldByName(param, "framelayout");
                        int left = framelayout.getPaddingStart();
                        int top = getStatusBarHeight((Context) param.thisObject);
                        int right = framelayout.getPaddingEnd();
                        int bottom = framelayout.getPaddingBottom();
                        framelayout.setPadding(left,top, right, bottom);
                    } catch (Throwable ignored) {}
                });
            }
        };

        // 普通版
        if (appId.equals(DMZJ_PKGN))
            OptimizationDMZJ(classLoader, onActivityFullscreen, onNovelBrowseActivity);

        // 社区版
        if(appId.equals(DMZJSQ_PKGN))
            OptimizationDMZJSQ(classLoader, onActivityFullscreen, onNovelBrowseActivity);
    }

    private static void allActivitySetStatusBar(String appId){

        XC_MethodHook setStatusBar = new XC_MethodHook() {
            @SuppressLint("InlinedApi")
            @Override  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(Color.WHITE);
            }
        };

        inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().contains(appId)) return;
            if (clazz.getName().contains("ImagePagerActivity")) return;
            try{
                findAndHookMethod(clazz, "onCreate", Bundle.class, setStatusBar);
                return;
            }catch (Throwable ignored){}
            try{
                findAndHookMethod(clazz, "onStart", setStatusBar);
                return;
            }catch (Throwable ignored){}
            try{
                findAndHookMethod(clazz, "initData", setStatusBar);
                return;
            }catch (Throwable ignored){}
            try{
                findAndHookMethod(clazz, "createContent", setStatusBar);
            }catch (Throwable ignored){}
        });
    }

    private static void OptimizationDMZJSQ(ClassLoader classLoader, XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
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
            findAndHookMethod(findClass(NovelBrowseActivity, classLoader), "findViews", onNovelBrowseActivity);
        } catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelBrowseActivity)) return;
                findAndHookMethod(clazz, "findViews", onNovelBrowseActivity);
            });
        }
        String ShareActivityV2 = "com.dmzjsq.manhua.ui.ShareActivityV2";
        try { // 分享页
            findAndHookMethod(findClass(ShareActivityV2, classLoader), "createContent", onActivityFullscreen);
        } catch (Throwable igonred){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(ShareActivityV2)) return;
                findAndHookMethod(clazz, "createContent", onActivityFullscreen);
            });
        }
        // 状态栏优化
        allActivitySetStatusBar(DMZJSQ_PKGN);
    }

    private static void OptimizationDMZJ(ClassLoader classLoader, XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        String LaunchInterceptorActivity = "com.dmzj.manhua.ui.LaunchInterceptorActivity";
        try { // 启动页
            findAndHookMethod(findClass(LaunchInterceptorActivity, classLoader),"onCreate", Bundle.class, onActivityFullscreen);
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
            findAndHookMethod(findClass(NovelBrowseActivity, classLoader),"findViews", onNovelBrowseActivity);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelBrowseActivity)) return;
                findAndHookMethod(clazz, "findViews", onNovelBrowseActivity);
            });
        }
        String ShareActivity = "com.dmzj.manhua.ui.ShareActivity";
        try { // 分享页
            findAndHookMethod(findClass(ShareActivity, classLoader),"createContent", onNovelBrowseActivity);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(ShareActivity)) return;
                findAndHookMethod(clazz, "createContent", onNovelBrowseActivity);
            });
        }
        // 状态栏优化
        allActivitySetStatusBar(DMZJ_PKGN);
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
