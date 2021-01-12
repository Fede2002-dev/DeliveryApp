package com.balran.deliveryapp.ui.Home.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balran.deliveryapp.Data.RestorantViewModel;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.adapters.FoodsAdapter;
import com.balran.deliveryapp.adapters.ShopCartAdapter;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.ui.Home.ShopActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodsFragment extends Fragment {
    FoodsAdapter adapter;
    ShopCartAdapter cartAdapter;
    List<Food> foodList = new ArrayList<>();
    RestorantViewModel restorantViewModel;
    Map<Integer, Integer> foodMap;
    double total=0;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RESTORANT_ID = "idRestorant";

    private int idRestorant;

    public FoodsFragment() {
        // Required empty public constructor
    }

    public static FoodsFragment newInstance(int idRestorant, Map<Integer, Integer> foodMap) {
        FoodsFragment fragment = new FoodsFragment();
        Bundle args = new Bundle();
        args.putInt(RESTORANT_ID, idRestorant);
        fragment.setArguments(args);
        fragment.setFoodMap(foodMap);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restorantViewModel = ViewModelProviders.of(getActivity())
                .get(RestorantViewModel.class);

        if (getArguments() != null) {
            idRestorant = getArguments().getInt(RESTORANT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_foods, container, false);

        //Setting the adapter
        Context ctx = v.getContext();
        final RecyclerView rv;
        rv = v.findViewById(R.id.rv_foods);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        if(foodMap==null) {
            adapter = new FoodsAdapter(foodList, getActivity());
            rv.setAdapter(adapter);
            //Load the data
            loadFoodData(idRestorant);
        }else{
            cartAdapter = new ShopCartAdapter(foodList,getActivity(), foodMap);
            rv.setAdapter(cartAdapter);
            //Load the data
            loadShopData(idRestorant);
        }

        return v;
    }

    private void loadShopData(int idRestorant) {
        restorantViewModel.getRestorantFoods(idRestorant).observe(getActivity(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                foodList = foods;
                List<Food> shopFoodList = new ArrayList<>();
                for(Food food: foodList){
                    if(foodMap.containsKey(Integer.parseInt(food.getIdfood()))){
                        shopFoodList.add(food);
                        food.setBuys(String.valueOf(foodMap.get(Integer.parseInt(food.getIdfood()))));
                        total+= Double.parseDouble(food.getPrice()) * foodMap.get(Integer.parseInt(food.getIdfood()));
                    }
                    cartAdapter.setData(shopFoodList);
                    ShopActivity.shopActivity.setFoodList(shopFoodList);
                    ShopActivity.shopActivity.setTotal(total);
                }
            }
        });
    }

    private void loadFoodData(int idRestorant) {
        restorantViewModel.getRestorantFoods(idRestorant).observe(getActivity(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                for(Food food: foods) {
                    if(!food.getStock().equals("out")){
                        foodList.add(food);
                    }
                }
                adapter.setData(foodList);
            }
        });
    }

    public Map<Integer, Integer> getFoodMap(){
        Map foodMap = adapter.getFoodMap();
        if(foodMap !=null){
            return  foodMap;
        }else{return null;}
    }

    public  void setFoodMap(Map<Integer, Integer> foodMap){
        if(foodMap!=null) {
            this.foodMap = foodMap;
        }
    }
}