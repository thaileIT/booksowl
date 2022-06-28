package com.example.bookproject.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookproject.BookLists;
import com.example.bookproject.Categories;
import com.example.bookproject.R;
import com.example.bookproject.model.BookModel;
import com.example.bookproject.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> implements Filterable {
    private Context context;
    public ArrayList<Category> categoryArrayList,filterList;
    FirebaseAuth mAuth;

    private FilterCategory filter;
    public AdapterCategory(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.filterList = categoryArrayList;
        mAuth = FirebaseAuth.getInstance();

    }



    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_category,parent,false);
        return new HolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        Category category = categoryArrayList.get(position);
        String id = category.getId();
        String category_name = category.getCategory();
        String uid = category.getUid();
        long timestamp = category.getTimestamp();

        //set data
        holder.txtCategoryName.setText(category_name);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userType = "" + snapshot.child("userType").getValue();
                if(userType.equals("user")){
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Toast.makeText(context,"Just click!" ,Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                }
                else if (userType.equals("admin"))
                {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            showMenu(category,holder);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookLists.class);
                intent.putExtra("categoryId" , id);
                intent.putExtra("categoryTitle" , category_name);
                context.startActivity(intent);
            }
        });
    }
    private void showMenu(Category model, AdapterCategory.HolderCategory holder) {
        PopupMenu popupMenu = new PopupMenu(context,holder.itemView);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                switch (which){
                    case 0:
                        //delete
                        deleteCategory(model,holder);
                        break;
                }
                return false;
            }
        });
    }
    private void deleteCategory(Category model, HolderCategory holder){
        String id = model.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Failure: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterCategory(filterList,this);
        }
        return filter;
    }

    class HolderCategory extends RecyclerView.ViewHolder{
        TextView txtCategoryName;
        ImageView imgDelete;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.category_name);
            imgDelete = itemView.findViewById(R.id.delete_category);
        }
    }
}
