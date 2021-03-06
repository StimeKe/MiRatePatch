package st.s2ti.mimacropatch.plugins;

import java.util.Map;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import st.s2ti.mimacropatch.MainHook;
import st.s2ti.mimacropatch.MiMacroPatch;
import st.s2ti.mimacropatch.XposedHelper;

import static st.s2ti.mimacropatch.XposedHelper.findAndHookMethod;

public class SGame implements IPlugin {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, Map<String, Map<String, String>> currClassConfig) {
        //王者荣耀开关
        if (MiMacroPatch.prefs.getBoolean("enable_sgame", true)) {
            XposedHelper.log(MainHook.TAG, "Enable sgame");

            //黑名单,检查传入的游戏，是否属于黑名单
            if (currClassConfig.containsKey("gamebooster_game_blacklist")) {
                Map<String, String> game_blacklist = currClassConfig.get("gamebooster_game_blacklist");
                findAndHookMethod(game_blacklist.get("class"), lpparam.classLoader, game_blacklist.get("method"), String.class, XC_MethodReplacement.returnConstant(false));
            }
            //白名单,检查传入的游戏，是否属于白名单
            if (currClassConfig.containsKey("gamebooster_game_whitelist")) {
                Map<String, String> game_whitelist = currClassConfig.get("gamebooster_game_whitelist");
                findAndHookMethod(game_whitelist.get("class"), lpparam.classLoader, game_whitelist.get("method"), String.class, XC_MethodReplacement.returnConstant(true));
            }
        }
    }
}
