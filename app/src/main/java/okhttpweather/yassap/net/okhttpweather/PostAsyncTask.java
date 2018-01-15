package okhttpweather.yassap.net.okhttpweather;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by matsudayasunori on 2018/01/14.
 */

public class PostAsyncTask extends AsyncTask<Object, Void, Object> {

    // USE WEEK REFERENCE
    private final WeakReference<Activity> w_Activity;

    // コンストラクタで、呼び出しもとのActivityを弱参照で変数セット。
    PostAsyncTask(Activity activity) {

        // USE WEEK REFERENCE
        this.w_Activity = new WeakReference<>(activity);
    }

    // バックグラウンド処理。
    @Override
    protected Object doInBackground(Object[] data) {

        // Object配列でパラメータを持ってこれたか確認。
        String url = (String) data[0];
        String description = (String) data[1];
        String filePath = (String) data[2];

        // HTTP処理用オブジェクト作成。
        OkHttpClient client = new OkHttpClient();

        // 送信用POSTデータを構築（Multipartで！）
        final String BOUNDARY = String.valueOf(System.currentTimeMillis());
        final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
        final MediaType IMAGE = MediaType.parse("image/jpeg");

        RequestBody requestBody = new MultipartBody.Builder(BOUNDARY)
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"description\""),
                        RequestBody.create(TEXT, description)
                )
                .addFormDataPart(
                        "upload_file",
                        "cat.jpg",
                        RequestBody.create(IMAGE, new File(filePath))
                )
                .build();

        // 送信用リクエストを作成。
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        // 受信用オブジェクトを作成。
        Call call = client.newCall(request);
        String result = "";

        // 送信と受信
        try {

            Response response = call.execute();
            ResponseBody body = response.body();

            if (body != null) {

                result = body.string();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        // 結果を返し、onPostExecuteで受け取る。
        return result;
    }

    // バックグラウンドで完了処理。
    @Override
    protected void onPostExecute(Object result){

        super.onPostExecute(result);
        Log.i("onPostExecute",(String)result);

        // 画面表示。
        Activity activity = w_Activity.get();

        if(activity == null || activity.isFinishing() || activity.isDestroyed()){

            return;
        }

        TextView tv = activity.findViewById(R.id.textview);
        tv.setText((String)result);
    }
















}
