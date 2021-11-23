package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    //private List<String> items;
    private ArrayList<Police2_Item> mList;
    private Context mContext;

    public RecyclerViewAdapter1(Context context, ArrayList<Police2_Item> itmes) {
        this.mList = itmes;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_police2_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {

    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        //String item = mList.get(position);

        //binding
        holder.atcId.setText(mList.get(position).atcId);
        holder.lstPlace.setText(mList.get(position).lstPlace);
        holder.lstPrdtNm.setText(mList.get(position).lstPrdtNm);
        holder.lstYmd.setText(mList.get(position).lstYmd);


        Glide.with(holder.itemView.getContext())
                .load(mList.get(position).getlstFilePathImg())
                //.load("https://bit.ly/2V1ipNj")
                .into(holder.imageView2);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TextView atcId;
        public TextView lstPlace;
        public TextView lstPrdtNm;
        public TextView lstYmd;
        public ImageView imageView2;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);

            atcId = itemView.findViewById(R.id.tv_prdtClNm);
            lstPlace = itemView.findViewById(R.id.tv_lstPlace);
            lstPrdtNm = itemView.findViewById(R.id.tv_lstPrdtNm);
            lstYmd = itemView.findViewById(R.id.tv_lstYmd);
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

        /*
        public void setItem(String item) {
            textView.setText(item);
        }
        */

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}