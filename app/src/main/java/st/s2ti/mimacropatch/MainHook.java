package st.s2ti.mimacropatch;

import android.os.Build;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    public static final String TAG = "MiMacroPatch";
    public static final String TargetPackageName = "com.miui.securitycenter";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (TargetPackageName.equals(lpparam.packageName)) {
            Log.d(TAG, "Current sdk version " + Build.VERSION.SDK_INT);
            new MiMacroPatch().handleLoadPackage(lpparam);
        }
    }
}