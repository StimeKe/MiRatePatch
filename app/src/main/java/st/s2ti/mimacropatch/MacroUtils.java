package st.s2ti.mimacropatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class MacroUtils {

    @SuppressLint("QueryPermissionsNeeded")
    public static boolean isAppExist(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
