package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.MainHook.getClazz;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;

public class AdService{

    public static void AdServiceInit(){
        // AllClassMethods();
        LandscapeADActivity();
        LTUnionADPlatform();
        PortraitADActivity();
        GuangGaoBean();
        TKBaseTKView();
        CApplication();
        POFactoryImpl();
        ADActivity();
        JDAdSplash();
//        TempDealUtil();
//        NgSyCpAdHelp();
//        TTAdSdk();
    }

    private static void GuangGaoBean(){
        final String GuangGaoBean = TARGET_PACKAGE_NAME + ".bean.GuangGaoBean";
        final XC_MethodHook Fucked = onSetResult(-1, true);
        final Class<?> GuangGaoBeanClass = getClazz(GuangGaoBean);
        if (GuangGaoBeanClass != null) try{
            findAndHookMethod(GuangGaoBeanClass, "getCode", Fucked);
        }catch  (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(GuangGaoBean)) return;
            findAndHookMethod(clazz, "getCode", Fucked);
        });
    }

    private static void LTUnionADPlatform(){
        final String LTUnionADPlatform = TARGET_PACKAGE_NAME + ".ad.adv.LTUnionADPlatform";
        final XC_MethodHook Fucked = onCallMethod("onAdCloseView", true);
        final Class<?> LTUnionADPlatformClass = getClazz(LTUnionADPlatform);
        if (LTUnionADPlatformClass != null) try{
            findAndHookMethod(LTUnionADPlatformClass, "LoadShowInfo", int.class, String.class, Fucked);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(LTUnionADPlatform)) return;
                findAndHookMethod(clazz,"LoadShowInfo", int.class, String.class, Fucked);
            });
    }

    private static void CApplication(){
        final String CApplication = TARGET_PACKAGE_NAME + ".api.CApplication";
        final Class<?> CApplicationClass = getClazz(CApplication);
        if (CApplicationClass != null) {
            try {
                findAndHookMethod(CApplicationClass, "initWhSdk", beforeResultNull);
            } catch (Throwable ignored) {}
            try {
                findAndHookMethod(CApplicationClass, "initTouTiaoAd", beforeResultNull);
            } catch (Throwable ignored) {}
        }else{
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CApplication)) return;
                findAndHookMethod(clazz, "initWhSdk", beforeResultNull);
            });
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CApplication)) return;
                findAndHookMethod(clazz, "initTouTiaoAd", beforeResultNull);
            });
        }

    }

    private static void LandscapeADActivity(){
        final String LandscapeADActivity = "com.qq.e.ads.LandscapeADActivity";
        final Class<?> LandscapeADActivityClass = getClazz(LandscapeADActivity);
        if (LandscapeADActivityClass != null) try{
            findAndHookConstructor(LandscapeADActivityClass, beforeResultNull);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(LandscapeADActivity)) return;
            findAndHookConstructor(clazz, beforeResultNull);
        });
    }

    private static void PortraitADActivity(){
        final String PortraitADActivity = "com.qq.e.ads.PortraitADActivity";
        final Class<?> PortraitADActivityClass = getClazz(PortraitADActivity);
        if (PortraitADActivityClass != null) try{
            findAndHookConstructor(PortraitADActivityClass, beforeResultNull);
        }catch  (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(PortraitADActivity)) return;
            findAndHookConstructor(clazz, beforeResultNull);
        });
    }

    private static void TKBaseTKView(){
        final String[] ClassName = {"com.tachikoma.core.component.TKBase", "com.tachikoma.core.component.view.TKView"};
        final String[] MethodName = {"addEventListener", "dispatchEvent", "removeEventListener"};
        for (String clazz : ClassName) for (String method : MethodName){
            try{
                findAndHookMethod(getClazz(clazz), method, beforeResultNull);
            }catch (Throwable ignored){}
        }
    }

    private static void POFactoryImpl(){
        final String POFactoryImpl = "com.qq.e.comm.plugin.POFactoryImpl";
        final Class<?> POFactoryImplClass = getClazz(POFactoryImpl);
        if (POFactoryImplClass != null) try {
            findAndHookConstructor(POFactoryImplClass, beforeResultNull);
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(POFactoryImpl)) return;
            findAndHookConstructor(clazz, beforeResultNull);
        });
    }

    private static void ADActivity(){
        final String ADActivity = "com.qq.e.ads.ADActivity";
        final Class<?> ADActivityClass = getClazz(ADActivity);
        if (ADActivityClass != null) try{
            findAndHookMethod(ADActivityClass, "onCreate", Bundle.class, onActivityFinish(true));
        }catch (Throwable ignored){}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(ADActivity)) return;
            findAndHookMethod(clazz, "onCreate", Bundle.class, onActivityFinish(true));
        });
    }

    private void TempDealUtil() {
        final String TempDealUtil = TARGET_PACKAGE_NAME + ".ad.adv.channels.NgAdHelper.bean.TempDealUtil";
        final String OnKpAdShowListener = TARGET_PACKAGE_NAME + "ad.adv.channels.NgAdHelper.bean.OnKpAdShowListener";
        final Class<?> TempDealUtilClass = getClazz(TempDealUtil);
        final Class<?> OnKpAdShowListenerClass = getClazz(OnKpAdShowListener);
        if (TempDealUtilClass != null && OnKpAdShowListenerClass != null) try {
            findAndHookMethod(TempDealUtilClass,
                    "showColdStartCp", Context.class, String.class,
                    OnKpAdShowListenerClass, beforeResultNull
            );
        } catch (Throwable ignored) {}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(TempDealUtil)) return;
            hookAllMethods(clazz, "showColdStartCp", beforeResultNull);
        });
    }

    private void NgSyCpAdHelp(){
        final String NgSyCpAdHelp = TARGET_PACKAGE_NAME + ".ad.adv.channels.NgAdHelper.bean.NgSyCpAdHelp";
        final String OnCpAdListener = TARGET_PACKAGE_NAME + ".ad.adv.channels.NgAdHelper.bean.OnCpAdListener";
        final Class<?> NgSyCpAdHelpClass = getClazz(NgSyCpAdHelp);
        final Class<?> OnCpAdListenerClass = getClazz(OnCpAdListener);
        if (NgSyCpAdHelpClass != null && OnCpAdListenerClass != null) try {
            findAndHookMethod(NgSyCpAdHelpClass, "showCpAd", ViewGroup.class,
                    OnCpAdListenerClass, beforeResultNull
            );
        } catch (Throwable ignored) {}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(NgSyCpAdHelp)) return;
            hookAllMethods(clazz, "showCpAd", beforeResultNull);
        });
    }

    private void TTAdSdk(){
        final String TTAdSdk =  "com.bytedance.sdk.openadsdk.TTAdSdk";
        final Class<?> TTAdSdkClass = getClazz(TTAdSdk);
        if (TTAdSdkClass != null) try {
            hookAllMethods(TTAdSdkClass, "init", beforeResultNull);
        } catch (Throwable ignored) {}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(TTAdSdk)) return;
            hookAllMethods(clazz, "init", beforeResultNull);
        });
        TTAdConfig();
    }

    private void TTAdConfig(){
        final String TTAdConfig = "com.bytedance.sdk.openadsdk.TTAdConfig";
        Class<?> TTAdConfigClass = getClazz(TTAdConfig);
        if (TTAdConfigClass != null) try {
            findAndHookMethod(TTAdConfigClass, "getData", onSetResult("", true));
        } catch (Exception ignored) {}
        else inClassLoaderFindAndHook(clazz -> {
            if (!clazz.getName().equals(TTAdConfig)) return;
            findAndHookMethod(clazz, "getData", onSetResult("", true));
        });
    }

    private static void JDAdSplash(){
        final Class<?> JDAdSplash = getClazz("com.ap.android.trunk.sdk.ad.wrapper.jd.JDAdSplash");
        if (JDAdSplash != null) try {
            hookAllMethods(JDAdSplash, "realCreate", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.setResult(null);
                    Log.d(TAG, "狗东？？？");
                }
            });
        } catch (Throwable ignored) {}
    }

    private void JDAdSDK(){
        final Class<?> JDAdSDKClass = getClazz("com.ap.android.trunk.sdk.ad.wrapper.jd.JDAdSDK");
        if  (JDAdSDKClass == null) return;
        try {
            hookAllMethods(JDAdSDKClass, "realInit", beforeResultNull);
        } catch (Throwable ignored) {}
        try {
            findAndHookMethod(JDAdSDKClass, "isSDKAvaliable", onSetResult(false, true));
        } catch (Throwable ignored) {}
    }

    private void AllClassMethods(){
        inClassLoaderFindAndHook(clazz -> {

        });
    }
}
