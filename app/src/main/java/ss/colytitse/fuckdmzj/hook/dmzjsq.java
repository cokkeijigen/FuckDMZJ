package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedBridge.*;
import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.fucker.Others.*;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.hook.fucker.AdService;

public class dmzjsq implements IXposedHookLoadPackage {

    private static final String TAG = "test_";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                Context mContext = (Context) param.args[0];
                ClassLoader classLoader = mContext.getClassLoader();
                try {
                    XposedBridge.log("FuckDMZJ：DMZJSQ is running.");
                    new AdService(DMZJSQ_PKGN, classLoader);
                    BrowseActivityAncestors(DMZJSQ_PKGN, classLoader);
                    AppUpDataHelper(DMZJSQ_PKGN, classLoader);
                    TeenagerModeDialogActivity(DMZJSQ_PKGN, classLoader);
                    DoNotFuckMyClipboard();
                } catch (Throwable t) {
                    XposedBridge.log("FuckDMZJ -> dmzjsq handleLoadPackage： " + t);
                }
            }
        };
        findAndHookMethod(Application.class, "attach", Context.class, xc_methodHook);
    }

}
