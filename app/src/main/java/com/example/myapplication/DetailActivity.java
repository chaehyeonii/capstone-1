//문제점: 임의로 주소를 설정해서 불러오면 된다!
//api 주소를 불러오면 안 된다!!! ! ! ! ! !

package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;


public class DetailActivity extends Activity {

    //EditText edit;
    TextView textView1;
    TextView Text_lstSbjt, Text_lstPrdtNm,Text_prdtClNm,Text_lstLctNm,Text_lstPlace,
            Text_lstYmd, Text_uniq, Text_orgNm,Text_tel;
    TextView textView3;


    String key = "%2Byy%2BAg9TKR9ECCv0yM0FkpCbZUyhIAsutQKN5U%2BzwQLeB6cWr1mrzLwH68caK39fPNG1YDiZGj3uv3AjFg6mVw%3D%3D"; //발급 받은 인증키
    String data;

    //Intent intent = getIntent();
    String id;
    String imageget;
    String imageurl;
    String detail_data[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Text_lstSbjt = (TextView) findViewById(R.id.Text_lstSbjt);
        Text_lstPrdtNm = (TextView) findViewById(R.id.Text_lstPrdtNm);
        Text_prdtClNm = (TextView) findViewById(R.id.Text_prdtClNm);
        Text_lstLctNm = (TextView) findViewById(R.id.Text_lstLctNm);
        Text_lstPlace = (TextView) findViewById(R.id.Text_lstPlace);
        Text_lstYmd = (TextView) findViewById(R.id.Text_lstYmd);
        Text_uniq = (TextView) findViewById(R.id.Text_uniq);
        Text_orgNm = (TextView) findViewById(R.id.Text_orgNm);
        Text_tel = (TextView) findViewById(R.id.Text_tel);

        Intent intent = getIntent(); //1

        id=intent.getStringExtra("TEXT");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);


        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                data = getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기\


                    detail_data = data.split("\n");
                    Log.d("test", Arrays.toString(detail_data) );


                imageget = getXmlURL();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Text_lstSbjt.setText(detail_data[3]); //TextView에 문자열  data 출력
                        Text_lstPrdtNm .setText(detail_data[2]); //물품명
                        Text_prdtClNm.setText(detail_data[6]); //물
                        Text_lstLctNm.setText(detail_data[0]); //분실지역명
                        Text_lstPlace.setText(detail_data[1]); //분실장소
                        Text_lstYmd.setText(detail_data[4]);
                        Text_uniq.setText(detail_data[8]);
                        Text_orgNm.setText(detail_data[5]);
                        Text_tel.setText(detail_data[7]);
                        Glide.with(getApplicationContext()).load(imageget).error(R.drawable.flower).into(imageView);
                    }
                });

            }
        }).start();
    }

    String getXmlData() {

        StringBuffer buffer = new StringBuffer();
        String queryUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsDetailInfo?"//요청 URL
                + "ATC_ID=" + id + "&ServiceKey=" + key;

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("lstSbjt")) {
                            //buffer.append("게시제목 :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("lstLctNm")) {
                            buffer.append("지역: ");
                            xpp.next();
                            buffer.append(xpp.getText());//cpNm
                            buffer.append("\n");
                        } else if (tag.equals("lstPlace")) {
                            buffer.append("장소: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                        } else if (tag.equals("lstPlaceSeNm")) {
                            buffer.append(" / ");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        } else if (tag.equals("lstPrdtNm")) {
                            buffer.append("물품: ");
                            xpp.next();
                            buffer.append(xpp.getText());//csId
                            buffer.append("\n");
                        } else if (tag.equals("lstYmd")) {
                            buffer.append("일자: ");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        } else if (tag.equals("orgNm")) {
                            buffer.append("기관: ");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        } else if (tag.equals("prdtClNm")) {
                            buffer.append("분류: ");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        } else if (tag.equals("tel")) {
                            buffer.append("기관 전화번호: ");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        } else if (tag.equals("uniq")) {
                            buffer.append("특이사항: ");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

    String getXmlURL() { //이미지 불러오기 위한 주소

        StringBuffer image = new StringBuffer();
        String image1;
        String queryUrl = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsDetailInfo?"//요청 URL
                + "ATC_ID=" + id + "&ServiceKey=" + key;

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("lstFilePathImg")) {
                            xpp.next();
                            image.append(xpp.getText());
                        }
                        break;
//                    if (tag.equals("item")) {// 첫번째 검색결과
//                        if (tag.equals("lstFilePathImg")) {
//                            xpp.next();
//                            image.append(xpp.getText());
//                        }
//                        break;
//                    }

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과종료..줄바꿈
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return image.toString();//StringBuffer 문자열 객체 반환
    }
}