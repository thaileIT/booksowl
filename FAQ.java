package com.example.bookproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class FAQ extends AppCompatActivity {
    Toolbar faqToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faqToolbar = findViewById(R.id.toolbar_FAQ);
        setSupportActionBar(faqToolbar);
        faqToolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(FAQ.this,MainActivity.class);
            startActivity(intent);
        });
    }
}