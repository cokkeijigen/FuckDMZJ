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
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
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
                class temp{  // 在写了在写了...
                    private final char[] thisText;
                    private StringBuffer text;
                    private int count = 0;
                    private int index = 0;

                    public temp(String intext){
                        thisText = intext.toCharArray();
                    }

                    private void init(){
                        do{
                            ++index;
                        }while (!end());
                    }

                    private boolean end(){
                        return index == thisText.length;
                    }
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
        private static Class<?> thisHomeTabsActivitys = null;
        private static Class<?> thisUserModelTable = null;
        private static Class<?> thisdmzjMD5 = null;
        private static boolean thisUserModelInit = false;
        private static String thisUserToken = null;
        private static String thisUserSign = null;
        private static String thisUserId = null;

        @SuppressLint("NewApi")
        private static void onStart(){
            if (!thisUserModelInit) return;
            Class<?> RequestBuilderClass = getClazz("okhttp3.Request$Builder");
            Class<?> OkHttpClientClass = getClazz("okhttp3.OkHttpClient");
            Class<?> FormBodyBuilderClass = getClazz("okhttp3.FormBody$Builder");
            try {
                Object RequestBuilder = RequestBuilderClass.newInstance();
                RequestBuilder = callMethod(RequestBuilder, "url",
                        /* 获取签到状态接口 */
                        String.format("http://api.bbs.muwai.com/v1/sign/detail?uid=%s&token=%s&sign=%s&rand=%s",
                        thisUserId, thisUserToken, thisUserSign, (new Random()).nextDouble())
                );

                Object OkHttpClient = OkHttpClientClass.newInstance();
                Object Request = callMethod(RequestBuilder, "build");
                Object newCall = callMethod(OkHttpClient, "newCall", Request);
                Object Response = callMethod(newCall, "execute");
                Object ResponseBody = callMethod(Response, "body");
                String result = Arrays.stream(((String) callMethod(ResponseBody, "string")).split(","))
                        .filter(e -> e.contains("is_sign"))
                        .collect(Collectors.toList()).get(0);
                Log.d(TAG, "SignState -> " + result);
                if (!Objects.equals(result.split(":")[1], "0")) return;
            } catch (Exception e) {
                Log.d(TAG, "onStart: err-> " + e);
                return;
            }

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
                Log.d(TAG, "SignResult: " + result);
            }catch (Exception e){
                Log.d(TAG, "test: err-> " + e);
            }
        }

        private static void InitializationUserModelTableData() {
            try {
                Object UserModelTableInstance =  callStaticMethod(thisUserModelTable, "getInstance", thisActivity);
                Object UserModel = callMethod(UserModelTableInstance, "getActivityUser");
                thisUserId = (String) callMethod(UserModel, "getUid");
                thisUserToken = (String) callMethod(UserModel, "getDmzj_token");
                thisUserSign = (String) callStaticMethod(thisdmzjMD5, "MD5Encode", thisUserToken + thisUserId + "d&m$z*j_159753twt");
                if (thisUserId != null && thisUserToken != null && thisUserSign != null) thisUserModelInit = true;
            } catch (Exception e) {
                Log.d(TAG, "initUserModel: err-> " + e);
            }

            /*
            {   // 测试内容
                Log.d(TAG, "-----------------------------InitializationUserModelTableData-----------------------------");
                Log.d(TAG, "thisUserId: " + thisUserId);
                Log.d(TAG, "thisUserToken: " + thisUserToken);
                Log.d(TAG, "thisUserSign: " + thisUserSign);
                Log.d(TAG, "thisSignStatusUrl: " + thisSignStatusUrl);
            }
             */
        }

        public static void init(){
            thisHomeTabsActivitys = getClazz(TARGET_PACKAGE_NAME + ".ui.home.HomeTabsActivitys");
            thisUserModelTable = getClazz(TARGET_PACKAGE_NAME + ".dbabst.db.UserModelTable");
            thisdmzjMD5 = getClazz(TARGET_PACKAGE_NAME + ".utils.MD5");
            hookAllConstructors(thisHomeTabsActivitys, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    thisActivity = (Activity) param.thisObject;
                    InitializationUserModelTableData();
                    new Thread(AutoSign::onStart).start();
                }
            });
        }

        public static void SignInView(){
            final String SignInView = TARGET_PACKAGE_NAME + "_kt.views.task.SignInView";
            Class<?> SignInViewClass = getClazz(SignInView);
            if (SignInViewClass != null) try {
                hookAllMethods(SignInViewClass, "setDaySignTask", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        TextView signInTv = (TextView) getField(param, "signInTv");
                        if (signInTv.getText().equals("立即签到")) signInTv.performClick();
                    }
                });
            } catch (Throwable ignored) {}
        }
    }
}
