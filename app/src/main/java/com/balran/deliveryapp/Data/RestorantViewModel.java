package com.balran.deliveryapp.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.retrofit.Response.Restorant;

import java.util.List;

public class RestorantViewModel extends AndroidViewModel {
    private RestorantRepository restorantRepository;
    private LiveData<List<Restorant>> restorants;
    private LiveData<List<Restorant>> userRestorants;
    private LiveData<List<Food>> restorantFoods;

    public RestorantViewModel(@NonNull Application application) {
        super(application);
        restorantRepository = new RestorantRepository();
        restorants = restorantRepository.getAllRestorants();
    }

    public  LiveData<List<Restorant>> getRestorants(){return restorants;}

    public LiveData<List<Food>> getRestorantFoods(int idRestorant){
        restorantFoods = restorantRepository.getRestorantFood(idRestorant);
        return  restorantFoods;
    }

    public LiveData<List<Restorant>> getRestorantsByID(int idUser){
        userRestorants = restorantRepository.getRestorantsByID(idUser);
        return  userRestorants;
    }
}
