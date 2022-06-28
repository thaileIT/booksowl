package com.example.bookproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookproject.BookDetail;
import com.example.bookproject.Categories;
import com.example.bookproject.MyOrders;
import com.example.bookproject.R;
import com.example.bookproject.UserProfile;
import com.example.bookproject.model.BookModel;
import com.example.bookproject.model.BookOrdered;
import com.example.bookproject.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OrderedBookAdapter extends RecyclerView.Adapter<OrderedBookAdapter.OrderedBookViewHolder> {

    Context context;
    List<BookOrdered> orderedBookList;
    FirebaseAuth mAuth;
    String sum;


    public OrderedBookAdapter(Context context, List<BookOrdered> orderedBookList) {
        this.context = context;
        this.orderedBookList = orderedBookList;
        mAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public OrderedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new OrderedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedBookViewHolder holder, int position) {
        BookOrdered model = orderedBookList.get(position);
        String bookId = model.getBookID();
        int noOrder = model.getNumberOrders();
        String uid = model.getUID();
        long timestamp = model.getTimestamp();

        holder.BO_Amount.setText(""+noOrder);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = "" + snapshot.child("bookName").getValue();
                String price = "" + snapshot.child("price").getValue();
                String imgUrl = "" +snapshot.child("bookImage").getValue();
                holder.BO_Name.setText(title);
                holder.BO_Price.setText(price);
                Glide.with(context)
                        .load(imgUrl)
                        .placeholder(R.drawable.logo)
                        .into(holder.BO_Img);
                String _price = price.substring(1);
                Float price_f = Float.parseFloat(_price);
                float sum = price_f * noOrder;
                holder.BO_Sum.setText("$"+sum);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.BO_CHK.setChecked(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderedBookList.size();
    }

    public static final class OrderedBookViewHolder extends RecyclerView.ViewHolder{

        ImageView BO_Img,BO_Status;
        TextView BO_Name,BO_Price,BO_OrderDate,BO_DeliDate,BO_Amount,BO_Sum;
        CheckBox BO_CHK;
        public OrderedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            BO_Img = itemView.findViewById(R.id.imgBO_img);
            BO_Name = itemView.findViewById(R.id.txtBO_name);
            BO_Price = itemView.findViewById(R.id.txtBO_Price);
            BO_OrderDate = itemView.findViewById(R.id.txtOrderDay);
            BO_DeliDate = itemView.findViewById(R.id.txtDeliveryDay);
            BO_Amount = itemView.findViewById(R.id.txtNumberOrdered);
            BO_Status = itemView.findViewById(R.id.imgStatus);
            BO_Sum = itemView.findViewById(R.id.txtPrice_sum);
            BO_CHK=itemView.findViewById(R.id.checkBoxOrder);
        }
    }
}
