package ss.colytitse.fuckdmzj.hook;

import static de.robv.android.xposed.XposedHelpers.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import static ss.colytitse.fuckdmzj.hook.fucker.Others.*;
import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.hook.fucker.AdService;

public class dmzj implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
                try {
                    XposedBridge.log("FuckDMZJ：DMZJ is running.");
                    new AdService(DMZJ_PKGN, classLoader);
                    BrowseActivityAncestors(DMZJ_PKGN, classLoader);
                    AppUpDataHelper(DMZJ_PKGN, classLoader);
                    TeenagerModeDialogActivity(DMZJ_PKGN, classLoader);
                    DoNotFuckMyClipboard();
                } catch (Throwable t) {
                    XposedBridge.log("FuckDMZJ -> dmzj handleLoadPackage： " + t);
                }
            }
        };

        findAndHookMethod(Application.class, "attach", Context.class,xc_methodHook);
    }

}
