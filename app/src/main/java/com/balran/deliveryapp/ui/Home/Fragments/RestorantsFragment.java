package com.balran.deliveryapp.ui.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.Data.RestorantViewModel;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.adapters.MyRestorantsAdapter;
import com.balran.deliveryapp.adapters.RestorantsAdapter;
import com.balran.deliveryapp.retrofit.Response.Restorant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RestorantsFragment extends Fragment {
    private RestorantViewModel restorantViewModel;
    private List<Restorant> restorantList;
    private RestorantsAdapter adapter;
    private MyRestorantsAdapter myRestorantsAdapter;
    private Integer restorantListType;
    private FloatingActionButton fab;

    private static final String RESTORANT_LIST_TYPE = "idRestorant";

    public RestorantsFragment() {
        // Required empty public constructor
    }

    public static RestorantsFragment newInstance(Integer restorantListType) {
        RestorantsFragment fragment = new RestorantsFragment();
        Bundle args = new Bundle();
        args.putInt(RESTORANT_LIST_TYPE, restorantListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restorantViewModel = ViewModelProviders.of(getActivity()).get(RestorantViewModel.class);

        if(getArguments()!=null){
            restorantListType = getArguments().getInt(RESTORANT_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restorants, container, false);

        fab = v.findViewById(R.id.fab_add_restorant);
        fab.setVisibility(View.GONE);

        final RecyclerView rv;
        LinearLayoutManager layoutManager;

        layoutManager = new LinearLayoutManager((getContext()));
        rv = v.findViewById(R.id.recyclerview_restorants);
        rv.setLayoutManager(layoutManager);
        adapter = new RestorantsAdapter(restorantList, getActivity());
        rv.setAdapter(adapter);
        loadRestorantData();
        return  v;
    }

    private void loadUserRestorants(int idUser) {
        restorantViewModel.getRestorantsByID(idUser).observe(getActivity(), new Observer<List<Restorant>>() {
            @Override
            public void onChanged(List<Restorant> restorants) {
                restorantList = restorants;
                myRestorantsAdapter.setData(restorantList);
            }
        });
    }

    private void loadRestorantData() {
        restorantViewModel.getRestorants().observe(getActivity(), new Observer<List<Restorant>>() {
            @Override
            public void onChanged(List<Restorant> restorants) {
                restorantList = restorants;
                adapter.setData(restorantList);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRestorantData();
    }
}