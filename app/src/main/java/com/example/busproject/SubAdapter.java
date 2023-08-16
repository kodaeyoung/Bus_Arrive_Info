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

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.MyViewHolder> {

    private ArrayList<Busitem> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    private String stopName;
    private Intent intent;

    public SubAdapter(Context context, ArrayList<Busitem> itmes, String name) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
        this.stopName = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.busitem, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding
        holder.itemView.setTag(position);
        holder.locationNo1.setText(mList.get(position).locationNo1);
        holder.plateNo1.setText(mList.get(position).plateNo1);
        holder.routeId.setText(mList.get(position).routeId);
        holder.predictTime1.setText(mList.get(position).predictTime1);
        holder.lowPlate1.setText(mList.get(position).lowPlate1);
        //Click event

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mposition = holder.getAdapterPosition();
                intent = new Intent(mContext,ResultActivity.class); //look_memo.class부분에 원하는 화면 연결
                intent.putExtra("route", mList.get(mposition).getRouteId()); //변수값 인텐트로 넘기기
                intent.putExtra("location",mList.get(mposition).getLocationNo1());
                intent.putExtra("plate",mList.get(mposition).getPlateNo1());
                intent.putExtra("predict",mList.get(mposition).getPredictTime1());
                intent.putExtra("name",stopName);
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
        public TextView locationNo1;
        public TextView plateNo1;
        public TextView routeId;
        public TextView predictTime1;
        public TextView lowPlate1;

        public MyViewHolder(View itemView) {
            super(itemView);

            locationNo1 = itemView.findViewById(R.id.locationNo1);
            plateNo1 = itemView.findViewById(R.id.plateNo1);
            routeId = itemView.findViewById(R.id.routeId);
            predictTime1 = itemView.findViewById(R.id.predictTime1);
            lowPlate1=itemView.findViewById(R.id.rowPlate1);
        }
    }

}
