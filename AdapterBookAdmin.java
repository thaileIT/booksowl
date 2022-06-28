package com.example.bookproject.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookproject.BookDetail;
import com.example.bookproject.DetailActivity;
import com.example.bookproject.R;
import com.example.bookproject.UserProfile;
import com.example.bookproject.model.BookModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterBookAdmin extends RecyclerView.Adapter<AdapterBookAdmin.HolderBookAdmin> {
    private Context context;
    private ArrayList<BookModel> bookModelArrayList;
    Uri imageUri = null;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    public AdapterBookAdmin(Context context, ArrayList<BookModel> bookModelArrayList) {
        this.context = context;
        this.bookModelArrayList = bookModelArrayList;
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait ... ");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderBookAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_book,parent,false);
        return new HolderBookAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBookAdmin holder, int position) {
        BookModel model = bookModelArrayList.get(position);
        String title = model.getBookName();
        String author= model.getAuthor();
        String price = model.getPrice();
        String id = model.getId();
        long timestamp = model.getTimestamp();

        holder.txtBookName_Admin.setText(title);
        holder.txtAuthor_Admin.setText(author);
        holder.txtPrice_Admin.setText(price);

        loadCategory(model,holder);
        loadImage(model,holder,timestamp);
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

                            showMenu(model,holder);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, BookDetail.class);
            i.putExtra("id",bookModelArrayList.get(position).getId());
            i.putExtra("price",bookModelArrayList.get(position).getPrice());
            context.startActivity(i);
        });
    }

    private void showMenu(BookModel model, HolderBookAdmin holder) {
        PopupMenu popupMenu = new PopupMenu(context,holder.imgBook_Admin);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Edit");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                switch (which){
                    case 0:
                        //delete
                        deleteBook(model,holder);
                        break;
                    case 1:
                        //edit
                        edit();
                        break;
                }
                return false;
            }
        });
    }

    private void deleteBook(BookModel model, HolderBookAdmin holder) {
        String bookID = model.getId();
        String bookTitle = model.getBookName();
        progressDialog.setMessage("Deleting " + bookTitle + "...");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.child(bookID).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Failure: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void edit() {
        Toast.makeText(context,"Unavailable yet",Toast.LENGTH_SHORT).show();

    }


    private void loadImage(BookModel model, HolderBookAdmin holder,long timestamp) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgUrl = "" + snapshot.child("bookImage").getValue();
                Glide.with(context)
                        .load(imgUrl)
                        .placeholder(R.drawable.logo)
                        .into(holder.imgBook_Admin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCategory(BookModel model, HolderBookAdmin holder) {
        String categoryID = model.getCategoryID();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String category = "" + snapshot.child("category").getValue();
                holder.txtCategory_Admin.setText(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return bookModelArrayList.size();
    }

    class HolderBookAdmin extends RecyclerView.ViewHolder{
        ImageView imgBook_Admin;
        TextView txtBookName_Admin,txtAuthor_Admin,txtCategory_Admin,txtPrice_Admin;
        public HolderBookAdmin(@NonNull View itemView) {
            super(itemView);
            imgBook_Admin = itemView.findViewById(R.id.imgBook_row);
            txtBookName_Admin = itemView.findViewById(R.id.txtBookName_row);
            txtAuthor_Admin = itemView.findViewById(R.id.txtAuthor_row);
            txtCategory_Admin = itemView.findViewById(R.id.txtCategory_row);
            txtPrice_Admin = itemView.findViewById(R.id.txtPrice_row);
        }
    }
}
