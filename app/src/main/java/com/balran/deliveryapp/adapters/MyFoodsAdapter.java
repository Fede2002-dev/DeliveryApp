package com.balran.deliveryapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.ui.Home.MyFoodActivity;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFoodsAdapter extends RecyclerView.Adapter<MyFoodsAdapter.viewHolderAdapterChatList> {
    List<Food> mValues;
    Context ctx;
    Map<Integer, Integer> foodMap = new HashMap();

    public MyFoodsAdapter(List<Food> foodList, Context ctx) {
        this.mValues = foodList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public viewHolderAdapterChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myfood,parent,false);
        viewHolderAdapterChatList holder = new viewHolderAdapterChatList(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyFoodsAdapter.viewHolderAdapterChatList holder, int position) {
        if(mValues!=null){
            holder.mItem = mValues.get(position);

            holder.tv_food_name.setText(holder.mItem.getFoodName());
            holder.tv_food_description.setText(holder.mItem.getFoodDescription());
            holder.tv_food_price.setText("$"+holder.mItem.getPrice());

            Glide.with(ctx)
                    .load(Constantes.API_MINITWITTER_FILES_URL + holder.mItem.getImageUrl())
                    .into(holder.iv_food);

            if(holder.mItem.getStock().equals("stock")){
                holder.tv_stock.setText("En stock");
                holder.tv_stock.setTextColor(Color.GREEN);
            }else{
                holder.tv_stock.setText("Sin stock");
                holder.tv_stock.setTextColor(Color.RED);
            }

            holder.iv_edit_food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyFoodActivity.myFoodActivity.startEditActivity(holder.mItem);
                }
            });
        }
    }

    public Map<Integer, Integer> getFoodMap() {
        if(foodMap.size()>0) {
            return foodMap;
        }else{return null;}
    }

    @Override
    public int getItemCount() {
        if(mValues!=null){
            return mValues.size();
        }else {
            return 0;
        }
    }

    public  void setData(List<Food> foodList){
        this.mValues = foodList;
        notifyDataSetChanged();
    }

    public class viewHolderAdapterChatList extends  RecyclerView.ViewHolder{
        public CardView cardView;
        public ImageView iv_food;
        public ImageView iv_edit_food;
        public TextView tv_food_name;
        public TextView tv_food_description;
        public TextView tv_food_price;
        public TextView tv_stock;
        public Food mItem;
        public int counter=0;

        public viewHolderAdapterChatList(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.restorant_cardview);
            iv_food = itemView.findViewById(R.id.iv_food);
            iv_edit_food = itemView.findViewById(R.id.iv_edit_food);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_description = itemView.findViewById(R.id.tv_food_description);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_stock = itemView.findViewById(R.id.tv_stock);
        }
    }
}
