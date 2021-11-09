package ss.colytitse.fuckdmzj.hook;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import java.lang.reflect.Field;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ss.colytitse.fuckdmzj.MainHook;

public class fucks {

    // 获取控件
    private static Object getView(XC_MethodHook.MethodHookParam param,String id_name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(id_name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    // 去除广告
    public static void fuck_AD(ClassLoader classLoader, String PKGN) {

        try{   // 规则一
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".bean.GuangGaoBean", classLoader),
                    "getCode",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.setResult(-1);
                            XposedBridge.log("FUDM_RU_01: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_RU_01:" + t.toString());
        }

        try {   // 规则二
            Class<?> clazz = XposedHelpers.findClass(PKGN+".bean.GuangGaoBean",classLoader);
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".ad.adv.channels", classLoader), "loadFloatAd", clazz, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(null);
                            XposedBridge.log("FUDM_RU_02: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_RU_02:" + t.toString());
        }

        try{   // 规则三
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".ad.adv.LTUnionADPlatform", classLoader),
                    "LoadShowInfo", int.class, String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedHelpers.callMethod(param.thisObject, "onAdCloseView");
                            param.setResult(null);
                            XposedBridge.log("FUDM_RU_03: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_RU_03:" + t.toString());
        }

        try {  // 规则四
            Class<?> clazz = XposedHelpers.findClass(PKGN + ".ad.adv.LTUnionADPlatform", classLoader);
            XC_MethodHook FUCK = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(null);
                    XposedBridge.log("FUDM_RU_04: SUCCESS");
                }
            };
            XposedHelpers.findAndHookMethod(clazz,"displayAd",ViewGroup.class,int.class,FUCK);
            XposedHelpers.findAndHookMethod(clazz,"displayAd",ViewGroup.class,int.class,boolean.class,FUCK);
            XposedHelpers.findAndHookMethod(clazz,"displayAd",ViewGroup.class,int.class,Context.class,FUCK);
        }catch (Throwable t){
            XposedBridge.log("FUDM_RU_04:" + t.toString());
        }

        try{   // 去除详细页的广告位
            String[] ad_class_list = {".ui.CartoonInstructionActivity",".ui.NovelInstructionActivity"};
            for(String ad_class : ad_class_list){   /* 通用方案 */
                try{
                    XposedHelpers.findAndHookMethod(
                            XposedHelpers.findClass(PKGN + ad_class, classLoader),
                            "findViews",
                            new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    RelativeLayout layout_ad_layout = (RelativeLayout) getView(param,"layout_ad_layout");
                                    layout_ad_layout.setVisibility(View.GONE);
                                    XposedBridge.log("FUDM_AD_findViews_01_s1: SUCCESS");
                                }
                            }
                        );
                    }catch (Throwable ignored){}
            }

            if(PKGN.equals(MainHook.DMZJSQ_PKGN))  /* 社区版处理方案 */
                XposedHelpers.findAndHookConstructor("com.dmzjsq.manhua_kt.views.custom.CartoonDetailsView",
                        classLoader, Context.class, AttributeSet.class, int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                FrameLayout adLayout = (FrameLayout) getView(param,"adLayout");
                                adLayout.setVisibility(View.GONE);
                                XposedBridge.log("FUDM_AD_findViews_01_s2: SUCCESS");
                            }
                });
        }catch (Throwable t){
            XposedBridge.log("FUDM_AD_findViews_01:" + t.toString());
        }

        try {   // 小说阅读页面广告位
            XposedHelpers.findAndHookMethod(PKGN + ".ui.NovelBrowseActivity", classLoader,
                    "findViews", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    RelativeLayout layout_container = (RelativeLayout)getView(param,"layout_container");
                    ViewGroup.LayoutParams layoutParams = layout_container.getLayoutParams();
                    layoutParams.height = 0; // 不知道为啥设置Visibility不管用，只好把控件高度设置为0
                    layout_container.setLayoutParams(layoutParams);
                    XposedBridge.log("FUDM_AD_findViews_02: SUCCESS");
                }
            });
        }catch (Throwable t){
            XposedBridge.log("FUDM_AD_findViews_02:" + t.toString());
        }
    }

    // 阻止更新检测
    public static void fuck_CheckVersionInfo(ClassLoader classLoader, String PKGN){
        try{
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".helper.AppUpDataHelper",classLoader),
                    "checkVersionInfo", Activity.class, Class.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(null);
                            XposedBridge.log("FUDM_CheckVersionInfo: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_CheckVersionInfo:" + t.toString());
        }
    }

    // 关闭青少年傻逼弹窗
    public static void fuck_TeenagerMode(ClassLoader classLoader, String PKGN){
        try {
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + "_kt.ui.TeenagerModeDialogActivity", classLoader),
                    "initView",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedHelpers.callMethod(param.thisObject, "finish");
                            param.setResult(null);
                            XposedBridge.log("FUDM_TeenagerMode: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_TeenagerMode:" + t.toString());
        }
    }

    // 阻止粘贴板被强○
   public static void DoNotFuckMyClipboard(ClassLoader classLoader) {

    }
}