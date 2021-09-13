package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //-------------------url 연결----------------

        Button button_taxi=findViewById(R.id.button_taxi); //택시 url 연결 버튼 가져오기
        Button button_bus=findViewById(R.id.button_bus); //버스 url 연결 버튼 가져오기
        Button button_subway=findViewById(R.id.button_subway);

        //-------------------url 연결----------------


        Button button_police=findViewById(R.id.button_police); //경찰청 버튼 가져오기
        button_police.setOnClickListener(new View.OnClickListener(){ //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_police = new Intent(getApplicationContext(),PoliceActivity.class);
                startActivity(intent_police);
            }
        });

        Button button_police2=findViewById(R.id.button_police2); //경찰청 버튼 가져오기
        button_police2.setOnClickListener(new View.OnClickListener(){ //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_police2 = new Intent(getApplicationContext(),Police2Activity.class);
                startActivity(intent_police2);
            }
        });

        Button button_listview=findViewById(R.id.button_listview); //경찰청 버튼 가져오기
        button_listview.setOnClickListener(new View.OnClickListener(){ //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_listview = new Intent(getApplicationContext(),ListviewActivity.class);
                startActivity(intent_listview);
            }
        });

        Button button_click=findViewById(R.id.button_click); //경찰청 버튼 가져오기
        button_click.setOnClickListener(new View.OnClickListener(){ //경찰청 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_click = new Intent(getApplicationContext(),LostActivity.class);
                startActivity(intent_click);
            }
        });

    }


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