package com.example.bookproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    String bookName,author,price,price_str2int;
    Float price_float,price_book;
    TextView txtBookName_detail,txtAuthor_detail,txtPrice_detail,txtNumberOrders;
    ImageView imgBook_detail,imgBack,imgPlus,imgMinus;
    Button btnAdd2Cart;
    int imgBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        bookName = i.getStringExtra("BookName");
        author = i.getStringExtra("Author");
        price = i.getStringExtra("Price");
        imgBook= i.getIntExtra("Image",R.drawable.chuyen_nho);

        txtBookName_detail = findViewById(R.id.txtBookName_detail);
        txtAuthor_detail=findViewById(R.id.txtAuthor_detail);
        txtPrice_detail=findViewById(R.id.txtPrice_detail);
        imgBook_detail=findViewById(R.id.imgBook_detail);
        imgBack = findViewById(R.id.imageView5);

        txtBookName_detail.setText(bookName);
        txtAuthor_detail.setText(author);
        txtPrice_detail.setText(price);
        imgBook_detail.setImageResource(imgBook);

        imgBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}