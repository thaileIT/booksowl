package com.example.bookproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookproject.R;
import com.example.bookproject.model.Book;

import java.util.List;

public class GVRecommendBook extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final List<Book> booksList;

    public GVRecommendBook(Context context, int layout, List<Book> booksList) {
        this.context = context;
        this.layout = layout;
        this.booksList = booksList;
    }

    @Override
    public int getCount() {
        return booksList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder{
        ImageView imgBook_item;
        TextView txtBookName_item,txtAuthor_item,txtPrice_item;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.imgBook_item = view.findViewById(R.id.imgBook_item);
            holder.txtBookName_item= view.findViewById(R.id.txtBookName_item);
            holder.txtAuthor_item = view.findViewById(R.id.txtAuthor_item);
            holder.txtPrice_item = view.findViewById(R.id.txtPrice_item);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        Book book = booksList.get(i);
        holder.imgBook_item.setImageResource(book.getImgBook());
        holder.txtBookName_item.setText(book.getBookName());
        holder.txtAuthor_item.setText(book.getAuthor());
        holder.txtPrice_item.setText(book.getPrice());
        return view;
    }
}
