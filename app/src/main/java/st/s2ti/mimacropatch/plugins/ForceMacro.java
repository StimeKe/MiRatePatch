package st.s2ti.mimacropatch.plugins;

import android.content.Context;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import st.s2ti.mimacropatch.MainHook;
import st.s2ti.mimacropatch.MiMacroPatch;
import st.s2ti.mimacropatch.XposedHelper;

import static st.s2ti.mimacropatch.XposedHelper.findAndHookMethod;

public class ForceMacro implements IPlugin {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, Map<String, Map<String, String>> currClassConfig) {
        //强制开启自动连招
        if (MiMacroPatch.prefs.getBoolean("macro_switch", true)) {

            XposedHelper.log(MainHook.TAG, "Force macro on");

            //Hook 判断是否横屏
//                findAndHookMethod("com.miui.gamebooster.p.ua", lpparam.classLoader, "g", Context.class, new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        super.beforeHookedMethod(param);
//                        XposedHelper.log(MainHook.TAG,
//                                "--------------------Hook-------------------\n" +
//                                        "Title:判断是否横屏\n" +
//                                        "Param:\n" +
//                                        "Context:" + ((Context) param.args[0]) + "\n" +
//                                        "-------------------------------------------\n");
//                    }
//                });

            //Hook Macro 开关 判断包名
            if (currClassConfig.containsKey("gamebooster_is_pkg")) {
                Map<String, String> is_pkg = currClassConfig.get("gamebooster_is_pkg");
                findAndHookMethod(is_pkg.get("class"), lpparam.classLoader, is_pkg.get("method"), Context.class, String.class, XC_MethodReplacement.returnConstant(true));
            }

            //Hook 判断key_macro_toast
            if (currClassConfig.containsKey("gamebooster_is_macro_toast")) {
                Map<String, String> is_macro_toast = currClassConfig.get("gamebooster_is_macro_toast");
                findAndHookMethod(is_macro_toast.get("class"), lpparam.classLoader, is_macro_toast.get("method"), XC_MethodReplacement.returnConstant(false));
            }

            //Hook 判断是否国际版
            if (currClassConfig.containsKey("gamebooster_is_INTERNATIONAL")) {
                Map<String, String> is_INTERNATIONAL = currClassConfig.get("gamebooster_is_INTERNATIONAL");
                findAndHookMethod(is_INTERNATIONAL.get("class"), lpparam.classLoader, is_INTERNATIONAL.get("method"), XC_MethodReplacement.returnConstant(true));
            }

            if (currClassConfig.containsKey("gamebooster_persistence_get")) {
                Map<String, String> persistence_get = currClassConfig.get("gamebooster_persistence_get");

                //Hook获取Bool序列化配置
                findAndHookMethod(persistence_get.get("class"),
                        lpparam.classLoader,
                        persistence_get.get("method"),
                        String.class,
                        boolean.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                if ("game_macro_bubble".equals(param.args[0])) {
                                    param.setResult(true);
                                }
                            }
                        });

                //Hook获取String序列化配置
                findAndHookMethod(persistence_get.get("class"),
                        lpparam.classLoader,
                        persistence_get.get("method"),
                        String.class,
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                if ("key_macro_toast".equals(param.args[0])) {
                                    param.setResult("2#1614942234511");
                                }
                            }
                        });
            }
        }
    }
}
