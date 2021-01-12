package com.balran.deliveryapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

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
import com.balran.deliveryapp.retrofit.Request.RequestSignUp;
import com.balran.deliveryapp.retrofit.Response.User;
import com.balran.deliveryapp.ui.Home.DashboardActivity;
import com.balran.deliveryapp.ui.LocationActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    DeliveryService deliveryService;
    DeliveryClient deliveryClient;
    EditText et_email, et_password, et_name;
    Button btn_signup;
    TextView tv_goto_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

        //Setting variables
        et_email = findViewById(R.id.et_email_register);
        et_name = findViewById(R.id.et_username_register);
        et_password = findViewById(R.id.et_password_register);
        btn_signup = findViewById(R.id.btn_sign_up);
        tv_goto_login = findViewById(R.id.tv_goto_login);

        //OnClick Methods
        btn_signup.setOnClickListener(this);
        tv_goto_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_up:
                if(checkEmpty()){
                    makeRegister();
                }else{
                    return;
                }
                break;
            case R.id.tv_goto_login:
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private void makeRegister() {
        btn_signup.setText("Registrando...");
        btn_signup.setEnabled(false);
        RequestSignUp requestSignUp = new RequestSignUp(et_name.getText().toString(),et_email.getText().toString(),et_password.getText().toString());
        Call<User> call = deliveryService.doSignUp(requestSignUp);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                    SharedPreferencesManager.setSomeIntValue(getApplicationContext(), Constantes.PREF_IDUSER, Integer.parseInt(response.body().getIduser()));
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_EMAIL, response.body().getEmail());
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_PASSWORD, response.body().getPassword());
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_NAME, response.body().getName());
                    SharedPreferencesManager.setSomeStringValue(getApplicationContext(),Constantes.PREF_PHOTOURL, response.body().getPhotourl());


                    Intent i = new Intent(RegisterActivity.this, LocationActivity.class);
                    startActivity(i);
                    LoginActivity.activity.finish();
                    finish();
                }else{
                    btn_signup.setText("Iniciar sesion");
                    btn_signup.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Error, intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                btn_signup.setText("Iniciar sesion");
                btn_signup.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Error, intente nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkEmpty() {
        btn_signup.setText("Revisando datos...");
        btn_signup.setEnabled(false);
        String email = et_email.getText().toString();
        String username = et_name.getText().toString();
        String password = et_password.getText().toString();

        if(!email.isEmpty() && !username.isEmpty() && !password.isEmpty()){
            if(password.length() >=8){
                return true;
            }else{
                et_password.setError("La contraseña es demasiado corta");
                btn_signup.setText("Iniciar sesion");
                btn_signup.setEnabled(true);
                return false;
            }
        }else{
            if(email.isEmpty()){
                btn_signup.setText("Iniciar sesion");
                btn_signup.setEnabled(true);
                et_email.setError("Introduzca un email");
                return false;
            }
            else if(username.isEmpty()){
                btn_signup.setText("Iniciar sesion");
                btn_signup.setEnabled(true);
                et_name.setError("Introduzca un nombre de usuario");
                return  false;
            }
            else{
                btn_signup.setText("Iniciar sesion");
                btn_signup.setEnabled(true);
                et_password.setError("Introduzca una contraseña");
                return  false;
            }
        }

    }
}