package com.balran.deliveryapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.ui.LocationActivity;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Request.RequestLogin;
import com.balran.deliveryapp.retrofit.Response.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DeliveryService deliveryService;
    DeliveryClient deliveryClient;
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofitInit();

        try {
            email = SharedPreferencesManager.getSomeStringValue(getApplicationContext(), Constantes.PREF_EMAIL);
            password = SharedPreferencesManager.getSomeStringValue(getApplicationContext(), Constantes.PREF_PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(email!=null && password!=null) {
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<User> call = deliveryService.doLogin(requestLogin);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        Intent i = new Intent(MainActivity.this, LocationActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }



    private void retrofitInit() {
        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

    }
}