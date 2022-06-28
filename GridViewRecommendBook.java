package com.example.bookproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.bookproject.adapter.GVRecommendBook;
import com.example.bookproject.model.Book;

import java.util.ArrayList;

public class GridViewRecommendBook extends AppCompatActivity {

    GridView gvRecommend;
    ArrayList<Book> bookArrayList;
    GVRecommendBook adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_recommend_book);
        addControls();
        addEvents();
        bookArrayList = new ArrayList<>();
        bookArrayList.add(new Book("Chuyện nhỏ trong thế giới lớn","E.H.Gombrich","$6.05",R.drawable.chuyen_nho));
        bookArrayList.add(new Book("Nhà giả kim","Paulo Coelho","$8.15",R.drawable.nha_gia_kim__paulo_coelho));
        bookArrayList.add(new Book("Chúa ruồi","William Golding","$7.75",R.drawable.chua_ruoi));
        bookArrayList.add(new Book("Con mèo dạy hải âu bay","Luis Sepúlveda","$5.50",R.drawable.cmdhab));
        bookArrayList.add(new Book("Làm chủ cảm xúc","Kery Goyette","$12.50",R.drawable.lccx));
        bookArrayList.add(new Book("Con mèo dạy hải âu bay","Dale Carnegie","$10.60",R.drawable.dac_nhan_tam));
        bookArrayList.add(new Book("Harry Porter and Philosopher's Stone","J.K.Rowling","$18.99",R.drawable.hp_philosopher));
        bookArrayList.add(new Book("Tôi tài giỏi, bạn cũng thế","Adam Khoo","$13.33",R.drawable.toi_tai_gioi));
        bookArrayList.add(new Book("Hoàng tử bé","Dale Carnegie","$4.45",R.drawable.hoang_tu_be));
        bookArrayList.add(new Book("3 người thầy vĩ đại","Dale Carnegie","$14.45",R.drawable.ba_nguoi_thay));

        adapter = new GVRecommendBook(this,R.layout.card_book_item,bookArrayList);
        gvRecommend.setAdapter(adapter);

    }

    private void addEvents() {
        gvRecommend.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
            intent.putExtra("BookName",bookArrayList.get(i).getBookName());
            intent.putExtra("Author",bookArrayList.get(i).getAuthor());
            intent.putExtra("Price",bookArrayList.get(i).getPrice());
            intent.putExtra("Image",bookArrayList.get(i).getImgBook());
            startActivity(intent);
        });
    }

    private void addControls() {
        gvRecommend = findViewById(R.id.gvRecommend);
    }
}