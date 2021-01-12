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
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.bumptech.glide.Glide;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFoodActivity extends AppCompatActivity {
    private ImageView iv_food;
    private EditText et_price, et_name, et_description;
    private Button btn_save, btn_stock, btn_out_stock, btn_delete;

    Food food;
    Boolean stock;
    DeliveryClient deliveryClient;
    DeliveryService deliveryService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        food = (Food) getIntent().getExtras().getSerializable("food");
        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

        //Init visual components
        iv_food = findViewById(R.id.iv_food_edit);
        et_name = findViewById(R.id.et_food_name_edit);
        et_price = findViewById(R.id.et_price_edit);
        et_description = findViewById(R.id.et_food_description_edit);
        btn_stock = findViewById(R.id.btn_stock_edit);
        btn_out_stock = findViewById(R.id.btn_outstock_edit);
        btn_save = findViewById(R.id.btn_save_food_edit);
        btn_delete = findViewById(R.id.btn_delete_food_edit);

        //Setting food data

        Glide.with(getApplicationContext())
                .load(Constantes.API_MINITWITTER_FILES_URL + food.getImageUrl()).into(iv_food);

        et_name.setText(food.getFoodName());
        et_description.setText(food.getFoodDescription());
        et_price.setText(food.getPrice());


        //ONCLICKMETHODS
        iv_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        btn_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock=true;
                btn_stock.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btn_out_stock.setBackgroundColor(getResources().getColor(R.color.colorGray));
                food.setStock("stock");
            }
        });

        btn_out_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock=false;
                btn_out_stock.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btn_stock.setBackgroundColor(getResources().getColor(R.color.colorGray));
                food.setStock("out");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValues()){
                    editFood();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = deliveryService.deleteFood(Integer.parseInt(food.getIdfood()));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(EditFoodActivity.this, "Eliminado.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EditFoodActivity.this, "Error, intente nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditFoodActivity.this, "Error de conexion!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void editFood() {
        food.setPrice(et_price.getText().toString());
        food.setFoodName(et_name.getText().toString());
        food.setFoodDescription(et_description.getText().toString());

        Call<ResponseBody> call = deliveryService.updateFood(food);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EditFoodActivity.this, "Cambios guardados!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(EditFoodActivity.this, "Error, intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditFoodActivity.this, "Error de conexion!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkValues() {
        String price = et_price.getText().toString();
        String name = et_name.getText().toString();
        String descrtiption = et_description.getText().toString();

        if(!food.getImageUrl().equals("null")){
            if(!price.isEmpty() && !name.isEmpty() && !descrtiption.isEmpty()){
                return true;
            }else{
                Toast.makeText(this, "Complete todos los campos!", Toast.LENGTH_SHORT).show();
                return  false;
            }
        }else{
            Toast.makeText(this, "Debe seleccionar una imagen!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    --------------------------AWFUL METHODS FOR SELECT A PHOTO FROM GALLERY-------------------------
    ------------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    */

    private void uploadPhoto(){
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
                startActivityForResult(selectPhoto, Constantes.SELECT_PHOTO_GALlERY);


            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == Constantes.SELECT_PHOTO_GALlERY) {
                if (data != null) {
                    Uri selectedImage = data.getData();  //content://galery...
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn
                            , null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        //filename = filePath[0]
                        int imageIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imageIndex);


                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //Si el permiso es denegado, se solicita el permiso.
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        } else {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //Si el permiso es denegado, se solicita el permiso.
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else {
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
                                String name = photoPath.substring(photoPath.lastIndexOf("/") + 1);
                                String extension = photoPath.substring(photoPath.lastIndexOf("."));

                                Call<ResponseBody> call = deliveryService.saveImage(body);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            food.setImageUrl(name);
                                            Glide.with(getApplicationContext())
                                                    .load(Constantes.API_MINITWITTER_FILES_URL + name)
                                                    .centerCrop()
                                                    .skipMemoryCache(true)
                                                    .into(iv_food);
                                            Toast.makeText(EditFoodActivity.this, "Foto subida", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditFoodActivity.this, "Error, intente nuevamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(EditFoodActivity.this, "Error de red.", Toast.LENGTH_SHORT).show();
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