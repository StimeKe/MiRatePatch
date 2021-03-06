package st.s2ti.mimacropatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        checkEdXposed();
        checkExistMacro();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new SettingsFragment()).commit();
        }
    }

    @SuppressLint("WorldReadableFiles")
    private void checkEdXposed() {
        try {
            // getSharedPreferences will hooked by LSPosed and change xml file path to /data/misc/edxp**
            // will not throw SecurityException
            //noinspection deprecation
            getSharedPreferences("conf", Context.MODE_WORLD_READABLE);
        } catch (SecurityException exception) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.not_supported))
                    .setPositiveButton(android.R.string.ok, (dialog12, which) -> finish())
                    .setNegativeButton(R.string.ignore, null)
                    .show();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void checkExistMacro() {
        if (!MacroUtils.isAppExist(getApplicationContext(), MainHook.PremisePackageName)) {
            XposedHelper.log(MainHook.TAG, "Macro not found");
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.not_macro))
                    .setNegativeButton(android.R.string.ok, (dialog12, which) -> finish())
                    .show();
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("conf");
            addPreferencesFromResource(R.xml.prefs);
        }
    }
}
