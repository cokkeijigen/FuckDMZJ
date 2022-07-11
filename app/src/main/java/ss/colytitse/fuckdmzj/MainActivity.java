package ss.colytitse.fuckdmzj;

import static ss.colytitse.fuckdmzj.test.PublicContent.*;
import static ss.colytitse.fuckdmzj.MainHook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends Activity {

    // 获取当前版本
    public String getVersionName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return String.format("%s(%s)", packageInfo.versionName, packageInfo.versionCode);
        } catch (Exception e) {
            Log.d(TAG, "getVersionName: err-> " + e);
            return null;
        }
    }

    @Override @SuppressLint({"SetTextI18n", "NewApi"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        {   // 设置状态栏样式
            Window thisWindow = this.getWindow();
            if(this.getResources().getConfiguration().uiMode == 0x11)
                thisWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            thisWindow.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        }
        {   // 设置模块信息内容
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
        {   // 设置已安装状态
            HashMap<String, String> dmzj = new HashMap<>();
            getPackageManager().getInstalledPackages(0).stream()
                    .filter(e -> (e.packageName.equals(DMZJ_PKGN) || e.packageName.equals(DMZJSQ_PKGN)))
                    .forEach(e -> dmzj.put(e.packageName, e.versionName));
            TextView isInstall = findViewById(R.id.dmzjinstall);
            String text = (String) isInstall.getText();
            if (dmzj.size() == 2)
                text = String.format(text, String.format("普通版(v%s) & 社区版(v%s)", dmzj.get(DMZJ_PKGN) , dmzj.get(DMZJSQ_PKGN)));
            else if (dmzj.get(DMZJ_PKGN) != null)
                text = String.format(text, "仅普通版(v%)".replace("%", Objects.requireNonNull(dmzj.get(DMZJ_PKGN))));
            else if (dmzj.get(DMZJSQ_PKGN) != null)
                text = String.format(text, "仅社区版(v%)".replace("%", Objects.requireNonNull(dmzj.get(DMZJSQ_PKGN))));
            else
                text = String.format(text, "无");
            isInstall.setText(text);
        }
    }

    public void openGitHub(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/cokkeijigen/FuckDMZJ"));
        startActivity(intent);
    }
}
