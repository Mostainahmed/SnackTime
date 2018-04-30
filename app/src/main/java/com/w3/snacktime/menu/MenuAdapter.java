package com.w3.snacktime.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.w3.snacktime.R;
import com.w3.snacktime.others.MyBounceInterPolator;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context mCtx;
    private List<Menu> menuList;
    private int lastCheckedPosition = -1;
    private String items;
    private int itemsPosition;

//    private Layout relative;

    public MenuAdapter(Context mCtx, List<Menu> menuList) {
        this.mCtx = mCtx;
        this.menuList = menuList;
    }

    public String getItem() {
        return items;
    }

    public int getItemsPosition(){
        return itemsPosition;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.menu_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SharedPreferences sharedPref1 = mCtx.getSharedPreferences("lastOrder", Context.MODE_PRIVATE);
        int abs = sharedPref1.getInt("lastorder", -1);

        if(abs == position && abs !=-1){
            Menu menu = menuList.get(position);

            holder.menuTitle.setText(menu.getMenuname());
            Glide.with(mCtx)
                    .load(menu.getImage())
                    .into(holder.imageView);

                Animation myAnim = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator = new MyBounceInterPolator(0.05, 20);
                myAnim.setInterpolator(interpolator);

                Animation myAnim1 = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator1 = new MyBounceInterPolator(0.07, 20);
                myAnim1.setInterpolator(interpolator1);

                Animation myAnim2 = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator2 = new MyBounceInterPolator(0.06, 15);
                myAnim2.setInterpolator(interpolator2);

                Animation myAnim3 = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator3 = new MyBounceInterPolator(0.03, 10);
                myAnim3.setInterpolator(interpolator3);

                holder.linearLayout.startAnimation(myAnim3);
                holder.relative.startAnimation(myAnim);
                holder.imageView.startAnimation(myAnim1);
                holder.menuTitle.startAnimation(myAnim2);
                holder.relative.setBackgroundColor(Color.parseColor("#fce2da"));
                holder.linearLayout.setBackgroundColor(Color.parseColor("#DA4201"));
                holder.linearLayout.startAnimation(myAnim);

                items = menu.getMenuname();
                itemsPosition = menu.getId();


        }
        else{
            Menu menu = menuList.get(position);

            holder.menuTitle.setText(menu.getMenuname());
            Glide.with(mCtx)
                    .load(menu.getImage())
                    .into(holder.imageView);
            if(position == lastCheckedPosition){

                Animation myAnim = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator = new MyBounceInterPolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                Animation myAnim1 = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator1 = new MyBounceInterPolator(0.3, 20);
                myAnim1.setInterpolator(interpolator1);

                Animation myAnim2 = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator2 = new MyBounceInterPolator(0.1, 15);
                myAnim2.setInterpolator(interpolator2);

                Animation myAnim3 = AnimationUtils.loadAnimation(mCtx, R.anim.bounce);
                MyBounceInterPolator interpolator3 = new MyBounceInterPolator(0.1, 10);
                myAnim3.setInterpolator(interpolator3);

                holder.linearLayout.startAnimation(myAnim3);
                holder.relative.startAnimation(myAnim);
                holder.imageView.startAnimation(myAnim1);
                holder.menuTitle.startAnimation(myAnim2);
                holder.relative.setBackgroundColor(Color.parseColor("#fce2da"));
                holder.linearLayout.setBackgroundColor(Color.parseColor("#DA4201"));
                holder.linearLayout.startAnimation(myAnim);

                items = menu.getMenuname();




            }else{

                holder.relative.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        }


    }

    @Override
    public int getItemCount() {

        return menuList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView menuTitle;
        CheckBox menuCheckbox;
        RelativeLayout relative;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            menuTitle = itemView.findViewById(R.id.menuTitle);
            //menuCheckbox = itemView.findViewById(R.id.menuCheckbox);
            relative = itemView.findViewById(R.id.relativeCard);
            linearLayout = itemView.findViewById(R.id.linearCard);


            //menuCheckbox.setOnClickListener(this);
            relative.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.relativeCard:
                    lastCheckedPosition = getAdapterPosition();
                    SharedPreferences sharedPref = mCtx.getSharedPreferences("lastOrder", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("lastorder", lastCheckedPosition);
                    editor.commit();
                    editor.apply();
                    notifyDataSetChanged();
                    break;
                default:
                    break;

            }
        }
    }
}
