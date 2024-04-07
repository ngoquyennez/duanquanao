package com.example.demo_duanquanao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class delete_data extends AppCompatActivity {
    Button xoa;
    FirebaseFirestore db;
    EditText username,password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);
        db = FirebaseFirestore.getInstance();
        xoa = findViewById(R.id.xoa1);
        username = findViewById(R.id.usernamexoa);
        password = findViewById(R.id.password_delete);
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = username.getText().toString();
                String b = password.getText().toString();
                if(isEmpty(username)) username.setError("Vui lòng nhập tài khoản");
                if(isEmpty(password)) password.setError("Vui lòng nhập mật khẩu");
                else{
                    username.setText("");
                    password.setText("");
                    deletedata(a,b);
                }
            }
        });

    }
    private void deletedata(String username,String password){
        db.collection("user")
                .whereEqualTo("username",username)
                .whereEqualTo("password",password)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()&&!task.getResult().isEmpty()){
                            DocumentSnapshot ducumentsnapshot = task.getResult().getDocuments().get(0);
                            String documentID = ducumentsnapshot.getId();
                            db.collection("user")
                                    .document(documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(delete_data.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(delete_data.this,MainActivity.class);
                                            startActivity(i);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(delete_data.this,"Xóa thất bại",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}