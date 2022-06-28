package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCategory extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText txtCategoryName;
    Button btnAddCategory;
    ImageView imgBackToMainCategory;
    ProgressDialog progressDialog;
    String category = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        addControls();
        addEvents();
    }
    private void addControls() {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("PLease wait ... ");
        progressDialog.setCanceledOnTouchOutside(false);
        txtCategoryName = findViewById(R.id.txt_category_name_add);
        btnAddCategory = findViewById(R.id.btn_add_category);
        imgBackToMainCategory = findViewById(R.id.btn_back_to_main_add_category);
    }
    private void addEvents() {
        imgBackToMainCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        category = txtCategoryName.getText().toString().trim();
        if(category.isEmpty()){
            txtCategoryName.setError("Category's name is required !");
            txtCategoryName.requestFocus();
        }
        else{
            addCategoryFB();
        }

    }

    private void addCategoryFB() {
        progressDialog.setMessage("Adding category ...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("timestamp",timestamp);
        hashMap.put("category",""+category);
        hashMap.put("uid",firebaseAuth.getUid());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(AddCategory.this,"Category added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddCategory.this,"Fail to adding to database: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}