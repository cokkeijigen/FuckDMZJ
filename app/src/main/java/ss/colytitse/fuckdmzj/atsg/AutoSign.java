package ss.colytitse.fuckdmzj.atsg;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.test.PublicContent.*;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import de.robv.android.xposed.XC_MethodHook;
import ss.colytitse.fuckdmzj.test.PublicContent;

@SuppressLint({"NewApi", "StaticFieldLeak", "DefaultLocale"})
public final class AutoSign extends PublicContent {

    private static final String SIGN_RESULT_COM
            = "{\"code\":2,\"msg\":\"\\u4eca\\u5929\\u5df2\\u7ecf\\u7b7e\\u5230\\uff01\"}";
    private static final String SIGN_RESULT_OK
            = "{\"code\":0,\"msg\":\"\\u6210\\u529f\"}";
    private static Activity thisActivity = null;

    public static void initStart(){
        Class<?> HomeTabsActivitysClass = getClazz(TARGET_PACKAGE_NAME + ".ui.home.HomeTabsActivitys");
        if (HomeTabsActivitysClass != null) try {
            XC_MethodHook xc_methodHook = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    thisActivity = (Activity) param.thisObject;
                    UserInfo userInfo = new UserInfo((Context) param.thisObject);
                    // Log.d(TAG, "beforeHookedMethod: userInfo" + userInfo);
                    new Thread(() -> onStart(userInfo)).start();
                }
            };
            if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN))
                findAndHookMethod(HomeTabsActivitysClass, "onCreate", Bundle.class, xc_methodHook);
            else findAndHookMethod(HomeTabsActivitysClass, "initView", xc_methodHook);
        }catch (Exception ignored){}
    }

    private static void showToast(String text){
        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(
                    thisActivity.getApplicationContext(),
                    String.format("-- AutoSignInfo --\n%s", text),
                    Toast.LENGTH_SHORT
            ).show();
            Looper.loop();
        }).start();
    }

    private static List<String> onDaysTask(UserInfo userInfo){
        List<String> result = new ArrayList<>();
        for (int i = 1; i < 17; i++) try {
            Object Request = OkHttp.RequestBuilder(String.format((TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN) ?
                    "http://v3api.muwai.com" : "http://nnv3api.muwai.com") + /* ?????????????????? */
                    "/task/get_reward?uid=%s&token=%s&sign=%s&id=%d", userInfo.getUserId(), userInfo.getUserToken(), userInfo.getUserSign(), i
            ), null);
            String temp = OkHttp.ResponseBodyString(Request);
            result.add(String.format("id_%d ->%s\n",i,temp));
        } catch (Exception e) {
            Log.d(TAG, "DailyTask: err-> " + e);
        }
        return result;
    }

    private static String at1SignApi(UserInfo userInfo) throws Exception{
        Object Request = OkHttp.RequestBuilder(String.format((TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN) ?
                "http://v3api.muwai.com" : "http://nnv3api.muwai.com") + /* APP???????????? */
                "/task/sign?uid=%s&token=%s&sign=%s", userInfo.getUserId(), userInfo.getUserToken(), userInfo.getUserSign()
        ), null);
        return OkHttp.ResponseBodyString(Request);
    }

    private static String at2SignApi(UserInfo userInfo) throws Exception{
        Object FormBody = OkHttp.FormBodyBuilder("token=" + userInfo.getUserToken(), "uid=" + userInfo.getUserId(), "sign=" + userInfo.getUserSign());
        Object Request = OkHttp.RequestBuilder("http://api.bbs.muwai.com/v1/sign/add", FormBody); /* ???????????????????????????? */
        return OkHttp.ResponseBodyString(Request);
    }

    private static void onStart(UserInfo userInfo){
        if (OkHttp.init() && userInfo.initComplete()){
            boolean signComplete = false;
            try {
                UserInfo.user beforeSG = new UserInfo.user(userInfo);       // ???????????????;
                String at1SignResult = at1SignApi(userInfo);
                String at2SignResult = at2SignApi(userInfo);
                if (Objects.equals(at1SignResult, SIGN_RESULT_COM))
                    showToast("??????????????????");
                else if (Objects.equals(at1SignResult, SIGN_RESULT_OK)){
                    UserInfo.user afterSG = new UserInfo.user(userInfo);  // ???????????????
                    showToast("????????????" + (
                            beforeSG.notEquals(afterSG) ? String.format( "????????? + %d ?????? + %d",
                                    afterSG.credits_nums - beforeSG.credits_nums, afterSG.silver_nums - beforeSG.silver_nums
                            ) : "???")
                    );
                    showToast(String.format("?????????????????? %d ???", afterSG.sign_count));
                } else showToast("?????????????????????");
                signComplete = true;

                UserInfo.user beforeDT = new UserInfo.user(userInfo);
                List<String> DaysTaskResult = onDaysTask(userInfo);  // ????????????
                UserInfo.user afterDT = new UserInfo.user(userInfo);
                if (beforeDT.notEquals(afterDT)) showToast(String.format("????????????????????? + %d ?????? + %d",
                        afterDT.credits_nums - beforeDT.credits_nums, afterDT.silver_nums - beforeDT.silver_nums)
                );

                Log.d(INFO, "SignResult1 -> \n" + at1SignResult);
                Log.d(INFO, "SignResult2 -> \n" + at2SignResult);
                Log.d(INFO, "DaysTaskResult -> \n" + DaysTaskResult);
            }catch (Exception e){
                Log.d(TAG, "SignResult: err-> " + e);
                if(!signComplete) showToast("???????????????");
            }
        }
    }

    private static boolean fuckSignInView(Object object, UserInfo userInfo){
        if (!Objects.requireNonNull(object).getClass().getName().contains("TextView")) return false;
        TextView signInTv = (TextView) object;
        List<String> msgs = Arrays.asList("?????????????????? x 2", "????????????", "????????????");
        if (!msgs.contains(signInTv.getText().toString())) return false;
        if (signInTv.getText().equals("????????????")) new Thread(() -> {
            try {
                if (userInfo == null) return;
                String SignResult = at1SignApi(userInfo);
                if (!Objects.equals(SignResult, SIGN_RESULT_COM) || !Objects.equals(SignResult, SIGN_RESULT_OK))
                    signInTv.performClick();
            } catch (Exception ignored) {}
        }).start();
        signInTv.setText("???????????????");
        signInTv.setClickable(false);
        return true;
    }

    public static void SignInView() {
        // ???????????????????????????????????????????????????????????????????????????
        final String SignInView = TARGET_PACKAGE_NAME + "_kt.views.task.SignInView";
        Class<?> SignInViewClass = getClazz(SignInView);
        if (SignInViewClass != null) try {
            XC_MethodHook setDaySignTask = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context mContext = (Context) param.args[0];
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
        // ???????????????????????????
        if (TARGET_PACKAGE_NAME.equals(DMZJ_PKGN)) {
            final String MainSceneMineEnActivity = "com.dmzj.manhua.ui.home.MainSceneMineEnActivity";
            Class<?> MainSceneMineEnActivityClass = getClazz(MainSceneMineEnActivity);
            if (MainSceneMineEnActivityClass != null) try {
                findAndHookMethod(MainSceneMineEnActivityClass, "onStart", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Activity mActivty = (Activity) param.thisObject;
                        Context mContext = mActivty.getApplicationContext();
                        int identifier = mContext.getResources().getIdentifier("iv_my_unread_counts2", "id", DMZJ_PKGN);
                        ImageView imageView = (ImageView) mActivty.findViewById(identifier);
                        imageView.setImageAlpha(0);
                    }
                });
            } catch (Throwable ignored) {}
        } else if (TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN)){
            final String HomeMeFragment = "com.dmzjsq.manhua_kt.ui.home.HomeMeFragment";
            Class<?> HomeMeFragmentClass = getClazz(HomeMeFragment);
            if (HomeMeFragmentClass != null) try {
                findAndHookMethod(HomeMeFragmentClass, "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                View result = (View) param.getResult();
                                Context thisObject = result.getContext();
                                int resourceId = getIdentifier(thisObject,"id", "unread");
                                ImageView imageView = result.findViewById(resourceId);
                                imageView.setImageAlpha(0);
                            }
                        }
                );
            } catch (Throwable ignored) {}
        }

    }
}