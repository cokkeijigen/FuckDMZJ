package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static ss.colytitse.fuckdmzj.MainHook.TARGET_PACKAGE_NAME;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import ss.colytitse.fuckdmzj.test.PublicContent;

public final class MethodHook extends PublicContent {

    public static final XC_MethodReplacement onReturnVoid = new XC_MethodReplacement(){
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) {
            return null;
        }
    };

    public static <T>  XC_MethodHook onSetResult(T value, boolean before){
        return before ? new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable  {
                super.beforeHookedMethod(param);
                param.setResult(value);
            }
        } : new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(value);
            }
        };
    }

    public static XC_MethodHook onActivityFinish(boolean before){
        return before ? new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                activity.finish();
            }
        } : new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                activity.finish();
            }
        };
    }

    @SuppressLint("NewApi")
    public static void setActivityFullscreen(Activity activity){
        try {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(lp);
        } catch (Exception ignored) {}
    }

    @SuppressLint("InlinedApi")
    public static void onSetActivityStatusBar(Activity activity, int color){
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(color);
    }

    public static XC_MethodHook onSetActivityStatusBar(int Color){
        return new XC_MethodHook() {
            @Override  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                onSetActivityStatusBar(activity, Color);
            }
        };
    }

    public static int getIdentifier(Context mContext, String defType, String name){
        if (!defType.contains(":")) return mContext.getResources().getIdentifier(name, defType, TARGET_PACKAGE_NAME);
        String[] split = defType.split(":");
        return mContext.getResources().getIdentifier(name, split[1], split[0]);
    }

    public static class FuckerHook {

        private static Class<?> thisFuckerClass;
        public static final int BEFORE = -1;
        public static final int AFTER = 0;
        public static final int REPLACE = 1;

        // 在全部类加载器中查找并hook
        public static void inClassLoaderFindAndHook(Fucker fucker){
            hookAllMethods(ClassLoader.class, "loadClass", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if (param.hasThrowable() || param.args.length != 1) return;
                    try{
                        fucker.hook((Class<?>) param.getResult());
                    }catch (Throwable ignored){}
                }
            });
        }

        public static void hookMethods(Class<?> clazz, String methodName, Object callBack){
            hookMethods(clazz, methodName, callBack, FuckerHook.AFTER);
        }

        public static void hookMethods(Class<?> clazz, String methodName, Object callBack, int callBackType){
            new hookMethods(clazz, methodName, callBack, callBackType).run();
        }

        public static Class<?> getClass(String clazzName){
            inClassLoaderFindAndHook(clazz -> {
                if (clazz.getName().equals(clazzName)) thisFuckerClass = clazz;
                else thisFuckerClass = null;
            });
            return thisFuckerClass;
        }

        public static final class  hookMethods{

            private final Class<?> targetClass;
            private final String targetMethodName;
            private final XC_MethodHook hookCallBack;

            public hookMethods(Class<?> targetClazz, String methodName , Object callBack, int callBackType){
                this.targetClass = targetClazz;
                this.targetMethodName = methodName;
                this.hookCallBack = new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        if (!param.thisObject.getClass().equals(targetClass)) return;
                        if (callBackType ==  FuckerHook.REPLACE)
                            param.setResult(((HookReplaceCallBack) callBack).hook(param));
                        else if (callBackType == FuckerHook.BEFORE)
                            ((HookCallBack) callBack).hook(param);
                    }
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        if (param.thisObject.getClass().equals(targetClass) && callBackType == FuckerHook.AFTER)
                            ((HookCallBack) callBack).hook(param);
                    }
                };
            }

            private void nextFindAndHooks(Class<?> clazz) {
                int thisResult = 0;     // 成功找到method的次数
                for (Method declaredMethod : clazz.getDeclaredMethods())
                    if (declaredMethod.getName().equals(targetMethodName)) try {
                        ++thisResult;
                        XposedBridge.hookMethod(declaredMethod, hookCallBack);
                    } catch (Exception ignored) {}
                if (thisResult != 0) return;
                Class<?> superclass = clazz.getSuperclass();
                if (superclass != null) nextFindAndHooks(superclass);
            }

            public void run(){
                nextFindAndHooks(targetClass);
            }
        }

        public interface Fucker{
            void hook(Class<?> clazz);
        }
        public interface HookCallBack{
            void hook(XC_MethodHook.MethodHookParam param);
        }
        public interface HookReplaceCallBack{
            Object hook(XC_MethodHook.MethodHookParam param);
        }
    }
}
