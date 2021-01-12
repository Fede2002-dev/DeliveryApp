package com.balran.deliveryapp.retrofit;

import com.balran.deliveryapp.Common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeliveryClient {
    private static DeliveryClient instance = null;
    private DeliveryService deliveryService;
    private Retrofit retrofit;

    public DeliveryClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deliveryService = retrofit.create(DeliveryService.class);
    }

    //SINGLETON
    public static DeliveryClient getInstance(){
        if(instance==null){
            instance = new DeliveryClient();
        }
        return instance;
    }

    public DeliveryService getDeliveryService(){return deliveryService;}
}
