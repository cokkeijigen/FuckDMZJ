package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.robv.android.xposed.XC_MethodHook;

public final class AdLayout {

    public static void initClassHooks() {
        InstructionActivity();
        NovelBrowseActivity();
        CartoonDetailsView();
        AdLoadingActivity();
        LaunchInterceptorActivity();
    }

    private static void InstructionActivity() {
        final XC_MethodHook layout_ad_layout = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_ad_layout = (RelativeLayout) getField(param, "layout_ad_layout");
                layout_ad_layout.setVisibility(View.GONE);
            }
        };
        CartoonInstructionActivity(layout_ad_layout);
        NovelInstructionActivity(layout_ad_layout);
    }

    private static void CartoonInstructionActivity(XC_MethodHook Fucked){
        final String CartoonInstructionActivity = TARGET_PACKAGE_NAME + ".ui.CartoonInstructionActivity";
        final Class<?> CartoonInstructionActivityClass = getClazz(CartoonInstructionActivity);
        if (CartoonInstructionActivityClass != null) try{
            findAndHookMethod(CartoonInstructionActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
    }

    private static void NovelInstructionActivity(XC_MethodHook Fucked){
        final String NovelInstructionActivity = TARGET_PACKAGE_NAME + ".ui.NovelInstructionActivity";
        final Class<?> NovelInstructionActivityClass = getClazz(NovelInstructionActivity);
        if (NovelInstructionActivityClass != null) try {
            findAndHookMethod(NovelInstructionActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
    }

    private static void CartoonDetailsView(){
        final String CartoonDetailsView = TARGET_PACKAGE_NAME + "_kt.views.custom.CartoonDetailsView";
        final XC_MethodHook Fucked = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                FrameLayout adLayout = (FrameLayout) getField(param, "adLayout");
                adLayout.setVisibility(View.GONE);
            }
        };
        final Class<?> CartoonDetailsViewClass = getClazz(CartoonDetailsView);
        if (CartoonDetailsViewClass != null) try{
            findAndHookConstructor(CartoonDetailsViewClass, Context.class, AttributeSet.class, int.class, Fucked);
        }catch (Throwable ignored){}
    }

    private static void NovelBrowseActivity(){
        final String NovelBrowseActivity = TARGET_PACKAGE_NAME + ".ui.NovelBrowseActivity";
        final XC_MethodHook Fucked = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout layout_container = (RelativeLayout) getField(param, "layout_container");
                ViewGroup.LayoutParams layoutParams = layout_container.getLayoutParams();
                layoutParams.height = 0;
                layout_container.setLayoutParams(layoutParams);
            }
        };
        final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
        if (NovelBrowseActivityClass != null) try{
            findAndHookMethod(NovelBrowseActivityClass, "findViews", Fucked);
        }catch (Throwable ignored){}
    }

    private static void AdLoadingActivity(){
        final String AdLoadingActivity = TARGET_PACKAGE_NAME + "_kt.ui.AdLoadingActivity";
        final XC_MethodHook Fucked = onActivityFinish(false);
        final Class<?> AdLoadingActivityClass = getClazz(AdLoadingActivity);
        if (AdLoadingActivityClass == null) try{
            findAndHookMethod(AdLoadingActivityClass, "addFragment", Fucked);
        }catch (Throwable ignored){}
    }

    @SuppressLint({"NewApi", "UseCompatLoadingForDrawables", "SetTextI18n"})
    private static void LaunchReplacement(XC_MethodHook.MethodHookParam param, int color){
        Context mContext = (Context) param.thisObject;
        Activity mActivty = (Activity) param.thisObject;
        if (!mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE).getBoolean("app_usered", false))
            return;
        LinearLayout linearLayout = new LinearLayout(mActivty);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(color);
        linearLayout.setPadding(110, linearLayout.getPaddingTop(), 110, linearLayout.getPaddingBottom());
        ImageView imageView = new ImageView(mActivty);
        int resourceId = mContext.getResources().getIdentifier("img_lauch_bitch", "drawable", TARGET_PACKAGE_NAME);
        imageView.setImageDrawable(mContext.getDrawable(resourceId));
        TextView textView = new TextView(mActivty);
        textView.setText("-Started By FuckDMZJXposed-");
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(Color.parseColor("#ffffff"));
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
                intent.setClass(mContext, getClazz(TARGET_PACKAGE_NAME + ".ui.home.HomeTabsActivitys"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                callMethod(param.thisObject, "finish");
            } catch (Exception ignored) {}
        }).start();
    }

    public static void LaunchInterceptorActivity() {
        final String LaunchInterceptorActivity = TARGET_PACKAGE_NAME + ".ui.LaunchInterceptorActivity";
        final Class<?> LaunchInterceptorActivityClass = getClazz(LaunchInterceptorActivity);
        if (LaunchInterceptorActivityClass == null) return;
        if (TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN)) try {
            findAndHookMethod(LaunchInterceptorActivityClass, "createContent", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    LaunchReplacement(param, Color.parseColor("#ffaf25"));
                }
            });
        } catch (Throwable ignored) {}
        else try {
            findAndHookMethod(LaunchInterceptorActivityClass, "onResume", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    LaunchReplacement(param, Color.parseColor("#0080ec"));
                }
            });
        }catch (Throwable ignored){}
    }
}
