package ss.colytitse.fuckdmzj.hook.fucker;

import static de.robv.android.xposed.XposedHelpers.*;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class AdService {

    private final ClassLoader classLoader;
    private final String appId;

    public AdService(String appId, ClassLoader classLoader) {
        new AdLayout(appId, classLoader);
        this.classLoader = classLoader;
        this.appId = appId;
        this.init();
    }

    private void init(){

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(-1);
            }
        };
        XC_MethodHook onAdCloseView = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                callMethod(param.thisObject, "onAdCloseView");
                param.setResult(null);
            }
        };

        XC_MethodHook xc_methodHook1 = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(null);
            }
        };

        this.GuangGaoBean(xc_methodHook);
        this.LTUnionADPlatform(onAdCloseView);
        this.LandscapeADActivity(xc_methodHook1);
        this.PortraitADActivity(xc_methodHook1);
    }

    private void GuangGaoBean(XC_MethodHook FUCK){
        try{
            Class<?> GuangGaoBean = findClass(this.appId + ".bean.GuangGaoBean", classLoader);
            findAndHookMethod(GuangGaoBean, "getCode", FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> GuangGaoBean： " + t);
        }
    }

    private void LTUnionADPlatform(XC_MethodHook FUCK){
        try{
            Class<?> LTUnionADPlatform = findClass(this.appId + ".ad.adv.LTUnionADPlatform", classLoader);
            XposedHelpers.findAndHookMethod(LTUnionADPlatform, "LoadShowInfo", int.class, String.class, FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> LTUnionADPlatform： " + t);
        }
    }

    private void LandscapeADActivity(XC_MethodHook FUCK){
        try{
            Class<?> LandscapeADActivity = findClass("com.qq.e.ads.LandscapeADActivity", classLoader);
            XposedHelpers.findAndHookConstructor(LandscapeADActivity, FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> LandscapeADActivity： " + t);
        }
    }

    private void PortraitADActivity(XC_MethodHook FUCK){
        try{
            Class<?> PortraitADActivity = findClass("com.qq.e.ads.PortraitADActivity", classLoader);
            XposedHelpers.findAndHookConstructor(PortraitADActivity, FUCK);
        }catch (Throwable t){
            XposedBridge.log("FuckDMZJ -> PortraitADActivity： " + t);
        }
    }
}