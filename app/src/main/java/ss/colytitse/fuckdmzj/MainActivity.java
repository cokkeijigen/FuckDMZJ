package ss.colytitse.fuckdmzj;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    // 获取当前apk版本
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", "这里是要复制的文字");
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);

        setContentView(R.layout.main_layout);
        {
            String Name = "当前版本：" + getVersionName(this) +"\n\n By iTsukezigen";
            TextView VersionInfo = findViewById(R.id.versionName);
            VersionInfo.setText(Name);
        }
    }
}
