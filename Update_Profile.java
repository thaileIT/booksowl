package com.example.bookproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class Update_Profile extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextInputEditText txtNameEdit,txtEmailEdit,txtPhoneEdit,txtAddressEdit;
    Button btnUpdateEdit;
    ImageView imgUserEdit,btnBackToUserProfile;
    Uri imageUri = null;
    ProgressDialog progressDialog;
    String name = "",phone = "",address = "",email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);
        addControls();
        addEvents();
        loadUserInfo();
    }

    private void addControls() {
        txtNameEdit = findViewById(R.id.txtNameEdit);
        txtPhoneEdit = findViewById(R.id.txtPhoneEdit);
        txtEmailEdit = findViewById(R.id.txtEmailEdit);
        txtAddressEdit = findViewById(R.id.txtAddressEdit);
        btnUpdateEdit = findViewById(R.id.btnUpdateEdit);
        btnBackToUserProfile = findViewById(R.id.btnBackToUserProfile);
        imgUserEdit = findViewById(R.id.imgUserEdit);
        firebaseAuth = firebaseAuth.getInstance();
    }

    private void addEvents(){
        imgUserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttachMenu();
            }
        });

        btnUpdateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
                Intent intent = new Intent(Update_Profile.this,UserProfile.class);
                startActivity(intent);
            }
        });

        btnBackToUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Update_Profile.this,UserProfile.class);
                startActivity(intent);
            }
        });

    }

    private void validateData() {
        name = txtNameEdit.getText().toString().trim();
        phone = txtPhoneEdit.getText().toString().trim();
        address = txtAddressEdit.getText().toString().trim();

        if(name.isEmpty()){
            txtNameEdit.setError("Name is required");
            txtNameEdit.requestFocus();
        }
        else if (phone.isEmpty()){
            txtPhoneEdit.setError("Phone number is required");
            txtPhoneEdit.requestFocus();
        }
        else if(!Patterns.PHONE.matcher(phone).matches()){
            txtPhoneEdit.setError("Phone number is not valid");
            txtPhoneEdit.requestFocus();
        }
        else if (address.isEmpty()){
            txtAddressEdit.setError("Address is required");
            txtAddressEdit.requestFocus();
        }
        else {
            if(imageUri == null){
                //need to update without image
                updateProfile("");
            }
            else{
                //update with image
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        progressDialog.setMessage("Updating profile image ... ");
        progressDialog.show();
        String filePathAndName = "ProfileImage/" + firebaseAuth.getUid();

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedImageUrl = ""+uriTask.getResult();
                        updateProfile(uploadedImageUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Update_Profile.this,"Failed !!! " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfile(String imgUrl) {
        progressDialog.setMessage("Updating user profile ...");
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",""+name);
        hashMap.put("phone",""+phone);
        hashMap.put("address",""+address);
        if(imageUri != null){
            hashMap.put("profileImg","" + imgUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Update_Profile.this,"Profile updating ...",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Update_Profile.this,"Fail to update to database: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showImageAttachMenu() {
        PopupMenu popupMenu = new PopupMenu(this,imgUserEdit);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Camera");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Gallery");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                switch (which){
                    case 0:
                        //camera clicked
                        pickImageCamera();
                        break;
                    case 1:
                        // gallery clicked
                        pickImageGallery();
                        break;
                }
                return false;
            }
        });

    }

    private void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"New Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Sample Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActivityLauncher.launch(intent);

    }
    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imgUserEdit.setImageURI(imageUri);
                    }
                    else {
                        Toast.makeText(Update_Profile.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri =data.getData();
                        imgUserEdit.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(Update_Profile.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = "" + snapshot.child("email").getValue();
                String fullname = "" + snapshot.child("name").getValue();
                String imgProfile = "" + snapshot.child("profileImg").getValue();
                String phone = "" + snapshot.child("phone").getValue();
                String address = "" + snapshot.child("address").getValue();
                txtNameEdit.setText(fullname);
                txtEmailEdit.setText(email);
                txtPhoneEdit.setText(phone);
                txtAddressEdit.setText(address);
                Glide.with(Update_Profile.this)
                        .load(imgProfile)
                        .placeholder(R.drawable.person)
                        .into(imgUserEdit);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}