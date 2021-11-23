package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LostActivity extends Activity {

    public String dataKey = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D";
    private String requestUrl;
    private String requestUrl2;
    ArrayList<Police2_Item> list = new ArrayList<Police2_Item>(); //배열 선언
    Police2_Item item = null;
    RecyclerView recyclerView;

    EditText search;

    //-------무한 스크롤------------
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;

    //로딩중을 띄워주는 progressDialog
    private ProgressDialog progressDialog;

    // 1페이지에 10개씩 데이터 불러오기
    int page=1,limit=70;

    //----------검색
    ArrayList<Police2_Item> filteredList=new ArrayList<>();
    ArrayList<Police2_Item> arraylist=new ArrayList<>(); //복사용 ??

   //어답터
    Police2_Adapter adapter;

    Button button_search;

    //검색
    String search_category_data = "";   //물품 분류명 prdtClNm
    String search_color_data = "";
    String search_local_data = "";  //분실 지역명 lstLctNm
    String search_place_data = "";  //분실 지역명(장소) lstPlace
    String search_date1_data =""; //분실물 등록 날짜 START_YMD
    String search_date2_data = "";  //분실물 등록 날짜 END_YMD


    /*
    String search_category_data = intent.getStringExtra("search_category_data");    //물품 분류명 prdtClNm
    String search_color_data = intent.getStringExtra("search_color_data");
    String search_local_data = intent.getStringExtra("search_local_data");  //분실 지역명 lstLctNm
    String search_place_data = intent.getStringExtra("search_place_data");  //분실 지역명(장소) lstPlace
    String search_date1_data = intent.getStringExtra("search_date1_data");  //분실물 등록 날짜 START_YMD
    String search_date2_data = intent.getStringExtra("search_date2_data");  //분실물 등록 날짜 END_YMD
        */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police2);

        adapter = new Police2_Adapter(getApplicationContext(), arraylist);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

        //검색
        //Intent intent = getIntent();
        search=(EditText)findViewById(R.id.search);
        button_search=findViewById(R.id.button_search);

        button_search.setOnClickListener(new View.OnClickListener() { //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v) {
                //arraylist.clear();
                //adapter.notifyDataSetChanged();

                Intent intent_search = new Intent(getApplicationContext(), DetailSearch.class);
                //startActivity(intent_search);
                startActivityForResult(intent_search,3000);
            }
        });



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
                }
            }
        });
    }


    // DetailSearch 에서 처리된 결과를 받는 메소드
    // 처리된 결과 코드 (resultCode) 가 RESULT_OK 이면 requestCode 를 판별해 결과 처리를 진행한다.
    // DetailSerach 에서 처리 결과가 담겨온 데이터를 TextView 에 보여준다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK){

            //------------인텐트 후, 화면 갱신-------------
            //arraylist.clear();
            //adapter.notifyDataSetChanged();

            //-------------------------------------------
            //page++;

            switch (requestCode){
                // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    //mainResultTv.setText(data.getStringExtra("result"));
                    search_category_data = intent.getStringExtra("search_category_data");    //물품 분류명 prdtClNm
                    search_color_data = intent.getStringExtra("search_color_data");
                    search_local_data = intent.getStringExtra("search_local_data");  //분실 지역명 lstLctNm
                    search_place_data = intent.getStringExtra("search_place_data");  //분실 지역명(장소) lstPlace
                    search_date1_data = intent.getStringExtra("search_date1_data");  //분실물 등록 날짜 START_YMD
                    search_date2_data = intent.getStringExtra("search_date2_data");  //분실물 등록 날짜 END_YMD
                    Log.i("test", "category:" + search_category_data);
                    Log.i("test", "color:" + search_color_data);
                    Log.i("test", "local:" + search_local_data);
                    Log.i("test", "place:" + search_place_data);
                    Log.i("test", "date1:" + search_date1_data);
                    Log.i("test", "date2:" + search_date2_data);


                    SimpleDateFormat date=new SimpleDateFormat("yyyy-mm-dd");
                    Date date1=null;
                    Date date2=null;
                    Date date_compare=null;

                    //----------날짜(String->date) 변환--------------
                    try{
                        date1=date.parse(search_date1_data);
                        date2=date.parse(search_date2_data);
                    }catch(ParseException e){
                        e.printStackTrace();
                    }
                    Log.i("test", "date1:" + date1);
                    Log.i("test", "date2:" + date2);
                    //-----------------------------------------------


                    if (search_local_data.equals("")){
                        arraylist.clear();
                        arraylist.addAll(list);
                    }else{
                        arraylist.clear();
                        for (int i = 0; i < list.size(); i++) {
                            // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                            try{
                                date_compare=date.parse(list.get(i).getlstYmd());
                            }catch(ParseException e){
                                e.printStackTrace();
                            }
                            int result_date1=date_compare.compareTo(date1);
                            int result_date2=date_compare.compareTo(date2);

                            Log.i("test", "date:" + result_date1);
                            Log.i("test", "date:" + result_date2);

                            /*if (list.get(i).getlstLctNm().toLowerCase().contains(search_local_data)) { //지역 선택 시
                                // 검색된 데이터를 리스트에 추가한다.
                                arraylist.add(list.get(i));
                                Log.i("test", "OK");
                            } */
                            if (list.get(i).getlstLctNm().toLowerCase().contains(search_local_data)
                                    && result_date1>=0&&result_date2<=0) { //지역 / 날짜 선택 시
                                // 검색된 데이터를 리스트에 추가한다.
                                arraylist.add(list.get(i));
                                Log.i("test", "OK");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();//어댑터에 연결된 항목들을 갱신함

                    break;
            }
        }
    }




    void getXmlData(int page, int limit){
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        //arraylist.addAll(list);
    }






    //MyAsyncTask의 첫번째 인자는 doInBackground의 파라미터 타입이 될것이다.
    //두번째 인자는 doInBackground 작업 시 진행 단위의 타입
    //세번째 인자는 doInBackground 리턴값.
    //AsyncTask는 비교적 오래 걸리지 않은 작업에 유용하고, Task 캔슬이 용이하며 로직과 UI 조작이 동시에 일어나야 할 때 유용하게 사용됨
    //비동기 타입.  UI 스레드라고도 부릅. 비동기적으로 태스크를 실행하면  먼저 실행된 태스크가 종료되기 전에 다른 태스크를 실행할 수 있음
    //예를 들어 메인 스레드가 실행되는 중에, 다른 스레드를 백그라운드로 실행시켜 두고 계속 메인스레드는 자신의 작업을 하다가, 이 후 백그라운드에서 돌던 스레드가 종료시 결과값을 받을 수 있음

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {//전달된 URL을 사용하는 작업을 한다
            //해당 메서드가 background 스레드로 일처리를 해주는 곳. 네트워크를 이용할때 사용됨
            //스레드 이므로 UI스레드가 어떤 일을 하고 있는지 상관없이 별개의 일을 진행한다는 점이다. 따라서 AysncTask는 비동기적으로 작동한다.


            //1번 분실물 등록날짜(시작일, 종료일), 상위 하위 물품 코드(대분류, 중분류), 분실지역 코드드
            //2번 분실지역명, 분실물명
            String str= search.getText().toString();//EditText에 작성된 Text얻어오기
            String name = null;//약 이름으로 검색하기 위해 null로 초기화해줌
            try {//인코딩을 위한 try catch문
                name = URLEncoder.encode(str, "UTF-8");//Edit창에 적은 String값을 인코딩 해줌
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            requestUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccToClAreaPd?serviceKey=" +dataKey
                    + "&pageNo="+page+"&numOfRows="+limit;
            try {
                boolean b_atcId = false;
                boolean b_lstPlace =false;
                boolean b_lstPrdtNm = false;
                boolean b_lstYmd = false;

                //실질적으로 파싱해서 inputstream해주는 코드
                URL url = new URL(requestUrl); ////공공데이터 파싱 주소를 url에 넣음음
                InputStream is = url.openStream(); ////Stream파일로 읽어들이기 위해 가져온 url을 연결함.
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); //Tag 및 데이터를 가지고 올 때 필요함.
                XmlPullParser parser = factory.newPullParser(); //string을 xml로 바꾸어 넣을 곳
                parser.setInput(new InputStreamReader(is, "UTF-8")); //string을 xml로.

                String tag;
                //파싱해온 주소의 eventType을 가져옴. 이것을 이용하여 파싱의 시작과 끝을 구분해좀
                int eventType = parser.getEventType();

                //eventType이 END_DOCUMENT이 아닐때까지 while문이 돌아감
                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            //list = new ArrayList<Police2_Item>(); //배열 선언
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

                        case XmlPullParser.TEXT:  //eventType이 TEXT일 경우. parser가 내용에 접근했을때
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
                                                }else if (tag2.equals("lstLctNm")) {
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
                                item.setlstLctNm(lstLctNm.toString());//StringBuffer 문자열 객체 반환

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
        protected void onPostExecute(String s) {//adapter를 연결해주는 부분. 이 코드를 이용해 AsyncTask를 실행한다.
            //결과 파라미터를 리턴하면서 그 리턴값을 통해 스레드 작업이 끝났을 때의 동작을 구현합니다.

            super.onPostExecute(s);

            //검색 결과가 다 뜨면 progressDialog를 없앰
            progressDialog.dismiss();

            //어답터 연결
            //Police2_Adapter adapter = new Police2_Adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //---------------------검색------------------
            String test = "";

            Log.i("test", "data:" + search_local_data);

            //else if(list.get(i).getlstYmd()>)
            SimpleDateFormat date=new SimpleDateFormat("yyyy-mm-dd");
            Date date1=null;
            Date date2=null;
            Date date_compare=null;

            //----------날짜(String->date) 변환--------------
            try{
                date1=date.parse(search_date1_data);
                date2=date.parse(search_date2_data);
            }catch(ParseException e){
                e.printStackTrace();
            }
            Log.i("test", "date1:" + date1);
            Log.i("test", "date2:" + date2);
            //-----------------------------------------------

            /*
            if(search_category_data !="" || search_color_data !=""||
                    search_local_data !="" ||  search_place_data !="" ||
                    search_date1_data !="" ||   search_date2_data !="" ) {
                //------------인텐트 후, 화면 갱신-------------
                arraylist.clear();
                adapter.notifyDataSetChanged();

                //Intent intent_reset = getIntent();
                //finish();
                //startActivity(intent_reset);

                //-------------------------------------------
            }

             */




            //---------test1-----------
            //arraylist.addAll(list);

            if (search_local_data.equals("")){
                arraylist.clear();
                arraylist.addAll(list);
            }else{
                arraylist.clear();
                for (int i = 0; i < list.size(); i++) {
                    // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                    try{
                        date_compare=date.parse(list.get(i).getlstYmd());
                    }catch(ParseException e){
                        e.printStackTrace();
                    }

                    //게시글의 날짜 - 상세검색 지정 날짜
                    //result_date1 >=0 , result_date2<=0
                    int result_date1=date_compare.compareTo(date1);
                    int result_date2=date_compare.compareTo(date2);

                    Log.i("test", "date:" + result_date1);
                    Log.i("test", "date:" + result_date2);

                    /*
                    if (list.get(i).getlstLctNm().toLowerCase().contains(search_local_data)) { //지역 선택 시
                        // 검색된 데이터를 리스트에 추가한다.
                        arraylist.add(list.get(i));
                        Log.i("test", "OK");
                    }

                     */
                    if (list.get(i).getlstLctNm().toLowerCase().contains(search_local_data)
                            && result_date1>=0&&result_date2<=0) { //지역 / 날짜 선택 시
                        // 검색된 데이터를 리스트에 추가한다.
                        arraylist.add(list.get(i));
                        Log.i("test", "OK");
                    }

                }

            }
            adapter.notifyDataSetChanged();//어댑터에 연결된 항목들을 갱신함.

        }
    }

    public void searchFilter(String searchText) {
        filteredList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getlstPrdtNm().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(list.get(i));
            }
        }

        adapter.filterList(list);
    }

    // 검색을 수행하는 메소드
    public void search_method(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).getlstPrdtNm().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

}
