package com.example.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

//Adapter 객체화 후 container에 꽂기!
public class LostActivity extends Activity {
    //bundle 생성
    Bundle bundle = new Bundle();


    //수정 1
    ArrayList<Lost_Item> list;
    Lost_Item item = null;
    RecyclerView list_container;

    final String TAG = "LostActivity";
    public String dataKey = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D";
    private String requestUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        //list = new ArrayList<>();

        RecyclerView list_container=findViewById(R.id.list_container);
        RecyclerViewAdapter adaptor=new RecyclerViewAdapter(this, list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);

        //setHasFixedSize: 각 요소의 가로, 세로 폭 변경 등의 문제가 발생하는데 이런 불필요 작업을 배제
        list_container.setHasFixedSize(true);
        //setLayoutManager: ViewGroup은 LinearLayout이므로 LinearLayoutManager 객체를 할당해 사용
        list_container.setLayoutManager(layoutManager);
        //adaptor를 container에 할당
        list_container.setAdapter(adaptor);

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            requestUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccToClAreaPd?serviceKey=" +dataKey+ "&pageNo=1&numOfRows=10";
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
                            list = new ArrayList<Lost_Item>();
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
                                item = new Lost_Item();
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

        }
    }
}