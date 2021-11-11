package ss.colytitse.fuckdmzj.hook;

import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.MainHook;

public class dmzjsq implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        ClassLoader classLoader = ((Context)param.args[0]).getClassLoader();
                        try {
                            XposedBridge.log("FUDM：已运行动漫之家社区版");
                            fucks fk = new fucks(classLoader,MainHook.DMZJSQ_PKGN);
                            fk.fuckAdByAll();
                            fk.fuckAppUpData();
                            fk.fuckTeenagerMode();
                        }catch (Throwable t){
                            XposedBridge.log("FUDMSQ_ERR:"+t.toString());
                        }
                    }
                }
        );

    }
}
