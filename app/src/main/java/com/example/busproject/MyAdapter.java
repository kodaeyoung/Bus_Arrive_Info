package com.example.busproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    private Intent intent;


    public MyAdapter(Context context, ArrayList<Item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        //binding
        holder.itemView.setTag(position);
        holder.mobileNo.setText(mList.get(position).getMobileNo());
        holder.stationName.setText(mList.get(position).getStationName());
        holder.stationId.setText(mList.get(position).getStationId());
        //Click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mposition = holder.getAdapterPosition();
                intent = new Intent(mContext,SubActivity.class); //look_memo.class부분에 원하는 화면 연결
                intent.putExtra("id", mList.get(mposition).getStationId()); //변수값 인텐트로 넘기기
                intent.putExtra("name",mList.get(mposition).getStationName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent); //액티비티 열기

            }

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView stationName;
        public TextView mobileNo;
        public TextView stationId;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.mobileNo = itemView.findViewById(R.id.mobileNo);
            this.stationName = itemView.findViewById(R.id.stationName);
            this.stationId = itemView.findViewById(R.id.stationId);

        }

    }

}
