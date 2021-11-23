package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    //LostActivity.java에 Adapter 변수를 객체화할 것이므로 constructor를 활용해 list 값 가져옴
    private ArrayList<Lost_Item> mList;
    private LayoutInflater mInflate;

    public RecyclerViewAdapter(Context context,ArrayList<Lost_Item> items){
        this.mList=items;
        this.mContext=context;
        this.mInflate=LayoutInflater.from(context);
    }

    //view 재사용을 위한 inner class. 변수와 xml의 id를 findViewId() 함수를 통해 연결해주면 됨
    //recyclerView의 각 item을 재사용하는 myViewHolder에서 각 아이템에 대한 클릭 리스너를 달았음
    //myViewHolder가 recyclerView의 각 view 항목을 만듦.
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView atcId;
        public TextView lstPlace;
        public TextView lstPrdtNm;
        public TextView lstSbjt;
        public TextView lstYmd;
        // TextView item_Element;

        public MyViewHolder(@NonNull View itemView){

            super(itemView);

            atcId = itemView.findViewById(R.id.tv_prdtClNm);
            lstPlace = itemView.findViewById(R.id.tv_lstPlace);
            lstPrdtNm = itemView.findViewById(R.id.tv_lstPrdtNm);
            lstSbjt = itemView.findViewById(R.id.tv_lstSbjt);
            lstYmd = itemView.findViewById(R.id.tv_lstYmd);

            //수정 1!!!! tv_atcId이부분
            //xml에 있는 textView와 class내 변수 binding
           //item_Element = itemView.findViewById(R.id.tv_atcId);

            //아이템 클릭 시, 이벤트 처리를 위해 item에 클릭리스너 추가
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos=getAdapterPosition(); //사용자가 클릭한 아이템의 position을 줌
                    if(pos !=RecyclerView.NO_POSITION){ //position이 recyclerView의 item을 클릭했는지 또는 다른 곳을 클릭했는지 확인
                        //activity 전환을 위한 intent 사용. mContext는 startActivity 사용을 위함
                        Intent intent = new Intent(mContext,DetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //새로운 activity에 현재 item 항목의 name 전달을 위해 사용
                        intent.putExtra("TEXT",mList.get(pos).atcId);

                        //startActivity는 Activity를 상속받은 클래스가 사용할 수 있는 매서드이므로 adapter에서는 사용할 수 없음.
                        //따라서 LostActivity에서 어댑터에 인자로 context 값을 넘겨줘서 실행하도록 함
                        //activity 전환
                        mContext.startActivity(intent);


                    }
                }
            });
        }
    }

    //container가 원하는 값으로 변경해 할당하기 onCreateViewHolder 사용
    //container의 요소들은 view 객체가 들어가므로 activity_item.xml 파일을 View로 객체화시켜야 됨
    //xml 파일을 view로의 객체화를 위해 LayoutInflater 사용.
    //이후 inflate를 해 부풀림. 값과 장소를 지정할 수 있으며 값은 R.layout.list_items.xml 파일이 되며
    //장소가 parent(container)이 됨.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //activity_item.xml을 view로 객체화
        View view= mInflate.from(parent.getContext()).inflate(R.layout.activity_lost_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    //container 요소에 들어가기 위한 view 값으로 바꿔주었지만, 데이터가 빈 껍데기일 뿐
    //실제 데이터를 껍데기에 할당하기 위해 사용.
    //실제 데이터를 갖고 있는 곳은 LostActivity.java 이므로 데이터를 가진 list를 Adapter로 가져와야 함
    //position의 값을 통해 데이터가 담긴 리스트 요소에 접근.
    //즉, position은 container에 담길 view의 위치 값.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        holder.atcId.setText(mList.get(position).atcId);
    }

    //가져온 데이터 리스트를 adapter에서 미리 알고있어야 함.
    //기본적으로 0을 리턴하며, 모든 method 및 초기화를 진행하고 이 값을 바꾸지 않으면
    //안드로이드는 데이터 개수가 없다고 판단하여 container에 view는 만들지만, view의 내부 값 할당을 진행하지 않음
    //따라서 화면에 아무것도 나타나지 않음.
    @Override
    public int getItemCount(){
        return mList.size();
    }
}
