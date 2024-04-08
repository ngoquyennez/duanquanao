package com.example.demo_duanquanao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Useradapter extends RecyclerView.Adapter<Useradapter.MyViewHolder> {
    Context context;
    ArrayList<User> userArrayList;

    public Useradapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public Useradapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Useradapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);
        holder.username.setText(user.username);
        holder.password.setText(user.password);
        holder.fullname.setText(user.fullname);

    }

    @Override
    public int getItemCount() {

        return userArrayList.size();
    }
    public static  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username,password,fullname;

        Button xoa,sua;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);
            fullname = itemView.findViewById(R.id.fullname);


        }
    }
    public Bitmap convert64basetoImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
