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
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.reflect.Method;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
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
        int resourceId = getIdentifier(context, "android:dimen", "status_bar_height");;
        if (resourceId > 0) return context.getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    // 界面显示优化
    private static void ActivityOptimization(){
        {   // 漫画阅读界面
            final Class<?> BrowseActivityAncestorsClass
                    = getThisPackgeClass(".ui.BrowseActivityAncestors : .ui.abc.viewpager2.BrowseActivityAncestors4");
            if (BrowseActivityAncestorsClass != null) try {
                hookMethods(BrowseActivityAncestorsClass, "onStart", param -> setActivityFullscreen((Activity) param.thisObject));
            }catch (Throwable ignored){}
        }
        {   // 小说阅读界面
            final Class<?> NovelBrowseActivityClass = getThisPackgeClass(".ui.NovelBrowseActivity");
            if (NovelBrowseActivityClass != null) try {
                hookMethods(NovelBrowseActivityClass, "onStart", param -> {
                    Activity activity = (Activity) param.thisObject;
                    setActivityFullscreen(activity);
                    View decorView = activity.getWindow().getDecorView();
                    decorView.post(() -> {
                        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) return;
                        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) return;
                        if (decorView.getRootWindowInsets().getDisplayCutout() == null) return;
                        try {
                            int identifier = getIdentifier(activity.getApplicationContext(), "id", "framelayout");
                            FrameLayout framelayout = (FrameLayout) activity.findViewById(identifier);
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
                if (ShareActivityClass != null)try {
                    hookMethods(ShareActivityClass, "onStart", param -> {
                        onSetActivityStatusBar((Activity) param.thisObject, 0x80000000);
                    });
                }catch (Throwable ignored){}
            }else {
                final Class<?> ShareActivityV2Class = getThisPackgeClass(".ui.ShareActivityV2");
                if (ShareActivityV2Class != null) try {
                    hookMethods(ShareActivityV2Class, "onStart", param -> {
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
            for (Method declaredMethod : TeenagerModeDialogActivityClass.getDeclaredMethods()) {
                XposedBridge.hookMethod(declaredMethod, onActivityFinish(true));
            }
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
