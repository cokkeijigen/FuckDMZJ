package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

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
                        framelayout.setPadding(left, top, right, bottom);
                    } catch (Throwable ignored) {}
                });
            }
        };

        // 普通版
        if (appId.equals(DMZJ_PKGN))
            OptimizationDMZJ(classLoader, onActivityFullscreen, onNovelBrowseActivity);

        // 社区版
        if(appId.equals(DMZJSQ_PKGN)){
            OptimizationDMZJSQ(classLoader, onActivityFullscreen, onNovelBrowseActivity);
            LaunchInterceptorActivity(classLoader);
        }
    }

    // 移除社区版启动页的广告"点击跳过"
    private static void LaunchInterceptorActivity(ClassLoader classLoader) {
        try{
            Class<?> LaunchInterceptorActivity = findClass("com.dmzjsq.manhua.ui.LaunchInterceptorActivity", classLoader);
            findAndHookMethod(LaunchInterceptorActivity, "createContent", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) param.thisObject;
                    Activity activity = (Activity) param.thisObject;
                    int resourceId = context.getResources().getIdentifier("skip_view", "id", DMZJSQ_PKGN);
                    TextView textView = activity.findViewById(resourceId);
                    textView.setVisibility(View.GONE);
                }
            });
        }catch (Throwable ignored){}
    }

    // 状态栏优化
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void allActivitySetStatusBar(String appId){
        XC_MethodHook setStatusBar = onSetActivityStatusBar(Color.WHITE);
        inClassLoaderFindAndHook(clazz -> {
            String className = clazz.getName();
            if (!className.contains(appId)) return;
            for (String ClassName : new String[]{
                    "ImagePagerActivity", "CartoonDetailsActivityV3", "ShareActivity"
            }) if (className.contains(ClassName)) return;
            String HookMethodName;
            final List<String> MethodNames = Arrays.stream(clazz.getDeclaredMethods())
                    .map(Method::getName).collect(Collectors.toList());
            try{
                HookMethodName = "onCreate";
                if (MethodNames.contains(HookMethodName))
                    findAndHookMethod(clazz, HookMethodName, Bundle.class, setStatusBar);
            }catch (Throwable ignored){}
            try{
                HookMethodName = "onStart";
                if (MethodNames.contains(HookMethodName))
                    findAndHookMethod(clazz, HookMethodName, setStatusBar);
            }catch (Throwable ignored){}
            try{
                HookMethodName = "initData";
                if (MethodNames.contains(HookMethodName))
                    findAndHookMethod(clazz, HookMethodName, setStatusBar);
            }catch (Throwable ignored){}
            try{
                HookMethodName = "createContent";
                if (MethodNames.contains(HookMethodName))
                    findAndHookMethod(clazz, HookMethodName, setStatusBar);
            }catch (Throwable ignored){}
        });
    }

    private static void OptimizationDMZJSQ(ClassLoader classLoader, XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        final String BrowseActivityAncestors4 = "com.dmzjsq.manhua.ui.abc.viewpager2.BrowseActivityAncestors4";
        try { // 漫画阅读界面
            findAndHookMethod(findClass(BrowseActivityAncestors4, classLoader), "publicFindViews", onActivityFullscreen);
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(BrowseActivityAncestors4)) return;
                findAndHookMethod(clazz, "publicFindViews", onActivityFullscreen);
            });
        }
        final String NovelBrowseActivity = "com.dmzjsq.manhua.ui.NovelBrowseActivity";
        try { // 小说阅读界面
            findAndHookMethod(findClass(NovelBrowseActivity, classLoader), "findViews", onNovelBrowseActivity);
        } catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelBrowseActivity)) return;
                findAndHookMethod(clazz, "findViews", onNovelBrowseActivity);
            });
        }
        final String ShareActivityV2 = "com.dmzjsq.manhua.ui.ShareActivityV2";
        try { // 分享页
            findAndHookMethod(findClass(ShareActivityV2, classLoader), "createContent", onActivityFullscreen);
        } catch (Throwable igonred){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(ShareActivityV2)) return;
                findAndHookMethod(clazz, "createContent", onActivityFullscreen);
            });
        }
    }

    private static void OptimizationDMZJ(ClassLoader classLoader, XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        final String LaunchInterceptorActivity = "com.dmzj.manhua.ui.LaunchInterceptorActivity";
        try { // 启动页
            findAndHookMethod(findClass(LaunchInterceptorActivity, classLoader),"onCreate", Bundle.class, onActivityFullscreen);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(LaunchInterceptorActivity)) return;
                findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFullscreen);
            });
        }
        final String BrowseActivityAncestors = "com.dmzj.manhua.ui.BrowseActivityAncestors";
        try { // 漫画阅读界面
            findAndHookMethod(findClass(BrowseActivityAncestors, classLoader), "onCreate", Bundle.class, onActivityFullscreen);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(BrowseActivityAncestors)) return;
                findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFullscreen);
            });
        }
        final String NovelBrowseActivity = "com.dmzj.manhua.ui.NovelBrowseActivity";
        try { // 小说阅读界面
            findAndHookMethod(findClass(NovelBrowseActivity, classLoader),"findViews", onNovelBrowseActivity);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelBrowseActivity)) return;
                findAndHookMethod(clazz, "findViews", onNovelBrowseActivity);
            });
        }
        final String ShareActivity = "com.dmzj.manhua.ui.ShareActivity";
        try { // 分享页
            findAndHookMethod(findClass(ShareActivity, classLoader), "createContent", onSetActivityStatusBar(0x80000000));
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(ShareActivity)) return;
                findAndHookMethod(clazz, "createContent", onSetActivityStatusBar(0x80000000));
            });
        }
    }

    // 去除更新检测
    public static void AppUpDataHelper(String appId, ClassLoader classLoader){
        final String AppUpDataHelper = appId + ".helper.AppUpDataHelper";
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
        final String TeenagerModeDialogActivity = appId + "_kt.ui.TeenagerModeDialogActivity";
        try {
            findAndHookMethod(findClass(TeenagerModeDialogActivity, classLoader), "initView", onActivityFinish(true));
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(TeenagerModeDialogActivity)) return;
                findAndHookMethod(clazz, "initView", onActivityFinish(true));
            });
        }
    }

    // 尝试通过移除应用列表中的指定应用信息阻止拉起第三方应用
    public static void ApplicationPackageManager(ClassLoader classLoader){
        final String[] packagename = {"com.jingdong.app.mall", "com.taobao.taobao", "com.eg.android.AlipayGphone", "com.xunmeng.pinduoduo"};
        final String ApplicationPackageManager = "android.app.ApplicationPackageManager";
        try {
            findAndHookMethod(ApplicationPackageManager, classLoader, "getInstalledPackages", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    @SuppressWarnings("unchecked")
                    List<PackageInfo> packageInfos = (List<PackageInfo>) param.getResult();
                    List<String> pkgns = Arrays.asList(packagename);
                    for (PackageInfo packageInfo : packageInfos) {
                        if (pkgns.contains(packageInfo.packageName)) {
                            pkgns.remove(packageInfo.packageName);
                            packageInfos.remove(packageInfo);
                            if (pkgns.size() == 0) break;
                        }
                    }
                    param.setResult(packageInfos);
                }
            });
        }catch (Throwable ignored){}
        try{
            findAndHookMethod(ApplicationPackageManager, classLoader, "getInstalledApplications", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    @SuppressWarnings("unchecked")
                    List<ApplicationInfo> applicationInfos = (List<ApplicationInfo>) param.getResult();
                    List<String> pkgns = Arrays.asList(packagename);
                    for (ApplicationInfo applicationInfo : applicationInfos) {
                        if (pkgns.contains(applicationInfo.packageName)) {
                            pkgns.remove(applicationInfo.packageName);
                            applicationInfos.remove(applicationInfo);
                            if (pkgns.size() == 0) break;
                        }
                    }
                    param.setResult(applicationInfos);
                }
            });
        }catch (Throwable ignored){}
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
