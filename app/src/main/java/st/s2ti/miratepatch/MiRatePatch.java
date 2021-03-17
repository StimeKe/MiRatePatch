package st.s2ti.miratepatch;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import st.s2ti.miratepatch.plugins.HookRate;
import st.s2ti.miratepatch.plugins.IPlugin;

public class MiRatePatch extends XposedHelper implements IXposedHookLoadPackage {

    public static XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "conf");

    private static IPlugin[] plugins = {
            new HookRate()
    };

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelper.log(MainHook.TAG, String.format("Package: %s Status: Master Switch %s", lpparam.packageName, prefs.getBoolean("master_switch", true)));
        //总开关
        if (prefs.getBoolean("master_switch", true)) {
            loadPlugins(lpparam);
        }
    }

    private void loadPlugins(XC_LoadPackage.LoadPackageParam lpparam) {
        for (IPlugin plugin : plugins) {
            try {
                plugin.hook(lpparam);
            } catch (Error | Exception e) {
                XposedHelper.log(MainHook.TAG, "loadPlugins error" + e);
            }
        }
    }
}
