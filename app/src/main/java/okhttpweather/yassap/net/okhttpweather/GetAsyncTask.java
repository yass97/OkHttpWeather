package okhttpweather.yassap.net.okhttpweather;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by matsudayasunori on 2018/01/14.
 */

public class GetAsyncTask extends AsyncTask<Object,Void,Object> {

    // USE WEEK REFERENCE
    private final WeakReference<Activity> w_Activity;

    // コンストラクタで、呼び出しもとActivityを弱参照で変数セット。
    GetAsyncTask(Activity activity){

        this.w_Activity = new WeakReference<>(activity);
    }

    // バックグラウンド処理。
    @Override
    protected Object doInBackground(Object[] data){

        // Object配列でパラメータを持ってこれたか確認。
        String url = (String) data[0];
        String queryString = (String)data[1];

        // HTTP処理用オブジェクト生成。
        OkHttpClient client = new OkHttpClient();

        // 送信用リクエストを作成。
        Request request = new Request.Builder().url(url + queryString).get().build();

        // 受信用オブジェクトを作成。
        Call call = client.newCall(request);
        String result = "";

        // 送信と受信。
        try {

            Response response = call.execute();
            ResponseBody body = response.body();

            if(body != null){

                result = body.string();
            }

        }catch (IOException e){

            e.printStackTrace();
        }

        // 結果を返し、onPostExecuteで受け取る。
        return result;
    }

    // バックグラウンド完了処理。
    @Override
    protected void onPostExecute(Object result){

        super.onPostExecute(result);
        Log.i("onPostExecute",(String)result);

        // 簡単にJSONレスポンスをパースする。
        String title = "no response";
        String description = "";
        String publicTime = "";

        try {

            JSONObject json = new JSONObject((String)result);
            title = json.getString("title");
            JSONObject descriptionObj = (JSONObject)json.get("description");
            description = descriptionObj.getString("text");
            publicTime = descriptionObj.getString("publicTime");


        }catch (JSONException je){

            je.getStackTrace();
        }

        // 画面表示。
        Activity activity = w_Activity.get();

        if(activity == null || activity.isFinishing() || activity.isDestroyed()){

            // activity is no longer valid,don't do anything!
            return;
        }

        TextView tv = activity.findViewById(R.id.textview);
        String showText = title + "\n" + publicTime + "\n" + description;
        tv.setText(showText);

    }

}
