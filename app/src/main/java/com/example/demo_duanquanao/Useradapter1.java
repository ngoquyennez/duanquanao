package com.example.demo_duanquanao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Useradapter1 extends BaseAdapter {
    ArrayList<User> listuser;
    Context context;

    public Useradapter1(ArrayList<User> listuser, Context context) {
        this.listuser = listuser;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listuser.size();
    }

    @Override
    public Object getItem(int position) {
        return listuser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView fullname, username, password;
        ImageView imageView;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item, null);
        fullname = convertView.findViewById(R.id.fullname);
        username = convertView.findViewById(R.id.username);
        password = convertView.findViewById(R.id.password);
        imageView = convertView.findViewById(R.id.image_user);

        User user =(User) getItem(position);
        fullname.setText(user.getFullname());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        imageView.setImageBitmap(convert64basetoImage(user.getImageuser()));
        return convertView;
    }
    public Bitmap convert64basetoImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
