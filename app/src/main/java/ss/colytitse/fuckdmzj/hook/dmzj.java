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
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        ClassLoader classLoader = ((Context)param.args[0]).getClassLoader();
                        try {
                            XposedBridge.log("FUDM：DMZJ is running.");
                            fucks fk = new fucks(classLoader,MainHook.DMZJ_PKGN);
                            fk.fuckAdByAll();
                            fk.fuckAppUpData();
                            fk.fuckTeenagerMode();
                            fk.UserSign();
                            fk.DoNotFuckMyClipboard();
                        }catch (Throwable t){
                            XposedBridge.log("FUDM_ERR: "+t.toString());
                        }
                    }
                }
        );
    }
}
