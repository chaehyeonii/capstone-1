package com.example.capstone2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PostListActivity extends AppCompatActivity {
    private static String TAG = "getJsonList";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_PostTitle = "PostTitleData";
    private static final String TAG_PostPlace = "PostPlaceData";
    private static final String TAG_PostDate ="PostDateData";
    private static final String TAG_PostMoreInfo ="PostMoreInfoData";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        Button homeButton=findViewById(R.id.homeBtn);
        homeButton.setOnClickListener(view -> {
            Intent intent=new Intent(PostListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        Button postingButton=findViewById(R.id.postingBtn);
        postingButton.setOnClickListener(view -> {
            Intent intent=new Intent(PostListActivity.this, PostingActivity.class);
            startActivity(intent);
        });

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://jamong.ivyro.net/getjson.php");
    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PostListActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String PostTitleData = item.getString(TAG_PostTitle );
                String PostPlaceData = item.getString(TAG_PostPlace );
                String PostDateData = item.getString(TAG_PostDate );
                String PostMoreInfoData = item.getString(TAG_PostMoreInfo );

                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_PostTitle, PostTitleData);
                hashMap.put(TAG_PostPlace, PostPlaceData);
                hashMap.put(TAG_PostDate, PostDateData);
                hashMap.put(TAG_PostMoreInfo, PostMoreInfoData);

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    PostListActivity.this, mArrayList, R.layout.post_list_item,
                    new String[]{TAG_PostTitle ,TAG_PostPlace , TAG_PostDate,  TAG_PostMoreInfo },
                    new int[]{R.id.textView_list_title, R.id.textView_list_place, R.id.textView_list_date, R.id.textView_list_more_info}
            );
            mlistView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }
}
