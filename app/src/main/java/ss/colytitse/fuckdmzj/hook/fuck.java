package ss.colytitse.fuckdmzj.hook;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ss.colytitse.fuckdmzj.MainHook;

public class fuck {

    // 对象包名
    protected final  String PKGN;
    // 类加载器
    protected final ClassLoader classLoader;

    public fuck(String PKGN, ClassLoader classLoader)/* 初始化 */{
        this.classLoader = classLoader;
        this.PKGN = PKGN;
    }

    // 获取字段
    protected Object getFieldByName(XC_MethodHook.MethodHookParam param, String name) throws Throwable {
        Class<?> clazz = param.thisObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(param.thisObject);
    }

    protected boolean is(String url){

        return true;
    }

    // 阻止粘贴板被强○
    public void DoNotFuckMyClipboard() {

        String str = " DoNotFuckMyClipboard";
        String logs = PKGN.equals(MainHook.DMZJ_PKGN) ? "DMZJ"+str : "DMZJSQ"+str;

        try {
            XC_MethodHook PlainTextFuck  = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param){
                    String s = param.args[1].toString();
                    if(!(param.args[1].toString().contains("http"))){
                        param.args[1]="";
                        param.setResult(null);
                        XposedBridge.log(logs +": "+ s);
                    }
                }
            };

            XC_MethodHook setTextFuck = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    String s = (param.args[0]).toString();
                    if(!(param.args[0].toString().contains("http"))){
                        param.args[0]="";
                        param.setResult(null);
                        XposedBridge.log(logs +": "+ s);
                    }
                }
            };

            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass("android.content.ClipData", classLoader),
                    "newPlainText", CharSequence.class, CharSequence.class,PlainTextFuck);

            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass("android.content.ClipboardManager", classLoader),
                    "setText", CharSequence.class, setTextFuck);
        }catch (Throwable t){
            XposedBridge.log(logs + " ERR: " + t.toString());
        }
    }
}
