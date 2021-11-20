package ss.colytitse.fuckdmzj;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ss.colytitse.fuckdmzj.hook.dmzj;
import ss.colytitse.fuckdmzj.hook.dmzjsq;

public class MainHook implements IXposedHookLoadPackage {

    // 动漫之家普通版
    public static final String DMZJ_PKGN = "com.dmzj.manhua";
    // 动漫之家社区版
    public static final String DMZJSQ_PKGN = "com.dmzjsq.manhua";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            if(lpparam.packageName.equals(DMZJ_PKGN))
                /* 运行普通版 */
                new dmzj().handleLoadPackage(lpparam);
            if(lpparam.packageName.equals(DMZJSQ_PKGN))
                /* 运行社区版 */
                new dmzjsq().handleLoadPackage(lpparam);
        }catch (Throwable t){
            XposedBridge.log(".\n------------------------------------------------------\n"
                    +"FUDM_MAIN_ERR: " + t.toString()+
                    "\n------------------------------------------------------\n"
            );
        }
    }

}
