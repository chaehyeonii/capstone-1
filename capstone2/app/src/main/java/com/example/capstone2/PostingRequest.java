package com.example.capstone2;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostingRequest extends StringRequest{
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://jamong.ivyro.net/Posting.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;
    public PostingRequest(String PostTitleData, String PostPlaceData
            , String PostDateData, String PostMoreInfoData,
                          Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
//        public PostingRequest(String PostTitleData, String PostPlaceData
//                , String PostDateData, String PostMoreInfoData
//                , String PostColorData, String PostLostOrGet
//                , Response.Listener<String> listener) {
//            super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("PostTitleData", PostTitleData);
        map.put("PostPlaceData", PostPlaceData);
        map.put("PostDateData", PostDateData);
        map.put("PostMoreInfoData", PostMoreInfoData);
//        map.put("PostColorData", PostColorData);
//        map.put("PostLostOrGet", PostLostOrGet);
        // map.put("PostUserId", PostUserId);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
