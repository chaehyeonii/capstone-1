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
import android.widget.EditText;
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

    //String[] colorItems = {"선택", "검정색 ","흰색","빨강색","연두색","파랑색 ","노랑색","핑크색","보라색","회색","갈색"};
    String[] categoryItems = {"전체", "가방", "귀금속", "도서용품", "서류", "산업용품", "쇼핑백", "스포츠용품", "악기",
                            "유가증권","의류","자동차","전자기기","지갑","증명서","컴퓨터","카드","현금","휴대폰","기타물품"};

    String[] placeItems={"전체","우체국(통)","노상","기차","지하철","백화점/매장","택시","음식점(업소포함)","공공기관","버스"
                            ,"주택","공항","상점","영화관","놀이공원","유원지","학교","회사","불상","직접입력"};
    EditText searchGetPlaceData, searchGetThingsData; //지역, 물품 검색
    String search_place_data, search_things_data;
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

        //searchGetPlaceSpinner 처리 (색상선택처리)
        Spinner placeSpin = (Spinner)findViewById(R.id.searchGetPlaceSpinner);
        ArrayAdapter<String> placeAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,placeItems
        );
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        placeSpin.setAdapter(placeAdapter);
        placeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//선택되면
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                intent_lost.putExtra("search_place_data",placeItems[position]);
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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

                searchGetLocalData = findViewById(R.id.searchGetLocalData);
                address = searchGetLocalData.getText().toString().split(" ");
                region1=address[0];
                intent_lost.putExtra("search_local_data",region1);

                /*
                searchGetPlaceData = findViewById(R.id.searchGetPlaceData);
                search_place_data = searchGetPlaceData.getText().toString();
                intent_lost.putExtra("search_place_data",search_place_data);
                
                 */

                searchGetThingsData = findViewById(R.id.searchGetThingsData);
                search_things_data = searchGetThingsData.getText().toString();
                intent_lost.putExtra("search_things_data",search_things_data);

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


}