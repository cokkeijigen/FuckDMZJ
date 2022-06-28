package ss.colytitse.fuckdmzj.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static ss.colytitse.fuckdmzj.MainHook.DMZJSQ_PKGN;
import static ss.colytitse.fuckdmzj.MainHook.DMZJ_PKGN;
import static ss.colytitse.fuckdmzj.MainHook.TARGET_PACKAGE_NAME;
import static ss.colytitse.fuckdmzj.MainHook.getClazz;
import static ss.colytitse.fuckdmzj.MainHook.getField;
import static ss.colytitse.fuckdmzj.test.PublicContent.*;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;

@SuppressLint({"NewApi", "StaticFieldLeak", "DefaultLocale"})
public final class UserAutoSign {

    private static boolean thisUserModelInit = false;
    private static Activity thisActivity = null;
    private static String thisUserToken = null;
    private static String thisUserSign = null;
    private static String thisUserId = null;
    private static class user {

        String result;                // 请求结果
        int sign_count;               // 连续签到天数
        int max_sign_count;           // 最大连续天数
        int credits_nums;             // 积分数
        int silver_nums;              // 银币数
        boolean initComplete;         // 初始化状态

        public user() throws Exception {
            initComplete = false;
            if (!OkHttp.init()) return;
            Object Request = OkHttp.RequestBuilder(String.format((TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN) ?
                    "http://v3api.muwai.com" : "http://nnv3api.muwai.com") +  /* 获取状态接口 */
                    "/task/index?uid=%s&token=%s&sign=%s", thisUserId, thisUserToken, thisUserSign
            ), null);
            this.result = OkHttp.ResponseBodyString(Request);
            Arrays.stream(Objects.requireNonNull(this.result).split(","))
                    .filter(e -> e.contains("sign_count") || e.contains("credits_nums")|| e.contains("silver_nums"))
                    .filter(e -> !e.contains("}")).forEach(e -> {
                        if (e.contains("\"sign_count\""))
                            this.sign_count = Integer.parseInt(e.split(":")[2].replace("\"", "").trim());
                        else {
                            int num = Integer.parseInt(e.split(":")[1].replace("\"", "").trim());
                            if (e.contains("\"max_sign_count\"")) this.max_sign_count = num;
                            else if (e.contains("\"credits_nums\"")) this.credits_nums = num;
                            else if (e.contains("\"silver_nums\"")) this.silver_nums = num;
                        }
                });
            initComplete = true;
        }

        public boolean equals(user us) {
            return this.sign_count == us.sign_count && this.max_sign_count == us.max_sign_count &&
                    this.credits_nums == us.credits_nums && this.silver_nums == us.silver_nums;
        }

