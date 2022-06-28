package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookproject.adapter.AdapterBookAdmin;
import com.example.bookproject.model.BookModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BookDetail extends AppCompatActivity {
    String bookName,author,price,price_str2int,imgBook,id;
    Float price_float,price_book;
    TextView txtBookName_detail,txtAuthor_detail,txtPrice_detail,txtNumberOrders;
    ImageView imgBook_detail,imgBack,imgPlus,imgMinus;
    Button btnAdd2Cart;
    int numberOrders = 1;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail2);
        addControls();
        addEvents();
        Intent i = getIntent();
        id = i.getStringExtra("id");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imgBook = "" + snapshot.child("bookImage").getValue();
                bookName = "" + snapshot.child("bookName").getValue();
                author = "" + snapshot.child("author").getValue();
                price = "" + snapshot.child("price").getValue();
                Glide.with(BookDetail.this)
                        .load(imgBook)
                        .placeholder(R.drawable.logo)
                        .into(imgBook_detail);

                txtBookName_detail.setText(bookName);
                txtAuthor_detail.setText(author);
                txtPrice_detail.setText(price);
                price_str2int = price.substring(1);
                price_float = Float.parseFloat(price_str2int);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgPlus.setOnClickListener(view -> {
            numberOrders = numberOrders + 1;
            price_book = price_float * numberOrders;
            //price_book = (float) Math.round((price_book * 100) / 100);
            txtPrice_detail.setText("$" +String.valueOf(price_book));
            txtNumberOrders.setText(String.valueOf(numberOrders));
        });
        imgMinus.setOnClickListener(view -> {
            if(numberOrders > 1) {
                numberOrders = numberOrders - 1;
                price_book = price_float * numberOrders;
                //price_book = (float) Math.round((price_book * 100) / 100);
            }
            txtNumberOrders.setText(String.valueOf(numberOrders));
            txtPrice_detail.setText("$" + String.valueOf(price_book));
        });

        btnAdd2Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookDetail.this, MyOrders.class);
                uploadOrders();
                startActivity(i);

            }
        });
    }

    private void uploadOrders() {
        long timestamp = System.currentTimeMillis();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("bookID",""+id);
        hashMap.put("timestamp",timestamp);
        hashMap.put("UID",mAuth.getUid());
        hashMap.put("numberOrders",numberOrders);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookDetail.this,"Ordered",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookDetail.this,"Fail to adding to database: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addControls() {
        txtBookName_detail = findViewById(R.id.txtBookName_fb);
        txtAuthor_detail=findViewById(R.id.txtAuthor_fb);
        txtPrice_detail=findViewById(R.id.txtPrice_fb);
        imgBook_detail=findViewById(R.id.imgBook_fb);
        imgBack = findViewById(R.id.imgBack);
        txtNumberOrders = findViewById(R.id.txtNumber);
        imgPlus = findViewById(R.id.imgPlus);
        imgMinus = findViewById(R.id.imgMinus);
        btnAdd2Cart = findViewById(R.id.btnAdd2Order_2);
        mAuth = FirebaseAuth.getInstance();
    }

}