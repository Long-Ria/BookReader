package com.example.bookapp;

import android.os.Bundle;

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

import Adapter.ChapterAdapter;
import Models.Books;
import Models.Chapters;

public class BookTableOfContent extends AppCompatActivity {
    // get bundle
    private Bundle bundle;
    private Books book;
    //list chapter
    // list chapter
    private ArrayList<Chapters> chapters;

    private RecyclerView listChapterView;
    private ChapterAdapter chapterAdapter;
    private LinearLayoutManager layoutManager;


    private void bindingView(){
        // get Book
        bundle = getIntent().getExtras();
        if(bundle != null){
            book = (Books) bundle.get("book");
        }
        // create list chapter
        chapterAdapter = new ChapterAdapter(this);
        listChapterView = findViewById(R.id.book_chapter_recycler_view);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listChapterView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        listChapterView.addItemDecoration(itemDecoration);
        // set data
        setDataChapters();
        chapterAdapter.setData(chapters);
        listChapterView.setAdapter(chapterAdapter);
        //
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

    }
    private void setDataChapters(){
        chapters = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Chapters chapter = new Chapters();
            chapter.setChapterId(i);
            chapter.setChapterName("Chapter " + i);
            chapters.add(chapter);
        }

    }
}