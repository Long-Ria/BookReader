package com.example.bookapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ScrollView;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.view.Gravity;

import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.ChapterAdapter;
import Adapter.PageContentAdapter;
import Models.Chapters;
import Models.Pages;

public class ChapterContent extends AppCompatActivity {

    private TextView textContent;
    private LinearLayout toolbox;
    private ScrollView scrollView;
    private ImageButton buttonNextChapter;
    private ImageButton buttonPrevChapter;
    private ImageButton buttonTableOfContent;
    private boolean isToolboxVisible = false;
    private GestureDetector gestureDetector;
    // present chapter
    private Chapters chapter;
    // list chapter
    private ArrayList<Chapters> chapters;
    private DrawerLayout drawerLayout;
    private RecyclerView listChapterView;
    private ChapterAdapter chapterAdapter;
    private LinearLayoutManager layoutManager;
    // get bundle
    private Bundle bundle;
    // listPage
    private ArrayList<Pages> pages;
    private PageContentAdapter pageAdapter;
    private RecyclerView listPageView;
    private void setDataPages(){
        pages = new ArrayList<>();
        for (int i = 1; i <= 27; i++) {
            Pages page = new Pages();
            page.setPageId(i);
            page.setContent("https://i.pinimg.com/564x/e2/af/4a/e2af4a0596971973bf8d0041bdc28f73.jpg");
            page.setPageNumber(i);
            pages.add(page);
        }

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

        // TextView và ScrollView trong layout
        textContent = findViewById(R.id.textContent);
        scrollView = findViewById(R.id.scrollView);
        // binding button
        buttonNextChapter = findViewById(R.id.buttonNextChapter);
         buttonPrevChapter = findViewById(R.id.buttonPrevChapter);
        buttonTableOfContent = findViewById(R.id.buttonTableOfContents);
        // create list chapter
        drawerLayout = findViewById(R.id.chapter_drawer_layout);
        chapterAdapter = new ChapterAdapter(this);
        listChapterView = findViewById(R.id.chapter_recycler_view);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listChapterView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        listChapterView.addItemDecoration(itemDecoration);

        // create chapter's list data
        setDataChapters();
        chapterAdapter.setData(chapters);
        listChapterView.setAdapter(chapterAdapter);
        // binding page
        listPageView = findViewById(R.id.pages_recycler_view);
        listPageView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        pageAdapter = new PageContentAdapter(this);
        // create pages data
        setDataPages();
        pageAdapter.setData(pages);
        listPageView.setAdapter(pageAdapter);

        // get bundle
        bundle = getIntent().getExtras();
        if(bundle != null){
            chapter = (Chapters) bundle.get("chapter");
        }

    }

    private void bindingAction(){
        buttonNextChapter.setOnClickListener(this::btnNextOnClick);
        buttonPrevChapter.setOnClickListener(this::btnPrevOnClick);
        buttonTableOfContent.setOnClickListener(this::btnTableOfContent);
        scrollView.setOnTouchListener(this::toggleToolbox);
        listPageView.setOnTouchListener(this::toggleToolbox);
        // Đóng Drawer khi chạm ra ngoài
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private boolean toggleToolbox(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
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
        Toast.makeText(this, "Muc luc", Toast.LENGTH_SHORT).show();
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START); // open chapter's list
            if(chapter != null){
                scrollToItemChapter(chapter.getChapterId()-1);
            }

        } else {
            drawerLayout.closeDrawer(GravityCompat.START); // close chapter's list
        }
    }
    private void btnPrevOnClick(View view) {
        Toast.makeText(this, "Next Chapter", Toast.LENGTH_SHORT).show();

    }

    private void btnNextOnClick(View view) {
        Toast.makeText(this, "Next Chapter", Toast.LENGTH_SHORT).show();

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