        @Override
        public @NonNull String toString() {
            if (initComplete) return "    "+
                    "\nuser -> {"+
                    " \n    max_sign_count = "  + max_sign_count +
                    ";    sign_count = "     + sign_count +
                    "; \n    credits_nums = "   + credits_nums +
                    ";    silver_nums = "    + silver_nums +
                    ";\n} <- end";
            else return "user -> { null } <- end";
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
        if (HomeTabsActivitysClass != null) try {
            hookAllConstructors(HomeTabsActivitysClass, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    thisActivity = (Activity) param.thisObject;
                    initUserModelTableData();
                    new Thread(UserAutoSign::onStart).start();
                }
            });
        }catch (Throwable ignored){}
    }

    private static void showTost(String text, int count){
        Context mContext = thisActivity.getApplicationContext();
        Toast.makeText(
                mContext, String.format("-- AutoSignInfo --\n%s", text), Toast.LENGTH_SHORT
        ).show();
        if (count != -1) Toast.makeText(
                mContext, String.format("已连续签到： %s 天", count), Toast.LENGTH_SHORT
        ).show();
    }

    private static List<String> onDaysTask(){
        List<String> result = new ArrayList<>();
        for (int i = 1; i < 17; i++) try {
                Object Request = OkHttp.RequestBuilder(String.format((TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN) ?
                        "http://v3api.muwai.com" : "http://nnv3api.muwai.com") + /* 任务签到接口 */
                        "/task/get_reward?uid=%s&token=%s&sign=%s&id=%d", thisUserId, thisUserToken, thisUserSign, i
                ), null);
            String temp = OkHttp.ResponseBodyString(Request);
            result.add(String.format("id_%d ->%s\n",i,temp));
        } catch (Exception e) {
            Log.d(TAG, "DailyTask: err-> " + e);
        }
        return result;
    }

    private static String onSignApi_01() throws Exception{
        Object Request = OkHttp.RequestBuilder(String.format((TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN) ?
                "http://v3api.muwai.com" : "http://nnv3api.muwai.com") + /* APP签到接口 */
                "/task/sign?uid=%s&token=%s&sign=%s", thisUserId, thisUserToken, thisUserSign
        ), null);
        return OkHttp.ResponseBodyString(Request);
    }

    private static String onSignApi_02() throws Exception{
        Object FormBody = OkHttp.FormBodyBuilder("token=" + thisUserToken, "uid=" + thisUserId, "sign=" + thisUserSign);
        Object Request = OkHttp.RequestBuilder("http://api.bbs.muwai.com/v1/sign/add", FormBody); /* 貌似是网页签到接口? */
        return OkHttp.ResponseBodyString(Request);
    }

    private static void onStart() {
        if (!thisUserModelInit) return;
        Looper.prepare();
        try{
            user before = new user();       // 签到前数据;
            if(OkHttp.init()) {
                String SignResult1 = onSignApi_01();
                String SignResult2 = onSignApi_02();

                if (Objects.equals(SignResult1, "{\"code\":2,\"msg\":\"\\u4eca\\u5929\\u5df2\\u7ecf\\u7b7e\\u5230\\uff01\"}"))
                    showTost("今日已签到！", -1);
                else if (Objects.equals(SignResult1, "{\"code\":0,\"msg\":\"\\u6210\\u529f\"}")){
                    user afterSG = new user();  // 签到后数据
                    showTost("签到成功" + (!before.equals(afterSG) ? String.format( "：积分 + %d 银币 + %d",
                            afterSG.credits_nums - before.credits_nums, afterSG.silver_nums - before.silver_nums
                            ) : "！"), afterSG.sign_count
                    );
                } else showTost("签到状态未知！", -1);

                List<String> DaysTaskResult = onDaysTask();  // 任务签到
                user afterDT = new user();
                if (!before.equals(afterDT)) showTost(String.format( "完成任务：积分 + %d 银币 + %d",
                        afterDT.credits_nums - before.credits_nums, afterDT.silver_nums - before.silver_nums), -1
                );

                Log.d(INFO, "DaysTaskResult -> \n" + DaysTaskResult);
                Log.d(INFO, "SignResult1 -> \n" + SignResult1);
                Log.d(INFO, "SignResult2 -> \n" + SignResult2);
            }
        }catch (Exception e){
            Log.d(TAG, "Sign2: err-> " + e);
            showTost("签到失败！", -1);
        }
        Looper.loop();
    }

    public static void SignInView(){
        // 隐藏签到按钮的红点
        if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN)) {
            final String MainSceneMineEnActivity = "com.dmzj.manhua.ui.home.MainSceneMineEnActivity";
            Class<?> MainSceneMineEnActivityClass = getClazz(MainSceneMineEnActivity);
            if (MainSceneMineEnActivityClass != null) try {
                findAndHookMethod(MainSceneMineEnActivityClass, "ShowOrHideUm", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        ImageView iv_my_unread_counts2 = (ImageView) getField(param, "iv_my_unread_counts2");
                        iv_my_unread_counts2.setVisibility(View.GONE);
                    }
                });
            } catch (Throwable ignored) {}
        }
        if (TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN)){
            final String HomeMeFragment = "com.dmzjsq.manhua_kt.ui.home.HomeMeFragment";
            Class<?> HomeMeFragmentClass = getClazz(HomeMeFragment);
            if (HomeMeFragmentClass != null) try {
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
            } catch (Throwable ignored) {}
        }

        // 签到页自动点击签到按钮，前提是如果后台自动签到失败
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

final class OkHttp{

    private static Class<?> RequestBuilderClass = null;
    private static Class<?> FormBodyBuilderClass = null;
    private static Class<?> OkHttpClientClass = null;
    private static boolean InitComplete = false;

    public static boolean init(){
        if (InitComplete) return true;
        RequestBuilderClass = getClazz("okhttp3.Request$Builder");
        FormBodyBuilderClass = getClazz("okhttp3.FormBody$Builder");
        OkHttpClientClass = getClazz("okhttp3.OkHttpClient");
        InitComplete = RequestBuilderClass != null && FormBodyBuilderClass != null && OkHttpClientClass != null;
        return InitComplete;
    }

    public static Object RequestBuilder(String url, Object post) throws Exception {
        if (!OkHttp.init()) return null;
        Object requestBuilder = RequestBuilderClass.newInstance();
        requestBuilder = callMethod(requestBuilder, "url", url);
        if (post != null)
            requestBuilder = callMethod(requestBuilder, "post", post);
        return callMethod(requestBuilder, "build");
    }

    public static String ResponseBodyString(Object Request) throws Exception{
        if (!OkHttp.init()) return null;
        Object OkHttpClient = OkHttpClientClass.newInstance();
        Object newCall = callMethod(OkHttpClient, "newCall", Request);
        Object Response = callMethod(newCall, "execute");
        Object ResponseBody = callMethod(Response, "body");
        return  (String) callMethod(ResponseBody, "string");
    }

    public static Object FormBodyBuilder(String ...args) throws Exception{
        if (!OkHttp.init()) return null;
        Object formBodyBuilder = FormBodyBuilderClass.newInstance();
        for (String arg : args) {
            String[] temp = arg.split("=");
            formBodyBuilder = callMethod(formBodyBuilder, "add", temp[0], temp[1]);
        }
        return callMethod(formBodyBuilder, "build");
    }
}
