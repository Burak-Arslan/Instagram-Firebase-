package com.example.burakarslan.instagramfirebaseclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadAcitivity extends AppCompatActivity {

    EditText commentText;
    ImageView ımageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Uri selected;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_acitivity);

        commentText = (EditText) findViewById(R.id.edCommentText);
        ımageView = (ImageView) findViewById(R.id.imageView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void btnUpload(View view) {

        UUID uuıd = UUID.randomUUID();

        String imageName = "images/" + uuıd + ".jpg";
        StorageReference StorageReference = storageReference.child(imageName);


        StorageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String dowloandURL = taskSnapshot.getDownloadUrl().toString();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String userEmail = firebaseUser.getEmail().toString();
                String userComment = commentText.getText().toString();

                UUID uuıd = UUID.randomUUID();
                String uuidString = uuıd.toString();


                myRef.child("Posts").child(uuidString).child("useremail").setValue(userEmail);
                myRef.child("Posts").child(uuidString).child("comment").setValue(userComment);
                myRef.child("Posts").child(uuidString).child("downloadurl").setValue(dowloandURL);

                Toast.makeText(UploadAcitivity.this, "Post Yayınlandı", Toast.LENGTH_SHORT).show();

                Intent ıntent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(ıntent);


            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadAcitivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void chooseImage(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ıntent, 2);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 1) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ıntent, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            selected = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selected);
                ımageView.setImageBitmap(bitmap);

            } catch (IOException e) {

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
