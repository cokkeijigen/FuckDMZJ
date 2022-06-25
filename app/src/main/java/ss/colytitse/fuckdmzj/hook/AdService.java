package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.*;
import static ss.colytitse.fuckdmzj.MainHook.getClazz;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public final class AdService{

    public static void initClassHooks(){
        LandscapeADActivity();
        LTUnionADPlatform();
        PortraitADActivity();
        GuangGaoBean();
        TKBaseTKView();
        CApplication();
        POFactoryImpl();
        ADActivity();
        JDAdSDK();
        JDAdSplash();
        TempDealUtil();
        NgSyCpAdHelp();
        TTAdSdk();
    }

    private static void GuangGaoBean(){
        final String GuangGaoBean = TARGET_PACKAGE_NAME + ".bean.GuangGaoBean";
        final XC_MethodHook Fucked = onSetResult(-1, true);
        final Class<?> GuangGaoBeanClass = getClazz(GuangGaoBean);
        if (GuangGaoBeanClass != null) try{
            findAndHookMethod(GuangGaoBeanClass, "getCode", Fucked);
        }catch  (Throwable ignored){}
    }

    private static void LTUnionADPlatform(){
        final String LTUnionADPlatform = TARGET_PACKAGE_NAME + ".ad.adv.LTUnionADPlatform";
        final XC_MethodHook Fucked = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                callMethod(param.thisObject, "onAdCloseView");
            }
        };
        final Class<?> LTUnionADPlatformClass = getClazz(LTUnionADPlatform);
        if (LTUnionADPlatformClass != null) try{
            findAndHookMethod(LTUnionADPlatformClass, "LoadShowInfo", int.class, String.class, Fucked);
        }catch (Throwable ignored){}
    }

    private static void CApplication(){
        final String CApplication = TARGET_PACKAGE_NAME + ".api.CApplication";
        final Class<?> CApplicationClass = getClazz(CApplication);
        if (CApplicationClass != null) {
            try {
                findAndHookMethod(CApplicationClass, "initWhSdk", onReturnVoid);
            } catch (Throwable ignored) {}
            try {
                findAndHookMethod(CApplicationClass, "initTouTiaoAd", onReturnVoid);
            } catch (Throwable ignored) {}
        }else{
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CApplication)) return;
                findAndHookMethod(clazz, "initWhSdk", onReturnVoid);
            });
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CApplication)) return;
                findAndHookMethod(clazz, "initTouTiaoAd", onReturnVoid);
            });
        }
    }

    private static void LandscapeADActivity(){
        final String LandscapeADActivity = "com.qq.e.ads.LandscapeADActivity";
        final Class<?> LandscapeADActivityClass = getClazz(LandscapeADActivity);
        if (LandscapeADActivityClass != null) try{
            findAndHookConstructor(LandscapeADActivityClass, onReturnVoid);
        }catch (Throwable ignored){}
    }

    private static void PortraitADActivity(){
        final String PortraitADActivity = "com.qq.e.ads.PortraitADActivity";
        final Class<?> PortraitADActivityClass = getClazz(PortraitADActivity);
        if (PortraitADActivityClass != null) try{
            findAndHookConstructor(PortraitADActivityClass, onReturnVoid);
        }catch  (Throwable ignored){}
    }

    private static void TKBaseTKView(){
        final String[] ClassName = {"com.tachikoma.core.component.TKBase", "com.tachikoma.core.component.view.TKView"};
        final String[] MethodName = {"addEventListener", "dispatchEvent", "removeEventListener"};
        for (String clazz : ClassName) for (String method : MethodName){
            try{
                findAndHookMethod(getClazz(clazz), method, onReturnVoid);
            }catch (Throwable ignored){}
        }
    }

    private static void POFactoryImpl(){
        final String POFactoryImpl = "com.qq.e.comm.plugin.POFactoryImpl";
        final Class<?> POFactoryImplClass = getClazz(POFactoryImpl);
        if (POFactoryImplClass != null) try {
            findAndHookConstructor(POFactoryImplClass, onReturnVoid);
        }catch (Throwable ignored){}
    }

    private static void ADActivity(){
        final String ADActivity = "com.qq.e.ads.ADActivity";
        final Class<?> ADActivityClass = getClazz(ADActivity);
        if (ADActivityClass != null) try{
            findAndHookMethod(ADActivityClass, "onCreate", Bundle.class, onActivityFinish(true));
        }catch (Throwable ignored){}
    }

    private static void TempDealUtil() {
        final String TempDealUtil = TARGET_PACKAGE_NAME + ".ad.adv.channels.NgAdHelper.bean.TempDealUtil";
        final String OnKpAdShowListener = TARGET_PACKAGE_NAME + "ad.adv.channels.NgAdHelper.bean.OnKpAdShowListener";
        final Class<?> TempDealUtilClass = getClazz(TempDealUtil);
        final Class<?> OnKpAdShowListenerClass = getClazz(OnKpAdShowListener);
        if (TempDealUtilClass != null && OnKpAdShowListenerClass != null) try {
            findAndHookMethod(TempDealUtilClass,
                    "showColdStartCp", Context.class, String.class,
                    OnKpAdShowListenerClass, onReturnVoid
            );
        } catch (Throwable ignored) {}
    }

    private static void NgSyCpAdHelp(){
        final String NgSyCpAdHelp = TARGET_PACKAGE_NAME + ".ad.adv.channels.NgAdHelper.bean.NgSyCpAdHelp";
        final String OnCpAdListener = TARGET_PACKAGE_NAME + ".ad.adv.channels.NgAdHelper.bean.OnCpAdListener";
        final Class<?> NgSyCpAdHelpClass = getClazz(NgSyCpAdHelp);
        final Class<?> OnCpAdListenerClass = getClazz(OnCpAdListener);
        if (NgSyCpAdHelpClass != null && OnCpAdListenerClass != null) try {
            findAndHookMethod(NgSyCpAdHelpClass, "showCpAd", ViewGroup.class,
                    OnCpAdListenerClass, onReturnVoid
            );
        } catch (Throwable ignored) {}
    }

    private static void JDAdSplash(){
        final String JDAdSplash = "com.ap.android.trunk.sdk.ad.wrapper.jd.JDAdSplash";
        final Class<?> JDAdSplashClass = getClazz(JDAdSplash);
        if (JDAdSplashClass != null) try {
            hookAllMethods(JDAdSplashClass, "realCreate", onReturnVoid);
        } catch (Throwable ignored) {}
    }

    private static void JDAdSDK(){
        final String JDAdSDK = "com.ap.android.trunk.sdk.ad.wrapper.jd.JDAdSDK";
        final Class<?> JDAdSDKClass = getClazz(JDAdSDK);
        if  (JDAdSDKClass != null) {
            try {
                hookAllMethods(JDAdSDKClass, "realInit", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        XposedBridge.log("进入hook");
                    }
                });
            } catch (Throwable ignored) {
            }
            try {
                findAndHookMethod(JDAdSDKClass, "isSDKAvaliable", onSetResult(false, true));
            } catch (Throwable ignored) {
            }
        }else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(JDAdSDK)) return;
            XposedBridge.log("找到类" + clazz.getName());
            try{
                hookAllMethods(clazz, "realInit", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        XposedBridge.log("进入hook");
                    }
                });
            }catch (Exception e){
                XposedBridge.log("错误？" + e);
            }
        });
    }

    private static void TTAdSdk(){
        final String TTAdSdk =  "com.bytedance.sdk.openadsdk.TTAdSdk";
        final Class<?> TTAdSdkClass = getClazz(TTAdSdk);
        if (TTAdSdkClass != null) try {
            hookAllMethods(TTAdSdkClass, "init", onReturnVoid);
        } catch (Throwable ignored) {}
        TTAdConfig();
    }

    private static void TTAdConfig(){
        final String TTAdConfig = "com.bytedance.sdk.openadsdk.TTAdConfig";
        Class<?> TTAdConfigClass = getClazz(TTAdConfig);
        if (TTAdConfigClass != null) try {
            findAndHookMethod(TTAdConfigClass, "getData", onSetResult("", true));
        } catch (Throwable ignored) {}
    }
}
