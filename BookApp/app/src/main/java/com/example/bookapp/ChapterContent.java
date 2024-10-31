package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.LinearLayout;

import java.util.List;

import Adapter.ChapterAdapter;
import Adapter.PageContentAdapter;
import DAL.BookDatabase;
import Models.Books;
import Models.Chapters;
import Models.Pages;

public class ChapterContent extends AppCompatActivity {

    private TextView textContent;
    private LinearLayout toolbox;
    private ImageButton buttonNextChapter;
    private ImageButton buttonPrevChapter;
    private ImageButton buttonTableOfContent;
    private boolean isToolboxVisible = false;
    private GestureDetector gestureDetector;
    private ImageButton backButton;
    // present chapter
    private Chapters chapter;
    // list chapter
    private List<Chapters> chapters;
    private DrawerLayout drawerLayout;
    private RecyclerView listChapterView;
    private ChapterAdapter chapterAdapter;
    private LinearLayoutManager layoutManager;
    // get bundle
    private Bundle bundle;
    // db
    private BookDatabase db;
    // listPage
    private List<Pages> pages;
    private PageContentAdapter pageAdapter;
    private RecyclerView listPageView;
    private int currentChapter = -1;
    private void setDataPages(){
        pages = db.pageDAO().getPagesByChapterId(chapter.getChapterId());

    }
    private void setDataChapters(){
        chapters = db.chapterDAO().getChaptersByBookId(chapter.getBookId());

    }
    private void setGesture(){
        // tao gesture
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggleToolbox(); // Gọi hàm toggleToolbox khi nhấn
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Để cho phép cuộn, trả về false
                return false;
            }
        });
    }

    private void bindingView(){
        // Khai báo và gán LinearLayout bằng findViewById
        toolbox = findViewById(R.id.toolbox);
        // db
        db = BookDatabase.getInstance(this);
        // get bundle
        bundle = getIntent().getExtras();
        if(bundle != null){
            chapter = (Chapters) bundle.get("chapter");
        } else {
            return;
        }
        // create chapter's list data
        setDataChapters();
        for(int i =0; i < chapters.size(); i++){
            if(chapter.getChapterId() == chapters.get(i).getChapterId()){
                currentChapter = i;
                break;
            }
        }

        // TextView và ScrollView trong layout
        textContent = findViewById(R.id.tv_chapterUpdated);
        // binding button
        buttonNextChapter = findViewById(R.id.buttonNextChapter);
         buttonPrevChapter = findViewById(R.id.buttonPrevChapter);
        buttonTableOfContent = findViewById(R.id.buttonTableOfContents);
        backButton = findViewById(R.id.btn_back);
        // create list chapter
        drawerLayout = findViewById(R.id.chapter_drawer_layout);

        listChapterView = findViewById(R.id.chapter_recycler_view);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);


        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        listChapterView.addItemDecoration(itemDecoration);
        setChapterLayout();


        // binding page
        listPageView = findViewById(R.id.pages_recycler_view);
        listPageView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        // create pages data
        setDataPages();

        pageAdapter = new PageContentAdapter(this, pages.get(0).getContent().startsWith("http") || pages.get(0).getContent().startsWith("content"));
        pageAdapter.setData(pages);
        listPageView.setAdapter(pageAdapter);


    }
    private void setChapterLayout(){
        chapterAdapter = new ChapterAdapter(this);
        chapterAdapter.setCurrentIndex(currentChapter);
        listChapterView.setLayoutManager(layoutManager);
        chapterAdapter.setData(chapters);
        listChapterView.setAdapter(chapterAdapter);
    }
    private void bindingAction(){
        buttonNextChapter.setOnClickListener(this::btnNextOnClick);
        buttonPrevChapter.setOnClickListener(this::btnPrevOnClick);
        buttonTableOfContent.setOnClickListener(this::btnTableOfContent);
        backButton.setOnClickListener(this::onBackPressed);
        listPageView.setOnTouchListener(this::toggleToolbox);
        // Đóng Drawer khi chạm ra ngoài
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void onBackPressed(View view) {
        Intent intent = new Intent(this, BookDetail.class);
        Bundle bundle = new Bundle();
        Books b = db.bookDAO().getBookById(chapter.getBookId());
        bundle.putSerializable("book", b);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private boolean toggleToolbox(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private void updateChapterContent() {
        textContent.setText(chapter.getChapterName());
        setDataPages();
        chapterAdapter.setCurrentIndex(currentChapter);
        scrollToItemChapter(currentChapter);
        chapterAdapter.notifyDataSetChanged();
        db.bookDAO().updateViews(chapter.getBookId());
        pageAdapter.setData(pages);
        hideToolbox();
    }


    private void btnPrevOnClick(View view) {
        if (currentChapter <= 0) {
            buttonPrevChapter.setEnabled(false);
            return;
        }
        currentChapter--;

        chapter = chapters.get(currentChapter);
        updateChapterContent();
        listPageView.scrollToPosition(0); // Cuộn lên đầu danh sách trang

        buttonNextChapter.setEnabled(true);

        if (currentChapter <= 0) {
            buttonPrevChapter.setEnabled(false);
        }
    }


    private void btnNextOnClick(View view) {
        if (currentChapter >= chapters.size() - 1) {
            buttonNextChapter.setEnabled(false);
            return;
        }
        currentChapter++;

        chapter = chapters.get(currentChapter);
        updateChapterContent();
        listPageView.scrollToPosition(0);

        buttonPrevChapter.setEnabled(true);

        if (currentChapter >= chapters.size() - 1) {
            buttonNextChapter.setEnabled(false);
        }
    }


    private void toggleToolbox() {

        if (isToolboxVisible) {
            hideToolbox(); // Ẩn toolbox với hiệu ứng
        } else {
            showToolbox(); // Hiện toolbox với hiệu ứng
        }
        isToolboxVisible = !isToolboxVisible;
    }

    private void btnTableOfContent(View view) {
        hideToolbox();

        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START); // open chapter's list
            if(chapter != null){
                scrollToItemChapter(currentChapter);
            }

        } else {
            drawerLayout.closeDrawer(GravityCompat.START); // close chapter's list
        }
    }

    @Override
    public void onBackPressed() {
        // Đóng Drawer nếu đang mở khi nhấn nút Back
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        setGesture();
        bindingAction();
        db.bookDAO().updateViews(chapter.getBookId());

        if(chapter == null){
            return;
        }

        textContent.setText(chapter.getChapterName());
    }
    private void scrollToItemChapter(int index){
        if(layoutManager == null){
            return;
        }
        layoutManager.scrollToPositionWithOffset(index, 0);
    }
    // Hiện toolbox với hiệu ứng
    private void showToolbox() {
        toolbox.setVisibility(View.VISIBLE); // Đầu tiên đặt toolbox thành visible
        Animation fadeIn = new AlphaAnimation(0, 1); // Hiệu ứng fade in
        fadeIn.setDuration(300); // Thời gian hiệu ứng
        toolbox.startAnimation(fadeIn); // Bắt đầu hiệu ứng
    }

    // Ẩn toolbox với hiệu ứng
    private void hideToolbox() {
        Animation fadeOut = new AlphaAnimation(1, 0); // Hiệu ứng fade out
        fadeOut.setDuration(300); // Thời gian hiệu ứng
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Không cần làm gì
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toolbox.setVisibility(View.GONE); // Đặt toolbox thành gone sau khi hiệu ứng kết thúc
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Không cần làm gì
            }
        });
        toolbox.startAnimation(fadeOut); // Bắt đầu hiệu ứng
    }
}