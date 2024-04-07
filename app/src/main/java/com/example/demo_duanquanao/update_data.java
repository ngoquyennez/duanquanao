package com.example.demo_duanquanao;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_duanquanao.databinding.ActivityUpdateDataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class update_data extends AppCompatActivity {

    FirebaseFirestore db;
    String ImageUserUpdate;
    private ActivityUpdateDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.layoutImageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, imageURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                chonanh.launch(intent);
                binding.chonanhUpdate.setVisibility(View.INVISIBLE);
            }
        });
        binding.sua1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = binding.usernamecansua.getText().toString();
                String b = binding.matkhaucansua.getText().toString();
                String c = binding.hotencansua.getText().toString();
                String d = ImageUserUpdate;
                if(isEmpty(binding.usernamecansua)) binding.usernamecansua.setError("Vui lòng điền tên đăng nhập!");
                else if(isEmpty(binding.matkhaucansua)) binding.matkhaucansua.setError("Vui lòng điền mật khẩu cần thay đổi");
                else if(isEmpty(binding.hotencansua)) binding.hotencansua.setError("Vui lòng điền họ và tên");
                else{binding.usernamecansua.setText("");
                binding.matkhaucansua.setText("");
                binding.hotencansua.setText("");

                updatedata(a, b, c, d);}
            }
        });
    }

    private void updatedata(String user, String pass, String fullname, String ImageUserUpdate) {
        Map<String, Object> userdetail = new HashMap<>();
        userdetail.put("password", pass);
        userdetail.put("fullname", fullname);
        userdetail.put("imageuser",ImageUserUpdate);
        db.collection("user")
                .whereEqualTo("username", user)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("user")
                                    .document(documentID)
                                    .update(userdetail)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent i = new Intent(update_data.this,MainActivity.class);
                                            startActivity(i);
                                            Toast.makeText(update_data.this, "Ban da sua thong tin thanh cong!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(update_data.this, "Bạn đã sửa thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(update_data.this,"failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private final ActivityResultLauncher<Intent> chonanh = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.imageUpdate.setImageBitmap(bitmap);
                        ImageUserUpdate = convertImageto64Base(bitmap);
                    } catch (Exception e) {
                        showToast("Không có ảnh");
                    }
                }
            }
    );
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
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
        Toast.makeText(update_data.this, mess, Toast.LENGTH_SHORT).show();
    }
}
