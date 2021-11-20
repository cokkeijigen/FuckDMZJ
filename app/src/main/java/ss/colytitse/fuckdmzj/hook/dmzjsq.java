package ss.colytitse.fuckdmzj.hook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.MainHook;
import ss.colytitse.fuckdmzj.R;

public class dmzjsq implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        Context mContext = (Context)param.args[0];
                        ClassLoader classLoader = mContext.getClassLoader();
                        try {
                            XposedBridge.log("FUDM：DMZJSQ is running.");
                            fucks fk = new fucks(classLoader,MainHook.DMZJSQ_PKGN);
                            fk.fuckAdByAll();
                            fk.fuckAppUpData();
                            fk.fuckTeenagerMode();
                            fk.DoNotFuckMyClipboard();
                            fk.UserSign();
                        }catch (Throwable t){
                            XposedBridge.log("FUDMSQ_ERR: "+t.toString());
                        }
                    }
                }
        );

    }
}
