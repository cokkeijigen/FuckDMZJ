package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.*;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.reflect.Method;
import de.robv.android.xposed.XC_MethodHook;

public final class AdLayout {

    public static void initClassHooks() {
        InstructionActivity();
        NovelBrowseActivity();
        CartoonDetailsView();
        LaunchInterceptorActivity();
        AdLoadingActivity();
    }

    private static void InstructionActivity() {
        final XC_MethodHook layout_ad_layout = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_ad_layout = (RelativeLayout) getField(param, "layout_ad_layout");
                layout_ad_layout.setVisibility(View.GONE);
            }
        };
        CartoonInstructionActivity(layout_ad_layout);
        NovelInstructionActivity(layout_ad_layout);
    }

    private static void CartoonInstructionActivity(XC_MethodHook Fucked){
        final String CartoonInstructionActivity = TARGET_PACKAGE_NAME + ".ui.CartoonInstructionActivity";
        final Class<?> CartoonInstructionActivityClass = getClazz(CartoonInstructionActivity);
        // Log.d(TAG, "CartoonInstructionActivity: " + CartoonInstructionActivity);
        try{
            findAndHookMethod(CartoonInstructionActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CartoonInstructionActivity)) return;
                findAndHookMethod(clazz, "findViews", Fucked);
            });
        }
    }

    private static void NovelInstructionActivity(XC_MethodHook Fucked){
        final String NovelInstructionActivity = TARGET_PACKAGE_NAME + ".ui.NovelInstructionActivity";
        final Class<?> NovelInstructionActivityClass = getClazz(NovelInstructionActivity);
        if (NovelInstructionActivityClass != null) try {
            findAndHookMethod(NovelInstructionActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
    }

    private static void CartoonDetailsView(){
        final String CartoonDetailsView = TARGET_PACKAGE_NAME + "_kt.views.custom.CartoonDetailsView";
        final XC_MethodHook Fucked = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                FrameLayout adLayout = (FrameLayout) getField(param, "adLayout");
                adLayout.setVisibility(View.GONE);
            }
        };
        final Class<?> CartoonDetailsViewClass = getClazz(CartoonDetailsView);
        if (CartoonDetailsViewClass != null) try{
            findAndHookConstructor(CartoonDetailsViewClass, Context.class, AttributeSet.class, int.class, Fucked);
        }catch (Throwable ignored){}
    }

    private static void NovelBrowseActivity(){
        final String NovelBrowseActivity = TARGET_PACKAGE_NAME + ".ui.NovelBrowseActivity";
        final XC_MethodHook Fucked = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_container = (RelativeLayout) getField(param, "layout_container");
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 0);
                layout_container.setLayoutParams(layoutParams);
            }
        };
        final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
        if (NovelBrowseActivityClass != null) try{
            findAndHookMethod(NovelBrowseActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
    }

    private static void AdLoadingActivity(){
        final String AdLoadingActivity = TARGET_PACKAGE_NAME + "_kt.ui.AdLoadingActivity";
        final XC_MethodHook Fucked = onActivityFinish(false);
        final Class<?> AdLoadingActivityClass = getClazz(AdLoadingActivity);
        if (AdLoadingActivityClass == null) try{
            findAndHookMethod(AdLoadingActivityClass, "addFragment", Fucked);
        }catch (Throwable ignored){}
    }

    private static void LaunchInterceptorActivity() {
        final String LaunchInterceptorActivity = TARGET_PACKAGE_NAME + ".ui.LaunchInterceptorActivity";
        final Class<?> LaunchInterceptorActivityClass = getClazz(LaunchInterceptorActivity);
        final XC_MethodHook Fucked = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                Class<?> thisObjectClass = param.thisObject.getClass();
                Method goMainPage = thisObjectClass.getDeclaredMethod("goMainPage");
                goMainPage.setAccessible(true);
                goMainPage.invoke(param.thisObject);
                activity.finish();
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Context context = (Context) param.thisObject;
                Activity activity = (Activity) param.thisObject;
                int resourceId = context.getResources().getIdentifier("skip_view", "id", DMZJSQ_PKGN);
                TextView textView = activity.findViewById(resourceId);
                textView.setVisibility(View.GONE);
            }
        };
        if (LaunchInterceptorActivityClass == null) return;
        if (TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN)) try {
            findAndHookMethod(LaunchInterceptorActivityClass, "createContent", Fucked);
        } catch (Throwable ignored) {}
        try {
            findAndHookMethod(LaunchInterceptorActivityClass, "showColdCpAd", onReturnVoid);
        }catch (Throwable ignored){}
    }
}
