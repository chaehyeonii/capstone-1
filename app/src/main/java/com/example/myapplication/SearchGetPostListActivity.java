package com.example.myapplication;

import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchGetPostListActivity extends AppCompatActivity {
    private static final String TAG_GetPostTitle = "GetPostTitleData";
    private static final String TAG_GetPostCategory = "GetPostCategoryData";
    private static final String TAG_GetPostLocal ="GetPostLocalData";
    private static final String TAG_GetPostPlace = "GetPostPlaceData";
    private static final String TAG_GetPostDate ="GetPostDateData";
    private static final String TAG_GetPostColor ="GetPostColorData";
    private static final String TAG_GetPostMoreInfo ="GetPostMoreInfoData";
    private static final String TAG_GetPostImg ="GetPostImgData";

    private ImageView imgview;

    ArrayList mArrayList;
    ListView mlistView;

    /*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_post_list);

        Button homeButton=findViewById(R.id.homeBtn);
        homeButton.setOnClickListener(view -> {
            Intent intent=new Intent(SearchGetPostListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        Button postingButton=findViewById(R.id.postingBtn);
        postingButton.setOnClickListener(view -> {
            Intent intent=new Intent(SearchGetPostListActivity.this, SelectPostingActivity.class);
            startActivity(intent);
        });

        mlistView = (ListView) findViewById(R.id.get_listView_main_list);
        mArrayList = new ArrayList<>();

        //SearchLostPostActivity에서 넘긴 검색 조건 데이터 받기
        Intent intent = getIntent();
        mArrayList=(ArrayList) intent.getSerializableExtra("data");

        ListAdapter adapter = new SimpleAdapter(
                SearchGetPostListActivity.this, mArrayList, R.layout.get_post_list_item,
                new String[]{TAG_GetPostTitle ,TAG_GetPostCategory ,TAG_GetPostLocal, TAG_GetPostPlace , TAG_GetPostDate, TAG_GetPostColor,  TAG_GetPostMoreInfo, TAG_GetPostImg},
                new int[]{R.id.get_textView_list_title, R.id.get_textView_list_category, R.id.get_textView_list_local, R.id.get_textView_list_place, R.id.get_textView_list_date, R.id.get_textView_list_color, R.id.get_textView_list_more_info, R.id.get_imgView_list}
        );
        ((SimpleAdapter) adapter).setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view.getId() == R.id.get_imgView_list) {
                    ImageView imageView = (ImageView) view;
                    String drawable = data.toString();
                    Glide.with(SearchGetPostListActivity.this).load(drawable).override(200,200)
                            .error(R.drawable.defaultimg).into(imageView);
                    return true;
                }
                return false;
            }
        });
        mlistView.setAdapter(adapter);

        //목록 눌러서 해당 게시글 데이터 받아서 보내기
        mlistView.setOnItemClickListener((AdapterView.OnItemClickListener) (adapterView, view, index, l) -> {
            HashMap<String,String> data =(HashMap<String,String>) adapterView.getItemAtPosition(index);
            Log.e("itemdata",String.valueOf(data));
            String title=data.get(TAG_GetPostTitle);
            String category=data.get(TAG_GetPostCategory);
            String local = data.get(TAG_GetPostLocal);
            String place = data.get(TAG_GetPostPlace);
            String color = data.get(TAG_GetPostColor);
            String date = data.get(TAG_GetPostDate);
            String moreInfo=data.get(TAG_GetPostMoreInfo);
            String imgUri=data.get(TAG_GetPostImg);
            Intent postIntent = new Intent(SearchGetPostListActivity.this,GetPostActivity.class);
            postIntent.putExtra("title",title);
            postIntent.putExtra("category",category);
            postIntent.putExtra("local",local);
            postIntent.putExtra("place",place);
            postIntent.putExtra("color",color);
            postIntent.putExtra("date",date);
            postIntent.putExtra("moreInfo",moreInfo);
            postIntent.putExtra("imgUri",imgUri);
            startActivity(postIntent);
        });
            }

     */
    }