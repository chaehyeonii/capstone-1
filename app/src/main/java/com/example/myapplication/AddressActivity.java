//우선 주소 검색 창 누르면 검색 뷰로 이동하게 설정 해 두었음
//홈화면 구성할 때 버튼으로 변경해야 됨 ! !


package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AddressActivity extends AppCompatActivity {
    //-------데이터를 받아오기 위해-----------
    //url 연결 링크, 가져온 주소 값
    String[] address;
    String url_bus = "", url_taxi = "", url_subway = "";
    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();
    private static final String TAG_region = "region";
    private static final String TAG_bus_in = "bus_in";
    private static final String TAG_bus_out = "bus_out";
    private static final String TAG_taxi_private = "taxi_private";
    private static final String TAG_taxi_corporate = "taxi_corporate";
    private static final String TAG_subway = "subway";

    String region, bus_in, bus_out, taxi_private, taxi_corporate, subway;
    String region1, region2;


    //--------------------------------------

    // 초기변수설정
    EditText edit_addr;
    TextView textView4;
    // 주소 요청코드 상수 requestCode
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // UI 요소 연결
        edit_addr = findViewById(R.id.edit_addr);
        textView4=findViewById(R.id.textView4);

        // 터치 안되게 막기
        edit_addr.setFocusable(false);
        // 주소입력창 클릭
        edit_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("주소설정페이지", "주소입력창 클릭");
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent i = new Intent(getApplicationContext(), AddressActivity_webview.class);
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("test", "onActivityResult");

        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    //불러온 주소 값
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Log.i("test", "data:" + data);
                        edit_addr.setText(data);

                        // 주소를 intent값으로 받아와서 일치하는 지역으로 url 설정정
                        //address = data.substring(0, data.indexOf(" "));
                        address = data.split(" ");
                        region1 = address[0];
                        region2 = address[1];
                    }
                }
                break;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JsonObject = new JSONObject(response);
                    boolean success = JsonObject.getBoolean("success");
                    JSONArray jsonArray = JsonObject.getJSONArray("getpostdata");

                    Log.i("test", "성공?");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        region = item.getString(TAG_region);
                        bus_in = item.getString(TAG_bus_in);
                        bus_out = item.getString(TAG_bus_out);
                        taxi_private = item.getString(TAG_taxi_private);
                        taxi_corporate = item.getString(TAG_taxi_corporate);
                        subway = item.getString(TAG_subway);

                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put(TAG_region, region);
                        hashMap.put(TAG_bus_in, bus_in);
                        hashMap.put(TAG_bus_out, bus_out);
                        hashMap.put(TAG_taxi_private, taxi_private);
                        hashMap.put(TAG_taxi_corporate, taxi_corporate);
                        hashMap.put(TAG_subway, subway);
                        mArrayList.add(hashMap);
                        Log.e("test", String.valueOf(mArrayList));
                    }
                    if (success) {
                        Toast.makeText(getApplicationContext(), "주소입력 성공", Toast.LENGTH_SHORT).show();
                        url_bus = bus_in;
                        url_taxi = taxi_corporate;
                        url_subway = subway;
                        textView4.setText(url_bus);

                    } else {
                        Toast.makeText(getApplicationContext(), "주소입력 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Log.e("test", "test: catch");
                    e.printStackTrace();
                }
            }
        };
        //서버로 Volley 이용해서 요청
        RequestRegion RequestRegion = new RequestRegion(region1, region2, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AddressActivity.this);
        queue.add(RequestRegion);
    }
}