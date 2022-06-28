package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import de.robv.android.xposed.XC_MethodHook;

public final class Others {

    public static void initClassHooks(){
        TeenagerModeDialogActivity();
        allActivitySetStatusBar();
        ActivityOptimization();
        DoNotFuckMyClipboard();
        AppUpDataHelper();
    }

    // 获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) return context.getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    // 界面显示优化
    private static void ActivityOptimization(){
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
        if(TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN))
            OptimizationDMZJSQ(onActivityFullscreen, onNovelBrowseActivity);
    }

    // 状态栏优化
    private static void allActivitySetStatusBar(){
        final XC_MethodHook setStatusBar = onSetActivityStatusBar(Color.WHITE);
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, setStatusBar);
    }

    private static void OptimizationDMZJSQ(XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        {   // 漫画阅读界面
            final String BrowseActivityAncestors4 = "com.dmzjsq.manhua.ui.abc.viewpager2.BrowseActivityAncestors4";
            final Class<?> BrowseActivityAncestors4Class = getClazz(BrowseActivityAncestors4);
            if (BrowseActivityAncestors4Class != null) try {
                findAndHookMethod(BrowseActivityAncestors4Class, "publicFindViews", onActivityFullscreen);
            }catch (Throwable ignored){}
        }
        {   // 小说阅读界面
            final String NovelBrowseActivity = "com.dmzjsq.manhua.ui.NovelBrowseActivity";
            final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
            if (NovelBrowseActivityClass != null) try {
                findAndHookMethod(NovelBrowseActivityClass, "findViews", onNovelBrowseActivity);
            }catch (Throwable ignored){}
        }
        {   // 分享页
            final String ShareActivityV2 = "com.dmzjsq.manhua.ui.ShareActivityV2";
            final Class<?> ShareActivityV2Class = getClazz(ShareActivityV2);
            if (ShareActivityV2Class != null) try {
                findAndHookMethod(ShareActivityV2Class, "createContent", onActivityFullscreen);
            }catch (Throwable ignored){}
        }
    }

    private static void OptimizationDMZJ(XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        {   // 启动页
            final String LaunchInterceptorActivity = "com.dmzj.manhua.ui.LaunchInterceptorActivity";
            final Class<?> LaunchInterceptorActivityClass = getClazz(LaunchInterceptorActivity);
            if (LaunchInterceptorActivityClass != null) try {
                findAndHookMethod(LaunchInterceptorActivityClass,"onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){}
        }
        {   // 漫画阅读界面
            final String BrowseActivityAncestors = "com.dmzj.manhua.ui.BrowseActivityAncestors";
            final Class<?> BrowseActivityAncestorsClass = getClazz(BrowseActivityAncestors);
            if (BrowseActivityAncestorsClass != null) try {
                findAndHookMethod(BrowseActivityAncestorsClass, "onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){}
        }
        {   // 小说阅读界面
            final String NovelBrowseActivity = "com.dmzj.manhua.ui.NovelBrowseActivity";
            final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
            if (NovelBrowseActivityClass != null) try {
                findAndHookMethod(NovelBrowseActivityClass,"findViews", onNovelBrowseActivity);
            }catch (Throwable ignored){}
        }
        {   // 分享页
            final String ShareActivity = "com.dmzj.manhua.ui.ShareActivity";
            final Class<?> ShareActivityClass = getClazz(ShareActivity);
            if (ShareActivityClass != null)try {
                findAndHookMethod(ShareActivityClass, "createContent", onSetActivityStatusBar(0x80000000));
            }catch (Throwable ignored){}
        }
    }

    // 去除更新检测
    private static void AppUpDataHelper(){
        final String AppUpDataHelper = TARGET_PACKAGE_NAME + ".helper.AppUpDataHelper";
        final Class<?> AppUpDataHelperClass = getClazz(AppUpDataHelper);
        if (AppUpDataHelperClass != null) try {
            findAndHookMethod(AppUpDataHelperClass, "checkVersionInfo",
                Activity.class, Class.class, boolean.class, onReturnVoid);
        } catch (Throwable ignored) {}
    }

    // 关闭傻逼青少年弹窗
    private static void TeenagerModeDialogActivity(){
        final String TeenagerModeDialogActivity = TARGET_PACKAGE_NAME + "_kt.ui.TeenagerModeDialogActivity";
        final Class<?> TeenagerModeDialogActivityClass = getClazz(TeenagerModeDialogActivity);
        if (TeenagerModeDialogActivityClass != null) try {
            findAndHookMethod(TeenagerModeDialogActivityClass, "initView", onActivityFinish(true));
        } catch (Throwable ignored) {}
    }

    // 阻止剪切板被强○
    private static void DoNotFuckMyClipboard(){
        XC_MethodHook setPrimaryClip = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ClipData clipData = (ClipData) param.args[0];
                String inText = clipData.getItemAt(0).getText().toString().trim();
                class temp{
                    // 在写了在写了...
                }
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
