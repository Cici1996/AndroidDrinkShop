package com.example.asus.androiddrinkshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.asus.androiddrinkshop.Adafter.DrinkAdafter;
import com.example.asus.androiddrinkshop.Model.Drink;
import com.example.asus.androiddrinkshop.Retrofit.IDrinkShopAPI;
import com.example.asus.androiddrinkshop.Utils.Common;

import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {

    IDrinkShopAPI mService;

    RecyclerView list_drink;
    //RxJava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    TextView txt_menu_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mService = Common.getApi();

        list_drink = (RecyclerView)findViewById(R.id.recycle_drinks);
        list_drink.setLayoutManager(new GridLayoutManager(this,2));
        list_drink.setHasFixedSize(true);

        txt_menu_name = (TextView)findViewById(R.id.txt_menu_name);
        txt_menu_name.setText(Common.currentCategody.Name);

        loadListDrink(Common.currentCategody.Id);
    }

    private void loadListDrink(String id) {
        compositeDisposable.add(mService.getDrink(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayDrinkList(drinks);
                    }
                })
        );
    }

    private void displayDrinkList(List<Drink> drinks) {
        DrinkAdafter adafter = new DrinkAdafter(this,drinks);
        list_drink.setAdapter(adafter);
    }
}
