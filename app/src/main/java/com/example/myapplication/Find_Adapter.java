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

public class Find_Adapter extends RecyclerView.Adapter<Find_Adapter.MyViewHolder> {


    //-------------무한 스크롤을 위한 추가---------------
    private final int VIEW_TYPE_ITEM=0;
    private final int VIEW_TYPE_LOADING=1;
    //-----------------------------------------------


    private ArrayList<Find_Item> mList;
    private LayoutInflater mInflate;
    private Context mContext;

    public Find_Adapter(Context context, ArrayList<Find_Item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = mInflate.from(parent.getContext()).inflate(R.layout.activity_find_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding
        holder.atcId.setText(mList.get(position).atcId);
        holder.depPlace.setText(mList.get(position).depPlace);
        holder.fdPrdtNm.setText(mList.get(position).fdPrdtNm);
        holder.fdYmd.setText(mList.get(position).fdYmd);
        //holder.fdSn.setText(mList.get(position).fdSn);

        Glide.with(holder.itemView.getContext())
                .load(mList.get(position).getfdFilePathImg())
                //.load("https://bit.ly/2V1ipNj")
                .into(holder.imageView2);

        //Click event

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView atcId;
        public TextView depPlace;
        public TextView fdPrdtNm;
        public TextView fdYmd;
        //public TextView fdSn;
        public ImageView imageView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            atcId = itemView.findViewById(R.id.tv_atcId);
            depPlace = itemView.findViewById(R.id.tv_depPlace);
            fdPrdtNm = itemView.findViewById(R.id.tv_fdPrdtNm);
            fdYmd = itemView.findViewById(R.id.tv_fdYmd);
            //fdSn = itemView.findViewById(R.id.tv_fdSn);
            imageView2=itemView.findViewById(R.id.imageView2);

            //imageView2.setBackground(new ShapeDrawable(new OvalShape()));
            //imageView2.setClipToOutline(true);


            //click
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(mContext,Find_Detail.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("TEXT",mList.get(pos).atcId);
                        intent.putExtra("fdSn",mList.get(pos).fdSn);
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