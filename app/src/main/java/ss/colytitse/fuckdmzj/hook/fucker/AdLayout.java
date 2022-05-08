package ss.colytitse.fuckdmzj.hook.fucker;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.fucker.Others.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class AdLayout {

    private final ClassLoader classLoader;
    private final String appId;

    public AdLayout(String appId,ClassLoader classLoader) {
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
                ViewGroup.LayoutParams layoutParams = layout_container.getLayoutParams();
                layoutParams.height = 0; // 不知道为啥设置Visibility不管用，只好把控件高度设置为0
                layout_container.setLayoutParams(layoutParams);
            }
        };

        this.CartoonInstructionActivity(layout_ad_layout);
        this.NovelInstructionActivity(layout_ad_layout);
        this.NovelBrowseActivity(layout_container);
        this.CartoonDetailsView(adLayout);
    }

    private void CartoonInstructionActivity(XC_MethodHook FUCK){
        try{
            Class<?> CartoonInstructionActivity = findClass(this.appId + ".ui.CartoonInstructionActivity", classLoader);
            findAndHookMethod(CartoonInstructionActivity, "findViews", FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> CartoonInstructionActivity： " + t);
        }
    }

    private void NovelInstructionActivity(XC_MethodHook FUCK){
        try{
            Class<?> CartoonInstructionActivity = findClass(this.appId + ".ui.CartoonInstructionActivity", classLoader);
            findAndHookMethod(CartoonInstructionActivity, "findViews", FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> NovelInstructionActivity： " + t);
        }
    }

    private void CartoonDetailsView(XC_MethodHook FUCK){
        try{
            Class<?> CartoonDetailsView = findClass(this.appId + "_kt.views.custom.CartoonDetailsView", classLoader);
            findAndHookConstructor(CartoonDetailsView, Context.class, AttributeSet.class, int.class, FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> CartoonDetailsView： " + t);
        }
    }

    private void NovelBrowseActivity(XC_MethodHook FUCK){
        try{
            Class<?> NovelBrowseActivity = findClass(this.appId + ".ui.NovelBrowseActivity", classLoader);
            findAndHookMethod(NovelBrowseActivity, "findViews", FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> NovelBrowseActivity： " + t);
        }
    }
}
