package ss.colytitse.fuckdmzj.hook;

import ss.colytitse.fuckdmzj.MainHook;
import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class dmzj implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedBridge.log("FUDM：已运行动漫之家普通版");
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        ClassLoader classLoader = ((Context)param.args[0]).getClassLoader();
                        try {
                            fucks fk = new fucks(classLoader,MainHook.DMZJ_PKGN);
                            fk.fuck_AD();
                            fk.fuck_CheckVersionInfo();
                            fk.fuck_TeenagerMode();
                        }catch (Throwable t){
                            XposedBridge.log("FUDM_ERR:"+t.toString());
                        }
                    }
                }
        );
    }
}
