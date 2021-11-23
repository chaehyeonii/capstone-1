package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Police2_Adapter extends RecyclerView.Adapter<Police2_Adapter.MyViewHolder> {


    private ArrayList<Police2_Item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    //---추가
    private MyViewHolder viewHolder;

    public Police2_Adapter(Context context, ArrayList<Police2_Item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = mInflate.from(parent.getContext()).inflate(R.layout.activity_police2_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding
        //holder.atcId.setText(mList.get(position).atcId);
        holder.lstPlace.setText(mList.get(position).lstPlace);
        holder.lstPrdtNm.setText(mList.get(position).lstPrdtNm);
        holder.lstYmd.setText(mList.get(position).lstYmd);
        holder.lstLctNm.setText(mList.get(position).lstLctNm);


        Glide.with(holder.itemView.getContext())
                .load(mList.get(position).getlstFilePathImg())
                //.load("https://bit.ly/2V1ipNj")
                .into(holder.imageView2);

        //Click event

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //검색 기능을 위한 코드 ---------------------
    public void filterList(ArrayList<Police2_Item> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }
    //----------------------------------------

    //다른 검색 기능

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = mInflate.inflate(R.layout.activity_police2_item,null);

            viewHolder = new MyViewHolder(convertView);
            viewHolder.lstPrdtNm = (TextView) convertView.findViewById(R.id.tv_lstPrdtNm);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MyViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.lstPrdtNm.setText(mList.get(position).lstPrdtNm);

        return convertView;
    }
    //---------------------------------------

    //ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView atcId;
        public TextView lstPlace;
        public TextView lstPrdtNm;
        public TextView lstYmd;
        public TextView lstLctNm;
        public ImageView imageView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //atcId = itemView.findViewById(R.id.tv_atcId);
            lstPlace = itemView.findViewById(R.id.tv_lstPlace);
            lstPrdtNm = itemView.findViewById(R.id.tv_lstPrdtNm);
            lstYmd = itemView.findViewById(R.id.tv_lstYmd);
            lstLctNm = itemView.findViewById(R.id.tv_lstLctNm);
            imageView2=itemView.findViewById(R.id.imageView2);


            //click
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(mContext,DetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("TEXT",mList.get(pos).atcId);
                        /* intent.putExtra("TEXT",mList.get(pos).lstPlace);
                        intent.putExtra("TEXT",mList.get(pos).lstPrdtNm);
                        intent.putExtra("TEXT",mList.get(pos).lstSbjt);
                        intent.putExtra("TEXT",mList.get(pos).lstYmd); */

                        mContext.startActivity(intent);
                    }
                }
            });

        }




    }
}

// recyclerAdapter: http 리퀘스트를 통해 파싱과정을 거쳐 취합된 list라는 ArrayList를 받아서 뷰와 데이터를 바인딩 해준다.