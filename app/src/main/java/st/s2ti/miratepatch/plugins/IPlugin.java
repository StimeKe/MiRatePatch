package st.s2ti.miratepatch.plugins;

import java.util.Map;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface IPlugin {
    void hook(XC_LoadPackage.LoadPackageParam lpparam);
}
