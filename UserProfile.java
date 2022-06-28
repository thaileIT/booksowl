package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    Button btnUpdate;
    TextView txtProfileUserName,txtProfileUserEmail,txtProfilePhone,txtProfileAddress;
    ImageView imgUser,imgBackToMain;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        addControls();
        addEvents();
        loadUser();
    }

    private void loadUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = "" + snapshot.child("email").getValue();
                String fullname = "" + snapshot.child("name").getValue();
                String imgProfile = "" + snapshot.child("profileImg").getValue();
                String phone = "" + snapshot.child("phone").getValue();
                String address = "" + snapshot.child("address").getValue();

                txtProfileUserName.setText(fullname);
                txtProfileUserEmail.setText(email);
                txtProfilePhone.setText(phone);
                txtProfileAddress.setText(address);
                Glide.with(UserProfile.this)
                        .load(imgProfile)
                        .placeholder(R.drawable.person)
                        .into(imgUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfile.this,Update_Profile.class);
            startActivity(intent);
        });
        imgBackToMain.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfile.this,MainActivity.class);
            startActivity(intent);
        });
    }

    private void addControls() {
        txtProfileUserEmail = findViewById(R.id.txt_user_profile_email);
        txtProfileUserName = findViewById(R.id.txt_user_profile_name);
        txtProfilePhone = findViewById(R.id.txt_user_profile_phone);
        txtProfileAddress = findViewById(R.id.txt_user_profile_address);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBackToMain = findViewById(R.id.btnBackToMain);
        imgUser = findViewById(R.id.imgUser);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}