package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
    String address;
    String url_bus="", url_taxi="", url_subway="";


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
                            }else {
                                Intent intent_bus = new Intent(Intent.ACTION_VIEW, Uri.parse(url_bus));
                                startActivity(intent_bus);
                            }
                        }else if (menuItem.getItemId() == R.id.action_taxi){
                            if(url_bus.equals("")){
                                noAddress(); //주소 미입력 시, 안내창
                            }else {
                                Intent intent_taxi = new Intent(Intent.ACTION_VIEW, Uri.parse(url_taxi));
                                startActivity(intent_taxi);
                            }
                        }else if (menuItem.getItemId() == R.id.action_subway){
                            if(url_bus.equals("")){
                                noAddress(); //주소 미입력 시, 안내창
                            }else {
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
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    //불러온 주소 값
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Log.i("test", "data:" + data);
                        edit_addr.setText(data);

                        // 주소를 intent값으로 받아와서 일치하는 지역으로 url 설정정
                       address = data.substring(0, data.indexOf(" "));
                        if (address.equals("서울") ){
                            url_bus="https://www.seoul.go.kr/v2012/find.html?m=3";
                            url_taxi="http://www.stj.or.kr/";
                            url_subway="https://smrte.co.kr/support/lost";
                        } else if (address.equals("대구")){
                            url_bus="https://businfo.daegu.go.kr:8095/dbms_web/content/customer/found";
                            url_taxi="http://www.dgt.or.kr/popup/lost_tab_100420.html";
                            url_subway="http://www.dtro.or.kr/open_content_new/ko/sub_page/page.php?mnu_puid1=1&mnu_uid=13&mnu_puid2=12";
                        } else if(address.equals("부산")) {
                            url_bus = "http://www.busanbus.or.kr/busanbus/lost";
                            url_taxi = "http://www.bgtj.or.kr/webapps/freezone/loss_reply_list.jsp?table_name=TB_BBS_REPLY&boardtype=1";
                            url_subway = "http://www.humetro.busan.kr/homepage/default/lost/list.do?menu_no=1001010402#N";
                        } else if(address.equals("인천")) {
                            url_bus = "http://www.incheonbus.or.kr/";
                            url_taxi = "http://www.incheoncall.com/bbs/board_list.asp?curPage=3&SearchKey=&SearchWord=&PCD=D0003&bbs=lost&pSize=10";
                            url_subway = "";
                        } else if(address.equals("광주")) {
                            url_bus = "http://bus.gwangju.go.kr/guide/lost/lostList";
                            url_taxi = "http://www.gjtaxi.or.kr/module/board/board.php?bo_table=admin&device=pc";
                            url_subway = "https://www.grtc.co.kr/subway/board/list.do?rbsIdx=113";
                        } else if(address.equals("대전")) {
                            url_bus = "http://www.djbusterminal.co.kr/?module=Board&action=SiteBoard&iBrdNo=20&sMode=SELECT_FORM";
                            url_taxi = "http://www.djtaxi.co.kr/";
                            url_subway = "https://www.djet.co.kr/kor/page.do?menuIdx=311";
                        } else if(address.equals("울산")) {
                            url_bus = "http://www.ulsan.go.kr/s/lost/main.ulsan";
                            url_taxi = "http://www.ulsan.go.kr/s/lost/main.ulsan";
                            url_subway = "";
                        } else if(address.equals("경기")) {
                            url_bus = "http://www.gbus.or.kr/2006/lost/lost_01.htm?sm=3_1";
                            url_taxi = "http://www.ggtaxi.or.kr/03/03041.php";
                            url_subway = "";
                        } else if(address.equals("강원")) {
                            //시별로 유실물 센터 나눠진 듯
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("충북")) {
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("충남")) {
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("전북")) {
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("전남")) {
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("경북")) {
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("경남")) {
                            url_bus = "";
                            url_taxi = "";
                            url_subway = "";
                        } else if(address.equals("제주특별자치도")) {
                            url_bus = "https://www.jeju.go.kr/traffic/loss/guide.htm";
                            url_taxi = "";
                            url_subway = "";
                        }
                    }
                }
                break;
        }
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


}