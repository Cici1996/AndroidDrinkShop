package com.example.asus.androiddrinkshop.Adafter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.asus.androiddrinkshop.Interface.IItemClickListener;
import com.example.asus.androiddrinkshop.Model.Drink;
import com.example.asus.androiddrinkshop.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DrinkAdafter extends RecyclerView.Adapter<DrinkViewHolder> {

    Context context;
    List<Drink> drinkList;

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(com.example.asus.androiddrinkshop.R.layout.drink_item_layout,null);
        return new DrinkViewHolder(itemView);
    }

    public DrinkAdafter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, final int position) {

        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price.toString()));
        holder.txt_drink_name.setText(drinkList.get(position).Name.toString());

        holder.btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToChartDialog(position);
            }
        });

        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(holder.image_product);

        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddToChartDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout,null);

//        View
        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_cart_product);
        ElegantNumberButton txt_count = (ElegantNumberButton)itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = (TextView)itemView.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = (EditText)itemView.findViewById(R.id.edt_comment);

        RadioButton rdb_sizeM = (RadioButton)itemView.findViewById(R.id.rd_sizeM);
        RadioButton rdb_sizeL = (RadioButton)itemView.findViewById(R.id.rd_sizeL);

        RadioButton rdb_suggar100 = (RadioButton)itemView.findViewById(R.id.sugar_100);
        RadioButton rdb_suggar70 = (RadioButton)itemView.findViewById(R.id.sugar70);
        RadioButton rdb_suggar50 = (RadioButton)itemView.findViewById(R.id.sugar50);
        RadioButton rdb_suggar30 = (RadioButton)itemView.findViewById(R.id.sugar30);
        RadioButton rdb_sugarfree = (RadioButton)itemView.findViewById(R.id.sugarFree);

        RadioButton rdb_ice100 = (RadioButton)itemView.findViewById(R.id.ice_100);
        RadioButton rdb_ice70 = (RadioButton)itemView.findViewById(R.id.ice70);
        RadioButton rdb_ice50 = (RadioButton)itemView.findViewById(R.id.ice50);
        RadioButton rdb_ice30 = (RadioButton)itemView.findViewById(R.id.ice30);
        RadioButton rdb_icefree = (RadioButton)itemView.findViewById(R.id.iceFree);

        //set Data
        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(img_product_dialog);

        txt_product_dialog.setText(drinkList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
