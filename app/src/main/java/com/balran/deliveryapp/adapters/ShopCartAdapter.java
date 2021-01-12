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

import java.util.List;
import java.util.Map;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.viewHolderAdapterChatList> {
    List<Food> mValues;
    Context ctx;
    Map<Integer, Integer> foodMap;


    public ShopCartAdapter(List<Food> foodList, Context ctx, Map<Integer, Integer> foodMap){
        this.mValues = foodList;
        this.ctx = ctx;
        this.foodMap = foodMap;
    }

    @NonNull
    @Override
    public viewHolderAdapterChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shop,parent,false);
        viewHolderAdapterChatList holder = new viewHolderAdapterChatList(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCartAdapter.viewHolderAdapterChatList holder, int position) {
        if(mValues!=null){
            holder.mItem = mValues.get(position);

            holder.tv_food_name.setText(holder.mItem.getFoodName());
            holder.tv_food_description.setText(holder.mItem.getFoodDescription());
            holder.tv_food_price.setText("$"+holder.mItem.getPrice());

            Glide.with(ctx)
                    .load(Constantes.API_MINITWITTER_FILES_URL + holder.mItem.getImageUrl())
                    .into(holder.iv_food);
            if(foodMap!=null){
                int quantity = foodMap.get(Integer.parseInt(holder.mItem.getIdfood()));
                holder.tv_quantity.setText(String.valueOf(quantity));
                holder.tv_food_price.setText("$" + Double.parseDouble(holder.mItem.getPrice()) * quantity);
            }
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

    public  void setData(List<Food> foodList){
        this.mValues = foodList;
        notifyDataSetChanged();
    }

    public class viewHolderAdapterChatList extends  RecyclerView.ViewHolder{
        public CardView cardView;
        public ImageView iv_food;
        public TextView tv_quantity;
        public TextView tv_food_name;
        public TextView tv_food_description;
        public TextView tv_food_price;
        public Food mItem;
        public int counter=0;

        public viewHolderAdapterChatList(@NonNull View itemView) {
            super(itemView);
            iv_food = itemView.findViewById(R.id.iv_shop);
            tv_quantity = itemView.findViewById(R.id.tv_shop_quantity);
            tv_food_name = itemView.findViewById(R.id.tv_shop_name);
            tv_food_description = itemView.findViewById(R.id.tv_shop_description);
            tv_food_price = itemView.findViewById(R.id.tv_shop_price);
        }
    }
}
