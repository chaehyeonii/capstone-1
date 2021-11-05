package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends Activity {

    //---------------그리드 뷰---------
    final String TAG = "HomeActivity";
    public String dataKey = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D";
    private String requestUrl;
    ArrayList<Police2_Item> list = null;
    Police2_Item item = null;
    RecyclerView recyclerView;
    //----------------------------------

    //----------주소 찾아오기----------------------
    // 초기변수설정
    EditText edit_addr;
    // 주소 요청코드 상수 requestCode
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    //-------------------------------------------------

    //url 연결 링크, 가져온 주소 값
    String[] address;
    String url_bus="", url_taxi="", url_subway="";
    ArrayList<HashMap<String, String>> mArrayList=new ArrayList<>();
    private static final String TAG_region = "region";
    private static final String TAG_bus_in = "bus_in";
    private static final String TAG_bus_out ="bus_out";
    private static final String TAG_taxi_private = "taxi_private";
    private static final String TAG_taxi_corporate ="taxi_corporate";
    private static final String TAG_subway ="subway";

    String region, bus_in, bus_out, taxi_private, taxi_corporate,subway;
    String region1, region2;


    //-------GPS 기능 관련
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //-------------------url 연결----------------

        //Button button_taxi = findViewById(R.id.button_taxi); //택시 url 연결 버튼 가져오기
        //Button button_bus = findViewById(R.id.button_bus); //버스 url 연결 버튼 가져오기




        Button button_address = findViewById(R.id.button_address); //주소검색 버튼 가져오기
        button_address.setOnClickListener(new View.OnClickListener() { //주소검색 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_address = new Intent(getApplicationContext(), AddressActivity.class);
                startActivity(intent_address);
            }
        });



        Button button_police = findViewById(R.id.button_police); //경찰청 버튼 가져오기
        button_police.setOnClickListener(new View.OnClickListener() { //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_police = new Intent(getApplicationContext(), PoliceActivity.class);
                startActivity(intent_police);
            }
        });

        Button button_police2 = findViewById(R.id.button_police2); //경찰청 버튼 가져오기
        button_police2.setOnClickListener(new View.OnClickListener() { //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_police2 = new Intent(getApplicationContext(), Police2Activity.class);
                startActivity(intent_police2);
            }
        });

        Button button_listview = findViewById(R.id.button_listview); //경찰청 버튼 가져오기
        button_listview.setOnClickListener(new View.OnClickListener() { //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_listview = new Intent(getApplicationContext(), ListviewActivity.class);
                startActivity(intent_listview);
            }
        });

        Button button_click = findViewById(R.id.button_click); //리스트뷰 클릭
        button_click.setOnClickListener(new View.OnClickListener() { //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_click = new Intent(getApplicationContext(), LostActivity.class);
                startActivity(intent_click);
            }
        });

        Button button_badge = findViewById(R.id.button_badge); //뱃지 버튼 가져오기
        button_badge.setOnClickListener(new View.OnClickListener() { //뱃지 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_badge = new Intent(getApplicationContext(), BadgeActivity.class);
                startActivity(intent_badge);
            }
        });

        Button button_find = findViewById(R.id.button_find); //습득물 찾기
        button_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_find = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent_find);
            }
        });

        Button button_gps = findViewById(R.id.button_gps); //gps 버튼 가져오기
        button_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gps = new Intent(getApplicationContext(), GpsActivity.class);
                startActivity(intent_gps);
            }
        });


        //-----------GPS 연결----------------
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        //final TextView textview_address = (TextView)findViewById(R.id.textview);

        Button ShowLocationButton = (Button) findViewById(R.id.button_GPS);
        ShowLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                gpsTracker = new GpsTracker(HomeActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address_gps = getCurrentAddress(latitude, longitude)
                        .replace("대한민국","").replace("서울특별시","서울"); //대한민국이라고 뜨는 부분을 지움
                edit_addr.setText(address_gps);

                Toast.makeText(HomeActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();

                //----------gps를 이용하여 url 연결하기 위한 주소 설정--------
                //address = address_gps.split(" ");
                //String getaddress=edit_addr.getText().toString();
                address=address_gps.split(" ");
                address[0]=address[1];
                address[1]=address[2];
                region1=address[0];
                region2=address[1];
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JsonObject = new JSONObject(response);
                            boolean success=JsonObject.getBoolean("success");
                            JSONArray jsonArray = JsonObject.getJSONArray("getpostdata");

                            Log.i("test", "성공?");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject item = jsonArray.getJSONObject(i);
                                region = item.getString(TAG_region );
                                bus_in = item.getString(TAG_bus_in );
                                bus_out = item.getString(TAG_bus_out );
                                taxi_private = item.getString(TAG_taxi_private );
                                taxi_corporate = item.getString(TAG_taxi_corporate );
                                subway = item.getString(TAG_subway );

                                HashMap<String,String> hashMap = new HashMap<>();

                                hashMap.put(TAG_region, region);
                                hashMap.put(TAG_bus_in, bus_in);
                                hashMap.put(TAG_bus_out, bus_out);
                                hashMap.put(TAG_taxi_private, taxi_private);
                                hashMap.put(TAG_taxi_corporate, taxi_corporate);
                                hashMap.put(TAG_subway, subway);
                                mArrayList.add(hashMap);
                                Log.e("test",String.valueOf(mArrayList));
                            }
                            if(success){
                                Toast.makeText(getApplicationContext(),"주소입력 성공",Toast.LENGTH_SHORT).show();
                                url_bus=bus_out;
                                url_taxi=taxi_corporate;
                                url_subway=subway;
                                //TextView textView5=findViewById(R.id.textView5);
                                //textView5.setText(url_bus);

                            }else{
                                Toast.makeText(getApplicationContext(),"주소입력 실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            Log.e("test","test: catch");
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley 이용해서 요청
                RequestRegion RequestRegion = new RequestRegion( region1,region2, responseListener);
                RequestQueue queue = Volley.newRequestQueue( HomeActivity.this );
                queue.add( RequestRegion );


                /*

                if (address[1].equals("서울") ){
                    url_bus="https://www.seoul.go.kr/v2012/find.html?m=3";
                    url_taxi="http://www.stj.or.kr/";
                    url_subway="https://smrte.co.kr/support/lost";
                } else if (address[1].equals("대구")|address[1].equals("대구광역시")){
                    url_bus="https://businfo.daegu.go.kr:8095/dbms_web/content/customer/found";
                    url_taxi="http://www.dgt.or.kr/popup/lost_tab_100420.html";
                    url_subway="http://www.dtro.or.kr/open_content_new/ko/sub_page/page.php?mnu_puid1=1&mnu_uid=13&mnu_puid2=12";
                } else if(address[1].equals("부산")|address[1].equals("부산광역시")) {
                    url_bus = "http://www.busanbus.or.kr/busanbus/lost";
                    url_taxi = "http://www.bgtj.or.kr/webapps/freezone/loss_reply_list.jsp?table_name=TB_BBS_REPLY&boardtype=1";
                    url_subway = "http://www.humetro.busan.kr/homepage/default/lost/list.do?menu_no=1001010402#N";
                }


                 */

            }
        });

        //--------------그리드 뷰-------------
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(layoutManager);

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        //----------------------------------


        //----------주소 검색-----------------
        // UI 요소 연결
        edit_addr = findViewById(R.id.edit_addr);

        // 터치 안되게 막기
        edit_addr.setFocusable(false);
        // 주소입력창 클릭
        edit_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("주소설정페이지", "주소입력창 클릭");
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent i = new Intent(getApplicationContext(), AddressActivity_webview.class);
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //--------검색 팝업-------------
        findViewById(R.id.button_bus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popup_menu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.popup_menu,popup_menu.getMenu());
                popup_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_bus){
                            if(url_bus.equals("")){
                                noAddress(); //주소 미입력 시, 안내창
                            }else if(url_bus.equals("NULL")){
                                noProvide(); //해당 지역 url이 NULL일 시, 서비스 제공하지 않는 지역이라고 안내문구
                            }else {
                                Intent intent_bus = new Intent(Intent.ACTION_VIEW, Uri.parse(url_bus));
                                startActivity(intent_bus);
                            }
                        }else if (menuItem.getItemId() == R.id.action_taxi){
                            if(url_taxi.equals("")){
                                noAddress(); //주소 미입력 시, 안내창
                            }else if(url_taxi.equals("NULL")){
                                noProvide(); //해당 지역 url이 NULL일 시, 서비스 제공하지 않는 지역이라고 안내문구
                            }else {
                                Intent intent_taxi = new Intent(Intent.ACTION_VIEW, Uri.parse(url_taxi));
                                startActivity(intent_taxi);
                            }
                        }else if (menuItem.getItemId() == R.id.action_subway){
                            if(url_subway.equals("")){
                                noAddress(); //주소 미입력 시, 안내창
                            }else if(url_subway.equals("NULL")){
                                noProvide(); //해당 지역 url이 NULL일 시, 서비스 제공하지 않는 지역이라고 안내문구
                            }
                            else{
                                Intent intent_subway = new Intent(Intent.ACTION_VIEW, Uri.parse(url_subway));
                                startActivity(intent_subway);
                            }
                        }
                        return false;
                    }
                });
                popup_menu.show();
            }
        });

        //---------------------------------------------------------------





    }

