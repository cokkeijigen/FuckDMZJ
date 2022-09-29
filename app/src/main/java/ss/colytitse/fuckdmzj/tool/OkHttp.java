package ss.colytitse.fuckdmzj.tool;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static ss.colytitse.fuckdmzj.MainHook.getClazz;

import ss.colytitse.fuckdmzj.test.PublicContent;

public final class OkHttp extends PublicContent {

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

