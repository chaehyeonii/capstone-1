package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DetailSearch extends Activity {
    private TextView date, color, category, local;

    String[] colorItems = {"선택", "검정색 ","흰색","빨강색","연두색","파랑색 ","노랑색","핑크색","보라색","회색","갈색"};
    String[] categoryItems = {"선택", "가방", "의류", "전자제품", "악세서리", "모자", "신발", "시계", "휴대폰"};
    String[] localItems = {"선택", "서울특별시", "강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시"
            , "대전광역시", "부산광역시", "울산광역시", "인천광역시", "전라남도", "전라북도", "충청남도", "충청북도"
            , "제주특별자치도", "세종특별자치시", "기타"};

    private static final String TAG_GetPostTitle = "GetPostTitleData";
    private static final String TAG_GetPostCategory = "GetPostCategoryData";
    private static final String TAG_GetPostLocal ="GetPostLocalData";
    private static final String TAG_GetPostPlace = "GetPostPlaceData";
    private static final String TAG_GetPostDate ="GetPostDateData";
    private static final String TAG_GetPostColor ="GetPostColorData";
    private static final String TAG_GetPostMoreInfo ="GetPostMoreInfoData";
    private static final String TAG_GetPostImg ="GetPostImgData";

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;

    //날짜 선택 구현
    DatePickerDialog.OnDateSetListener dateSetListener1=
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    //DatePicker 선택한 날짜를 TextView 에 설정
                    TextView dateView=findViewById(R.id.searchGetDateData1);
                    dateView.setText(String.format("%d/%d/%d",yy,mm+1,dd));
                }
            };

    DatePickerDialog.OnDateSetListener dateSetListener2=
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    //DatePicker 선택한 날짜를 TextView 에 설정
                    TextView dateView=findViewById(R.id.searchGetDateData2);
                    dateView.setText(String.format("%d/%d/%d",yy,mm+1,dd));
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search);


        //colorSpinner 처리 (색상선택처리)
        Spinner colorSpin = (Spinner)findViewById(R.id.searchGetColorSpinner);
        color=findViewById(R.id.searchGetColorData);
        ArrayAdapter<String> colorAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,colorItems
        );
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        colorSpin.setAdapter(colorAdapter);
        colorSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//선택되면
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                color.setText(colorItems[position]);
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {
                color.setText("색상선택");
            }
        });

        //categorySpinner 처리 (색상선택처리)
        Spinner categorySpin = (Spinner)findViewById(R.id.searchGetCategorySpinner);
        category=findViewById(R.id.searchGetCategoryData);
        ArrayAdapter<String> categoryAdapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,categoryItems
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpin.setAdapter(categoryAdapter);
        categorySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//선택되면
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                category.setText(categoryItems[position]);
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {
                category.setText("물건 분류 선택");
            }
        });

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
            }
            @Override//아무것도 선택 안되면
            public void onNothingSelected(AdapterView<?> parent) {
                local.setText("물건 분류 선택");
            }
        });

        //날짜 선택 구현
        Button dateBtn1=findViewById(R.id.searchSelectDateBtn1);
        dateBtn1.setOnClickListener(view -> {
            Calendar calender = Calendar.getInstance();
            new DatePickerDialog(this, dateSetListener1, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE)).show();});

        Button dateBtn2=findViewById(R.id.searchSelectDateBtn2);
        dateBtn2.setOnClickListener(view -> {
            Calendar calender = Calendar.getInstance();
            new DatePickerDialog(this, dateSetListener2, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE)).show();});

        mArrayList = new ArrayList<>();

        //검색 버튼 클릭 이벤트
        Button searchLostBtn=findViewById(R.id.searchGetFunctionBtn);
        searchLostBtn.setOnClickListener(view -> searchGetData());
    }

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
        startActivity(intent);
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
         */
    }
}