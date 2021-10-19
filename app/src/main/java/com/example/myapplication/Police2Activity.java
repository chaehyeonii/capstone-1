//추가 해야할 것: 무한 스크롤
//무한 스크롤: 스크롤 끝에 다다르면 다음 데이터 불러오기


package com.example.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class Police2Activity extends Activity{

    public String dataKey = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D";
    private String requestUrl;
    private String requestUrl2;
    ArrayList<Police2_Item> list = null;
    Police2_Item item = null;
    RecyclerView recyclerView;

    //-------무한 스크롤------------
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;

    // 1페이지에 10개씩 데이터 불러오기
    int page=1,limit=40;


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
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }





    public class MyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            requestUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccToClAreaPd?serviceKey=" +dataKey+ "&pageNo="+page+"&numOfRows="+limit;
            try {
                boolean b_atcId = false;
                boolean b_lstPlace =false;
                boolean b_lstPrdtNm = false;
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
                            if (parser.getName().equals("lstYmd")) b_lstYmd = true;
                            break;

                        case XmlPullParser.TEXT:
                            if(b_atcId){
                                item.setatcId(parser.getText());
                                b_atcId = false;

                                //목록에 이미지 불러오기 위한 코드
                                StringBuffer image = new StringBuffer();
                                String queryUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsDetailInfo?"//요청 URL
                                        + "ATC_ID=" + parser.getText() + "&ServiceKey=" + dataKey;

                                try {
                                    URL url2 = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
                                    InputStream is2 = url2.openStream(); //url위치로 입력스트림 연결

                                    XmlPullParserFactory factory2 = XmlPullParserFactory.newInstance();
                                    XmlPullParser xpp2 = factory2.newPullParser();
                                    xpp2.setInput(new InputStreamReader(is2, "UTF-8")); //inputstream 으로부터 xml 입력받기

                                    String tag2;

                                    xpp2.next();
                                    int eventType2 = xpp2.getEventType();

                                    while (eventType2 != XmlPullParser.END_DOCUMENT) {

                                        switch (eventType2) {
                                            case XmlPullParser.START_DOCUMENT:
                                                break;

                                            case XmlPullParser.START_TAG:
                                                tag2 = xpp2.getName();//태그 이름 얻어오기

                                                if (tag2.equals("item")) ;// 첫번째 검색결과
                                                else if (tag2.equals("lstFilePathImg")) {
                                                    xpp2.next();
                                                    image.append(xpp2.getText());
                                                }
                                                break;

                                            case XmlPullParser.TEXT:
                                                break;

                                            case XmlPullParser.END_TAG:
                                                tag2 = xpp2.getName(); //태그 이름 얻어오기

                                                if (tag2.equals("item")) ;// 첫번째 검색결과종료..줄바꿈
                                                break;
                                        }
                                        eventType2 = xpp2.next();
                                    }

                                } catch (Exception e) {
                                    // TODO Auto-generated catch blocke.printStackTrace();
                                }
                                item.setlstFilePathImg(image.toString());//StringBuffer 문자열 객체 반환

                            } else if(b_lstPlace) {
                                item.setlstPlace(parser.getText());
                                b_lstPlace = false;
                            } else if (b_lstPrdtNm) {
                                item.setlstPrdtNm(parser.getText());
                                b_lstPrdtNm = false;
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
        //------------------------------


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            Police2_Adapter adapter = new Police2_Adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
        }
    }
}