package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.EditText;

import com.example.bookproject.adapter.AdapterBookAdmin;
import com.example.bookproject.adapter.AdapterCategory;
import com.example.bookproject.model.BookModel;
import com.example.bookproject.model.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookLists extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvBooks;
    FirebaseAuth firebaseAuth;
    ArrayList<BookModel> bookArrayList;
    AdapterBookAdmin adapterBookAdmin;
    EditText txtSearch;
    String categoryID , categoryTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_lists);
        addControls();
        addEvents();
        loadBooks();
    }

    private void loadBooks() {
        bookArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("categoryID").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookArrayList.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            BookModel model = ds.getValue(BookModel.class);
                            bookArrayList.add(model);
                        }
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookLists.this,RecyclerView.VERTICAL,false);
                        rvBooks.setLayoutManager(layoutManager);
                        adapterBookAdmin = new AdapterBookAdmin(BookLists.this,bookArrayList);
                        rvBooks.setAdapter(adapterBookAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void addControls() {
        rvBooks = findViewById(R.id.rvBooksList);
        toolbar = findViewById(R.id.toolbar_BooksList);
        firebaseAuth = FirebaseAuth.getInstance();
        txtSearch = findViewById(R.id.txtSearch_Book);
        Intent intent = getIntent();
        categoryID = intent.getStringExtra("categoryId");
        categoryTitle=intent.getStringExtra("categoryTitle");
        toolbar.setSubtitle(categoryTitle + " Category");
        setSupportActionBar(toolbar);
    }
}