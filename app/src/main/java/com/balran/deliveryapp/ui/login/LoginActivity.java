package com.balran.deliveryapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Request.RequestLogin;
import com.balran.deliveryapp.retrofit.Response.User;
import com.balran.deliveryapp.ui.Home.DashboardActivity;
import com.balran.deliveryapp.ui.LocationActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    DeliveryService deliveryService;
    DeliveryClient deliveryClient;
    EditText et_email, et_password;
    Button btn_sign_in;
    TextView tv_goto_signup;
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity=this;
        getSupportActionBar().hide();

        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

        //Setting variables
        et_email = findViewById(R.id.et_email_login);
        et_password = findViewById(R.id.et_password_login);
        btn_sign_in = findViewById(R.id.btn_login);
        tv_goto_signup = findViewById(R.id.tv_signup);

        //OnClick Methods
        btn_sign_in.setOnClickListener(this);
        tv_goto_signup.setOnClickListener(this);

    }


    private void doLogin(String username, String password) {
        btn_sign_in.setEnabled(false);
        btn_sign_in.setText("Iniciando sesion...");
        RequestLogin requestLogin = new RequestLogin(username, password);
        Call<User> call = deliveryService.doLogin(requestLogin);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    SharedPreferencesManager.setSomeIntValue(getApplicationContext(),Constantes.PREF_IDUSER, Integer.parseInt(response.body().getIduser()));
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_EMAIL, response.body().getEmail());
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_PASSWORD, response.body().getPassword());
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_NAME, response.body().getName());
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_PHOTOURL, response.body().getPhotourl());

                    Intent i = new Intent(LoginActivity.this, LocationActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("iniciar sesion");
                    Toast.makeText(LoginActivity.this, "Error, revise los datos ingresados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                btn_sign_in.setEnabled(true);
                btn_sign_in.setText("iniciar sesion");
                Toast.makeText(LoginActivity.this, "Algo fue mal", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                doLogin(et_email.getText().toString(), et_password.getText().toString());
                break;
            case R.id.tv_signup:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }
}