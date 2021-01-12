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
import android.widget.ImageView;
import android.widget.Toast;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.CustomEditText;
import com.balran.deliveryapp.Common.DrawableClickListener;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Response.Restorant;
import com.balran.deliveryapp.ui.Home.MyFoodActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestorantActivity extends AppCompatActivity {
    private ImageView iv_logo, iv_restorant;
    private CustomEditText et_address, et_restorant_name;
    Button btn_show_foods, btn_save_changes, btn_delete;
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

        Bundle extras = getIntent().getExtras();
        restorant = (Restorant)extras.getSerializable("restorant");

        //Init visual components
        et_restorant_name = findViewById(R.id.tv_name_restorant_user);
        iv_logo = findViewById(R.id.iv_logo_restorant_user);
        iv_restorant = findViewById(R.id.iv_restorant_user);
        et_address = findViewById(R.id.etAddress);
        btn_show_foods = findViewById(R.id.btn_show_foods);
        btn_save_changes = findViewById(R.id.btn_save_changes_user);
        btn_delete = findViewById(R.id.btn_delete_restorant);

        et_restorant_name.setText(restorant.getRestorantName());
        et_restorant_name.setEnabled(false);
        et_address.setText(restorant.getRestorantAddress());
        et_address.setEnabled(false);


        Glide.with(this)
                .load(Constantes.API_MINITWITTER_FILES_URL + restorant.getRestorantLogo())
                .skipMemoryCache(true)
                .centerCrop()
                .into(iv_logo);

        Glide.with(this)
                .load(Constantes.API_MINITWITTER_FILES_URL + restorant.getRestorantPhoto())
                .skipMemoryCache(true)
                .centerCrop()
                .into(iv_restorant);


        onClickMethods();

    }

    private void onClickMethods() {
        et_restorant_name.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                btn_save_changes.setVisibility(View.VISIBLE);
                et_restorant_name.setEnabled(true);
            }
        });

        et_address.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                btn_save_changes.setVisibility(View.VISIBLE);
                et_address.setEnabled(true);
            }
        });

        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull()) {
                    saveChanges();
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

        btn_show_foods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestorantActivity.this, MyFoodActivity.class);
                i.putExtra("idRestorant", Integer.parseInt(restorant.getIdrestorant()));
                startActivity(i);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRestorant();
            }
        });
    }

    private void deleteRestorant() {
        Call<ResponseBody> call = deliveryService.deleteRestorant(Integer.parseInt(restorant.getIdrestorant()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RestorantActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RestorantActivity.this, "Error, intente nuevamente!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RestorantActivity.this, "Error de conexion.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkNull() {
        String name = et_restorant_name.getText().toString();
        String address = et_address.getText().toString();

        if(!name.isEmpty() && !address.isEmpty()){
            return true;
        }else{
            Toast.makeText(this, "Complete todos los campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void saveChanges() {
        btn_save_changes.setEnabled(false);
        btn_save_changes.setText("Guardando...");

        String name = et_restorant_name.getText().toString();
        String address = et_address.getText().toString();

        restorant.setRestorantName(name);
        restorant.setRestorantAddress(address);

        Call<ResponseBody> call =deliveryService.updateRestorant(restorant);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    btn_save_changes.setVisibility(View.GONE);
                    btn_save_changes.setEnabled(true);
                    btn_save_changes.setText("Guardar");
                    Toast.makeText(RestorantActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    btn_save_changes.setEnabled(true);
                    btn_save_changes.setText("Guardar");
                    Toast.makeText(RestorantActivity.this, "Error, intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btn_save_changes.setEnabled(true);
                btn_save_changes.setText("Guardar");
                Toast.makeText(RestorantActivity.this, "Error de conexion!", Toast.LENGTH_SHORT).show();
            }
        });
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
                                            Toast.makeText(RestorantActivity.this, "Foto subida", Toast.LENGTH_SHORT).show();
                                            btn_save_changes.setVisibility(View.VISIBLE);
                                        }else{
                                            Toast.makeText(RestorantActivity.this, "Error, intente nuevamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(RestorantActivity.this, "Error de red.", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(RestorantActivity.this, "Foto subida", Toast.LENGTH_SHORT).show();
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