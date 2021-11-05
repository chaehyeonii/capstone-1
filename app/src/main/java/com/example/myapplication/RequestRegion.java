package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestRegion extends StringRequest{
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://skysea750.dothome.co.kr/getpostdata.php";
    private Map<String, String> map;
    //public RequestRegion(String address[0],String address[1],
    //                               Response.Listener<String> listener) {
    public RequestRegion(String region1, String region2,
                         Response.Listener<String> listener) {

        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("region1", region1);
        map.put("region2", region2);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}