package com.example.bookproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookproject.model.ExampleBroadcastReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    ExampleBroadcastReceiver exampleBroadcastReceiver = new ExampleBroadcastReceiver();

    TextView txtSignIn,txtForgotPassword,txtOthers;
    MaterialButton btnLogin,btnSignUp;
    EditText txtUsername;
    ProgressBar progressBarLogin;
    TextInputEditText txtPassword;
    FirebaseAuth mAuth;
    String email = "" , password = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(view -> {
            validateData();
        });
        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,Register.class);
            startActivity(intent);
        });
        txtForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
            startActivity(intent);
        });
    }

    private void validateData() {
        email = txtUsername.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            txtUsername.setError("Email is required");
            txtUsername.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtUsername.setError("Please provide valid email");
            txtUsername.requestFocus();
            return;
        }
        else if(password.isEmpty())
        {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }
        else if(password.length()<6)
        {
            txtPassword.setError("Password must have 6 characters");
            txtPassword.requestFocus();
            return;
        }
        else {
            loginUser();
        }
    }

    private void loginUser() {
        progressBarLogin.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLogin.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUser() {
        progressBarLogin.setVisibility(View.VISIBLE);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBarLogin.setVisibility(View.GONE);
                String userType = "" + snapshot.child("userType").getValue();
                if(userType.equals("user")){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else if (userType.equals("admin"))
                {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void addControls() {
        txtSignIn = findViewById(R.id.txtSignIn);
        btnLogin = findViewById(R.id.btnLogin);
        txtUsername = findViewById(R.id.txtUserName);
        txtForgotPassword = findViewById(R.id.txtForgotPass);
        txtOthers = findViewById(R.id.txtOthers);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        txtPassword = findViewById(R.id.txtPassword);
        mAuth=FirebaseAuth.getInstance();
        Typeface comicsFont = Typeface.createFromAsset(getAssets(),"fonts/comic.ttf");
        txtSignIn.setTypeface(comicsFont);
        btnLogin.setTypeface(comicsFont);
        txtUsername.setTypeface(comicsFont);
        txtForgotPassword.setTypeface(comicsFont);
        txtOthers.setTypeface(comicsFont);
        btnSignUp.setTypeface(comicsFont);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(exampleBroadcastReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(exampleBroadcastReceiver);
    }
}