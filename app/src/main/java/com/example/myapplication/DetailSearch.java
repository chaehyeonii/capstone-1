package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DetailSearch extends Activity {
    private TextView date, color, category, local;

    //----------주소 찾아오기----------------------
    // 초기변수설정
    TextView searchGetLocalData;
    // 주소 요청코드 상수 requestCode
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    String[] address;
    String region1, region2;
    String local_data;
    //-------------------------------------------------

    String[] colorItems = {"선택", "검정색 ","흰색","빨강색","연두색","파랑색 ","노랑색","핑크색","보라색","회색","갈색"};
    String[] categoryItems = {"선택", "가방", "의류", "전자제품", "악세서리", "모자", "신발", "시계", "휴대폰"};
    /*
    String[] localItems = {"선택", "서울특별시", "강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시"
            , "대전광역시", "부산광역시", "울산광역시", "인천광역시", "전라남도", "전라북도", "충청남도", "충청북도"
            , "제주특별자치도", "세종특별자치시", "기타"};

     */

    //int code;

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent_lost=new Intent(this,LostActivity.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //날짜 선택 구현
        DatePickerDialog.OnDateSetListener dateSetListener1=
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        //DatePicker 선택한 날짜를 TextView 에 설정
                        TextView dateView=findViewById(R.id.searchGetDateData1);
                        dateView.setText(String.format("%d/%d/%d",yy,mm+1,dd));
                        String startdate1 = String.format("%d-%d-%d",yy,mm+1,dd);
                        intent_lost.putExtra("search_date1_data",startdate1);
                    }
                };

        DatePickerDialog.OnDateSetListener dateSetListener2=
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        //DatePicker 선택한 날짜를 TextView 에 설정
                        TextView dateView=findViewById(R.id.searchGetDateData2);
                        dateView.setText(String.format("%d/%d/%d",yy,mm+1,dd));
                        String startdate2 = String.format("%d-%d-%d",yy,mm+1,dd);
                        intent_lost.putExtra("search_date2_data",startdate2);
                    }
                };

        //colorSpinner 처리 (색상선택처리)
        Spinner colorSpin = (Spinner)findViewById(R.id.searchGetColorSpinner);

        ArrayAdapter<String> colorAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,colorItems
        );
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        colorSpin.setAdapter(colorAdapter);
        colorSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//선택되면
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                intent_lost.putExtra("search_color_data",colorItems[position]);
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //categorySpinner 처리 (색상선택처리)
        Spinner categorySpin = (Spinner)findViewById(R.id.searchGetCategorySpinner);
        ArrayAdapter<String> categoryAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,categoryItems
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpin.setAdapter(categoryAdapter);
        categorySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//선택되면
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                intent_lost.putExtra("search_category_data",categoryItems[position]);
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        /*
        //localSpinner 처리 (물건분류선택처리)
        Spinner localSpin = (Spinner)findViewById(R.id.searchGetLocalSpinner);
        local=findViewById(R.id.searchGetLocalData);
        ArrayAdapter<String> localAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,localItems
        );
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        localSpin.setAdapter(localAdapter);
        localSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//선택되면
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                local.setText(localItems[position]);
                intent.putExtra("search_local_data",localItems[position]);
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {
                local.setText("물건 분류 선택");
                //intent.putExtra("search_local_data","");
            }
        });


         */

        //지역 선택 구현
        //----------주소 검색-----------------
        // UI 요소 연결
        searchGetLocalData = findViewById(R.id.searchGetLocalData);

        // 터치 안되게 막기
        searchGetLocalData.setFocusable(false);
        // 주소입력창 클릭
        searchGetLocalData.setOnClickListener(new View.OnClickListener() {
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

        /*
        address = searchGetLocalData.toString().split(" ");
        Log.i("test", "data:" + searchGetLocalData.toString());
        region1 = address[0];
        region2 = address[1];

        intent_lost.putExtra("search_local_data",region1);

         */


        //날짜 선택 구현
        Button dateBtn1=findViewById(R.id.searchSelectDateBtn1);
        dateBtn1.setOnClickListener(view -> {
            Calendar calender = Calendar.getInstance();
            new DatePickerDialog(this, dateSetListener1, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE)).show();
            //String startdate1 = String.format("%d%d%d",calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DATE));
            //intent.putExtra("search_date1_data",startdate1);
        });

        Button dateBtn2=findViewById(R.id.searchSelectDateBtn2);
        dateBtn2.setOnClickListener(view -> {
            Calendar calender = Calendar.getInstance();
            new DatePickerDialog(this, dateSetListener2, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE)).show();
            //String startdate2 = String.format("%d%d%d",calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DATE));
            //intent.putExtra("search_date2_data",startdate2);
        });

        mArrayList = new ArrayList<>();

        //Intent intent=new Intent(this,LostActivity.class);
        //검색 버튼 클릭 이벤트
        Button searchLostBtn=findViewById(R.id.searchGetFunctionBtn);
        //searchLostBtn.setOnClickListener(view -> searchGetData());
        searchLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent_searchLostBtn = new Intent(getApplicationContext(), LostActivity.class);
                //startActivity(intent_searchLostBtn);
                //searchGetData();
                setResult(RESULT_OK,intent_lost);
                finish();
            }
        });




    }

    //------주소 검색 ------------
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("test", "onActivityResult");


        switch (requestCode) {


            //---------주소 검색 부분-----------
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    //불러온 주소 값
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Log.i("test", "data:" + data);
                        searchGetLocalData.setText(data);

                        // 주소를 intent값으로 받아와서 일치하는 지역으로 url 설정정
                        //address = data.substring(0, data.indexOf(" "));
                        address = data.split(" ");
                        region1 = address[0];
                        region2 = address[1];


                        //Intent intent_lost=new Intent(this,LostActivity.class);
                        //intent_lost.putExtra("search_local_data", region1);
                    }
                    break;
                }
        }
    }


     /*
    public void searchGetData(){
        //검색 조건 데이터 받기
        TextView search_category, search_color, search_local, search_date1, search_date2;
        EditText search_place;
        search_category = findViewById(R.id.searchGetCategoryData);
        search_color = findViewById(R.id.searchGetColorData);
        search_local = findViewById(R.id.searchGetLocalData);
        search_place = findViewById(R.id.searchGetPlaceData);
        search_date1 = findViewById(R.id.searchGetDateData1);
        search_date2 = findViewById(R.id.searchGetDateData2);

        String search_category_data = search_category.getText().toString();
        String search_color_data = search_color.getText().toString();
        String search_local_data = search_local.getText().toString();
        String search_place_data = search_place.getText().toString();
        String search_date1_data = search_date1.getText().toString();
        String search_date2_data = search_date2.getText().toString();

        Intent intent=new Intent(this,LostActivity.class);
        intent.putExtra("search_category_data",search_category_data);
        intent.putExtra("search_color_data",search_color_data);
        intent.putExtra("search_local_data",search_local_data);
        intent.putExtra("search_place_data",search_place_data);
        intent.putExtra("search_date1_data",search_date1_data);
        intent.putExtra("search_date2_data",search_date2_data);
        //startActivity(intent);
        //setResult(RESULT_OK,intent);
        //finish();

      */

        /*

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JsonObject = new JSONObject(response);
                    boolean success=JsonObject.getBoolean("success");
                    JSONArray jsonArray = JsonObject.getJSONArray("getpostdata");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);

                        String GetPostTitleData = item.getString(TAG_GetPostTitle );
                        String GetPostCategoryData = item.getString(TAG_GetPostCategory );
                        String GetPostLocalData = item.getString(TAG_GetPostLocal );
                        String GetPostPlaceData = item.getString(TAG_GetPostPlace );
                        String GetPostDateData = item.getString(TAG_GetPostDate );
                        String GetPostColorData = item.getString(TAG_GetPostColor );
                        String GetPostMoreInfoData = item.getString(TAG_GetPostMoreInfo );
                        String GetPostImgData = item.getString(TAG_GetPostImg );

                        HashMap<String,String> hashMap = new HashMap<>();

                        hashMap.put(TAG_GetPostTitle, GetPostTitleData);
                        hashMap.put(TAG_GetPostCategory, GetPostCategoryData);
                        hashMap.put(TAG_GetPostLocal, GetPostLocalData);
                        hashMap.put(TAG_GetPostPlace, GetPostPlaceData);
                        hashMap.put(TAG_GetPostDate, GetPostDateData);
                        hashMap.put(TAG_GetPostColor, GetPostColorData);
                        hashMap.put(TAG_GetPostMoreInfo, GetPostMoreInfoData);
                        hashMap.put(TAG_GetPostImg, GetPostImgData);
                        mArrayList.add(hashMap);
                        Log.e("test",String.valueOf(mArrayList));
                    }
                    if(success){
                        Toast.makeText(getApplicationContext(),"검색 조회 성공",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SearchGetPostActivity.this,SearchGetPostListActivity.class);
                        intent.putExtra("data",mArrayList);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"검색 조회 실패",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Log.e("test","test: catch");
                    e.printStackTrace();
                }
            }
        };
        //서버로 Volley 이용해서 요청
        SearchGetPostRequest searchGetPostRequest = new SearchGetPostRequest( search_category_data, search_color_data, search_local_data,  search_place_data
                ,  search_date1_data,  search_date2_data, responseListener);
        RequestQueue queue = Volley.newRequestQueue( SearchGetPostActivity.this );
        queue.add( searchGetPostRequest );

    }
    */
}