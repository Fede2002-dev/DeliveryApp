package com.balran.deliveryapp.adapters;

import android.content.Context;
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
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.viewHolderAdapterChatList> {
    List<Food> mValues;
    Context ctx;
    Map<Integer, Integer> foodMap = new HashMap();

    public FoodsAdapter(List<Food> foodList, Context ctx) {
        this.mValues = foodList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public viewHolderAdapterChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food,parent,false);
        viewHolderAdapterChatList holder = new viewHolderAdapterChatList(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodsAdapter.viewHolderAdapterChatList holder, int position) {
        if(mValues!=null){
            holder.mItem = mValues.get(position);

            if(holder.mItem.getStock().equals("out")){
                holder.cardView.setVisibility(View.GONE);
            }

            holder.tv_food_name.setText(holder.mItem.getFoodName());
            holder.tv_food_description.setText(holder.mItem.getFoodDescription());
            holder.tv_food_price.setText("$"+holder.mItem.getPrice());

            Glide.with(ctx)
                    .load(Constantes.API_MINITWITTER_FILES_URL + holder.mItem.getImageUrl())
                    .into(holder.iv_food);

            //On Click methods
            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.counter >= 0){
                        holder.counter++;
                        foodMap.put(Integer.parseInt(holder.mItem.getIdfood()), holder.counter);
                        holder.tv_quantity.setText(String.valueOf(holder.counter));
                    }
                }
            });//onclick end

            holder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.counter > 0){
                        holder.counter--;
                        holder.tv_quantity.setText(String.valueOf(holder.counter));
                        if(holder.counter ==0){
                            foodMap.remove(Integer.valueOf(holder.mItem.getIdfood()));
                        }else{
                            foodMap.put(Integer.parseInt(holder.mItem.getIdfood()), holder.counter);
                        }
                    }
                }
            });//onclick end
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
        public ImageView iv_add;
        public ImageView iv_remove;
        public TextView tv_quantity;
        public TextView tv_food_name;
        public TextView tv_food_description;
        public TextView tv_food_price;
        public Food mItem;
        public int counter=0;

        public viewHolderAdapterChatList(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView_food);
            iv_food = itemView.findViewById(R.id.iv_food);
            iv_add = itemView.findViewById(R.id.iv_add_food);
            iv_remove = itemView.findViewById(R.id.iv_remove_food);
            tv_quantity = itemView.findViewById(R.id.tv_food_quantity);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_description = itemView.findViewById(R.id.tv_food_description);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
        }
    }
}
