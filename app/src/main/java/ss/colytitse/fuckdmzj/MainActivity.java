package ss.colytitse.fuckdmzj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    public static final String TAG = "test_";

    // 获取当前apk版本
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return String.format("%s(%s)", packageInfo.versionName, packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override @SuppressLint("SetTextI18n")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        {
            String Name = "当前版本：" + getVersionName(this) +"\n\n By iTsukezigen";
            TextView VersionInfo = findViewById(R.id.versionName);
            VersionInfo.setText(Name);
        }
        {
            String url = "https://github.com/cokkeijigen/FuckDMZJ";
            TextView linkinfo = findViewById(R.id.link);
            linkinfo.setText(String.format("仓库地址：%s", url));
            linkinfo.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            });
        }
    }
}
