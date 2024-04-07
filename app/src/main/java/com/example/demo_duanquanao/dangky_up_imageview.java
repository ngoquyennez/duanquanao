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
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.demo_duanquanao.databinding.ActivityDangkyUpImageviewBinding;
import com.example.demo_duanquanao.databinding.ActivityUpdateDataBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class dangky_up_imageview extends AppCompatActivity {
    private ActivityDangkyUpImageviewBinding binding;
    private String ImageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangkyUpImageviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.chonimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, imageURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                chonanh.launch(intent);
            }
        });
        binding.adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                HashMap<String, Object> product = new HashMap<>();
//                product.put("tensp", binding.edtTensp.getText().toString().trim());
//                product.put("giasp", binding.edtGiasp.getText().toString());
//                product.put("motasp", binding.edtMotasp.getText().toString().trim());
                product.put("imageuser", ImageUser);
                db.collection("user")
                        .add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
//                                loading(false);
                                showToast("Thêm sản phẩm thành công");
                            }
                        });
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

    public void showToast(String message) {
        Toast.makeText(dangky_up_imageview.this, message, Toast.LENGTH_LONG).show();
    }
}