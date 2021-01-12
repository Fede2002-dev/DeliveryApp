package com.balran.deliveryapp.Data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.balran.deliveryapp.Common.MyApp;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.retrofit.Response.Restorant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestorantRepository {
    private DeliveryClient deliveryClient;
    private DeliveryService deliveryService;
    private MutableLiveData<List<Restorant>> allRestorants;
    private MutableLiveData<List<Restorant>> UserRestorants;
    private MutableLiveData<List<Food>> restorantFoods;


    RestorantRepository(){
        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

    }

    public MutableLiveData<List<Restorant>> getAllRestorants(){
        if(allRestorants ==null){
            allRestorants = new MutableLiveData<>();
        }

        Call<List<Restorant>> call = deliveryService.getAllRestorants();
        call.enqueue(new Callback<List<Restorant>>() {
            @Override
            public void onResponse(Call<List<Restorant>> call, Response<List<Restorant>> response) {
                if(response.isSuccessful()){
                    allRestorants.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Error al obtener los registros.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Restorant>> call, Throwable t) {
                Toast.makeText(MyApp.getInstance(), "Error de red.", Toast.LENGTH_SHORT).show();
            }
        });
        return allRestorants;
    }

    public MutableLiveData<List<Food>> getRestorantFood(int idRestorant){
        if(restorantFoods ==null){
            restorantFoods = new MutableLiveData<>();
        }
        Call<List<Food>> call = deliveryService.getRestorantFoods(idRestorant);
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if(response.isSuccessful()){
                    restorantFoods.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("MSG", "ERROR");
            }
        });

        return restorantFoods;
    }

    public MutableLiveData<List<Restorant>> getRestorantsByID(int idUser){
        if(UserRestorants == null){
            UserRestorants = new MutableLiveData<>();
        }
        Call<List<Restorant>> call = deliveryService.getRestorantByID(idUser);
        call.enqueue(new Callback<List<Restorant>>() {
            @Override
            public void onResponse(Call<List<Restorant>> call, Response<List<Restorant>> response) {
                if(response.isSuccessful()){
                    UserRestorants.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Restorant>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de red.", Toast.LENGTH_SHORT).show();
            }
        });
        return UserRestorants;
    }

}
