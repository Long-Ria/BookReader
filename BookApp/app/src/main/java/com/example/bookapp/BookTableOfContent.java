package com.example.bookapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.ChapterAdapter;
import DAL.BookDatabase;
import Models.Books;
import Models.Chapters;

public class BookTableOfContent extends AppCompatActivity {
    // get bundle
    private Bundle bundle;
    private Books book;
    //list chapter
    // list chapter
    private List<Chapters> chapters;
    private ImageButton backButton;

    private RecyclerView listChapterView;
    private ChapterAdapter chapterAdapter;
    private LinearLayoutManager layoutManager;

    //Database
    BookDatabase db;
    private void bindingView(){
        // get Book
        bundle = getIntent().getExtras();
        if(bundle != null){
            book = (Books) bundle.get("book");
        }
        backButton = findViewById(R.id.btn_back);
        // db
        db = BookDatabase.getInstance(this);
        // create list chapter
        chapterAdapter = new ChapterAdapter(this);
        listChapterView = findViewById(R.id.book_chapter_recycler_view);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listChapterView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        listChapterView.addItemDecoration(itemDecoration);
        // set data
        setDataChapters();
        if (chapters == null) {
            chapters = new ArrayList<>();
            Chapters chapter = new Chapters();
            chapter.setChapterId(0);
            chapter.setChapterName("No chapter");
            chapters.add(chapter);
            chapterAdapter.setData(chapters);
        } else {
            chapterAdapter.setData(chapters);
        }


        listChapterView.setAdapter(chapterAdapter);
        //
    }
    private void bindingAction(){
        backButton.setOnClickListener(this::onClickBackButton);

    }

    private void onClickBackButton(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_table_of_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();

    }
    private void setDataChapters(){
        chapters = db.chapterDAO().getChaptersByBookId(book.getBookId());



    }
}