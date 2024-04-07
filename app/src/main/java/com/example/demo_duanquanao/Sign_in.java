package com.example.demo_duanquanao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo_duanquanao.databinding.ActivitySignInBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign_in extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  =new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61557713075817"));
                startActivity(i);
            }
        });
        binding.textCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  =new Intent(Sign_in.this,Sign_up.class);
                startActivity(i);
            }
        });
        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                if(checkuser()){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("user")
                            .whereEqualTo("username",binding.inputUsername.getText().toString())
                            .whereEqualTo("password",binding.inputPassword.getText().toString())
                            .get()
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()&&task.getResult()!=null&&task.getResult().getDocuments().size()>0){
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    showToast("Đăng nhập thành công!");
                                    Intent i  = new Intent(Sign_in.this,MainActivity.class);
                                    startActivity(i);
                                }
                                else{
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    showToast("Đăng nhập thất bại!");
                                }
                            });
                }
            }
        });
    }
    public boolean checkuser(){
        if(isEmpty(binding.inputUsername)){
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.inputUsername.setError("Vui lòng nhập tài khoản!");
            return false;
        }
        if(isEmpty(binding.inputPassword)){
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.inputPassword.setError("Vui lòng nhập mật khẩu!");
            return false;
        }
        return true;
    }
    private void showToast(String mess) {
        Toast.makeText(Sign_in.this, mess, Toast.LENGTH_SHORT).show();
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}