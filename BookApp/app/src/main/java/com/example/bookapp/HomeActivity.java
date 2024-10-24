package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import Adapter.ListBookAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAL.BookDatabase;
import Models.BookCategoryCrossRef;
import Models.Books;
import Models.Categories;
import Models.Users;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    ListView listView;
    ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        BookDatabase db = BookDatabase.getInstance(this);

        // Insert categories, books, and cross-references
        insertCategories(db);
        insertBooks(db);
        insertBookCategoryCrossRefs(db);
        home();
        ActionBar();
        ActionViewFlipper();
        recyclerView = findViewById(R.id.book_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // Đặt số cột là 2
        recyclerView.setLayoutManager(gridLayoutManager);
        List<Books> booksList = db.bookDAO().getAllBooks();
        ListBookAdapter adapter = new ListBookAdapter(booksList, this);
        recyclerView.setAdapter(adapter);
        insertUser(db);
        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void ActionViewFlipper(){
        List<String> array = new ArrayList<>();
        array.add("https://upload.wikimedia.org/wikipedia/vi/thumb/2/28/Norwegian-wood_poster.jpg/330px-Norwegian-wood_poster.jpg");
        array.add("https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_32831.jpg");
        array.add("https://cdn0.fahasa.com/media/catalog/product/i/m/image_180812.jpg");
        for(int i=0; i<array.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(array.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_show_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_show_right);
        Animation slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_show_right);
        viewFlipper.setOutAnimation(slide_out_right);

    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    private void  home(){
        toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerView = findViewById(R.id.book_recycler_view);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
    }

    private void insertCategories(BookDatabase db) {
        String[] categories = new String[]{"Tiểu Thuyết", "Truyện Tranh"};

        for (String categoryName : categories) {
            Categories category = db.categoryDAO().getCategoryByName(categoryName);
            if (category == null) {
                db.categoryDAO().insertCategory(new Categories(0, categoryName));
            }
        }
    }
    private void insertUser(BookDatabase db){
        Users user = new Users();
        user.setUserId(1);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setRole(1);
        user.setCreatedDate(new Date());
        if(db.userDAO().getUserByUsername("admin") == null){
            db.userDAO().insertUser(user);
        }

        //
        user.setUserId(2);
        user.setUsername("user");
        user.setPassword("user");
        user.setCreatedDate(new Date());
        user.setRole(2);
        if(db.userDAO().getUserByUsername("user") == null){
            db.userDAO().insertUser(user);
        }
    }
    private void insertBooks(BookDatabase db) {
        List<Books> booksList = new ArrayList<>();

        booksList.add(new Books(1, "Rừng Na Uy", "Haruki Murakami",
                "https://upload.wikimedia.org/wikipedia/vi/thumb/2/28/Norwegian-wood_poster.jpg/330px-Norwegian-wood_poster.jpg",
                1, "Câu chuyện bắt đầu từ một chuyến bay trong ngày đông...",
                100, 1));

        booksList.add(new Books(2, "Kafka bên bờ biển", "Haruki Murakami",
                "https://cdn.enovel.mobi/covers/26/26564-sac-duc-tam-220x283.jpg",
                1, "Kafka bên bờ biển có hai câu chuyện song song...",
                79, 1));

        booksList.add(new Books(3, "Sự im lặng của bầy cừu", "Thomas Harris",
                "https://upload.wikimedia.org/wikipedia/vi/8/86/The_Silence_of_the_Lambs_poster.jpg",
                1, "Những cuộc phỏng vấn ở xà lim với kẻ ăn thịt người Hannibal Lecter...",
                119, 1));
        booksList.add(new Books(4, "One piece", "Oda Eiichiro",
                "https://upload.wikimedia.org/wikipedia/vi/9/90/One_Piece%2C_Volume_61_Cover_%28Japanese%29.jpg",
                1, "Câu chuyện kể về Monkey D. Luffy, một chàng trai trẻ tuổi...",
                500, 2));
        booksList.add(new Books(5, "Naruto", "Kishimoto Masashi",
                "https://upload.wikimedia.org/wikipedia/vi/thumb/c/c7/Naruto_Volume_1_manga_cover.jpg/330px-Naruto_Volume_1_manga_cover.jpg",
                1, " Câu chuyện xoay quanh Uzumaki Naruto, một ninja trẻ ...",
                223, 2));
        booksList.add(new Books(6, "Fairy Tail", "Hiro Mashima",
                "https://translate.google.com/website?sl=en&tl=vi&hl=vi&client=srp&u=https://upload.wikimedia.org/wikipedia/en/e/e1/FairyTail-Volume_1_Cover.jpg",
                1, "  Natsu Dragneel , một pháp sư Dragon Slayer từ hội nhóm Fairy Tail, khám phá Vương quốc Fiore để tìm kiếm người cha nuôi mất tích của mình, con rồng Igneel ...",
                69, 2));
        booksList.add(new Books(7, "Thiên sứ nhà bên", "Saekisan",
                "https://upload.wikimedia.org/wikipedia/vi/thumb/1/19/The_Angel_Next_Door_Spoils_Me_Rotten_volume_1_cover.tiff/lossy-page1-330px-The_Angel_Next_Door_Spoils_Me_Rotten_volume_1_cover.tiff.jpg",
                2, " Fujimiya Amane sống một mình trong căn hộ của mình, và cô gái xinh nhất ở trường cậu...",
                111, 1));
        booksList.add(new Books(8, "Horimiya", "Hagiwara Daisuke",
                "https://translate.google.com/website?sl=en&tl=vi&hl=vi&client=srp&u=https://upload.wikimedia.org/wikipedia/en/4/46/Hori-san_to_Miyamura-kun_volume_1_cover.jpg",
                2, " Câu chuyện chủ yếu xoay quanh hai học sinh trung học: Kyouko Hori, một học sinh thông minh và nổi tiếng và Izumi Miyamura...",
                222, 1));
        for (Books book : booksList) {
            Books existingBook = db.bookDAO().getBookByNameAndAuthor(book.getBookName(), book.getBookAuthor());
            if (existingBook == null) {
                db.bookDAO().insertBook(book);
            }
        }
    }

    private void insertBookCategoryCrossRefs(BookDatabase db) {
        String[] categoryNames = new String[]{"Tiểu Thuyết", "Truyện Tranh"};

        for (String categoryName : categoryNames) {
            Categories category = db.categoryDAO().getCategoryByName(categoryName);
            if (category != null) {
                List<Books> booksList = db.bookDAO().getAllBooks();
                for (Books book : booksList) {
                    BookCategoryCrossRef existingCrossRef = db.bookCategoryCrossRefDAO()
                            .getCrossRefByBookAndCategory(book.getBookId(), category.getCategoryId());
                    if (existingCrossRef == null) {
                        BookCategoryCrossRef crossRef = new BookCategoryCrossRef();
                        crossRef.bookId = book.getBookId();
                        crossRef.categoryId = category.getCategoryId();
                        db.bookCategoryCrossRefDAO().insertBookCategoryCrossRef(crossRef);
                    }
                }
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_author) {
            Intent intent = new Intent(this, AuthorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}