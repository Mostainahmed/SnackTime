package com.w3.snacktime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by W3E16 on 05-Apr-18.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    private Context mCtx;
    private List<Menu> menuList;

    public MenuAdapter(Context mCtx, List<Menu> menuList) {
        this.mCtx = mCtx;
        this.menuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.menu_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Menu menu = menuList.get(position);

        holder.menuTitle.setText(menu.getMenuname());
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(menu.getImage()));

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView menuTitle;
        CheckBox menuCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            menuTitle = itemView.findViewById(R.id.menuTitle);
            menuCheckbox = itemView.findViewById(R.id.menuCheckbox);

        }
    }

}
