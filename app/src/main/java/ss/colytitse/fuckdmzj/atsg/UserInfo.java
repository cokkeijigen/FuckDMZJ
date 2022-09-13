package ss.colytitse.fuckdmzj.atsg;

import static ss.colytitse.fuckdmzj.MainHook.*;
import android.database.sqlite.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import androidx.annotation.NonNull;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import ss.colytitse.fuckdmzj.test.PublicContent;

@SuppressLint({"NewApi", "StaticFieldLeak", "DefaultLocale"})
public final class UserInfo extends PublicContent {

    private String userId;
    private String userToken;
    private String userSign;
    private final String database;
    private final String table;

    public UserInfo(Context mContext){
        this.database = app() ? "cartoon" : "room_dmzjsq.db";
        this.table = app() ? "user" : "User";
        Map<String, String> data = this.getData(mContext);
        if (data == null) return;
        this.userId = data.get("uid");
        this.userToken = data.get("token");
        this.userSign = createUserSign();
    }

    private boolean app(){
        return TARGET_PACKAGE_NAME.equals(DMZJ_PKGN);
    }

    @SuppressLint({"Recycle","Range"})
    private Map<String, String> getData(Context mContext){
        try {
            // 读取本地数据库
            SQLiteDatabase sqLiteDatabase = mContext.openOrCreateDatabase(this.database, Context.MODE_PRIVATE, null);
            Cursor user = sqLiteDatabase.query(this.table, null, null, null, null, null, null);
            if (user != null){
                Map<String, String> result = new HashMap<>();
                while(user.moveToNext()){
                    if (user.getInt(user.getColumnIndex("status"))!= 1) continue;
                    result.put("uid", user.getString(user.getColumnIndex("uid")));
                    result.put("token", user.getString(user.getColumnIndex("dmzj_token")));
                    return result;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getData: 错误？" + e);
        }
        return null;
    }

    private String createUserSign(){
        String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        StringBuilder result = new StringBuilder();
        try {
            for (int md5 : MessageDigest.getInstance("MD5").digest((userToken + userId + "d&m$z*j_159753twt").getBytes())) {
                if (md5 < 0) md5 += 256;
                result.append(hexDigits[md5 / 16]).append(hexDigits[md5 % 16]);
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserSign(){
        return this.userSign;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public boolean initComplete(){
        return (userId != null && userToken != null && userSign != null);
    }

    @Override @NonNull
    public String toString() {
        return String.format(
                "UserInfo ->{ uid(%s), token(%s), sign(%s) }",
                this.userId, this.userToken, this.userSign
        );
    }

    public static class user {

        String result;                // 请求结果
        int sign_count;               // 连续签到天数
        int max_sign_count;           // 最大连续天数
        int credits_nums;             // 积分数
        int silver_nums;              // 银币数
        boolean initComplete;         // 初始化状态

        public user(UserInfo userInfo) throws Exception {
            initComplete = false;
            if (!OkHttp.init()) return;
            Object Request = OkHttp.RequestBuilder(String.format((TARGET_PACKAGE_NAME.equals(DMZJSQ_PKGN) ?
                    "http://v3api.muwai.com" : "http://nnv3api.muwai.com") +  /* 获取状态接口 */
                    "/task/index?uid=%s&token=%s&sign=%s", userInfo.userId, userInfo.userToken, userInfo.userSign
            ), null);
            this.result = OkHttp.ResponseBodyString(Request);
            Arrays.stream(Objects.requireNonNull(this.result).split(","))
                    .filter(e -> e.contains("sign_count") || e.contains("credits_nums")|| e.contains("silver_nums"))
                    .filter(e -> !e.contains("}")).forEach(e -> {
                        if (e.contains("\"sign_count\""))
                            this.sign_count = Integer.parseInt(e.split(":")[2].replace("\"", "").trim());
                        else {
                            int num = Integer.parseInt(e.split(":")[1].replace("\"", "").trim());
                            if (e.contains("\"max_sign_count\"")) this.max_sign_count = num;
                            else if (e.contains("\"credits_nums\"")) this.credits_nums = num;
                            else if (e.contains("\"silver_nums\"")) this.silver_nums = num;
                        }
                    });
            initComplete = true;
        }

        public boolean notEquals(user us) {
            return this.sign_count != us.sign_count || this.max_sign_count != us.max_sign_count ||
                    this.credits_nums != us.credits_nums || this.silver_nums != us.silver_nums;
        }

        @Override @NonNull
        public String toString() {
            if (initComplete) return "    " +
                    "\nuser -> {" +
                    " \n    max_sign_count = "  + max_sign_count +
                    ";    sign_count = "     + sign_count +
                    "; \n    credits_nums = "   + credits_nums +
                    ";    silver_nums = "    + silver_nums +
                    ";\n} <- end";
            else return "user -> { null } <- end";
        }
    }
}

