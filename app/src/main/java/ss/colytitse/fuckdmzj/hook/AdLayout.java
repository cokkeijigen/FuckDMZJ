package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.newHookMethods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import ss.colytitse.fuckdmzj.test.PublicContent;

public final class AdLayout extends PublicContent {

    public static void initClassHooks() {
        InstructionActivity();
        NovelBrowseActivity();
        CartoonDetailsView();
        AdLoadingActivity();
    }

    private static void InstructionActivity() {
        final XC_MethodHook layout_ad_layout = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                Context thisObject = (Context) param.thisObject;
                int identifier = getIdentifier(thisObject,  "id", "layout_ad_layout");
                RelativeLayout layout_ad_layout =  activity.findViewById(identifier);
                ViewGroup.LayoutParams layoutParams = layout_ad_layout.getLayoutParams();
                layoutParams.height = 0;
                layout_ad_layout.setLayoutParams(layoutParams);
                layout_ad_layout.setVisibility(View.GONE);
            }
        };
        // CartoonInstructionActivity   --漫画详细界面广告位
        final Class<?> CartoonInstructionActivityClass = getThisPackgeClass(".ui.CartoonInstructionActivity");
        if (CartoonInstructionActivityClass != null) try{
            findAndHookMethod(CartoonInstructionActivityClass, "onResume", layout_ad_layout);
        }catch (Throwable ignored){}
        // NovelInstructionActivity   --小说详细界面广告位
        final Class<?> NovelInstructionActivityClass = getThisPackgeClass(".ui.NovelInstructionActivity");
        if (NovelInstructionActivityClass != null) try {
            findAndHookMethod(NovelInstructionActivityClass, "onResume", layout_ad_layout);
        }catch (Throwable ignored){}

    }

    private static void CartoonDetailsView(){
        final Class<?> CartoonDetailsViewClass = getThisPackgeClass("_kt.views.custom.CartoonDetailsView");
        if (CartoonDetailsViewClass != null) try{
            final XC_MethodHook Fucked = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    FrameLayout adLayout = (FrameLayout) getField(param, "adLayout");
                    adLayout.setVisibility(View.GONE);
                }
            };
            findAndHookConstructor(CartoonDetailsViewClass, Context.class, AttributeSet.class, int.class, Fucked);
        }catch (Throwable ignored){}
    }

    private static void NovelBrowseActivity(){
        final Class<?> NovelBrowseActivityClass = getThisPackgeClass(".ui.NovelBrowseActivity");
        if (NovelBrowseActivityClass != null) try {
            newHookMethods(NovelBrowseActivityClass, "onStart", (HookCallBack) param -> {
                Context thisContext = (Context) param.thisObject;
                Activity thisActivity = (Activity) param.thisObject;
                int identifier = getIdentifier(thisContext, "id", "layout_container");
                RelativeLayout layout_container =  thisActivity.findViewById(identifier);
                ViewGroup.LayoutParams layoutParams = layout_container.getLayoutParams();
                layoutParams.height = 0;
                layout_container.setLayoutParams(layoutParams);
            });
        } catch (Exception ignored) {}
    }

    private static void AdLoadingActivity(){
        final Class<?> AdLoadingActivityClass = getThisPackgeClass("_kt.ui.AdLoadingActivity");
        if (AdLoadingActivityClass != null) try{
            final XC_MethodHook Fucked = onActivityFinish(false);
            findAndHookMethod(AdLoadingActivityClass, "addFragment", Fucked);
        }catch (Throwable ignored){}
    }

    @SuppressLint({"NewApi", "UseCompatLoadingForDrawables", "SetTextI18n", "ResourceType"})
    private static void LaunchReplacement(XC_MethodHook.MethodHookParam param, int res){
        int color = Color.parseColor(DMZJ_PKGN.equals(TARGET_PACKAGE_NAME) ? "#0080ec" : "#ffaf25");
        Context mContext = (Context) param.thisObject;
        Activity mActivty = (Activity) param.thisObject;
        LinearLayout linearLayout = new LinearLayout(mActivty);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(110, linearLayout.getPaddingTop(), 110, linearLayout.getPaddingBottom());
        TextView textView = new TextView(mActivty);
        textView.setText("-Started By FuckDMZJXposed-");
        ImageView imageView = new ImageView(mActivty);
        textView.setTextColor(Color.parseColor("#ffffff"));
        try {
            imageView.setImageDrawable(mContext.getDrawable(res));
        }catch (Exception igonred){
            // lspatched
            int lsp_img_lauch_bitch = getIdentifier(mContext, "drawable", "img_lauch_bitch");
            imageView.setImageDrawable(mContext.getDrawable(lsp_img_lauch_bitch));
            if (DMZJSQ_PKGN.equals(TARGET_PACKAGE_NAME)) {
                textView.setTextColor(Color.parseColor("#000000"));
                color = Color.parseColor("#ffffff");
            }
        }
        linearLayout.setBackgroundColor(color);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setPadding(0,100,0,0);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        mActivty.setContentView(linearLayout);
        new Thread(()->{
            try {
                Thread.sleep(500);
                Intent intent = new Intent();
                intent.setClass(mContext, getThisPackgeClass(".ui.home.HomeTabsActivitys"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                callMethod(param.thisObject, "finish");
            } catch (Exception ignored) {}
        }).start();
        setActivityFullscreen(mActivty);
    }

    public static boolean notFirstRunning(XC_MethodHook.MethodHookParam param){
        return ((Context)param.thisObject).getSharedPreferences("user_info", Context.MODE_PRIVATE)
                .getBoolean("app_usered", false);
    }

    public static void LaunchInterceptorActivity(int res) {
        final Class<?> LaunchInterceptorActivity = getThisPackgeClass(".ui.LaunchInterceptorActivity");
        if (LaunchInterceptorActivity == null) return;
        try {
            findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if(!param.thisObject.getClass().equals(LaunchInterceptorActivity)) return;
                    if (!notFirstRunning(param)) return;
                    for (Method method : LaunchInterceptorActivity.getDeclaredMethods()) {
                        if (method.getReturnType().equals(void.class)  && (
                                !"onCreate&onStart&onRestart&onResume&onPause&onStop&onDestroy"
                                        .contains(method.getName())
                        )) try {
                            XposedBridge.hookMethod(method, onReturnVoid);
                        }catch (Exception ignored){}
                    }
                }
            });
            newHookMethods(LaunchInterceptorActivity,"onCreate", (HookCallBack) param-> {
                if (notFirstRunning(param)) LaunchReplacement(param, res);
            });
        }catch (Throwable ignored){}
    }
}
