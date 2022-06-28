package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookproject.adapter.AdapterCategory;
import com.example.bookproject.model.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Categories extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvCategories;
    FirebaseAuth firebaseAuth;
    ArrayList<Category> categoryArrayList;
    AdapterCategory adapterCategory;
    EditText txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        addControls();
        addEvents();
        loadCategories();
    }

    private void addControls() {
        rvCategories = findViewById(R.id.rvCategories);
        toolbar = findViewById(R.id.toolbar_Categories);
        firebaseAuth = FirebaseAuth.getInstance();
        txtSearch = findViewById(R.id.txtSearch_Category);
        setSupportActionBar(toolbar);
    }
    private void addEvents() {
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(Categories.this,MainActivity.class);
            startActivity(intent);
        });
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    adapterCategory.getFilter().filter(charSequence);
                }
                catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void loadCategories() {
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Category model = ds.getValue(Category.class);
                    categoryArrayList.add(model);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Categories.this,RecyclerView.VERTICAL,false);
                rvCategories.setLayoutManager(layoutManager);
                adapterCategory = new AdapterCategory(Categories.this,categoryArrayList);
                rvCategories.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}