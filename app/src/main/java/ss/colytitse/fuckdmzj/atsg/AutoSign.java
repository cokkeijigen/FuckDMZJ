package ss.colytitse.fuckdmzj.atsg;

import static de.robv.android.xposed.XposedBridge.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import de.robv.android.xposed.XC_MethodHook;
import ss.colytitse.fuckdmzj.test.PublicContent;
import ss.colytitse.fuckdmzj.tool.OkHttp;

@SuppressLint({"NewApi", "StaticFieldLeak", "DefaultLocale"})
public final class AutoSign extends PublicContent {

    private static final String SIGN_RESULT_COM
            = "{\"code\":2,\"msg\":\"\\u4eca\\u5929\\u5df2\\u7ecf\\u7b7e\\u5230\\uff01\"}";
    private static final String SIGN_RESULT_OK
            = "{\"code\":0,\"msg\":\"\\u6210\\u529f\"}";
    private static Activity thisActivity = null;

    public static boolean hasNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    public static void initStart(){
        Class<?> HomeTabsActivitysClass = getThisPackgeClass(".ui.home.HomeTabsActivitys");
        if (HomeTabsActivitysClass != null) try {
            newHookMethods(HomeTabsActivitysClass,"onCreate", (HookCallBack) param -> {
                thisActivity = (Activity) param.thisObject;
                UserInfo userInfo = new UserInfo(thisActivity);
                if(hasNetworkAvailable(thisActivity))
                    new Thread(() -> onStart(userInfo)).start();
            });
        }catch (Exception ignored){}
    }

    public static void showToast(String text){
        showToast(thisActivity, String.format("-- AutoSignInfo --\n%s", text));
    }

    private static List<String> onDaysTask(UserInfo userInfo){
        List<String> result = new ArrayList<>();
        for (int i = 1; i < 17; i++) try {
            Object Request = OkHttp.RequestBuilder(String.format((DMZJSQ_PKGN.equals(TARGET_PACKAGE_NAME) ?
                    "https://v3api.idmzj.com" : "https://nnv3api.idmzj.com") + /* 任务签到接口 */
                    "/task/get_reward?uid=%s&token=%s&sign=%s&id=%d", userInfo.getUserId(), userInfo.getUserToken(), userInfo.getUserSign(), i
            ));
            String temp = OkHttp.ResponseBodyString(Request);
            result.add(String.format("id_%d ->%s\n",i,temp));
        } catch (Exception e) {
            Log.d(TAG, "DailyTask: err-> " + e);
        }
        return result;
    }

    private static String atSignApi(UserInfo userInfo) throws Exception {
        Object Request = OkHttp.RequestBuilder(String.format((DMZJSQ_PKGN.equals(TARGET_PACKAGE_NAME) ?
                "https://v3api.idmzj.com" : "https://nnv3api.idmzj.com") + /* APP签到接口 */
                "/task/sign?uid=%s&token=%s&sign=%s", userInfo.getUserId(), userInfo.getUserToken(), userInfo.getUserSign()
        ));
        return OkHttp.ResponseBodyString(Request);
    }

    private static void onStart(UserInfo userInfo){
        if (!(OkHttp.init() && userInfo.initComplete())) return;
        boolean signComplete = false;
        try {
            UserInfo.user beforeSG = new UserInfo.user(userInfo);       // 签到前数据;
            String atSignResult = atSignApi(userInfo);
            if (Objects.equals(atSignResult, SIGN_RESULT_COM))
                showToast("今日已签到！");
            else if (Objects.equals(atSignResult, SIGN_RESULT_OK)){
                UserInfo.user afterSG = new UserInfo.user(userInfo);  // 签到后数据
                showToast("签到成功" + (
                        beforeSG.notEquals(afterSG) ? String.format( "：积分 + %d 银币 + %d",
                                afterSG.credits_nums - beforeSG.credits_nums, afterSG.silver_nums - beforeSG.silver_nums
                        ) : "！")
                );
                showToast(String.format("已连续签到： %d 天", afterSG.sign_count));
            } else showToast("签到状态未知！");
            signComplete = true;
            UserInfo.user beforeDT = new UserInfo.user(userInfo);
            List<String> DaysTaskResult = onDaysTask(userInfo);  // 任务签到
            UserInfo.user afterDT = new UserInfo.user(userInfo);
            if (beforeDT.notEquals(afterDT)) showToast(String.format("完成任务：积分 + %d 银币 + %d",
                    afterDT.credits_nums - beforeDT.credits_nums, afterDT.silver_nums - beforeDT.silver_nums)
            );
            Log.d(INFO, "SignResult -> \n" + atSignResult);
            Log.d(INFO, "DaysTaskResult -> \n" + DaysTaskResult);
        }catch (Exception e){
            Log.d(TAG, "SignResult: err-> " + e);
            if(!signComplete) showToast("签到失败！");
        }
    }

