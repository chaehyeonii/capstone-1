package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //-------------------url 연결----------------

        Button button_taxi = findViewById(R.id.button_taxi); //택시 url 연결 버튼 가져오기
        Button button_bus = findViewById(R.id.button_bus); //버스 url 연결 버튼 가져오기
        Button button_subway = findViewById(R.id.button_subway);

        //-------------------url 연결----------------


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

        Button button_click = findViewById(R.id.button_click); //경찰청 버튼 가져오기
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




        //--------------그리드 뷰-------------
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(layoutManager);

        //AsyncTask
        HomeActivity.MyAsyncTask myAsyncTask = new HomeActivity.MyAsyncTask();
        myAsyncTask.execute();

        //----------------------------------
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

//-----url 연결 버튼 클릭 시-------
public void onClick_url(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    switch (v.getId()) {
        case R.id.button_bus:
            intent.setData(Uri.parse("https://www.seoul.go.kr/v2012/find.html?m=3"));
            startActivity(intent);
            break;

            /*
        case R.id.button_taxi:
            intent.setData(Uri.parse(""));
            startActivity(intent);
            break;
        case R.id.button_subway:
            intent.setData(Uri.parse(""));
            startActivity(intent);
            break;
            */

    }
    }
}