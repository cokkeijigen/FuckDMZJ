package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import de.robv.android.xposed.XC_MethodHook;

public class AdService {

    private final String appId;
    private final ClassLoader classLoader;
    private final String TAG = "test_";

    public AdService(String appId, ClassLoader classLoader) {
        new AdLayout(appId, classLoader);
        this.appId = appId;
        this.classLoader = classLoader;
        this.init();
    }

    private void init(){
        GuangGaoBean(onSetResult(-1, true));
        LTUnionADPlatform(onCallMethod("onAdCloseView", true));
        LandscapeADActivity(beforeResultNull());
        PortraitADActivity(beforeResultNull());
        TKBaseTKView(beforeResultNull());
        CApplication(beforeResultNull());
//        AllClassMethods(beforeResultNull());
    }

    private void GuangGaoBean(XC_MethodHook FUCK){
        final String GuangGaoBean = appId + ".bean.GuangGaoBean";
        try{
            findAndHookMethod(findClass(GuangGaoBean, classLoader), "getCode", FUCK);
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(GuangGaoBean)) return;
                findAndHookMethod(clazz, "getCode", FUCK);
            });
        }
    }

    private void LTUnionADPlatform(XC_MethodHook FUCK){
        final String LTUnionADPlatform = appId + ".ad.adv.LTUnionADPlatform";
        try{
            findAndHookMethod(findClass(LTUnionADPlatform, classLoader), "LoadShowInfo", int.class, String.class, FUCK);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(LTUnionADPlatform)) return;
                findAndHookMethod(clazz,"LoadShowInfo", int.class, String.class, FUCK);
            });
        }
    }

    private void CApplication(XC_MethodHook FUCK){
        final String CApplication = appId + ".api.CApplication";
        try{
            findAndHookMethod(findClass(CApplication, classLoader), "initTouTiaoAd", FUCK);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(CApplication)) return;
                findAndHookMethod(clazz, "initTouTiaoAd", FUCK);
            });
        }
    }

    private void LandscapeADActivity(XC_MethodHook FUCK){
        final String LandscapeADActivity = "com.qq.e.ads.LandscapeADActivity";
        try{
            findAndHookConstructor(findClass(LandscapeADActivity, classLoader), FUCK);
        }catch (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(LandscapeADActivity)) return;
                findAndHookConstructor(clazz, FUCK);
            });
        }
    }

    private void PortraitADActivity(XC_MethodHook FUCK){
        final String PortraitADActivity = "com.qq.e.ads.PortraitADActivity";
        try{
            findAndHookConstructor(findClass(PortraitADActivity, classLoader), FUCK);
        }catch  (Throwable ignored){
            inClassLoaderFindAndHook(clazz -> {
                if (!clazz.getName().equals(PortraitADActivity)) return;
                findAndHookConstructor(clazz, FUCK);
            });
        }
    }

    private void TKBaseTKView(XC_MethodHook FUCK){
        final String[] ClassName = {"com.tachikoma.core.component.TKBase", "com.tachikoma.core.component.view.TKView"};
        final String[] MethodName = {"addEventListener", "dispatchEvent", "removeEventListener"};
        for (String clazz : ClassName) for (String method : MethodName){
            try{
                findAndHookMethod(findClass(clazz, classLoader), method, FUCK);
            }catch (Throwable ignored){}
        }
    }

    private void AllClassMethods(XC_MethodHook FUCK){
        inClassLoaderFindAndHook(clazz -> {

        });
    }
}
