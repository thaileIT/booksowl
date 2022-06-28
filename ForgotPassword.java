package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    Button btnDone;
    EditText txtEmail_Verify;
    ImageView btnBacktoLogin;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        addControls();
        addEvents();
        btnDone= findViewById(R.id.btnDone);
        txtEmail_Verify = findViewById(R.id.txtVerify_Email);
    }

    private void addEvents() {
        btnBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        email = txtEmail_Verify.getText().toString().trim();
        if(email.isEmpty()){
            txtEmail_Verify.setError("Email is required !");
            txtEmail_Verify.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail_Verify.setError("Email is not valid !");
            txtEmail_Verify.requestFocus();
        }
        else{
            recoverPassword();
        }
    }

    private void recoverPassword() {
        progressDialog.setMessage("Sending verify email to " + email);
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassword.this,"Success sending to " + email,Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this,"Failed: " + e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void addControls() {
        btnDone= findViewById(R.id.btnDone);
        txtEmail_Verify = findViewById(R.id.txtVerify_Email);
        btnBacktoLogin = findViewById(R.id.btnBacktoLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}