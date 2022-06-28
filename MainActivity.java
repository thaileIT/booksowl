package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.os.Parcelable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.drawerlayout.widget.*;

import com.bumptech.glide.Glide;
import com.example.bookproject.adapter.ForeignBookAdapter;
import com.example.bookproject.adapter.RecommendBookAdapter;
import com.example.bookproject.adapter.VietnameseBookAdapter;
import com.example.bookproject.model.Book;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewFlipper viewFlipper;
    RecyclerView recommendRecycler;
    RecommendBookAdapter recommendBookAdapter;

    RecyclerView vietnameseRecycler;
    VietnameseBookAdapter vietnameseBookAdapter;

    RecyclerView foreignRecycler;
    ForeignBookAdapter foreignBookAdapter;

    TextView txtView1,txtView2,txtView3;
    TextView txtHeaderName,txtHeaderEmail;
    ImageView imgHeaderUser,btnMainToCategories;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        ActionViewFlipper();
        ActionNavigation();
        hideItem();
        setDataHeader();
        List<Book> recommendBookList = new ArrayList<>();
        recommendBookList.add(new Book("Chuyện nhỏ trong thế giới lớn","E.H.Gombrich","$6.05",R.drawable.chuyen_nho));
        recommendBookList.add(new Book("Nhà giả kim","Paulo Coelho","$8.15",R.drawable.nha_gia_kim__paulo_coelho));
        recommendBookList.add(new Book("Chúa ruồi","William Golding","$7.75",R.drawable.chua_ruoi));
        recommendBookList.add(new Book("Con mèo dạy hải âu bay","Luis Sepúlveda","$5.50",R.drawable.cmdhab));
        recommendBookList.add(new Book("Làm chủ cảm xúc","Kery Goyette","$12.50",R.drawable.lccx));
        recommendBookList.add(new Book("Đắc Nhân Tâm","Dale Carnegie","$10.60",R.drawable.dac_nhan_tam));
        recommendBookList.add(new Book("Harry Porter and Philosopher's Stone","J.K.Rowling","$18.99",R.drawable.hp_philosopher));
        recommendBookList.add(new Book("Tôi tài giỏi, bạn cũng thế","Adam Khoo","$13.33",R.drawable.toi_tai_gioi));
        recommendBookList.add(new Book("Hoàng tử bé","Dale Carnegie","$4.45",R.drawable.hoang_tu_be));
        recommendBookList.add(new Book("3 người thầy vĩ đại","Dale Carnegie","$14.45",R.drawable.ba_nguoi_thay));


        List<Book> vietnameseBookList = new ArrayList<>();
        vietnameseBookList.add(new Book("Đất rừng phương nam","Đoàn Giỏi","$15.75",R.drawable.dat_rung_phuong_nam));
        vietnameseBookList.add(new Book("Dế mèn phiêu lưu ký","Tô Hoài","$5.35",R.drawable.de_men_phieu_luu_ky));
        vietnameseBookList.add(new Book("Tắt đèn","Ngô Tất Tố","$7.15",R.drawable.tat_den));
        vietnameseBookList.add(new Book("Tiêu sơn tráng sĩ","Khái Hưng","$14.50",R.drawable.tieu_son_trang_si));
        vietnameseBookList.add(new Book("Hai đứa trẻ","Thạch Lam","$7.99",R.drawable.hai_dua_tre));
        vietnameseBookList.add(new Book("Những ngôi sao xa xôi","Lê Minh Khuê","$11.10",R.drawable.ngoi_sao_xa_xoi));

        List<Book> foreignBookList = new ArrayList<>();
        foreignBookList.add(new Book("The god father","Mario Puzo","$20.75",R.drawable.god_father));
        foreignBookList.add(new Book("Forrest Gump","Winston Groom","$19.95",R.drawable.forrest_gump));
        foreignBookList.add(new Book("Kane and Abel","Jeffrey Archer","$18.55",R.drawable.kane_and_abel));
        foreignBookList.add(new Book("Lord of the ring","J.R.R.Tolkien","$17.65",R.drawable.lord_of_the_ring));
        foreignBookList.add(new Book("Sans Famille","Hector Malot","$19.99",R.drawable.sans_famille));
        foreignBookList.add(new Book("The old man and the sea","Ernest Hemingway","$21.00",R.drawable.the_old_man_sea));
        foreignBookList.add(new Book("The silent of the lambs","Thomas Harris","$22.80",R.drawable.the_silent_of_the_lambs));


        setRecommendRecycler(recommendBookList);
        setVietnameseRecycler(vietnameseBookList);
        setForeignRecycler(foreignBookList);

    }



    private void addEvents() {
        txtView1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,GridViewRecommendBook.class);
            startActivity(intent);
        });
        txtView2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,GridViewRecommendBook.class);
            startActivity(intent);
        });
        txtView3.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,GridViewRecommendBook.class);
            startActivity(intent);
        });
        btnMainToCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Categories.class);
                startActivity(intent);
            }
        });
    }
    private void addControls() {
        txtView1 = findViewById(R.id.txtViewAll);
        txtView2 = findViewById(R.id.txtViewAll_1);
        txtView3 = findViewById(R.id.txtViewAll_2);
        viewFlipper = findViewById(R.id.vfAds);
        btnMainToCategories = findViewById(R.id.imageView2);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar3);
        mAuth  = FirebaseAuth.getInstance();

    }
    private void setDataHeader() {
        View navView =  navigationView.inflateHeaderView(R.layout.header);
        imgHeaderUser = (ImageView)navView.findViewById(R.id.imgHeaderImage);
        txtHeaderName = (TextView)navView.findViewById(R.id.txtHeaderName);
        txtHeaderEmail = navView.findViewById(R.id.txtHeaderEmail);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = "" + snapshot.child("email").getValue();
                String fullname = "" + snapshot.child("name").getValue();
                String imgProfile = "" + snapshot.child("profileImg").getValue();

                txtHeaderName.setText(fullname);
                txtHeaderEmail.setText(email);
                Glide.with(MainActivity.this)
                        .load(imgProfile)
                        .placeholder(R.drawable.person)
                        .into(imgHeaderUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setRecommendRecycler(List<Book> recommendBookList)
    {
        recommendRecycler = findViewById(R.id.recommend_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recommendRecycler.setLayoutManager(layoutManager);
        recommendBookAdapter = new RecommendBookAdapter(this,recommendBookList);
        recommendRecycler.setAdapter(recommendBookAdapter);
    }
    private void setVietnameseRecycler(List<Book> vietnameseBookList)
    {
        vietnameseRecycler = findViewById(R.id.vietnam_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        vietnameseRecycler.setLayoutManager(layoutManager);
        vietnameseBookAdapter = new VietnameseBookAdapter(this,vietnameseBookList);
        vietnameseRecycler.setAdapter(vietnameseBookAdapter);
    }
    private void setForeignRecycler(List<Book> foreignBookList) {
        foreignRecycler = findViewById(R.id.foreign_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        foreignRecycler.setLayoutManager(layoutManager);
        foreignBookAdapter = new ForeignBookAdapter(this,foreignBookList);
        foreignRecycler.setAdapter(foreignBookAdapter);
    }
    private void ActionViewFlipper(){
        ArrayList<String> arrADS = new ArrayList<>();
        arrADS.add("https://topicanative.edu.vn/wp-content/uploads/2020/07/sach-hoc-tieng-anh-5.jpg");
        arrADS.add("https://mcbooks.vn/wp-content/uploads/2017/06/bia-truoc-tu-hoc-tieng-trung-danh-cho-nguoi-moi-bat-dau.jpg");
        arrADS.add("https://upload.wikimedia.org/wikipedia/vi/thumb/9/9c/Nh%C3%A0_gi%E1%BA%A3_kim_%28s%C3%A1ch%29.jpg/150px-Nh%C3%A0_gi%E1%BA%A3_kim_%28s%C3%A1ch%29.jpg");
        arrADS.add("https://upload.wikimedia.org/wikipedia/vi/thumb/6/6e/Fatezero_cover.jpg/220px-Fatezero_cover.jpg");
        arrADS.add("https://cdn0.fahasa.com/media/flashmagazine/images/page_images/chuyen_con_meo_day_hai_au_bay_tai_ban_2019/2020_03_19_15_56_54_1-390x510.jpg");
        for (int i = 0; i<arrADS.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(arrADS.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void ActionNavigation(){
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }
    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu;
        nav_Menu = navigationView.getMenu();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userType = "" + snapshot.child("userType").getValue();
                if(userType.equals("user")){
                    nav_Menu.findItem(R.id.nav_Admin).setVisible(false);
                    nav_Menu.findItem(R.id.nav_Admin_2).setVisible(false);

                }
                else if (userType.equals("admin"))
                {
                    nav_Menu.findItem(R.id.nav_Admin).setVisible(true);
                    nav_Menu.findItem(R.id.nav_Admin_2).setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this,UserProfile.class));
                break;
            case R.id.nav_FAQ:
                startActivity(new Intent(MainActivity.this,FAQ.class));
                break;
            case R.id.nav_orders:
                startActivity(new Intent(MainActivity.this,MyOrders.class));
                break;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
            case R.id.nav_wishlist:
                startActivity(new Intent(MainActivity.this,WishlistActivity.class));
                break;
            case R.id.nav_Admin:
                startActivity(new Intent(MainActivity.this,Admin.class));
                break;
            case R.id.nav_Admin_2:
                startActivity(new Intent(MainActivity.this,AddCategory.class));
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}

