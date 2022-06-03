package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import de.robv.android.xposed.XC_MethodHook;

public class AdLayout {

    public static void AdLayoutInit() {
        XC_MethodHook layout_ad_layout = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_ad_layout = (RelativeLayout) getField(param, "layout_ad_layout");
                layout_ad_layout.setVisibility(View.GONE);
            }
        };
        XC_MethodHook adLayout = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                FrameLayout adLayout = (FrameLayout) getField(param, "adLayout");
                adLayout.setVisibility(View.GONE);
            }
        };
        XC_MethodHook layout_container = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_container = (RelativeLayout) getField(param, "layout_container");
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)layout_container.getLayoutParams();
                layoutParams.height = 0;
                layout_container.setLayoutParams(layoutParams);
            }
        };
        CartoonInstructionActivity(layout_ad_layout);
        NovelInstructionActivity(layout_ad_layout);
        NovelBrowseActivity(layout_container);
        CartoonDetailsView(adLayout);
        AdLoadingActivity();
    }

    private static void CartoonInstructionActivity(XC_MethodHook Fucked){
        final String CartoonInstructionActivity = TARGET_PACKAGE_NAME + ".ui.CartoonInstructionActivity";
        final Class<?> CartoonInstructionActivityClass = getClazz(CartoonInstructionActivity);
        if (CartoonInstructionActivityClass != null) try{
            findAndHookMethod(CartoonInstructionActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CartoonInstructionActivity)) return;
                findAndHookMethod(clazz, "findViews", Fucked);
        });
    }

    private static void NovelInstructionActivity(XC_MethodHook Fucked){
        final String NovelInstructionActivity = TARGET_PACKAGE_NAME + ".ui.NovelInstructionActivity";
        final Class<?> NovelInstructionActivityClass = getClazz(NovelInstructionActivity);
        if (NovelInstructionActivityClass != null) try {
            findAndHookMethod(NovelInstructionActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelInstructionActivity)) return;
                findAndHookMethod(clazz, "findViews", Fucked);
        });
    }

    private static void CartoonDetailsView(XC_MethodHook Fucked){
        final String CartoonDetailsView = TARGET_PACKAGE_NAME + "_kt.views.custom.CartoonDetailsView";
        final Class<?> CartoonDetailsViewClass = getClazz(CartoonDetailsView);
        if (CartoonDetailsViewClass != null) try{
            findAndHookConstructor(CartoonDetailsViewClass, Context.class, AttributeSet.class, int.class, Fucked);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CartoonDetailsView)) return;
                findAndHookConstructor(clazz, Context.class, AttributeSet.class, int.class, Fucked);
        });
    }

    private static void NovelBrowseActivity(XC_MethodHook Fucked){
        final String NovelBrowseActivity = TARGET_PACKAGE_NAME + ".ui.NovelBrowseActivity";
        final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
        if (NovelBrowseActivityClass != null) try{
            findAndHookMethod(NovelBrowseActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelBrowseActivity)) return;
                findAndHookMethod(clazz, "findViews", Fucked);
        });
    }

    private static void AdLoadingActivity(){
        final String AdLoadingActivity = TARGET_PACKAGE_NAME + "_kt.ui.AdLoadingActivity";
        final XC_MethodHook Fucked = onActivityFinish(false);
        final Class<?> AdLoadingActivityClass = getClazz(AdLoadingActivity);
        if (AdLoadingActivityClass != null) try{
            findAndHookMethod(AdLoadingActivityClass, "addFragment", Fucked);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(AdLoadingActivity)) return;
                findAndHookMethod(clazz, "addFragment", Fucked);
        });
    }
}
