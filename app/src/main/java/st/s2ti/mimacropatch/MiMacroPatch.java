package st.s2ti.mimacropatch;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MiMacroPatch extends XposedHelper implements IXposedHookLoadPackage {
    XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "conf");

    public static final String TargetClassName = "com.miui.gamebooster.p.H";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Log.d(MainHook.TAG, "master_switch:" + prefs.getBoolean("master_switch", true));

        //总开关
        if(prefs.getBoolean("master_switch", true)){

            //王者荣耀开关
            if(prefs.getBoolean("enable_sgame", true)){
                //黑名单,检查传入的游戏，是否属于黑名单
                findAndHookMethod(TargetClassName, loadPackageParam.classLoader, "b", String.class, XC_MethodReplacement.returnConstant(false));
                //白名单,检查传入的游戏，是否属于白名单
                findAndHookMethod(TargetClassName, loadPackageParam.classLoader, "a", String.class, XC_MethodReplacement.returnConstant(true));
            }
        }
    }
}
