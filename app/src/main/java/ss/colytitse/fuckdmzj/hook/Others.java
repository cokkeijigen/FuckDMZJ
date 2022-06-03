package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.annotation.SuppressLint;
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import de.robv.android.xposed.XC_MethodHook;

public class Others {

    public static final String TAG = "test_";

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) return context.getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    // 界面显示优化
    public static void ActivityOptimization(){
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
                        FrameLayout framelayout = (FrameLayout) getField(param, "framelayout");
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
        if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN))
            OptimizationDMZJ(onActivityFullscreen, onNovelBrowseActivity);
        // 社区版
        if(TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN)){
            OptimizationDMZJSQ(onActivityFullscreen, onNovelBrowseActivity);
            LaunchInterceptorActivity();
        }
    }

    // 移除社区版启动页的广告"点击跳过"
    private static void LaunchInterceptorActivity() {
        final Class<?> LaunchInterceptorActivityClass = getClazz("com.dmzjsq.manhua.ui.LaunchInterceptorActivity");
        if (LaunchInterceptorActivityClass != null) try{
            findAndHookMethod(LaunchInterceptorActivityClass, "createContent", new XC_MethodHook() {
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
    @SuppressLint("NewApi")
    public static void allActivitySetStatusBar(){
        DownLoadManageAbstractActivity();
        final XC_MethodHook setStatusBar = onSetActivityStatusBar(Color.WHITE);
        inClassLoaderFindAndHook(clazz -> {
            String className = clazz.getName();
            if (!className.contains(TARGET_PACKAGE_NAME)) return;
            for (String ClassName : new String[]{
                    "ImagePagerActivity", "CartoonDetailsActivityV3", "ShareActivity"
            }) if (className.contains(ClassName)) return;
            final List<String> MethodNames = Arrays.stream(clazz.getDeclaredMethods())
                    .map(Method::getName).collect(Collectors.toList());
            try{
                if (MethodNames.contains("onCreate"))
                    findAndHookMethod(clazz, "onCreate", Bundle.class, setStatusBar);
            }catch (Throwable ignored){}
            for (String MethodName : new String[]{
                    "onStart", "initData", "findViews", "createContent"
            }){ if (!MethodNames.contains(MethodName)) continue;
                try {
                    findAndHookMethod(clazz, MethodName, setStatusBar);
                }catch (Throwable ignored){}
            }
        });
    }

    private static void DownLoadManageAbstractActivity(){
        final String DownLoadManageAbstractActivity = TARGET_PACKAGE_NAME + ".download.DownLoadManageAbstractActivity";
        final XC_MethodHook setStatusBar = onSetActivityStatusBar(Color.WHITE);
        final Class<?> DownLoadManageAbstractActivityClass = getClazz(DownLoadManageAbstractActivity);
        if (DownLoadManageAbstractActivityClass != null) try{
            findAndHookMethod(DownLoadManageAbstractActivityClass, "createContent", setStatusBar);
        }catch (Throwable ignored){}
    }

    private static void OptimizationDMZJSQ(XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        {   // 漫画阅读界面
            final String BrowseActivityAncestors4 = "com.dmzjsq.manhua.ui.abc.viewpager2.BrowseActivityAncestors4";
            final Class<?> BrowseActivityAncestors4Class = getClazz(BrowseActivityAncestors4);
            if (BrowseActivityAncestors4Class != null) try {
                findAndHookMethod(BrowseActivityAncestors4Class, "publicFindViews", onActivityFullscreen);
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(BrowseActivityAncestors4)) return;
                    findAndHookMethod(clazz, "publicFindViews", onActivityFullscreen);
            });
        }
        {   // 小说阅读界面
            final String NovelBrowseActivity = "com.dmzjsq.manhua.ui.NovelBrowseActivity";
            final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
            if (NovelBrowseActivityClass != null) try {
                findAndHookMethod(NovelBrowseActivityClass, "findViews", onNovelBrowseActivity);
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(NovelBrowseActivity)) return;
                    findAndHookMethod(clazz, "findViews", onNovelBrowseActivity);
            });
        }
        {   // 分享页
            final String ShareActivityV2 = "com.dmzjsq.manhua.ui.ShareActivityV2";
            final Class<?> ShareActivityV2Class = getClazz(ShareActivityV2);
            if (ShareActivityV2Class != null) try {
                findAndHookMethod(ShareActivityV2Class, "createContent", onActivityFullscreen);
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(ShareActivityV2)) return;
                    findAndHookMethod(clazz, "createContent", onActivityFullscreen);
            });
        }
    }

    private static void OptimizationDMZJ(XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        {   // 启动页
            final String LaunchInterceptorActivity = "com.dmzj.manhua.ui.LaunchInterceptorActivity";
            final Class<?> LaunchInterceptorActivityClass = getClazz(LaunchInterceptorActivity);
            if (LaunchInterceptorActivityClass != null) try {
                findAndHookMethod(LaunchInterceptorActivityClass,"onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(LaunchInterceptorActivity)) return;
                    findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFullscreen);
            });
        }
        {   // 漫画阅读界面
            final String BrowseActivityAncestors = "com.dmzj.manhua.ui.BrowseActivityAncestors";
            final Class<?> BrowseActivityAncestorsClass = getClazz(BrowseActivityAncestors);
            if (BrowseActivityAncestorsClass != null) try {
                findAndHookMethod(BrowseActivityAncestorsClass, "onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(BrowseActivityAncestors)) return;
                        findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFullscreen);
            });
        }
        {   // 小说阅读界面
            final String NovelBrowseActivity = "com.dmzj.manhua.ui.NovelBrowseActivity";
            final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
            if (NovelBrowseActivityClass != null) try {
                findAndHookMethod(NovelBrowseActivityClass,"findViews", onNovelBrowseActivity);
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(NovelBrowseActivity)) return;
                    findAndHookMethod(clazz, "findViews", onNovelBrowseActivity);
            });
        }
        {   // 分享页
            final String ShareActivity = "com.dmzj.manhua.ui.ShareActivity";
            final Class<?> ShareActivityClass = getClazz(ShareActivity);
            if (ShareActivityClass != null)try {
                findAndHookMethod(ShareActivityClass, "createContent", onSetActivityStatusBar(0x80000000));
            }catch (Throwable ignored){}
            else inClassLoaderFindAndHook(clazz -> {
                    if (!clazz.getName().equals(ShareActivity)) return;
                    findAndHookMethod(clazz, "createContent", onSetActivityStatusBar(0x80000000));
            });
        }
    }

    // 去除更新检测
    public static void AppUpDataHelper(){
        final String AppUpDataHelper = TARGET_PACKAGE_NAME + ".helper.AppUpDataHelper";
        final Class<?> AppUpDataHelperClass = getClazz(AppUpDataHelper);
        if (AppUpDataHelperClass != null) try {
            findAndHookMethod(AppUpDataHelperClass, "checkVersionInfo",
                    Activity.class, Class.class, boolean.class, beforeResultNull);
        } catch (Throwable ignored) {}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(AppUpDataHelper)) return;
                findAndHookMethod(clazz, "checkVersionInfo",
                    Activity.class, Class.class, boolean.class, beforeResultNull);
        });
    }

    // 关闭傻逼青少年弹窗
    public static void TeenagerModeDialogActivity(){
        final String TeenagerModeDialogActivity = TARGET_PACKAGE_NAME + "_kt.ui.TeenagerModeDialogActivity";
        final Class<?> TeenagerModeDialogActivityClass = getClazz(TeenagerModeDialogActivity);
        if (TeenagerModeDialogActivityClass != null) try {
            findAndHookMethod(TeenagerModeDialogActivityClass, "initView", onActivityFinish(true));
        } catch (Throwable ignored) {}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(TeenagerModeDialogActivity)) return;
                findAndHookMethod(clazz, "initView", onActivityFinish(true));
        });
    }
    
    // 尝试通过移除应用列表中的指定应用信息阻止拉起第三方应用
    public static void ApplicationPackageManager(ClassLoader classLoader){
        final List<String> packageNames = Arrays.asList(
                "com.jingdong.app.mall", "com.taobao.taobao", "com.eg.android.AlipayGphone", "com.xunmeng.pinduoduo"
        );
        final String ApplicationPackageManager = "android.app.ApplicationPackageManager";
        try {
            findAndHookMethod(ApplicationPackageManager, classLoader, "getInstalledPackages", int.class, new XC_MethodHook() {
                @Override @SuppressLint("NewApi")
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    @SuppressWarnings("unchecked")
                    List<PackageInfo> packageInfos = (List<PackageInfo>) param.getResult();
                    packageInfos.stream().filter(info -> packageNames.contains(info.packageName))
                            .forEach(packageInfos::remove);
                    param.setResult(packageInfos);
                }
            });
        }catch (Throwable ignored){}
        try{
            findAndHookMethod(ApplicationPackageManager, classLoader, "getInstalledApplications", int.class, new XC_MethodHook() {
                @Override @SuppressLint("NewApi")
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    @SuppressWarnings("unchecked")
                    List<ApplicationInfo> applicationInfos = (List<ApplicationInfo>) param.getResult();
                    applicationInfos.stream().filter(info -> packageNames.contains(info.packageName))
                            .forEach(applicationInfos::remove);
                    param.setResult(applicationInfos);
                }
            });
        }catch (Throwable ignored){}
    }

    // 阻止剪切板被强○
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
