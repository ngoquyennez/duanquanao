package com.example.demo_duanquanao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demo_duanquanao.databinding.ActivityMain2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;
    ArrayList<User> listuser;
    Useradapter1 useradapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        binding.listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showdialogEdit(position);
            }
        });
    }
    public void getData(){
        listuser = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                User user = new User();
                                user.setFullname(queryDocumentSnapshot.getString("fullname"));
                                user.setUsername(queryDocumentSnapshot.getString("username"));
                                user.setPassword(queryDocumentSnapshot.getString("password"));
                                user.setImageuser(queryDocumentSnapshot.getString("imageuser"));
                                listuser.add(user);
                            }
                            if(listuser.size()>0){

                                useradapter1 = new Useradapter1(listuser,MainActivity2.this);
                                binding.listUser.setAdapter(useradapter1);
                            }else{

                                showToast("Không có dữ liệu");
                            }
                        }
                    }
                });
    }
    public void showdialogEdit(int positon){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_suaxoa_user,null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        EditText fullname,username,password;
        ImageView imageView;
        Button btnEdit,btnDelete;
        fullname = view.findViewById(R.id.edit_fullname);
        username = view.findViewById(R.id.edit_username);
        password = view.findViewById(R.id.edit_password);
        imageView = view.findViewById(R.id.edit_image);

        User user = listuser.get(positon);
        fullname.setText(user.getFullname());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        imageView.setImageBitmap(convert64basetoImage(user.getImageuser()));

        btnEdit = view.findViewById(R.id.edit_sua);
        btnDelete = view.findViewById(R.id.edit_xoa);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSP(fullname.getText().toString().trim(),user.getUsername().toString().trim(),password.getText().toString().trim());
                dialog.dismiss();
                getData();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSP(user.getUsername());
                dialog.dismiss();
                getData();
            }
        });
    }
    public void showToast(String message){
        Toast.makeText(MainActivity2.this,message,Toast.LENGTH_LONG).show();
    }
    public void updateSP(String fullname,String username,String password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> product = new HashMap<>();
        product.put("fullname",fullname);
        product.put("password",password);
        db.collection("user")
                .document(username)
                .update(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showToast("Sửa thành công");
                        }else showToast("Thất bại");
                    }
                });
    }
    public void deleteSP(String username){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .document(username)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showToast("Xóa thành công");
                        }
                    }
                });
    }
    public Bitmap convert64basetoImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}