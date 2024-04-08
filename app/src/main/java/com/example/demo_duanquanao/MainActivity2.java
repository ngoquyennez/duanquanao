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
import android.widget.SearchView;
import android.widget.TextView;
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
    SearchView searchView;
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
//        binding.searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                MainActivity2.this.listuser.getFilter().fi
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
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
                                user.setId(queryDocumentSnapshot.getId());
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
        EditText fullname,password;
        TextView username;
        ImageView imageView;
        Button btnEdit,btnDelete;
        fullname = view.findViewById(R.id.edit_fullname);
        username = view.findViewById(R.id.edit_username);
        password = view.findViewById(R.id.edit_password);


        User user = listuser.get(positon);
        fullname.setText(user.getFullname());
        username.setText(user.getUsername());
        password.setText(user.getPassword());


        btnEdit = view.findViewById(R.id.edit_sua);
        btnDelete = view.findViewById(R.id.edit_xoa);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSP(user.getId(),fullname.getText().toString().trim(),password.getText().toString().trim());
                dialog.dismiss();
                getData();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSP(user.getId());
                dialog.dismiss();
                getData();
            }
        });
    }
    public void showToast(String message){
        Toast.makeText(MainActivity2.this,message,Toast.LENGTH_LONG).show();
    }
    public void updateSP(String id,String fullname,String password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> product = new HashMap<>();
        product.put("fullname",fullname);
        product.put("password",password);
        db.collection("user")
                .document(id)
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
    public void deleteSP(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .document(id)
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

}