package com.balran.deliveryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.MyApp;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.Response.Restorant;
import com.balran.deliveryapp.ui.Home.DashboardActivity;
import com.balran.deliveryapp.ui.RestorantActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyRestorantsAdapter extends RecyclerView.Adapter<MyRestorantsAdapter.viewHolderAdapterChatList> {
    List<Restorant> mValues;
    Context ctx;

    public MyRestorantsAdapter(List<Restorant> restorantList, Context ctx) {
        this.mValues = restorantList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public viewHolderAdapterChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restorant,parent,false);
        viewHolderAdapterChatList holder = new viewHolderAdapterChatList(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRestorantsAdapter.viewHolderAdapterChatList holder, int position) {
        if(mValues!=null){
            holder.mItem = mValues.get(position);

            holder.tv_name.setText(holder.mItem.getRestorantName());
            Glide.with(ctx)
                    .load(Constantes.API_MINITWITTER_FILES_URL + holder.mItem.getRestorantPhoto())
                    .into(holder.iv_restorant);
            Glide.with(ctx)
                    .load(Constantes.API_MINITWITTER_FILES_URL + holder.mItem.getRestorantLogo())
                    .into(holder.iv_logo);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DashboardActivity.showRestorant(holder.mItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mValues!=null){
            return mValues.size();
        }else {
            return 0;
        }
    }

    public  void setData(List<Restorant> restorantList){
        this.mValues = restorantList;
        notifyDataSetChanged();
    }

    public class viewHolderAdapterChatList extends  RecyclerView.ViewHolder{
        public CardView cardView;
        public ImageView iv_restorant;
        public ImageView iv_logo;
        public TextView tv_name;
        public TextView tv_stars;
        public Restorant mItem;

        public viewHolderAdapterChatList(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.restorant_cardview);
            iv_restorant = itemView.findViewById(R.id.iv_restorant_portrait);
            iv_logo = itemView.findViewById(R.id.iv_logo_restorant);
            tv_name = itemView.findViewById(R.id.tv_restorant_name);
        }
    }
}
