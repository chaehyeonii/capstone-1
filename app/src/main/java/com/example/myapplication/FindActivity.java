//데이터키 아직 안나와서 수정 안 함!

//추가 해야할 것: 무한 스크롤
//무한 스크롤: 스크롤 끝에 다다르면 다음 데이터 불러오기


package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class FindActivity extends Activity{

    public String dataKey = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D";
    private String requestUrl;
    private String requestUrl2;
    ArrayList<Find_Item> list = new ArrayList<Find_Item>();
    Find_Item item = null;
    RecyclerView recyclerView;
    int page=1;
    int limit=30;

    EditText search;

    //-------무한 스크롤------------
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;

    //로딩중을 띄워주는 progressDialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police2);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

        //검색
        search=(EditText)findViewById(R.id.search);

        //-----무한 스크롤--------
        nestedScrollView=findViewById(R.id.scroll_view);
        progressBar=findViewById(R.id.progress_bar);


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY){
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getXmlData(page, limit);

                    //myAsyncTask.execute();
                }
            }
        });
    }

    void getXmlData(int page, int limit){
        FindActivity.MyAsyncTask myAsyncTask = new FindActivity.MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            requestUrl = "http://apis.data.go.kr/1320000/LosfundInfoInqireService/getLosfundInfoAccToClAreaPd?serviceKey=" +dataKey
                    + "&pageNo="+page+"&numOfRows="+limit;
            try {
                boolean b_atcId = false;
                boolean b_depPlace =false;
                boolean b_fdPrdtNm = false;
                boolean b_fdYmd = false;
                boolean b_fdFilePathImg = false;
                boolean b_fdSn = false;

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
                            //list = new ArrayList<Find_Item>();
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
                                item = new Find_Item();
                            }
                            if (parser.getName().equals("atcId")) b_atcId = true;
                            if (parser.getName().equals("depPlace")) b_depPlace = true;
                            if (parser.getName().equals("fdPrdtNm")) b_fdPrdtNm = true;
                            if (parser.getName().equals("fdYmd")) b_fdYmd = true;
                            if (parser.getName().equals("fdFilePathImg")) b_fdFilePathImg = true;
                            if (parser.getName().equals("fdSn")) b_fdSn = true;
                            break;

                        case XmlPullParser.TEXT:
                            if(b_atcId){
                                item.setatcId(parser.getText());
                                b_atcId = false;
                            } else if(b_depPlace) {
                                item.setdepPlace(parser.getText());
                                b_depPlace = false;
                            } else if (b_fdPrdtNm) {
                                item.setfdPrdtNm(parser.getText());
                                b_fdPrdtNm = false;
                            } else if(b_fdYmd) {
                                item.setfdYmd(parser.getText());
                                b_fdYmd = false;
                            } else if(b_fdFilePathImg) {
                                item.setfdFilePathImg(parser.getText());
                                b_fdFilePathImg = false;
                            } else if(b_fdSn) {
                                item.setfdSn(parser.getText());
                                b_fdSn = false;
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
        //------------------------------


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            Find_Adapter adapter = new Find_Adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
        }
    }
}