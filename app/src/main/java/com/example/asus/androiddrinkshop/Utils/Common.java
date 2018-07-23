package com.example.asus.androiddrinkshop.Utils;

import com.example.asus.androiddrinkshop.Model.Category;
import com.example.asus.androiddrinkshop.Model.User;
import com.example.asus.androiddrinkshop.Retrofit.IDrinkShopAPI;
import com.example.asus.androiddrinkshop.Retrofit.RetrofitClient;

public class Common {
    private static final String BASE_URL = "https://drinkshopapi.000webhostapp.com/";
    public static User currentUser = null;

    public static Category currentCategody = null;

    public static IDrinkShopAPI getApi(){
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
