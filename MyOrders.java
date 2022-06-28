package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.bookproject.adapter.AdapterBookAdmin;
import com.example.bookproject.adapter.OrderedBookAdapter;
import com.example.bookproject.model.BookModel;
import com.example.bookproject.model.BookOrdered;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvOrder;
    FirebaseAuth firebaseAuth;
    TextView txtSL,txtTotal;
    ArrayList<BookOrdered> bookOrderedArrayList;
    OrderedBookAdapter orderedBookAdapter;
    Button btn2;
    String id;
    String bookName,price,noOrders;
    float total;
    int SL = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        addControls();
        addEvents();
        setToolbar();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        bookOrderedArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
        ref.orderByChild("UID").equalTo(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookOrderedArrayList.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            BookOrdered model = ds.getValue(BookOrdered.class);
                            bookOrderedArrayList.add(model);
                        }
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyOrders.this,RecyclerView.VERTICAL,false);
                        rvOrder.setLayoutManager(layoutManager);
                        orderedBookAdapter = new OrderedBookAdapter(MyOrders.this,bookOrderedArrayList);
                        rvOrder.setAdapter(orderedBookAdapter);
                        int size = bookOrderedArrayList.size();
                        for(int i = 0;i<size;i++){
                            int sl = bookOrderedArrayList.get(i).getNumberOrders();
                            String bookID = bookOrderedArrayList.get(i).getBookID();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
                            reference.child(bookID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String price = "" + snapshot.child("price").getValue();
                                    String _price = price.substring(1);
                                    Float price_f = Float.parseFloat(_price);
                                    total = total + (price_f * sl);
                                    SL = SL + sl;
                                    txtSL.setText("Number of book: "+SL);
                                    txtTotal.setText("Total: "+total);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void addEvents() {
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyOrders.this,"Thank for your orders <3",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControls() {
        firebaseAuth = FirebaseAuth.getInstance();
        rvOrder = findViewById(R.id.rvOrders);
        btn2= findViewById(R.id.button2);
        txtSL=findViewById(R.id.txtSL);
        txtTotal=findViewById(R.id.txtTotal);
    }

    private  void setToolbar()
    {
        toolbar = findViewById(R.id.toolbar_MyOrders);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }
}