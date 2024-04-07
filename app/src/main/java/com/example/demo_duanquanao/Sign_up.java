package com.example.demo_duanquanao;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo_duanquanao.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class Sign_up extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    String ImageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, imageURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                chonanh.launch(intent);
                binding.textAddImage.setVisibility(View.INVISIBLE);
            }
        });
        binding.textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sign_up.this, Sign_in.class);
                startActivity(i);
            }
        });
        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                if (checkuser()) {
                    FirebaseFirestore lg = FirebaseFirestore.getInstance();
                    lg.collection("user")
                            .whereEqualTo("username", binding.inputUsername.getText().toString())
                            .get()
                            .addOnCompleteListener(task -> {

                                if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    showToast("Đã tồn tại username này");


                                } else {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    HashMap<String, Object> product = new HashMap<>();
                                    product.put("fullname", binding.inputName.getText().toString().trim());
                                    product.put("username", binding.inputUsername.getText().toString().trim());
                                    product.put("password", binding.inputPassword.getText().toString().trim());
                                    product.put("imageuser", ImageUser);
                                    db.collection("user")
                                            .add(product)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
//                                loading(false);
                                                    showToast("Đăng ký thành công");
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    Intent i = new Intent(Sign_up.this,Sign_in.class);
                                                    startActivity(i);
                                                }
                                            });

                                }

                            });
                }
            }
        });
    }


    public boolean checkuser() {
        if (ImageUser == null) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            showToast("Vui lòng chọn ảnh!");
            return false;
        }
        if (isEmpty(binding.inputName)) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.inputName.setError("Vui lòng nhập họ tên");
            return false;
        }
        if (isEmpty(binding.inputUsername)) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.inputUsername.setError("Vui lòng nhập tài khoản");
            return false;
        }
        if (isEmpty(binding.inputPassword)) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.inputPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if (isEmpty(binding.inputConfirmpassword)) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.inputConfirmpassword.setError("Vui lòng nhập lại mật khẩu");
            return false;
        }
        if(binding.inputPassword.getText().toString().equals(binding.inputConfirmpassword.getText().toString())==false){
            binding.progressBar.setVisibility(View.INVISIBLE);
            showToast("Mật khẩu nhập lại không khớp!");
            return false;
        }
        return true;
    }

    private final ActivityResultLauncher<Intent> chonanh = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.imvUser.setImageBitmap(bitmap);
                        ImageUser = convertImageto64Base(bitmap);
                    } catch (Exception e) {
                        showToast("Không có ảnh");
                    }
                }
            }
    );

    private String convertImageto64Base(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = 150;
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void showToast(String mess) {
        Toast.makeText(Sign_up.this, mess, Toast.LENGTH_SHORT).show();
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}