    private static boolean fuckSignInView(Object object, UserInfo userInfo){
        if (!Objects.requireNonNull(object).getClass().getName().contains("TextView")) return false;
        TextView signInTv = (TextView) object;
        List<String> msgs = Arrays.asList("观看视频奖励 x 2", "立即签到", "明日再来");
        if (!msgs.contains(signInTv.getText().toString())) return false;
        if (signInTv.getText().equals("立即签到")) new Thread(() -> {
            try {
                if (userInfo == null) return;
                String SignResult = atSignApi(userInfo);
                if (!Objects.equals(SignResult, SIGN_RESULT_COM) || !Objects.equals(SignResult, SIGN_RESULT_OK))
                    signInTv.performClick();
            } catch (Exception ignored) {}
        }).start();
        signInTv.setText("今日已签到");
        signInTv.setClickable(false);
        return true;
    }

    public static void SignInView() {
        // 签到页自动点击签到按钮，前提是如果后台自动签到失败
        Class<?> SignInViewClass = getThisPackgeClass("_kt.views.task.SignInView");
        if (SignInViewClass != null) try {
            XC_MethodHook setDaySignTask = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context mContext = (Context) param.args[0];
                    if(!hasNetworkAvailable(mContext)) return;
                    UserInfo userInfo = new UserInfo(mContext);
                    XC_MethodHook xc_methodHook = new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            for (Field declaredField : param.thisObject.getClass().getDeclaredFields()) try {
                                declaredField.setAccessible(true);
                                Object object = declaredField.get(param.thisObject);
                                if (fuckSignInView(object, userInfo)) break;
                            } catch (Exception ignored) {}
                        }
                    };
                    hookAllMethods(SignInViewClass, "setDaySignTask", xc_methodHook);
                }
            };
            hookAllConstructors(SignInViewClass, setDaySignTask);
        } catch (Exception ignored) {}
    }

    public static void clearSignButtonView(){
        // 隐藏签到按钮的红点
        if (DMZJ_PKGN.equals(TARGET_PACKAGE_NAME)) {
            Class<?> MainSceneMineEnActivityClass = getThisPackgeClass(".ui.home.MainSceneMineEnActivity");
            if (MainSceneMineEnActivityClass != null) try {
                newHookMethods(MainSceneMineEnActivityClass,"onStart", (HookCallBack) param ->{
                    Activity mActivty = (Activity) param.thisObject;
                    Context mContext = mActivty.getApplicationContext();
                    int identifier = getIdentifier(mContext, "id", "iv_my_unread_counts2");
                    ImageView imageView = mActivty.findViewById(identifier);
                    imageView.setImageAlpha(0);
                });
            } catch (Throwable ignored) {}
        } else if (DMZJSQ_PKGN.equals(TARGET_PACKAGE_NAME)){
            Class<?> HomeMeFragmentClass = getThisPackgeClass("_kt.ui.home.HomeMeFragment");
            if (HomeMeFragmentClass != null) try {
                newHookMethods(HomeMeFragmentClass, "onCreateView", (HookCallBack) param ->{
                    if(param.args.length != 3) return;
                    View result = (View) param.getResult();
                    Context thisObject = result.getContext();
                    int resourceId = getIdentifier(thisObject,"id", "unread");
                    ImageView imageView = result.findViewById(resourceId);
                    imageView.setImageAlpha(0);
                });
            } catch (Throwable ignored) {}
        }
    }
}