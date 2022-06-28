package com.example.bookproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookproject.DetailActivity;
import com.example.bookproject.R;
import com.example.bookproject.model.Book;

import java.util.List;


public class VietnameseBookAdapter extends RecyclerView.Adapter<VietnameseBookAdapter.VietnameseBookViewHolder> {

    Context context;
    List<Book> vietnameseBookList;

    public VietnameseBookAdapter(Context context, List<Book> vietnameseBookList) {
        this.context = context;
        this.vietnameseBookList = vietnameseBookList;
    }

    @NonNull
    @Override
    public VietnameseBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_book_item,parent,false);
        return new VietnameseBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VietnameseBookViewHolder holder, int position) {
        holder.imgBook_item.setImageResource(vietnameseBookList.get(position).getImgBook());
        holder.txtBookName_item.setText(vietnameseBookList.get(position).getBookName());
        holder.txtAuthor_item.setText(vietnameseBookList.get(position).getAuthor());
        holder.txtPrice_item.setText(vietnameseBookList.get(position).getPrice());
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("BookName",vietnameseBookList.get(position).getBookName());
            i.putExtra("Author",vietnameseBookList.get(position).getAuthor());
            i.putExtra("Price",vietnameseBookList.get(position).getPrice());
            i.putExtra("Image",vietnameseBookList.get(position).getImgBook());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return vietnameseBookList.size();
    }

    public static final class VietnameseBookViewHolder extends RecyclerView.ViewHolder{

        ImageView imgBook_item;
        TextView txtBookName_item,txtAuthor_item,txtPrice_item;
        public VietnameseBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook_item = itemView.findViewById(R.id.imgBook_item);
            txtBookName_item = itemView.findViewById(R.id.txtBookName_item);
            txtAuthor_item = itemView.findViewById(R.id.txtAuthor_item);
            txtPrice_item = itemView.findViewById(R.id.txtPrice_item);
        }
    }
}
