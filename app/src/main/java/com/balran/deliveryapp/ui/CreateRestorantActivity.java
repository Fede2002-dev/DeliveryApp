package com.balran.deliveryapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.CustomEditText;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Response.Restorant;
import com.bumptech.glide.Glide;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRestorantActivity extends AppCompatActivity {
    private ImageView iv_logo, iv_restorant;
    private CustomEditText et_address, et_restorant_name;
    private Button btn_show_foods, btn_save_changes, btn_delete;
    private Restorant restorant;
    private DeliveryService deliveryService;
    private DeliveryClient deliveryClient;

    private final int CHANGE_LOGO = 2;
    private final int CHANGE_PHOTO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorant);

        getSupportActionBar().hide();

        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

        restorant = new Restorant(null,null,null,null,null,null,null);

        //Init visual components
        et_restorant_name = findViewById(R.id.tv_name_restorant_user);
        iv_logo = findViewById(R.id.iv_logo_restorant_user);
        iv_restorant = findViewById(R.id.iv_restorant_user);
        et_address = findViewById(R.id.etAddress);
        btn_show_foods = findViewById(R.id.btn_show_foods);
        btn_save_changes = findViewById(R.id.btn_save_changes_user);
        btn_delete = findViewById(R.id.btn_delete_restorant);

        btn_save_changes.setText("Guardar");
        btn_save_changes.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.GONE);

        et_address.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_restorant_name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        btn_show_foods.setVisibility(View.GONE);

        //OnClickMehods
        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull()){
                    createRestorant();
                }
            }
        });

        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLogo();
            }
        });

        iv_restorant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoto();
            }
        });
    }

    private void createRestorant() {
        btn_save_changes.setEnabled(false);
        btn_save_changes.setText("Guardando...");

        String name = et_restorant_name.getText().toString();
        String address = et_address.getText().toString();

        restorant.setRestorantName(name);
        restorant.setRestorantAddress(address);
        restorant.setUserId(String.valueOf(SharedPreferencesManager.getSomeIntValue(this,Constantes.PREF_IDUSER)));

        Call<ResponseBody> call = deliveryService.createRestorant(restorant);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(CreateRestorantActivity.this, "Restaurante creado exitosamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    btn_save_changes.setEnabled(true);
                    btn_save_changes.setText("Guardar");
                    Toast.makeText(CreateRestorantActivity.this, "Error, intente nuevamente.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btn_save_changes.setEnabled(true);
                btn_save_changes.setText("Guardar");
                Toast.makeText(CreateRestorantActivity.this, "Error de conexion!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkNull() {
        String name = et_restorant_name.getText().toString();
        String address = et_address.getText().toString();
        if(restorant.getRestorantLogo()!=null && restorant.getRestorantPhoto()!=null) {
            if (!name.isEmpty() && !address.isEmpty()) {
                return true;
            } else {
                Toast.makeText(this, "Complete todos los campos!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            if(restorant.getRestorantPhoto()==null){
                Toast.makeText(this, "Debe seleccionar una imagen de portada!", Toast.LENGTH_SHORT).show();
                return false;
            }
            else{
                Toast.makeText(this, "Debe seleccionar una imagen de perfil!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }



    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    --------------------------AWFUL METHODS FOR SELECT A PHOTO FROM GALLERY-------------------------
    ------------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    */

    private void updateLogo(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso es denegado, se solicita el permiso.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Si el permiso es denegado, se solicita el permiso.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else {
                DeliveryClient deliveryClient = DeliveryClient.getInstance();
                DeliveryService deliveryService = deliveryClient.getDeliveryService();

                Intent selectPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(selectPhoto, CHANGE_LOGO);


            }
        }
    }

    private void updatePhoto(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso es denegado, se solicita el permiso.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Si el permiso es denegado, se solicita el permiso.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else {
                DeliveryClient deliveryClient = DeliveryClient.getInstance();
                DeliveryService deliveryService = deliveryClient.getDeliveryService();

                Intent selectPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(selectPhoto, CHANGE_PHOTO);


            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == CHANGE_LOGO){
                if(data != null){
                    Uri selectedImage = data.getData();  //content://galery...
                    String[]filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn
                            ,null,null,null);
                    if(cursor !=null){
                        cursor.moveToFirst();
                        //filename = filePath[0]
                        int imageIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imageIndex);



                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //Si el permiso es denegado, se solicita el permiso.
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }else {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //Si el permiso es denegado, se solicita el permiso.
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            }else {
                                DeliveryClient deliveryClient = DeliveryClient.getInstance();
                                DeliveryService deliveryService = deliveryClient.getDeliveryService();

                                //pass it like this
                                File file = new File(photoPath);


                                RequestBody requestFile =
                                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                                // MultipartBody.Part is used to send also the actual file name
                                MultipartBody.Part body =
                                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                                //Setting user data
                                String name = photoPath.substring(photoPath.lastIndexOf("/")+1);
                                String extension = photoPath.substring(photoPath.lastIndexOf("."));

                                Toast.makeText(this, "Subiendo imagen...", Toast.LENGTH_SHORT).show();

                                Call<ResponseBody> call = deliveryService.saveImage(body);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.isSuccessful()){
                                            restorant.setRestorantLogo(name);
                                            Glide.with(getApplicationContext())
                                                    .load(Constantes.API_MINITWITTER_FILES_URL + name)
                                                    .centerCrop()
                                                    .skipMemoryCache(true)
                                                    .into(iv_logo);
                                            Toast.makeText(CreateRestorantActivity.this, "Foto subida", Toast.LENGTH_SHORT).show();
                                            btn_save_changes.setVisibility(View.VISIBLE);
                                        }else{
                                            Toast.makeText(CreateRestorantActivity.this, "Error, intente nuevamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(CreateRestorantActivity.this, "Error de red.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        cursor.close();
                    }

                }
            }

            if(requestCode == CHANGE_PHOTO){
                if(data != null){
                    Uri selectedImage = data.getData();  //content://galery...
                    String[]filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn
                            ,null,null,null);
                    if(cursor !=null){
                        cursor.moveToFirst();
                        //filename = filePath[0]
                        int imageIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imageIndex);



                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //Si el permiso es denegado, se solicita el permiso.
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }else {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //Si el permiso es denegado, se solicita el permiso.
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            }else {
                                DeliveryClient deliveryClient = DeliveryClient.getInstance();
                                DeliveryService deliveryService = deliveryClient.getDeliveryService();

                                //pass it like this
                                File file = new File(photoPath);


                                RequestBody requestFile =
                                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                                // MultipartBody.Part is used to send also the actual file name
                                MultipartBody.Part body =
                                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                                //Setting user data
                                String name = photoPath.substring(photoPath.lastIndexOf("/")+1);
                                String extension = photoPath.substring(photoPath.lastIndexOf("."));

                                Toast.makeText(this, "Subiendo imagen...", Toast.LENGTH_SHORT).show();

                                Call<ResponseBody> call = deliveryService.saveImage(body);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            restorant.setRestorantPhoto(name);
                                            Glide.with(getApplicationContext())
                                                    .load(Constantes.API_MINITWITTER_FILES_URL + name)
                                                    .centerCrop()
                                                    .skipMemoryCache(true)
                                                    .into(iv_restorant);
                                            Toast.makeText(CreateRestorantActivity.this, "Foto subida", Toast.LENGTH_SHORT).show();
                                            btn_save_changes.setVisibility(View.VISIBLE);

                                        }else{
                                            Toast.makeText(getApplicationContext(), "Error al subir la foto, intente nuevamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Error de conexion!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        cursor.close();
                    }

                }
            }
        }
    }
}