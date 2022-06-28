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


public class RecommendBookAdapter extends RecyclerView.Adapter<RecommendBookAdapter.RecommendBookViewHolder> {

    Context context;
    List<Book> recommendBookList;

    public RecommendBookAdapter(Context context, List<Book> recommendBookList) {
        this.context = context;
        this.recommendBookList = recommendBookList;
    }

    @NonNull
    @Override
    public RecommendBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_book_item,parent,false);
        return new RecommendBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendBookViewHolder holder, int position) {
        holder.imgBook_item.setImageResource(recommendBookList.get(position).getImgBook());
        holder.txtBookName_item.setText(recommendBookList.get(position).getBookName());
        holder.txtAuthor_item.setText(recommendBookList.get(position).getAuthor());
        holder.txtPrice_item.setText(recommendBookList.get(position).getPrice());

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("BookName",recommendBookList.get(position).getBookName());
            i.putExtra("Author",recommendBookList.get(position).getAuthor());
            i.putExtra("Price",recommendBookList.get(position).getPrice());
            i.putExtra("Image",recommendBookList.get(position).getImgBook());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return recommendBookList.size();
    }

    public static final class RecommendBookViewHolder extends RecyclerView.ViewHolder{

        ImageView imgBook_item;
        TextView txtBookName_item,txtAuthor_item,txtPrice_item;
        public RecommendBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook_item = itemView.findViewById(R.id.imgBook_item);
            txtBookName_item = itemView.findViewById(R.id.txtBookName_item);
            txtAuthor_item = itemView.findViewById(R.id.txtAuthor_item);
            txtPrice_item = itemView.findViewById(R.id.txtPrice_item);


        }
    }
}
