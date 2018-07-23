package com.example.asus.androiddrinkshop.Adafter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.androiddrinkshop.DrinkActivity;
import com.example.asus.androiddrinkshop.Interface.IItemClickListener;
import com.example.asus.androiddrinkshop.Model.Category;
import com.example.asus.androiddrinkshop.R;
import com.example.asus.androiddrinkshop.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdafter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> categories;

    public CategoryAdafter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        //load Image
        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.img_product);

        holder.txt_menu_name.setText(categories.get(position).Name);

        //Event
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentCategody = categories.get(position);

                //start new Activity
                context.startActivity(new Intent(context,DrinkActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
