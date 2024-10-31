package com.example.bookapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryTagAdapter;
import DAL.BookDatabase;
import Models.BookCategoryCrossRef;
import Models.Books;
import Models.Categories;
import Models.Chapters;
import Models.Users;

public class BookDetail extends AppCompatActivity {

    //view
    private ImageView img_book;
    private TextView tv_book_name;
    private TextView tv_book_author;
    private TextView tv_book_views;
    private ImageButton backButton;
    private TextView tv_book_description;

    // categories book
    private ArrayList<Categories> categories;
    private RecyclerView categoryTagsRecylerView;
    private CategoryTagAdapter categoryTagAdapter;
    private LinearLayoutManager categoryTagLayoutManager;
    private TextView tv_lastest_chapter;
    //Book
    private Books book;
    private Bundle bundle;
    //DB
    private BookDatabase db;
    private void bindingView(){

        //bindind view
        db = BookDatabase.getInstance(this);
        img_book = findViewById(R.id.iv_book_image);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_views = findViewById(R.id.tv_book_views);
        tv_book_description = findViewById(R.id.tv_books_description);
        backButton = findViewById(R.id.btn_back);

        // set book
        // get bundle
        bundle = getIntent().getExtras();
        if(bundle != null){
            book = (Books) bundle.get("book");
            tv_book_name.setText(book.getBookName());
            tv_book_author.setText(book.getBookAuthor());
            tv_book_views.setText(book.getViews().toString());
            tv_book_description.setText(book.getBookDescription());
            setImage(this, img_book, book.getImage());
        }

        // create list tag categories
        categoryTagsRecylerView = findViewById(R.id.category_tag_recycler_view);
        categoryTagAdapter = new CategoryTagAdapter(this);
        categoryTagLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        //set layout manager
        categoryTagsRecylerView.setLayoutManager(categoryTagLayoutManager);

        //set data
        setCategoriesData();
        categoryTagAdapter.setData(categories);
        categoryTagsRecylerView.setAdapter(categoryTagAdapter);
        tv_lastest_chapter = findViewById(R.id.tv_lastest_chapter);




    }
    private void setImage(Context mContext, ImageView holder, String imageUrl){
        // Sử dụng Glide để tải ảnh
        Glide.with(mContext)
                .load(imageUrl)
                .error(R.drawable.error_image)
                .placeholder(R.drawable.loading_image)
                .into(holder);
    }

    private void bindingAction(){
        tv_lastest_chapter.setOnClickListener(this::onClickLastestChapter);
        backButton.setOnClickListener(this::onClickBackButton);
    }

    private void onClickBackButton(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }

    private void onClickLastestChapter(View view) {
        Intent intent = new Intent(this, BookTableOfContent.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();
    }
    private void setCategoriesData(){



        categories = new ArrayList<>();
        //         get data
        List<BookCategoryCrossRef> bookCategories = db.bookCategoryCrossRefDAO().getBookCategoryByBookId(book.getBookId());
        for (BookCategoryCrossRef bookCategory: bookCategories) {
            Categories c = db.categoryDAO().getCategoryById(bookCategory.getCategoryId());
            categories.add(c);


        }
    }
}