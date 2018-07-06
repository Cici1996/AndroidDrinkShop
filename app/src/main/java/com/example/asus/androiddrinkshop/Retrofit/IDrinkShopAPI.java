package com.example.asus.androiddrinkshop.Retrofit;

import com.example.asus.androiddrinkshop.Model.Banner;
import com.example.asus.androiddrinkshop.Model.CheckUserResponse;
import com.example.asus.androiddrinkshop.Model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IDrinkShopAPI {

    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserResponseCall(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthDate") String birtDate,
                               @Field("address") String address);



    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUserInformation(@Field("phone") String phone);

    @GET("getBanner.php")
    Observable<List<Banner>> getBanner();
}
