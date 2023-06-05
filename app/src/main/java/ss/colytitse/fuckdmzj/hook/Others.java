package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.*;
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
import ss.colytitse.fuckdmzj.test.PublicContent;

public final class Others extends PublicContent {

    public static void initClassHooks(){
        TeenagerModeDialogActivity();
        allActivitySetStatusBar();
        ActivityOptimization();
        DoNotFuckMyClipboard();
        AppUpDataHelper();
    }

    // 获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        int resourceId = getIdentifier(context, "android:dimen", "status_bar_height");
        if (resourceId > 0) return context.getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    // 界面显示优化
    private static void ActivityOptimization(){
        {   // 漫画阅读界面
            final Class<?> BrowseActivityAncestorsClass
                    = getThisPackgeClass(".ui.BrowseActivityAncestors : .ui.abc.viewpager2.BrowseActivityAncestors4");
            if (BrowseActivityAncestorsClass != null) try {
                newHookMethods(BrowseActivityAncestorsClass, "onStart",
                        (HookCallBack) param -> setActivityFullscreen((Activity) param.thisObject)
                );
            }catch (Throwable ignored){}
        }
        {   // 小说阅读界面
            final Class<?> NovelBrowseActivityClass = getThisPackgeClass(".ui.NovelBrowseActivity");
            if (NovelBrowseActivityClass != null) try {
                newHookMethods(NovelBrowseActivityClass, "onStart", (HookCallBack) param -> {
                    Activity activity = (Activity) param.thisObject;
                    setActivityFullscreen(activity);
                    View decorView = activity.getWindow().getDecorView();
                    decorView.post(() -> {
                        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) return;
                        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) return;
                        if (decorView.getRootWindowInsets().getDisplayCutout() == null) return;
                        try {
                            int identifier = getIdentifier(activity.getApplicationContext(), "id", "framelayout");
                            FrameLayout framelayout = activity.findViewById(identifier);
                            int left = framelayout.getPaddingStart();
                            int top = getStatusBarHeight((Context) param.thisObject);
                            int right = framelayout.getPaddingEnd();
                            int bottom = framelayout.getPaddingBottom();
                            framelayout.setPadding(left, top, right, bottom);
                        } catch (Throwable ignored) {}
                    });
                });
            }catch (Throwable ignored){}
        }
        {   // 分享页
            if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN)) {
                final Class<?> ShareActivityClass = getThisPackgeClass(".ui.ShareActivity");
                if (ShareActivityClass != null) try {
                    newHookMethods(ShareActivityClass, "onStart",(HookCallBack) param -> {
                        onSetActivityStatusBar((Activity) param.thisObject, 0x80000000);
                    });
                }catch (Throwable ignored){}
            }else {
                final Class<?> ShareActivityV2Class = getThisPackgeClass(".ui.ShareActivityV2");
                if (ShareActivityV2Class != null) try {
                    newHookMethods(ShareActivityV2Class, "onStart", (HookCallBack) param -> {
                        setActivityFullscreen((Activity) param.thisObject);
                    });
                }catch (Throwable ignored){}
            }
        }
    }

    // 状态栏优化
    private static void allActivitySetStatusBar(){
        final XC_MethodHook setStatusBar = onSetActivityStatusBar(Color.WHITE);
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, setStatusBar);
    }

    // 去除更新检测
    private static void AppUpDataHelper(){
        newHookMethods("okhttp3.Request$Builder", "url", (HookCallBack) param -> {
            Object url = param.args[0];
            if (url.getClass().equals(String.class) && ((String) url).contains("/dynamic/comicversion/android"))
                param.args[0] = "https://1919.114.514/";
        }, BEFORE);
    }

    // 关闭傻逼青少年弹窗
    private static void TeenagerModeDialogActivity(){
        final Class<?> TeenagerModeDialogActivityClass = getThisPackgeClass("_kt.ui.TeenagerModeDialogActivity");
        if (TeenagerModeDialogActivityClass != null) try {
            newHookMethods(TeenagerModeDialogActivityClass, "onStart", (HookCallBack) param -> {
                callMethod(param.thisObject, "finish");
            });
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
                if ((boolean) (newMethodResult(() -> {
                    String clazzName;
                    for (StackTraceElement stack : Thread.currentThread().getStackTrace())
                        if ((clazzName = stack.getClassName()) != null && (clazzName.contains(DMZJ_PKGN) || clazzName.contains(DMZJSQ_PKGN)))
                            return true;
                    return false;
                }))) return;
                for(String reg : new String[]{".*[A-Z]+.*", ".*[a-z]+.*", ".*[~!@#$%^&*()_+|<>,.?/:;'\\\\[\\\\]{}\\\"]+.*"})
                    if (inText.matches(reg) && !(inText.contains("http") && (inText.contains("idmzj.com") || inText.contains("dmzj.com")))) {
                        param.args[0] = ClipData.newPlainText("","");
                        return;
                    }
            }
        };
        try {
            findAndHookMethod(ClipboardManager.class, "setPrimaryClip", ClipData.class, setPrimaryClip);
        }catch  (Throwable ignored){}
    }
}
