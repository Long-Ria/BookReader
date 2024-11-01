package com.example.bookapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import DAL.BookDatabase;
import Models.Books;
import Adapter.ListBookAdapter;

public class TopViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_view);

        // Khởi tạo cơ sở dữ liệu
        BookDatabase db = BookDatabase.getInstance(this);

        // Thiết lập Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Khởi tạo Navigation Drawer
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        // Thiết lập RecyclerView để hiển thị danh sách sách
        recyclerView = findViewById(R.id.top_view_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 cột
        recyclerView.setLayoutManager(gridLayoutManager);

        // Lấy danh sách sách đã xem nhiều nhất
        List<Books> topViewedBooksList = db.bookDAO().getBooksByTopViews(); // Phương thức này cần được định nghĩa trong DAO
        ListBookAdapter adapter = new ListBookAdapter(topViewedBooksList, this);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);  // Sử dụng biểu tượng menu tùy thích

        username = getIntent().getStringExtra("username");

        if (username == null) {
            Toast.makeText(this, "Cannot found username", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            username = getIntent().getStringExtra("username");
            intent.putExtra("username", username);
            startActivity(intent);
        } else if (id == R.id.nav_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            // Truyền tên đăng nhập vào Intent
            username = getIntent().getStringExtra("username");
            intent.putExtra("username", username);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
