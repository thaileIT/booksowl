package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText txtUsernameREG,txtPasswordREG,txtEmail,txtRePasswordREG;
    MaterialButton btnSignUpReg;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String fullName = "",passWord = "",email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSignUpReg.setOnClickListener(view -> validationUser());
    }

    private void validationUser() {
        fullName = txtUsernameREG.getText().toString().trim();
        passWord = txtPasswordREG.getText().toString().trim();
        String repassWord = txtRePasswordREG.getText().toString().trim();
        email = txtEmail.getText().toString().trim();

        if(fullName.isEmpty()){
            txtUsernameREG.setError("Full name is required");
            txtUsernameREG.requestFocus();
            return;
        }
        else if (email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Please provide valid email");
            txtEmail.requestFocus();
            return;
        }
        else if(passWord.isEmpty()){
            txtPasswordREG.setError("Password is required");
            txtPasswordREG.requestFocus();
            return;
        }
        else if(passWord.length()< 6){
            txtPasswordREG.setError("Password must be at least 6 characters");
            txtPasswordREG.requestFocus();
            return;
        }
        else if (repassWord.isEmpty()){
            txtRePasswordREG.setError("Confirm your password !");
            txtRePasswordREG.requestFocus();
            return;
        }
        else if (!passWord.equals(repassWord)){
            txtRePasswordREG.setError("Password doesn't match !");
            txtRePasswordREG.requestFocus();
            return;
        }
        else{
            createUser();
        }
    }

    private void createUser() {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,passWord).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                updateUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Register.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        long timeStamp = System.currentTimeMillis();
        String uid = mAuth.getUid();
        // setup data to add in db
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("email",email);
        hashMap.put("name",fullName);
        hashMap.put("profileImg","");//add later
        hashMap.put("phone","");//add later
        hashMap.put("address","");//add later
        hashMap.put("timestamp",timeStamp);
        hashMap.put("userType","user");

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //data added to db
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Register.this,"Register successful !",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this,LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Register.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addControls() {
        txtUsernameREG = findViewById(R.id.txtRegisterName);
        txtPasswordREG = findViewById(R.id.txtRegisterPassword);
        txtRePasswordREG = findViewById(R.id.txtRePassword);
        btnSignUpReg = findViewById(R.id.btnSignUpReg);
        txtEmail = findViewById(R.id.txtEmail);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }
}