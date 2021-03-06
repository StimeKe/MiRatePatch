package st.s2ti.mimacropatch;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import st.s2ti.mimacropatch.plugins.ForceMacro;
import st.s2ti.mimacropatch.plugins.IPlugin;
import st.s2ti.mimacropatch.plugins.SGame;

public class MiMacroPatch extends XposedHelper implements IXposedHookLoadPackage {

    public static XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "conf");

    private static IPlugin[] plugins = {
            new ForceMacro(),
            new SGame(),
    };


    private static Map<String, Map<String, Map<String, String>>> classConfig = JSON.parseObject("{\n" +
            "    \"unknown\":{\n" +
            "        \"gamebooster_is_pkg\":{\n" +
            "            \"class\":\"com.miui.gamebooster.k.a\",\n" +
            "            \"method\":\"a\"\n" +
            "        },\n" +
            "        \"gamebooster_is_macro_toast\":{\n" +
            "            \"class\":\"com.miui.gamebooster.k.a\",\n" +
            "            \"method\":\"a\"\n" +
            "        },\n" +
            "        \"gamebooster_is_INTERNATIONAL\":{\n" +
            "            \"class\":\"com.miui.gamebooster.k.a\",\n" +
            "            \"method\":\"d\"\n" +
            "        },\n" +
            "        \"gamebooster_persistence_get\":{\n" +
            "            \"class\":\"com.miui.common.persistence.b\",\n" +
            "            \"method\":\"a\"\n" +
            "        },\n" +
            "        \"gamebooster_game_blacklist\":{\n" +
            "            \"class\":\"com.miui.gamebooster.p.H\",\n" +
            "            \"method\":\"b\"\n" +
            "        },\n" +
            "        \"gamebooster_game_whitelist\":{\n" +
            "            \"class\":\"com.miui.gamebooster.p.H\",\n" +
            "            \"method\":\"a\"\n" +
            "        }\n" +
            "    }\n" +
            "}", new TypeReference<Map<String, Map<String, Map<String, String>>>>() {
    });

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        XposedHelper.log(MainHook.TAG, "master_switch: " + prefs.getBoolean("master_switch", true));

        //总开关
        if (prefs.getBoolean("master_switch", true)) {
            try {
                XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Context context = (Context) param.args[0];

                        PackageInfo targetPackage = getVersion(context, MainHook.TargetPackageName);
                        PackageInfo premisePackage = getVersion(context, MainHook.PremisePackageName);

                        if (premisePackage == null) {
                            XposedHelper.log(MainHook.TAG, "You don't have a macro app");
                            return;
                        }

                        String versionCode = String.valueOf(targetPackage.getLongVersionCode());
                        XposedHelper.log(MainHook.TAG, "Target app version " + versionCode);

                        if (!classConfig.containsKey(versionCode)) {
                            XposedHelper.log(MainHook.TAG, "The current version is not supported " + versionCode);
                            versionCode = "unknown";
                        }

                        Map<String, Map<String, String>> currClassConfig = (Map<String, Map<String, String>>) classConfig.get(versionCode);

                        loadPlugins(lpparam, currClassConfig);
                    }
                });
            } catch (Error | Exception e) {
                XposedHelper.log(MainHook.TAG, "attachBaseContext error" + e);
            }
        }
    }

    private PackageInfo getVersion(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadPlugins(XC_LoadPackage.LoadPackageParam lpparam, Map<String, Map<String, String>> currClassConfig) {
        for (IPlugin plugin : plugins) {
            try {
                plugin.hook(lpparam, currClassConfig);
            } catch (Error | Exception e) {
                XposedHelper.log(MainHook.TAG, "loadPlugins error" + e);
            }
        }
    }
}
