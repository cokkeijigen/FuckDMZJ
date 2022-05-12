package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.hook.Others.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import de.robv.android.xposed.XC_MethodHook;

public class AdLayout {

    private final String appId;
    private final ClassLoader classLoader;

    public AdLayout(String appId, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.appId = appId;
        this.init();
    }

    private void init() {

        XC_MethodHook layout_ad_layout = new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_ad_layout = (RelativeLayout) getFieldByName(param, "layout_ad_layout");
                layout_ad_layout.setVisibility(View.GONE);
            }
        };

        XC_MethodHook adLayout = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                FrameLayout adLayout = (FrameLayout) getFieldByName(param, "adLayout");
                adLayout.setVisibility(View.GONE);
            }
        };

        XC_MethodHook layout_container = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_container = (RelativeLayout) getFieldByName(param, "layout_container");
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)layout_container.getLayoutParams();
                layoutParams.height = 0;
                layout_container.setLayoutParams(layoutParams);
            }
        };

        CartoonInstructionActivity(layout_ad_layout);
        NovelInstructionActivity(layout_ad_layout);
        NovelBrowseActivity(layout_container);
        CartoonDetailsView(adLayout);
        AdLoadingActivity(onActivityFinish(false));
    }

    private void CartoonInstructionActivity(XC_MethodHook FUCK){
        String CartoonInstructionActivity = this.appId + ".ui.CartoonInstructionActivity";
        try{
            findAndHookMethod(findClass(CartoonInstructionActivity, classLoader), "findViews", FUCK);
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CartoonInstructionActivity)) return;
                findAndHookMethod(clazz, "findViews", FUCK);
            });
        }
    }

    private void NovelInstructionActivity(XC_MethodHook FUCK){
        String NovelInstructionActivity = this.appId + ".ui.NovelInstructionActivity";
        try{
            findAndHookMethod(findClass(NovelInstructionActivity, classLoader), "findViews", FUCK);
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelInstructionActivity)) return;
                findAndHookMethod(clazz, "findViews", FUCK);
            });
        }
    }

    private void CartoonDetailsView(XC_MethodHook FUCK){
        String CartoonDetailsView = this.appId + "_kt.views.custom.CartoonDetailsView";
        try{
            findAndHookConstructor(findClass(CartoonDetailsView, classLoader), Context.class, AttributeSet.class, int.class, FUCK);
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CartoonDetailsView)) return;
                findAndHookConstructor(clazz, Context.class, AttributeSet.class, int.class, FUCK);
            });
        }
    }

    private void NovelBrowseActivity(XC_MethodHook FUCK){
        String NovelBrowseActivity = this.appId + ".ui.NovelBrowseActivity";
        try{
            findAndHookMethod(findClass(NovelBrowseActivity, classLoader), "findViews", FUCK);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(NovelBrowseActivity)) return;
                findAndHookMethod(clazz, "findViews", FUCK);
            });
        }
    }

    private void AdLoadingActivity(XC_MethodHook FUCK){
        String AdLoadingActivity = this.appId + "_kt.ui.AdLoadingActivity";
        try{
            findAndHookMethod(findClass(AdLoadingActivity, classLoader), "addFragment", FUCK);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(AdLoadingActivity)) return;
                findAndHookMethod(clazz, "addFragment", FUCK);
            });
        }
    }
}
