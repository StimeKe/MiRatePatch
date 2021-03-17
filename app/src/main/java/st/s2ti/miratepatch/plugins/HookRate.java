package st.s2ti.miratepatch.plugins;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import st.s2ti.miratepatch.MainHook;
import st.s2ti.miratepatch.XposedHelper;

import static st.s2ti.miratepatch.XposedHelper.findAndHookMethod;

public class HookRate implements IPlugin {
    private static final String TargetClassNm = "com.xiaomi.misettings.display.RefreshRate.RefreshRateActivity";
    private static final String TargetMethodNm = "b";
    private static final int[] fpsList = {144,120,90,60};

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelper.log(MainHook.TAG, String.format("Package: %s Status: Load Plugins HookRate", lpparam.packageName));

        findAndHookMethod(TargetClassNm, lpparam.classLoader, TargetMethodNm, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Class clazz = lpparam.classLoader.loadClass(TargetClassNm);
                Field field = clazz.getDeclaredField("d");
                field.setAccessible(true);
                int[] tmpDb = (int[])field.get(param.thisObject);
                XposedHelper.log(MainHook.TAG, "before:"+ Arrays.toString(tmpDb));
                field.set(param.thisObject,fpsList);
                int[] tmpDa = (int[])field.get(param.thisObject);
                XposedHelper.log(MainHook.TAG, "current:"+ Arrays.toString(tmpDa));
            }
        });
    }
}
