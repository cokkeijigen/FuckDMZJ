package ss.colytitse.fuckdmzj.gdhd;

import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.FuckerHook.*;
import static ss.colytitse.fuckdmzj.hook.MethodHook.*;
import static ss.colytitse.fuckdmzj.tool.OkHttp.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RelativeLayout;
import ss.colytitse.fuckdmzj.test.PublicContent;
import ss.colytitse.fuckdmzj.tool.OkHttp;
import ss.colytitse.fuckdmzj.tool.RSAUtils;

public class FilterKamiGakusi extends PublicContent {

    static final String RSA_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK8nNR1lTnIfIes6oRW" +
            "JNj3mB6OssDGx0uGMpgpbVCpf6+VwnuI2stmhZNoQcM417Iz7WqlPzbUmu9R4dEKmLGEEqOhOdVaeh9Xk2IPPjqIu5Tbk" +
            "LZRxkY3dJM1htbz57d/roesJLkZXqssfG5EJauNc+RcABTfLb4IiFjSMlTsnAgMBAAECgYEAiz/pi2hKOJKlvcTL4jpHJ" +
            "Gjn8+lL3wZX+LeAHkXDoTjHa47g0knYYQteCbv+YwMeAGupBWiLy5RyyhXFoGNKbbnvftMYK56hH+iqxjtDLnjSDKWnhc" +
            "B7089sNKaEM9Ilil6uxWMrMMBH9v2PLdYsqMBHqPutKu/SigeGPeiB7VECQQDizVlNv67go99QAIv2n/ga4e0wLizVuaN" +
            "BXE88AdOnaZ0LOTeniVEqvPtgUk63zbjl0P/pzQzyjitwe6HoCAIpAkEAxbOtnCm1uKEp5HsNaXEJTwE7WQf7PrLD4+Bp" +
            "GtNKkgja6f6F4ld4QZ2TQ6qvsCizSGJrjOpNdjVGJ7bgYMcczwJBALvJWPLmDi7ToFfGTB0EsNHZVKE66kZ/8Stx+ezue" +
            "ke4S556XplqOflQBjbnj2PigwBN/0afT+QZUOBOjWzoDJkCQClzo+oDQMvGVs9GEajS/32mJ3hiWQZrWvEzgzYRqSf3XV" +
            "cEe7PaXSd8z3y3lACeeACsShqQoc8wGlaHXIJOHTcCQQCZw5127ZGs8ZDTSrogrH73Kw/HvX55wGAeirKYcv28eauveCG" +
            "7iyFR0PFB/P/EDZnyb+ifvyEFlucPUI0+Y87F";

    public static void Runnenr(){

        newHookMethods(getThisPackgeClass(".proto.Comic$ComicResponse"),
                "getErrno",
                (HookCallBack) param -> param.setResult(0)
        );

        Class<?> CartoonInstructionActivity = getThisPackgeClass(".ui.CartoonInstructionActivity");

        newHookMethods(CartoonInstructionActivity, "setTitle", (HookCallBack) param -> {
            if ("".equals(param.args[0])) param.setResult(null);
        }, BEFORE);

//        Class<?> CartoonDescription = getClazz("com.dmzj.manhua.bean.CartoonDescription");
        newHookMethods(CartoonInstructionActivity, "onStart", (HookCallBack) param -> {
            Context mContext = (Context)param.thisObject;
            Activity mActivity = (Activity)param.thisObject;
            new Thread(() -> {
                String comicId = (String) newMethodResult(() ->{
                    Intent intent = mActivity.getIntent();
                    String action = intent.getAction();
                    if ("android.intent.action.VIEW".equals(action)) {
                        Uri data = intent.getData();
                        if (data != null) return data.getQueryParameter("id");
                    }
                    return intent.getStringExtra("intent_extra_cid");
                }, TAG);
                if(isKamiGakushi(comicId))
                    mActivity.runOnUiThread(() -> {
                        showToast(mContext, "此乃神隐！！！");
                        int layout_inst_title = getIdentifier(mContext, "id", "layout_inst_title");
                        RelativeLayout layoutInstTitle = mActivity.findViewById(layout_inst_title);
                        layoutInstTitle.removeAllViews();
                        layoutInstTitle.invalidate();
                    });
            }).start();
        });
    }

    private static boolean isKamiGakushi(String comicId)  {
        try {
            Object Request = RequestBuilder("https://nnv4api.muwai.com/comic/detail/" + comicId);
            String result = OkHttp.ResponseBodyString(Request);
            String decrypt = RSAUtils.decrypt(result, RSA_PRIVATE_KEY);
            return decrypt.contains("漫画不存在");
        } catch (Exception e) {
            Log.d(TAG, "喵喵喵？" + e);
        }
        return false;
    }


}