// -----------------그리드 뷰-----------------
public class MyAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {

        requestUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccToClAreaPd?serviceKey=" +dataKey+ "&pageNo=1&numOfRows=4";
        try {
            boolean b_atcId = false;
            boolean b_lstPlace =false;
            boolean b_lstPrdtNm = false;
            boolean b_lstSbjt = false;
            boolean b_lstYmd = false;

            URL url = new URL(requestUrl);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<Police2_Item>();
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item") && item != null) {
                            list.add(item);
                        }
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("item")){
                            item = new Police2_Item();
                        }
                        if (parser.getName().equals("atcId")) b_atcId = true;
                        if (parser.getName().equals("lstPlace")) b_lstPlace = true;
                        if (parser.getName().equals("lstPrdtNm")) b_lstPrdtNm = true;
                        if (parser.getName().equals("lstSbjt")) b_lstSbjt = true;
                        if (parser.getName().equals("lstYmd")) b_lstYmd = true;
                        break;
                    case XmlPullParser.TEXT:
                        if(b_atcId){
                            item.setatcId(parser.getText());
                            b_atcId = false;
                        } else if(b_lstPlace) {
                            item.setlstPlace(parser.getText());
                            b_lstPlace = false;
                        } else if (b_lstPrdtNm) {
                            item.setlstPrdtNm(parser.getText());
                            b_lstPrdtNm = false;
                        } else if(b_lstSbjt) {
                            item.setlstSbjt(parser.getText());
                            b_lstSbjt = false;
                        } else if(b_lstYmd) {
                            item.setlstYmd(parser.getText());
                            b_lstYmd = false;
                        }

                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //어답터 연결
        Police2_Adapter adapter = new Police2_Adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);
    }
}
    //   --------------------------------------


    //------주소 검색 ------------
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("test", "onActivityResult");

        switch (requestCode) {
            //-------GPS 부분-------------------
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;

                //---------주소 검색 부분-----------
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
                        region1=address[0];
                        region2=address[1];
                    }
                }
                break;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JsonObject = new JSONObject(response);
                    boolean success=JsonObject.getBoolean("success");
                    JSONArray jsonArray = JsonObject.getJSONArray("getpostdata");

                    Log.i("test", "성공?");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        region = item.getString(TAG_region );
                        bus_in = item.getString(TAG_bus_in );
                        bus_out = item.getString(TAG_bus_out );
                        taxi_private = item.getString(TAG_taxi_private );
                        taxi_corporate = item.getString(TAG_taxi_corporate );
                        subway = item.getString(TAG_subway );

                        HashMap<String,String> hashMap = new HashMap<>();

                        hashMap.put(TAG_region, region);
                        hashMap.put(TAG_bus_in, bus_in);
                        hashMap.put(TAG_bus_out, bus_out);
                        hashMap.put(TAG_taxi_private, taxi_private);
                        hashMap.put(TAG_taxi_corporate, taxi_corporate);
                        hashMap.put(TAG_subway, subway);
                        mArrayList.add(hashMap);
                        Log.e("test",String.valueOf(mArrayList));
                    }
                    if(success){
                        Toast.makeText(getApplicationContext(),"주소입력 성공",Toast.LENGTH_SHORT).show();
                        url_bus=bus_out;
                        url_taxi=taxi_corporate;
                        url_subway=subway;
                        //TextView textView5=findViewById(R.id.textView5);
                        //textView5.setText(url_bus);

                    }else{
                        Toast.makeText(getApplicationContext(),"주소입력 실패",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Log.e("test","test: catch");
                    e.printStackTrace();
                }
            }
        };
        //서버로 Volley 이용해서 요청
        RequestRegion RequestRegion = new RequestRegion( region1,region2, responseListener);
        RequestQueue queue = Volley.newRequestQueue( HomeActivity.this );
        queue.add( RequestRegion );

    }

    //--------------------------안내 팝업
    void noAddress() { //주소 미입력 시, 나올 팝업
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(HomeActivity.this)
                .setTitle("주소 미입력") .setMessage("상단에 주소를 추가해주세요.")
                .setPositiveButton("확 인", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(HomeActivity.this, "확인", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    void noProvide() { //해당 지역에서 습득물/분실물 사이트 제공하지 않을 시 나올 팝업
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(HomeActivity.this)
                .setTitle("제공 불가 지역") .setMessage("해당지역은 서비스 제공을 하지않습니다.")
                .setPositiveButton("확 인", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(HomeActivity.this, "확인", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }


    //----------GPS 기능 관련 --------------------
    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                //위치 값을 가져올 수 있음;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(HomeActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(HomeActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(HomeActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(HomeActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(HomeActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }


    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

 */

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}