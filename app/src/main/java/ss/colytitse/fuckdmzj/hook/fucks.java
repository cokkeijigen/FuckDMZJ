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

    // 对象包名
    private static String PKGN;
    // 类加载器
    private static ClassLoader classLoader;

    public fucks(ClassLoader classLoader, String PKGN) /* 初始化 */ {
        fucks.classLoader = classLoader;
        fucks.PKGN = PKGN;
    }

    // 获取字段
    private static Object getField(XC_MethodHook.MethodHookParam param,String name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    // 去除广告
    public void fuckAdByAll() {

        try /* 规则一 */ {
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

        try /* 规则二 */ {
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".ad.adv.LTUnionADPlatform", classLoader),
                    "LoadShowInfo", int.class, String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedHelpers.callMethod(param.thisObject, "onAdCloseView");
                            param.setResult(null);
                            XposedBridge.log("FUDM_RU_02: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_RU_02: " + t.toString());
        }

        try /* 去除小说与漫画详细页的广告位 */ {
            String[] ad_class_list = {".ui.CartoonInstructionActivity",".ui.NovelInstructionActivity"};
            for(String ad_class : ad_class_list) /* 通用方案 */{
                try{
                    XposedHelpers.findAndHookMethod(
                            XposedHelpers.findClass(PKGN + ad_class, classLoader),
                            "findViews",
                            new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    RelativeLayout layout_ad_layout = (RelativeLayout) getField(param,"layout_ad_layout");
                                    layout_ad_layout.setVisibility(View.GONE);
                                    XposedBridge.log("FUDM_AD_findViews_01_s1: SUCCESS");
                                }
                            }
                        );
                    }catch (Throwable ignored){}
            }

            if(PKGN.equals(MainHook.DMZJSQ_PKGN))  /* 社区版处理方案 */{
                XposedHelpers.findAndHookConstructor(PKGN + "_kt.views.custom.CartoonDetailsView",
                        classLoader, Context.class, AttributeSet.class, int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                FrameLayout adLayout = (FrameLayout) getField(param,"adLayout");
                                adLayout.setVisibility(View.GONE);
                                XposedBridge.log("FUDM_AD_findViews_01_s2: SUCCESS");
                            }
                });}
        }catch (Throwable t){
            XposedBridge.log("FUDM_AD_findViews_01:" + t.toString());
        }

        try /* 去除小说阅读页面广告位 */ {
            XposedHelpers.findAndHookMethod(PKGN + ".ui.NovelBrowseActivity", classLoader,
                    "findViews", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    RelativeLayout layout_container = (RelativeLayout)getField(param,"layout_container");
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
    public void fuckAppUpData(){
        try{
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + ".helper.AppUpDataHelper",classLoader),
                    "checkVersionInfo", Activity.class, Class.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.setResult(null);
                            XposedBridge.log("FUDM_AppUpData: SUCCESS");
                        }
                    }
            );
        }catch (Throwable t){
            XposedBridge.log("FUDM_AppUpData:" + t.toString());
        }
    }

    // 关闭青少年傻逼弹窗
    public void fuckTeenagerMode(){
        try {
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(PKGN + "_kt.ui.TeenagerModeDialogActivity", classLoader),
                    "initView",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
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
   public void DoNotFuckMyClipboard() {
        String str = " DoNotFuckMyClipboard";
        String logs = PKGN.equals(MainHook.DMZJ_PKGN) ? "DMZJ"+str : "DMZJSQ"+str;

        try {
           XposedHelpers.findAndHookMethod(
                   XposedHelpers.findClass("android.content.ClipData", classLoader),
                   "newPlainText", CharSequence.class, CharSequence.class,
                   new XC_MethodHook() {
                       @Override
                       protected void beforeHookedMethod(MethodHookParam param) {
                           String s = param.args[1].toString();
                           if(!(param.args[1].toString().contains("http"))){
                               param.args[1]="";
                               param.setResult(null);
                               XposedBridge.log(logs +":"+ s);
                           }
                       }
                   }
           );
        }catch (Throwable t){
            XposedBridge.log(logs +" 01 ERR:" + t.toString());
        }
        try{
           XposedHelpers.findAndHookMethod(
                   XposedHelpers.findClass("android.content.ClipboardManager", classLoader),
                   "setText", CharSequence.class, new XC_MethodHook() {
                       @Override
                       protected void beforeHookedMethod(MethodHookParam param) {
                           String s = (param.args[0]).toString();
                           if(!(param.args[0].toString().contains("http"))){
                               param.args[0]="";
                               param.setResult(null);
                               XposedBridge.log(logs +":"+ s);
                           }
                       }
                   }
           );
        }catch (Throwable t){
            XposedBridge.log(logs +" 02 ERR:" + t.toString());
        }
    }

    // 自动签到
    public void UserSign(){
        XposedBridge.log("");
    }
}
