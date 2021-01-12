package com.balran.deliveryapp.ui.Home.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balran.deliveryapp.Data.RestorantViewModel;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.adapters.FoodsAdapter;
import com.balran.deliveryapp.adapters.MyFoodsAdapter;
import com.balran.deliveryapp.adapters.ShopCartAdapter;
import com.balran.deliveryapp.retrofit.Response.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFoodsFragment extends Fragment {
    MyFoodsAdapter adapter;
    List<Food> foodList;
    RestorantViewModel restorantViewModel;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RESTORANT_ID = "idRestorant";

    private int idRestorant;

    public MyFoodsFragment() {
        // Required empty public constructor
    }


    public static MyFoodsFragment newInstance(int idRestorant) {
        MyFoodsFragment fragment = new MyFoodsFragment();
        Bundle args = new Bundle();
        args.putInt(RESTORANT_ID, idRestorant);
        fragment.setArguments(args);
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

        adapter = new MyFoodsAdapter(foodList, getActivity());
        rv.setAdapter(adapter);
        //Load the data
        loadFoodData(idRestorant);


        return v;
    }


    private void loadFoodData(int idRestorant) {
        restorantViewModel.getRestorantFoods(idRestorant).observe(getActivity(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                foodList = foods;
                adapter.setData(foodList);
            }
        });
    }

}