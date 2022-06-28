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


public class ForeignBookAdapter extends RecyclerView.Adapter<ForeignBookAdapter.ForeignBookViewHolder> {

    Context context;
    List<Book> foreignBookList;

    public ForeignBookAdapter(Context context, List<Book> foreignBookList) {
        this.context = context;
        this.foreignBookList = foreignBookList;
    }

    @NonNull
    @Override
    public ForeignBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_book_item,parent,false);
        return new ForeignBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForeignBookViewHolder holder, int position) {
        holder.imgBook_item.setImageResource(foreignBookList.get(position).getImgBook());
        holder.txtBookName_item.setText(foreignBookList.get(position).getBookName());
        holder.txtAuthor_item.setText(foreignBookList.get(position).getAuthor());
        holder.txtPrice_item.setText(foreignBookList.get(position).getPrice());
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("BookName",foreignBookList.get(position).getBookName());
            i.putExtra("Author",foreignBookList.get(position).getAuthor());
            i.putExtra("Price",foreignBookList.get(position).getPrice());
            i.putExtra("Image",foreignBookList.get(position).getImgBook());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return foreignBookList.size();
    }

    public static final class ForeignBookViewHolder extends RecyclerView.ViewHolder{

        ImageView imgBook_item;
        TextView txtBookName_item,txtAuthor_item,txtPrice_item;
        public ForeignBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook_item = itemView.findViewById(R.id.imgBook_item);
            txtBookName_item = itemView.findViewById(R.id.txtBookName_item);
            txtAuthor_item = itemView.findViewById(R.id.txtAuthor_item);
            txtPrice_item = itemView.findViewById(R.id.txtPrice_item);
        }
    }
}
