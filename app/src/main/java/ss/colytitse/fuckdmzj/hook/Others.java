package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.stream.Collectors;
import de.robv.android.xposed.XC_MethodHook;

public final class Others {

    public static final String TAG = "test_";

    public static void initClassHooks(){
        allActivitySetStatusBar();
        AppUpDataHelper();
        ActivityOptimization();
        TeenagerModeDialogActivity();
        DoNotFuckMyClipboard();
        AutoSign.SignInView();
        AutoSign.init();
    }

    // 获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) return context.getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    // 界面显示优化
    private static void ActivityOptimization(){
        XC_MethodHook onActivityFullscreen = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                setActivityFullscreen((Activity) param.thisObject);
            }
        };

        XC_MethodHook onNovelBrowseActivity = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                setActivityFullscreen(activity);
                View decorView = activity.getWindow().getDecorView();
                decorView.post(() -> {
                    if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) return;
                    if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) return;
                    if (decorView.getRootWindowInsets().getDisplayCutout() == null) return;
                    try {
                        FrameLayout framelayout = (FrameLayout) getField(param, "framelayout");
                        int left = framelayout.getPaddingStart();
                        int top = getStatusBarHeight((Context) param.thisObject);
                        int right = framelayout.getPaddingEnd();
                        int bottom = framelayout.getPaddingBottom();
                        framelayout.setPadding(left, top, right, bottom);
                    } catch (Throwable ignored) {}
                });
            }
        };

        // 普通版
        if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN))
            OptimizationDMZJ(onActivityFullscreen, onNovelBrowseActivity);
        // 社区版
        if(TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN))
            OptimizationDMZJSQ(onActivityFullscreen, onNovelBrowseActivity);
    }

    // 状态栏优化
    private static void allActivitySetStatusBar(){
        final XC_MethodHook setStatusBar = onSetActivityStatusBar(Color.WHITE);
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, setStatusBar);
    }

    private static void OptimizationDMZJSQ(XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        {   // 漫画阅读界面
            final String BrowseActivityAncestors4 = "com.dmzjsq.manhua.ui.abc.viewpager2.BrowseActivityAncestors4";
            final Class<?> BrowseActivityAncestors4Class = getClazz(BrowseActivityAncestors4);
            if (BrowseActivityAncestors4Class != null) try {
                findAndHookMethod(BrowseActivityAncestors4Class, "publicFindViews", onActivityFullscreen);
            }catch (Throwable ignored){}
        }
        {   // 小说阅读界面
            final String NovelBrowseActivity = "com.dmzjsq.manhua.ui.NovelBrowseActivity";
            final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
            if (NovelBrowseActivityClass != null) try {
                findAndHookMethod(NovelBrowseActivityClass, "findViews", onNovelBrowseActivity);
            }catch (Throwable ignored){}
        }
        {   // 分享页
            final String ShareActivityV2 = "com.dmzjsq.manhua.ui.ShareActivityV2";
            final Class<?> ShareActivityV2Class = getClazz(ShareActivityV2);
            if (ShareActivityV2Class != null) try {
                findAndHookMethod(ShareActivityV2Class, "createContent", onActivityFullscreen);
            }catch (Throwable ignored){}
        }
    }

    private static void OptimizationDMZJ(XC_MethodHook onActivityFullscreen, XC_MethodHook onNovelBrowseActivity) {
        {   // 启动页
            final String LaunchInterceptorActivity = "com.dmzj.manhua.ui.LaunchInterceptorActivity";
            final Class<?> LaunchInterceptorActivityClass = getClazz(LaunchInterceptorActivity);
            if (LaunchInterceptorActivityClass != null) try {
                findAndHookMethod(LaunchInterceptorActivityClass,"onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){}
        }
        {   // 漫画阅读界面
            final String BrowseActivityAncestors = "com.dmzj.manhua.ui.BrowseActivityAncestors";
            final Class<?> BrowseActivityAncestorsClass = getClazz(BrowseActivityAncestors);
            if (BrowseActivityAncestorsClass != null) try {
                findAndHookMethod(BrowseActivityAncestorsClass, "onCreate", Bundle.class, onActivityFullscreen);
            }catch (Throwable ignored){}
        }
        {   // 小说阅读界面
            final String NovelBrowseActivity = "com.dmzj.manhua.ui.NovelBrowseActivity";
            final Class<?> NovelBrowseActivityClass = getClazz(NovelBrowseActivity);
            if (NovelBrowseActivityClass != null) try {
                findAndHookMethod(NovelBrowseActivityClass,"findViews", onNovelBrowseActivity);
            }catch (Throwable ignored){}
        }
        {   // 分享页
            final String ShareActivity = "com.dmzj.manhua.ui.ShareActivity";
            final Class<?> ShareActivityClass = getClazz(ShareActivity);
            if (ShareActivityClass != null)try {
                findAndHookMethod(ShareActivityClass, "createContent", onSetActivityStatusBar(0x80000000));
            }catch (Throwable ignored){}
        }
    }

    // 去除更新检测
    private static void AppUpDataHelper(){
        final String AppUpDataHelper = TARGET_PACKAGE_NAME + ".helper.AppUpDataHelper";
        final Class<?> AppUpDataHelperClass = getClazz(AppUpDataHelper);
        if (AppUpDataHelperClass != null) try {
            findAndHookMethod(AppUpDataHelperClass, "checkVersionInfo",
                Activity.class, Class.class, boolean.class, onReturnVoid);
        } catch (Throwable ignored) {}
    }

    // 关闭傻逼青少年弹窗
    private static void TeenagerModeDialogActivity(){
        final String TeenagerModeDialogActivity = TARGET_PACKAGE_NAME + "_kt.ui.TeenagerModeDialogActivity";
        final Class<?> TeenagerModeDialogActivityClass = getClazz(TeenagerModeDialogActivity);
        if (TeenagerModeDialogActivityClass != null) try {
            findAndHookMethod(TeenagerModeDialogActivityClass, "initView", onActivityFinish(true));
        } catch (Throwable ignored) {}
    }

    // 阻止剪切板被强○
    private static void DoNotFuckMyClipboard(){
        XC_MethodHook setPrimaryClip = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ClipData clipData = (ClipData) param.args[0];
                String inText = clipData.getItemAt(0).getText().toString().trim();
                class temp{
                    // 在写了在写了...
                }
                int isReg = 0;
                for(String reg : new String[]{".*[A-Z]+.*", ".*[a-z]+.*", ".*[~!@#$%^&*()_+|<>,.?/:;'\\\\[\\\\]{}\\\"]+.*"}){
                    if (inText.matches(reg)) ++isReg;
                }
                if (isReg == 0 || (isReg > 0 && inText.contains("http") &&
                        (inText.contains("muwai.com") || inText.contains("dmzj.com")))) return;
                param.args[0] = ClipData.newPlainText("","");
            }
        };
        try {
            findAndHookMethod(ClipboardManager.class, "setPrimaryClip", ClipData.class, setPrimaryClip);
        }catch  (Throwable ignored){}
    }

    private static class AutoSign {

        private static @SuppressLint("StaticFieldLeak") Activity thisActivity = null;
        private static boolean thisUserModelInit = false;
        private static String thisUserToken = null;
        private static String thisUserSign = null;
        private static String thisUserId = null;

        @SuppressLint("NewApi")
        private static void onStart(){
            if (!thisUserModelInit) return;
            Class<?> RequestBuilderClass = getClazz("okhttp3.Request$Builder");
            Class<?> FormBodyBuilderClass = getClazz("okhttp3.FormBody$Builder");
            Class<?> OkHttpClientClass = getClazz("okhttp3.OkHttpClient");
            try {
                Object FormBodyBuilder = FormBodyBuilderClass.newInstance();
                FormBodyBuilder = callMethod(FormBodyBuilder, "add", "token", thisUserToken);
                FormBodyBuilder = callMethod(FormBodyBuilder, "add", "sign", thisUserSign);
                FormBodyBuilder = callMethod(FormBodyBuilder, "add", "uid", thisUserId);
                Object FormBody = callMethod(FormBodyBuilder, "build");

                Object RequestBuilder = RequestBuilderClass.newInstance();
                RequestBuilder = callMethod(RequestBuilder, "url", /* 签到接口 */"http://api.bbs.muwai.com/v1/sign/add");
                RequestBuilder = callMethod(RequestBuilder, "post", FormBody);
                Object Request = callMethod(RequestBuilder, "build");

                Object OkHttpClient = OkHttpClientClass.newInstance();
                Object newCall = callMethod(OkHttpClient, "newCall", Request);
                Object Response = callMethod(newCall, "execute");
                Object ResponseBody = callMethod(Response, "body");

                String result = Arrays.stream(((String) callMethod(ResponseBody, "string")).split(","))
                        .filter(e -> e.contains("msg"))
                        .collect(Collectors.toList()).get(0);
                // Log.d(TAG, "SignResult: " + result);
                Looper.prepare();
                Context mContext = thisActivity.getApplicationContext();
                result = result.replace("msg", "AutoSign")
                        .replace("您已签到", "今日已签到！")
                        .replace(":", " -> ")
                        .replace("\"", "");
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }catch (Exception e){
                Log.d(TAG, "onStart: err-> " + e);
            }
        }

        private static void initUserModelTableData() {
            Class<?> UserModelTableClass = getClazz(TARGET_PACKAGE_NAME + ".dbabst.db.UserModelTable");
            Class<?> dmzjMD5Class = getClazz(TARGET_PACKAGE_NAME + ".utils.MD5");
            try {
                Object UserModelTableInstance =  callStaticMethod(UserModelTableClass, "getInstance", thisActivity);
                Object UserModel = callMethod(UserModelTableInstance, "getActivityUser");
                thisUserId = (String) callMethod(UserModel, "getUid");
                thisUserToken = (String) callMethod(UserModel, "getDmzj_token");
                thisUserSign = (String) callStaticMethod(dmzjMD5Class, "MD5Encode", thisUserToken + thisUserId + "d&m$z*j_159753twt");
                thisUserModelInit = thisUserId != null && thisUserToken != null && thisUserSign != null;
            } catch (Exception e) {
                Log.d(TAG, "initUserModelTableData: err-> " + e);
            }
        }

        public static void init(){
            Class<?> HomeTabsActivitysClass = getClazz(TARGET_PACKAGE_NAME + ".ui.home.HomeTabsActivitys");
            hookAllConstructors(HomeTabsActivitysClass, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    thisActivity = (Activity) param.thisObject;
                    initUserModelTableData();
                    new Thread(AutoSign::onStart).start();
                }
            });
        }

        public static void SignInView(){
            // 隐藏签到按钮的红点
            if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN)) {
                final String MainSceneMineEnActivity = "com.dmzj.manhua.ui.home.MainSceneMineEnActivity";
                Class<?> MainSceneMineEnActivityClass = getClazz(MainSceneMineEnActivity);
                findAndHookMethod(MainSceneMineEnActivityClass, "ShowOrHideUm", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        ImageView iv_my_unread_counts2 = (ImageView) getField(param, "iv_my_unread_counts2");
                        iv_my_unread_counts2.setVisibility(View.GONE);
                    }
                });
            }
            if (TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN)){
                final String HomeMeFragment = "com.dmzjsq.manhua_kt.ui.home.HomeMeFragment";
                Class<?> HomeMeFragmentClass = getClazz(HomeMeFragment);
                findAndHookMethod(HomeMeFragmentClass, "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                View result = (View) param.getResult();
                                Context context = result.getContext();
                                int resourceId = context.getResources().getIdentifier("unread", "id", DMZJSQ_PKGN);
                                ImageView imageView = result.findViewById(resourceId);
                                imageView.setImageAlpha(0);
                            }
                        }
                );
            }
            // 签到页自动点击签到按钮
            final String SignInView = TARGET_PACKAGE_NAME + "_kt.views.task.SignInView";
            Class<?> SignInViewClass = getClazz(SignInView);
            if (SignInViewClass != null) try {
                hookAllMethods(SignInViewClass, "setDaySignTask", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        TextView signInTv = (TextView) getField(param, "signInTv");
                        if (signInTv.getText().equals("立即签到")) signInTv.performClick();
                        signInTv.setText("今日已签到");
                        signInTv.setClickable(false);
                    }
                });
            } catch (Throwable ignored) {}
        }
    }
}
