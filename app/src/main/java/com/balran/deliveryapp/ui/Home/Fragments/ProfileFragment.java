package com.balran.deliveryapp.ui.Home.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.CustomEditText;
import com.balran.deliveryapp.Common.DrawableClickListener;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.DeliveryClient;
import com.balran.deliveryapp.retrofit.DeliveryService;
import com.balran.deliveryapp.retrofit.Response.User;
import com.balran.deliveryapp.ui.login.LoginActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    CustomEditText et_email;
    CustomEditText et_name;
    CustomEditText et_password;
    CustomEditText et_confirm_password;
    CircleImageView user_image;
    Button btn_signout, btn_savechanges;
    Boolean checkPasswords = false;
    DeliveryClient deliveryClient;
    DeliveryService deliveryService;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        et_email = v.findViewById(R.id.et_email_profile);
        et_name = v.findViewById(R.id.et_name_profile);
        et_password = v.findViewById(R.id.et_password_profile);
        et_confirm_password = v.findViewById(R.id.et_confirm_password_profile);
        btn_signout = v.findViewById(R.id.btn_signout);
        btn_savechanges = v.findViewById(R.id.btn_savechanges);
        user_image = v.findViewById(R.id.profile_image);

        deliveryClient = DeliveryClient.getInstance();
        deliveryService = deliveryClient.getDeliveryService();

        et_email.setEnabled(false);
        et_name.setEnabled(false);
        et_password.setEnabled(false);
        btn_savechanges.setEnabled(false);
        et_confirm_password.setVisibility(View.GONE);
        String url_profile_image = SharedPreferencesManager.getSomeStringValue(getContext(), Constantes.PREF_PHOTOURL);


        //Setting user data
        et_email.setText(SharedPreferencesManager.getSomeStringValue(getContext(), Constantes.PREF_EMAIL));
        et_name.setText(SharedPreferencesManager.getSomeStringValue(getContext(), Constantes.PREF_NAME));
        et_password.setText(SharedPreferencesManager.getSomeStringValue(getContext(), Constantes.PREF_PASSWORD));


        Glide.with(getContext())
                .load(Constantes.API_MINITWITTER_FILES_URL + url_profile_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(user_image);

        //OnClickDrawable methods
        onClickDrawable();
        //On Click methods
        btn_signout.setOnClickListener(this);
        btn_savechanges.setOnClickListener(this);
        user_image.setOnClickListener(this);
        return v;
    }

    private void onClickDrawable() {
        et_email.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        if (!et_email.isEnabled()) {
                            btn_savechanges.setEnabled(true);
                            et_email.setEnabled(true);
                        } else {
                            et_email.setEnabled(false);
                        }
                        break;
                }
            }
        });
        et_name.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        if (!et_name.isEnabled()) {
                            btn_savechanges.setEnabled(true);
                            et_name.setEnabled(true);
                        } else {
                            et_name.setEnabled(false);
                        }
                        break;
                }
            }
        });
        et_password.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        btn_savechanges.setEnabled(true);
                        et_password.setEnabled(true);
                        et_confirm_password.setVisibility(View.VISIBLE);
                        checkPasswords = true;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signout:
                SharedPreferencesManager.deleteAllValues(getContext());
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                break;
            case R.id.btn_savechanges:
                saveChanges();
                break;
            case R.id.profile_image:
                updatePhoto();
                break;
        }
    }

    private void saveChanges() {
        if (checkFields()) {
            if (checkPasswords) {
                if (checkPassword()) {
                    updateUser();
                }
            } else {
                updateUser();
            }
        }
    }

    private void updateUser() {
        String email = et_email.getText().toString();
        String name = et_name.getText().toString();
        String password = et_password.getText().toString();
        String idUser = String.valueOf(SharedPreferencesManager.getSomeIntValue(getContext(), Constantes.PREF_IDUSER));
        String photo = SharedPreferencesManager.getSomeStringValue(getContext(), Constantes.PREF_PHOTOURL);

        User user = new User(idUser, email, password, photo, name);
        Call<User> call = deliveryService.updateProfile(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    SharedPreferencesManager.setSomeIntValue(getContext(),Constantes.PREF_IDUSER, Integer.parseInt(response.body().getIduser()));
                    SharedPreferencesManager.setSomeStringValue(getContext(),Constantes.PREF_EMAIL, response.body().getEmail());
                    SharedPreferencesManager.setSomeStringValue(getContext(),Constantes.PREF_PASSWORD, response.body().getPassword());
                    SharedPreferencesManager.setSomeStringValue(getContext(),Constantes.PREF_NAME, response.body().getName());
                    SharedPreferencesManager.setSomeStringValue(getContext(),Constantes.PREF_PHOTOURL, response.body().getPhotourl());

                    Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Algo ha ido mal.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkFields() {
        String email = et_email.getText().toString();
        String name = et_name.getText().toString();
        if (!email.isEmpty() && !name.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPassword() {
        String password = et_password.getText().toString();
        String confirm_password = et_confirm_password.getText().toString();

        if (!password.isEmpty() && !confirm_password.isEmpty()) {
            if (password.equals(confirm_password)) {
                if (password.length() >= 8) {
                    return true;
                } else {
                    Toast.makeText(getContext(), "La contraseña tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getContext(), "Complete todos los campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updatePhoto(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso es denegado, se solicita el permiso.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Si el permiso es denegado, se solicita el permiso.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == Constantes.SELECT_PHOTO_GALlERY){
                if(data != null){
                    Uri selectedImage = data.getData();  //content://galery...
                    String[]filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn
                            ,null,null,null);
                    if(cursor !=null){
                        cursor.moveToFirst();
                        //filename = filePath[0]
                        int imageIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imageIndex);



                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //Si el permiso es denegado, se solicita el permiso.
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }else {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //Si el permiso es denegado, se solicita el permiso.
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                                int id = SharedPreferencesManager.getSomeIntValue(getContext(), Constantes.PREF_IDUSER);
                                String extension = photoPath.substring(photoPath.lastIndexOf("."));

                                Toast.makeText(getContext(), "Subiendo imagen...", Toast.LENGTH_SHORT).show();

                                Call<ResponseBody> call = deliveryService.uploadImage(body,id);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            SharedPreferencesManager.setSomeStringValue(getContext(), Constantes.PREF_PHOTOURL, id+"profile"+extension);
                                            Glide.with(getContext())
                                                    .load(Constantes.API_MINITWITTER_FILES_URL + SharedPreferencesManager.getSomeStringValue(getContext(),Constantes.PREF_PHOTOURL))
                                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                    .skipMemoryCache(true)
                                                    .centerCrop()
                                                    .into(user_image);

                                        }else{
                                            Toast.makeText(getContext(), "Error al subir la foto, intente nuevamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getContext(), "Error de conexion!", Toast.LENGTH_SHORT).show();
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