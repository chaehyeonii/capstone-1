package com.example.capstone2;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class PostingActivity extends AppCompatActivity {
    private EditText postingTitle, placeData, dateData, moreInfo;
    //    private RadioGroup whatRg, colorRg;
    //private TextView result;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posting);

        Button homeButton=findViewById(R.id.homeBtn);
        homeButton.setOnClickListener(view -> finish());

        postingTitle = (EditText) findViewById(R.id.postingTitle);
        placeData = (EditText) findViewById(R.id.placeData);
        dateData = (EditText) findViewById(R.id.dateData);
        moreInfo = (EditText) findViewById(R.id.moreInfo);
//        colorRg=findViewById(R.id.colorGroup);
//        whatRg=findViewById(R.id.whatGroup);
//        RadioButton whatCheck = (RadioButton) findViewById(whatRg.getCheckedRadioButtonId());
//        RadioButton colorCheck = (RadioButton) findViewById(colorRg.getCheckedRadioButtonId());

        Button inputButton=findViewById(R.id.inputBtn);

        inputButton.setOnClickListener(view -> {

//            int whatCheck=whatRg.getCheckedRadioButtonId();
//            int colorCheck=colorRg.getCheckedRadioButtonId();
            String PostTitleData=postingTitle.getText().toString();
            String PostPlaceData=placeData.getText().toString();
            String PostDateData=dateData.getText().toString();
            String PostMoreInfoData=moreInfo.getText().toString();
//            String PostColorData= colorCheck.getText().toString();
//            String PostLostOrGet=  whatCheck.getText().toString();
//            String PostColorData= Integer.toString(colorCheck);
//            String PostLostOrGet= Integer.toString(whatCheck);

//            Intent intent=new Intent(PostingActivity.this, LostPostActivity.class);
//            startActivity(intent);
            //한 칸이라도 입력 안했을 경우
            if (PostTitleData.equals("") || PostPlaceData.equals("") || PostDateData.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostingActivity.this);
                dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                dialog.show();
                return;
            }

            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //Log.e("test","test: 공백");
                    try {
                        JSONObject JsonObject = new JSONObject(response);
                        boolean success=JsonObject.getBoolean("success");
                        if(success){
                            Toast.makeText(getApplicationContext(),"게시글 등록 성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostingActivity.this,PostListActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"게시글 등록 실패",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 Volley를 이용해서 요청
//            PostingRequest postingRequest = new PostingRequest(  PostTitleData,  PostPlaceData,  PostDateData,  PostMoreInfoData,  PostColorData,  PostLostOrGet, responseListener);
            PostingRequest postingRequest = new PostingRequest(  PostTitleData,  PostPlaceData
                    ,  PostDateData,  PostMoreInfoData, responseListener);
            RequestQueue queue = Volley.newRequestQueue( PostingActivity.this );
            queue.add( postingRequest );
        });
    }
}