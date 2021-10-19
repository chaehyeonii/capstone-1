//추가 해야할 것: 무한 스크롤
//무한 스크롤: 스크롤 끝에 다다르면 다음 데이터 불러오기

//xml에서 검색창 스크롤과 상관없이 계속 뜨도록 냅둬보기

//검색 원리: 리스트 목록을 처음부터 리스트 사이즈 만큼 돌면서 검색 부분과 동일한 글 들고옴


package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

public class LostActivity extends Activity{

    public String dataKey = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D";
    private String requestUrl;
    ArrayList<Police2_Item> list = null;
    Police2_Item item = null;
    RecyclerView recyclerView;

    //-----검색 기능 구현 코드--------
    ArrayList<Police2_Item> filteredList;
    EditText search;
    //---------------------------

    //MyAsyncTask myAsyncTask = new MyAsyncTask();

    //-------무한 스크롤------------
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;

    // 1페이지에 10개씩 데이터 불러오기
    int page=1,limit=40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police2);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask(page, limit);
        myAsyncTask.execute(page, limit);



        //getXmlData(page, limit);

        //-----무한 스크롤--------
        nestedScrollView=findViewById(R.id.scroll_view);
        progressBar=findViewById(R.id.progress_bar);

        //검색----
        search=findViewById(R.id.search);
        filteredList=new ArrayList<>();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = search.getText().toString();
                searchFilter(searchText);
            }
        });


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY){
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);

                    myAsyncTask.execute(page, limit);

                }
            }
        });


        //---------상세 검색 이동 -------------
        Button button_search = findViewById(R.id.button_search); //경찰청 버튼 가져오기
        button_search.setOnClickListener(new View.OnClickListener() { //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                Intent intent_detail_search = new Intent(getApplicationContext(), DetailSearch.class);
                startActivity(intent_detail_search);
            }
        });


    }


    void getXmlData(int page, int limit){
        //MyAsyncTask myAsyncTask = new MyAsyncTask();
        //myAsyncTask.execute();
    }

    //---------검색----------------
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void searchFilter(String searchText) {
        filteredList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getlstLctNm().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(list.get(i));
            }
        }

        Police2_Adapter adapter = new Police2_Adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);
        adapter.filterList(filteredList);
    }

    //-------------------------



    public class MyAsyncTask extends AsyncTask<String, Void, String>{
        private int page;
        private int limit;

        public MyAsyncTask(int page, int limit){
            this.page=page;
            this.limit=limit;
        }

        void execute(int page, int limit){
            this.page=page;
            this.limit=limit;
        }

        @Override
        protected String doInBackground(String... strings) {
            requestUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccToClAreaPd?serviceKey=" +dataKey
                    + "&pageNo="+page+"&numOfRows="+limit;
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
                                StringBuffer lstLctNm = new StringBuffer();
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
                                                else if (tag2.equals("lstLctNm")) {
                                                    xpp2.next();
                                                    lstLctNm.append(xpp2.getText());
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
                                item.setlstLctNm(lstLctNm.toString());

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