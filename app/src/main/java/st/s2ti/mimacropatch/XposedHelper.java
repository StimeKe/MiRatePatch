package st.s2ti.mimacropatch;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;


public class XposedHelper {

    public static void log(String Tag, String text) {
        XposedBridge.log(Tag + ": " + text);
    }

    public static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            if (findClass(p1, lpparam) != null) {
                XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
        }
    }

    public static void hookAllMethods(String p1, ClassLoader lpparam, String methodName, XC_MethodHook parameterTypesAndCallback) {
        try {
            Class<?> packageParser = findClass(p1, lpparam);
            XposedBridge.hookAllMethods(packageParser, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
        }

    }

    public void hookAllMethods(Class<?> packageManagerServiceUtils, String verifySignatures, XC_MethodHook methodHook) {
        try {
            XposedBridge.hookAllMethods(packageManagerServiceUtils, verifySignatures, methodHook);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
        }
    }

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
        }
        return null;
    }

    public static void hookAllConstructors(String p1, XC_MethodHook parameterTypesAndCallback) {
        try {
            Class<?> packageParser = findClass(p1, null);
            hookAllConstructors(packageParser, parameterTypesAndCallback);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
        }
    }

    private static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
        try {
            return XposedBridge.hookAllConstructors(hookClass, callback);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
            return null;
        }

    }
}
