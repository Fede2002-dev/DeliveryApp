package com.balran.deliveryapp.retrofit;

import com.balran.deliveryapp.retrofit.Request.Email;
import com.balran.deliveryapp.retrofit.Request.RequestLogin;
import com.balran.deliveryapp.retrofit.Request.RequestSignUp;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.retrofit.Response.User;
import com.balran.deliveryapp.retrofit.Response.Restorant;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DeliveryService {
    @POST("login")
    Call<User> doLogin(@Body RequestLogin requestLogin);

    @POST("register")
    Call<User> doSignUp(@Body RequestSignUp requestSignUp);

    @Multipart
    @POST("photo/{idUser}")
    Call <ResponseBody> uploadImage(@Part MultipartBody.Part image, @Path("idUser") int idUser);

    @Multipart
    @POST("photo")
    Call<ResponseBody> saveImage(@Part MultipartBody.Part image);

    @GET("restorants")
    Call<List<Restorant>> getAllRestorants();

    @GET("restorant/{idRestorant}")
    Call<List<Restorant>> getRestorantByID(@Path("idRestorant") int idRestorant);

    @GET("food/{idRestorant}")
    Call<List<Food>> getRestorantFoods(@Path("idRestorant") int idRestorant);

    @POST("updateprofile")
    Call<User> updateProfile(@Body User updateUser);

    @POST("updaterestorant")
    Call<ResponseBody> updateRestorant(@Body Restorant restorant);

    @POST("food")
    Call<ResponseBody> createFood(@Body Food food);

    @POST("updatefood")
    Call<ResponseBody> updateFood(@Body Food food);

    @POST("food/{idFood}")
    Call<ResponseBody> deleteFood(@Path("idFood") int idFood);

    @POST("restorant")
    Call<ResponseBody> createRestorant(@Body Restorant restorant);

    @POST("restorant/{idRestorant}")
    Call<ResponseBody> deleteRestorant(@Path("idRestorant") int idRestorant);

    @POST("email")
    Call<ResponseBody> sendMail(@Body Email email);

}
