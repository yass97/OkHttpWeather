package okhttpweather.yassap.net.okhttpweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_get).setOnClickListener(this);
//        findViewById(R.id.button_post).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_get:

                // GET リクエストサンプルとして「Livedoor天気情報」にアクセス
                String weatherUrl = "http://weather.livedoor.com/forecast/webservice/json/v1";
                // 福岡のIDを指定。

                Spinner cityName = (Spinner) findViewById(R.id.spinner);
                String getName = (String) cityName.getSelectedItem();
                String cityId = "";

                Log.d("getNameのデータ", getName);

                if (getName.equals("福岡")) {

                    // 福岡
                    cityId = "400010";

                } else if (getName.equals("長崎")) {

                    // 佐世保
                    cityId = "420020";

                } else if (getName.equals("熊本")) {

                    // 熊本市
                    cityId = "430010";

                } else if (getName.equals("大分")) {

                    cityId = "440010";

                } else if (getName.equals("宮崎")) {

                    // 宮崎市
                    cityId = "450010";

                } else if (getName.equals("鹿児島")) {

                    // 鹿児島市
                    cityId = "460010";

                } else if (getName.equals("沖縄")) {

                    // 那覇
                    cityId = "471010";
                } else if (getName.equals("佐賀")) {

                    cityId = "410010";
                }

                String queryString = "?city=" + cityId;

                // GetAsyncTaskに渡すパラメータをObject配列に設定。
                Object[] getParams = new Object[2];
                getParams[0] = weatherUrl;
                getParams[1] = queryString;

                // GetAsyncTaskを実行。
                new GetAsyncTask(this).execute(getParams);

                break;

//            case R.id.button_post:
//
//                // POSTリクエストサンプルとして、自前のファイル受信サイトへアクセス。
//                // TODO サーバーURL追加。
//                String uploaderUrl = "http://160.16.141.35/home/yasu/fileDir/";
//                String title = "Success Upload image file!";
//                // TODO ファイル追加。
//                String uploadFile = "/storage/emulated/0/Pictures/cat.jpg";
//
//                // PostAsyncTaskに渡すパラメータをObject配列に設定。
//                Object[] postParams = new Object[3];
//                postParams[0] = uploaderUrl;
//                postParams[1] = title;
//                postParams[2] = uploadFile;
//
//                // PostAsyncTaskを実行。
//                new PostAsyncTask(this).execute(postParams);
//
//                break;

            default:

                break;
        }
    }
}
