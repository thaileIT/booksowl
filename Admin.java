package com.example.bookproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookproject.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin extends AppCompatActivity {
    EditText txtAdd_Book_Name,txtAdd_Author,txtAdd_Price,txtAdd_Category;
    Button btnAdd_Book;
    ImageView btnBackToMainAdmin,btnAttach,imgAdd_Book;
    ProgressDialog progressDialog;
    Uri imageUri = null;
    FirebaseAuth firebaseAuth;
    ArrayList<String> categoryTitleArrayList,categoryIDArrayList;

    String book_name = "",author = "",price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addControls();
        addEvents();
        loadCategory();
    }

    private void loadCategory() {
        categoryTitleArrayList = new ArrayList<>();
        categoryIDArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryIDArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String categoryID = ""+ ds.child("id").getValue();
                    String categoryTitle = "" + ds.child("category").getValue();

                    categoryTitleArrayList.add(categoryTitle);
                    categoryIDArrayList.add(categoryID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addControls() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);
        txtAdd_Book_Name= findViewById(R.id.txt_book_name_add);
        txtAdd_Author = findViewById(R.id.txt_author_add);
        txtAdd_Price = findViewById(R.id.txt_price_add);
        btnAdd_Book = findViewById(R.id.btn_add_book);
        txtAdd_Category = findViewById(R.id.txt_category_add);
        btnBackToMainAdmin = findViewById(R.id.btn_back_to_main_admin);
        imgAdd_Book = findViewById(R.id.imgAdd_Book);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {
        btnBackToMainAdmin.setOnClickListener(view -> onBackPressed());
        btnAdd_Book.setOnClickListener(view -> validateData());
        imgAdd_Book.setOnLongClickListener(view -> {
            showImageAttachMenu();
            return false;
        });
        txtAdd_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });
    }
    private String  selectedCategoryID,selectedCategoryTitle;
    private void categoryPickDialog() {
        String [] categoriesArray = new String[categoryTitleArrayList.size()];
        for(int  i =0;i<categoryTitleArrayList.size();i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedCategoryTitle = categoryTitleArrayList.get(i);
                        selectedCategoryID = categoryIDArrayList.get(i);
                        txtAdd_Category.setText(selectedCategoryTitle);
                    }
                }).show();
    }

    private ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imgAdd_Book.setImageURI(imageUri);
                    }
                    else {
                        Toast.makeText(Admin.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri =data.getData();
                        imgAdd_Book.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(Admin.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private void validateData() {
        book_name = txtAdd_Book_Name.getText().toString().trim();
        author = txtAdd_Author.getText().toString().trim();
        price =txtAdd_Price.getText().toString().trim();


        if(book_name.isEmpty()){
            txtAdd_Book_Name.setError("Book's name is required !");
            txtAdd_Book_Name.requestFocus();
        }
        else if(author.isEmpty()){
            txtAdd_Author.setError("Author is required !");
            txtAdd_Author.requestFocus();
        }
        else if (price.isEmpty()){
            txtAdd_Price.setError("Price is required !");
            txtAdd_Price.requestFocus();
        }
        else if(selectedCategoryTitle.isEmpty()){
            txtAdd_Category.setError("Category is required !");
            txtAdd_Category.requestFocus();
        }

        else {
            if(imageUri == null){
                //need to update without image
                updateBookProfile("");
            }
            else{
                //update with image
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        progressDialog.setMessage("Adding book's profile ... ");
        progressDialog.show();
        String filePathAndName = "ProfileBookImage/" + firebaseAuth.getUid();

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedImageUrl = ""+uriTask.getResult();
                        updateBookProfile(uploadedImageUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Admin.this,"Failed !!! " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBookProfile(String imgUrl) {
        progressDialog.setMessage("Adding book profile ...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("timestamp",timestamp);
        hashMap.put("uid",firebaseAuth.getUid());
        hashMap.put("bookName",""+book_name);
        hashMap.put("author",""+author);
        hashMap.put("price","$"+price);
        hashMap.put("categoryID",""+selectedCategoryID);
        hashMap.put("categoryTitle",""+selectedCategoryTitle);
        hashMap.put("description" ,"");//add later

        if(imageUri != null){
            hashMap.put("bookImage","" + imgUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Admin.this,"Profile book added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Admin.this,"Fail to adding to database: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showImageAttachMenu() {
        PopupMenu popupMenu = new PopupMenu(this,imgAdd_Book);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Camera");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Gallery");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                switch (which){
                    case 0:
                        //camera clicked
                        pickImageCamera();
                        break;
                    case 1:
                        // gallery clicked
                        pickImageGallery();
                        break;
                }
                return false;
            }
        });

    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityLauncher.launch(intent);
    }

    private void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"New Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Sample Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActivityLauncher.launch(intent);
    }


}