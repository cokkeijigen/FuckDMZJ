package ss.colytitse.fuckdmzj;

import static ss.colytitse.fuckdmzj.MainHook.*;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends Activity {

    public static final String TAG = "test_";

    // 获取当前apk版本
    public String getVersionName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return String.format("%s(%s)", packageInfo.versionName, packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("InlinedApi")
    public void setActivityStatusBar(){
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        if(this.getApplicationContext().getResources().getConfiguration().uiMode == 0x11)
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    @SuppressLint({"SetTextI18n", "NewApi"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setActivityStatusBar();
        {
            TextView AppVersionName = findViewById(R.id.version_name);
            AppVersionName.setText(String.format((String) AppVersionName.getText(), getVersionName()));
            TextView SupportItems = findViewById(R.id.support_items);
            try {
                InputStream inputStream = this.getAssets().open("xposed_info");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp; StringBuilder ContentText = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) ContentText.append(String.format("　%s\n",temp));
                SupportItems.setText(String.format((String) SupportItems.getText(), ContentText.toString().replace("<\n","")));
            } catch (Exception e) {
                Log.d(TAG, "setSupportItems: err - >" + e);
            }
        }
        {
            @SuppressLint("QueryPermissionsNeeded")
            List<PackageInfo> allAppList = getPackageManager().getInstalledPackages(0);
            List<String> dmzj = allAppList.stream().filter(e -> e.packageName.contains("dmzj"))
                    .map(e -> e.packageName)
                    .filter(e -> (e.equals(DMZJ_PKGN) || e.equals(DMZJSQ_PKGN)))
                    .collect(Collectors.toList());
            TextView isInstall = findViewById(R.id.dmzjinstall);
            isInstall.setText(String.format((String) isInstall.getText(),
                    (dmzj.size() == 0) ? "无" : (
                        (dmzj.size() == 2) ? "普通版 & 社区版" :
                                (dmzj.get(0).equals(DMZJSQ_PKGN)) ? "仅社区版"  : "仅普通版"
                        )
                    ));
        }
    }

    public void openGitHub(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/cokkeijigen/FuckDMZJ"));
        startActivity(intent);
    }
}
