package st.s2ti.miratepatch;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    public static final String TAG = "MiRatePatch";
    public static final String TargetPackageNm = "com.xiaomi.misettings";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelper.log(TAG, String.format("Package: %s Status: Init",lpparam.packageName));
        if (TargetPackageNm.equals(lpparam.packageName)) {
            XposedHelper.log(TAG, String.format("Package: %s Status: Success",lpparam.packageName));
            new MiRatePatch().handleLoadPackage(lpparam);
        }
    }
}