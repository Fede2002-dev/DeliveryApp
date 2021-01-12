package com.balran.deliveryapp.ui.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Request.Email;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.ui.Home.Fragments.FoodsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {
    private Map<Integer, Integer> foodMap;
    private List<Food> foodList=null;
    private TextView tv_total;
    private Button btn_sendemail;
    int idRestorant;
    String idUser;
    double total;
    public static ShopActivity shopActivity;
    private DeliveryClient deliveryClient;
    private DeliveryService deliveryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shopActivity = this;

        getSupportActionBar().hide();

        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

        //Getting extras
        Bundle bundle = getIntent().getExtras();

        //Init visual components
        tv_total = findViewById(R.id.tv_total);
        btn_sendemail = findViewById(R.id.btn_sendemail);
        btn_sendemail.setEnabled(false);

        if(bundle!=null) {
            idRestorant = bundle.getInt("idRestorant");
            idUser = bundle.getString("idUser");
            foodMap = (HashMap<Integer, Integer>) bundle.getSerializable("foodMap");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_shop_container, FoodsFragment.newInstance(idRestorant, foodMap))
                .commit();

        //OnClick methods

        btn_sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sendemail.setText("Enviando...");
                btn_sendemail.setEnabled(false);
                Email email =  new Email(idUser, SharedPreferencesManager.getSomeStringValue(getApplicationContext(), Constantes.PREF_ADDRESS), String.valueOf(total), foodList);
                Call<ResponseBody> call = deliveryService.sendMail(email);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            btn_sendemail.setText("Enviado");
                            Toast.makeText(ShopActivity.this, "Pedido enviado!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ShopActivity.this, DashboardActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            btn_sendemail.setText("Enviar pedido");
                            btn_sendemail.setEnabled(true);
                            Toast.makeText(ShopActivity.this, "Error, intente nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btn_sendemail.setText("Enviar pedido");
                        btn_sendemail.setEnabled(true);
                        Toast.makeText(ShopActivity.this, "Error de conexion!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
        btn_sendemail.setEnabled(true);
    }

    public void setTotal(double total) {
        this.total = total;
        tv_total.setText("$"+total);
    }
}