package com.example.bookapp;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import Adapter.CategoryAdapter;
import Adapter.ListBookAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAL.BookDatabase;
import DAL.UserDAO;
import Models.BookCategoryCrossRef;
import Models.Books;
import Models.Categories;
import Models.Chapters;
import Models.Pages;
import Models.Users;

import android.util.Log;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    ListView listView;
    ViewFlipper viewFlipper;
    SearchView searchView;
    ListBookAdapter adapter;
    Spinner spnCategory;
    CategoryAdapter categoryAdapter;
    BookDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        db = BookDatabase.getInstance(this);

        home();
        ActionBar();
        ActionViewFlipper();

        recyclerView = findViewById(R.id.book_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // Đặt số cột là 2
        recyclerView.setLayoutManager(gridLayoutManager);
        List<Books> booksList = db.bookDAO().getAllBooks();

        adapter = new ListBookAdapter(booksList, this);
        recyclerView.setAdapter(adapter);

        String username = getIntent().getStringExtra("username");

        // Setup the navigation header with username
        setupNavigationHeader(username);

        insertBooks(db);
        insertAndDeleteCategories(db);
        insertBookCategoryCrossRefs(db);
        insertChapter(db);
        insertPage(db);

        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        

        List<Categories> categoriesList = db.categoryDAO().getAllCategories();
        spnCategory = findViewById(R.id.spn_category);
        List<Categories> cate = new ArrayList<>();
        cate.add(new Categories(0, "All"));
        cate.addAll(categoriesList);
        categoryAdapter = new CategoryAdapter(this, R.layout.item_selected, cate);
        spnCategory.setAdapter(categoryAdapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               Toast.makeText(HomeActivity.this, categoryAdapter.getItem(i).getCategoryName(), Toast.LENGTH_SHORT).show();
                if (i > 0) {
                    Categories selectedCategory = categoriesList.get(i);
                    displayBooksByCategory(selectedCategory.getCategoryId());
                } else {
                    displayAllBooks();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                displayAllBooks();
            }
        });

    }

    private void displayBooksByCategory(int categoryId) {

        List<Books> booksByCategory = db.bookCategoryCrossRefDAO().getBooksByCategory(categoryId);
        adapter.updateBookList(booksByCategory);
    }

    private void displayAllBooks() {
        List<Books> allBooks = db.bookDAO().getAllBooks();
        adapter.updateBookList(allBooks);
    }

    private List<Books> getTopViewBooks(BookDatabase db) {
        List<Books> allBooks = db.bookDAO().getAllBooks();

        allBooks.sort((book1, book2) -> Integer.compare(book2.getViews(), book1.getViews()));
        // Return the top 3
        return allBooks.subList(0, Math.min(allBooks.size(), 3));
    }

    private void ActionViewFlipper() {
        List<Books> topBooks = getTopViewBooks(db);
        viewFlipper.removeAllViews();

        for (Books book : topBooks) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(book.getImage()).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setTag(book);
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



    private void insertAndDeleteCategories(BookDatabase db) {
        String[] categoryNames = {
                "Action", "Shounen", "Adventure", "Fantasy", "Romance",
                "Mystery", "Horror", "Comedy", "Rom-com", "Novel", "Manhwa", "Manhua", "Manga", " Philosophy", "Culture",
                "Education"
        };

        // Thêm các danh mục mới nếu chưa tồn tại
        for (String categoryName : categoryNames) {
            Categories category = db.categoryDAO().getCategoryByName(categoryName);
            if (category == null) {
                db.categoryDAO().insertCategory(new Categories(0, categoryName)); // '0' vì `categoryId` tự động tăng
            }
        }

        Categories novelCategory = db.categoryDAO().getCategoryByName("Tiểu Thuyết");
        Categories comicCategory = db.categoryDAO().getCategoryByName("Truyện Tranh");

        if (novelCategory != null) {
            db.categoryDAO().deleteCategoryById(novelCategory.getCategoryId());
        }

        if (comicCategory != null) {
            db.categoryDAO().deleteCategoryById(comicCategory.getCategoryId());
        }
    }

    private void insertPage(BookDatabase db) {
        // Check if pages already exist for the chapters
        for (int chapterId = 1; chapterId <= 6; chapterId++) {
            List<Pages> existingPages = db.pageDAO().getPagesByChapterId(chapterId);
            if (!existingPages.isEmpty()) {
                // Pages already exist for this chapter, skip inserting pages
                continue;
            }

            // Proceed to insert pages only if they don't already exist
            for (int i = 1; i <= 22; i++) { // Adjust the range according to your chapter page count
                Pages page = new Pages();
                page.setPageNumber(i);
                page.setContent("https://ddntcthcd.com/nettruyen/hoi-phap-su-nhiem-vu-tram-nam/" + chapterId + "/" + i + ".jpg");
                page.setChapterId(chapterId);
                db.pageDAO().insertPage(page);
            }
        }
    }


    private void insertChapter(BookDatabase db) {
        List<Chapters> existingChapters = db.chapterDAO().getChaptersByBookId(9);
        if (!existingChapters.isEmpty()) {
            return;
        }
        Chapters chapter1 = new Chapters();
        chapter1.setChapterName("Chapter 1");
        chapter1.setCreatedDate(new Date());
        chapter1.setBookId(9);

        Chapters chapter2 = new Chapters();
        chapter2.setChapterName("Chapter 2");
        chapter2.setCreatedDate(new Date());
        chapter2.setBookId(9);

        Chapters chapter3 = new Chapters();
        chapter3.setChapterName("Chapter 3");
        chapter3.setCreatedDate(new Date());
        chapter3.setBookId(9);

        Chapters chapter4 = new Chapters();
        chapter4.setChapterName("Chapter 4");
        chapter4.setCreatedDate(new Date());
        chapter4.setBookId(9);

        Chapters chapter5 = new Chapters();
        chapter5.setChapterName("Chapter 5");
        chapter5.setCreatedDate(new Date());
        chapter5.setBookId(9);

        Chapters chapter6 = new Chapters();
        chapter6.setChapterName("Chapter 6");
        chapter6.setCreatedDate(new Date());
        chapter6.setBookId(9);


        db.chapterDAO().insertChapter(chapter1);
        db.chapterDAO().insertChapter(chapter2);
        db.chapterDAO().insertChapter(chapter3);
        db.chapterDAO().insertChapter(chapter4);
        db.chapterDAO().insertChapter(chapter5);
        db.chapterDAO().insertChapter(chapter6);
    }
    private void insertBookCategoryCrossRefs(BookDatabase db) {
        // One piece categories
        Categories actionCategory = db.categoryDAO().getCategoryById(1);
        Categories shounenCategory = db.categoryDAO().getCategoryById(2);
        Categories adventureCategory = db.categoryDAO().getCategoryById(3);
        Categories fantasyCategory = db.categoryDAO().getCategoryById(4);
        Categories romanceCategory = db.categoryDAO().getCategoryById(5);
        Categories mysteryCategory = db.categoryDAO().getCategoryById(6);
        Categories horrorCategory = db.categoryDAO().getCategoryById(7);
        Categories comedyCategory = db.categoryDAO().getCategoryById(8);
        Categories romcomCategory = db.categoryDAO().getCategoryById(9);
        Categories novelCategory = db.categoryDAO().getCategoryById(10);
        Categories manhwaCategory = db.categoryDAO().getCategoryById(11);
        Categories manhuaCategory = db.categoryDAO().getCategoryById(12);
        Categories mangaCategory = db.categoryDAO().getCategoryById(13);
        Categories philosophyCategory = db.categoryDAO().getCategoryById(14);
        Categories cultureCategory = db.categoryDAO().getCategoryById(15);
        Categories educationCategory = db.categoryDAO().getCategoryById(16);
        Books onePiece = db.bookDAO().getBookById(4);
        if (onePiece != null) {
            insertCrossRef(db, onePiece, actionCategory);
            insertCrossRef(db, onePiece, shounenCategory);
            insertCrossRef(db, onePiece, adventureCategory);
            insertCrossRef(db, onePiece, fantasyCategory);
            insertCrossRef(db, onePiece, mangaCategory);
        }

        // Naruto categories
        Books naruto = db.bookDAO().getBookById(5);
        if (naruto != null) {
            insertCrossRef(db, naruto, actionCategory);
            insertCrossRef(db, naruto, shounenCategory);
            insertCrossRef(db, naruto, adventureCategory);
            insertCrossRef(db, naruto, fantasyCategory);
            insertCrossRef(db, naruto, mangaCategory);
        }

        // Kafka bên bờ biển categories
        Books kafka = db.bookDAO().getBookById(2);
        if (kafka != null) {
            insertCrossRef(db, kafka, novelCategory);
        }

        // Sự im lặng của bầy cừu categories
        Books silenceOfTheLambs = db.bookDAO().getBookById(3);
        if (silenceOfTheLambs != null) {
            insertCrossRef(db, silenceOfTheLambs, novelCategory);
        }

        // Fairy Tail categories
        Books fairyTail = db.bookDAO().getBookById(6);
        if (fairyTail != null) {
            insertCrossRef(db, fairyTail, actionCategory);
            insertCrossRef(db, fairyTail, shounenCategory);
            insertCrossRef(db, fairyTail, adventureCategory);
            insertCrossRef(db, fairyTail, fantasyCategory);
            insertCrossRef(db, fairyTail, mangaCategory);
        }

        // Thiên sứ nhà bên categories
        Books angelNextDoor = db.bookDAO().getBookById(7);
        if (angelNextDoor != null) {
            insertCrossRef(db, angelNextDoor, novelCategory);
            insertCrossRef(db, angelNextDoor, romanceCategory);
            insertCrossRef(db, angelNextDoor, romcomCategory);;
        }

        // Horimiya categories
        Books horimiya = db.bookDAO().getBookById(8);
        if (horimiya != null) {
            insertCrossRef(db, horimiya, novelCategory);
            insertCrossRef(db, horimiya, romanceCategory);
            insertCrossRef(db, horimiya, romcomCategory);
            insertCrossRef(db, horimiya, comedyCategory);
        }

        // FairyTail100 categories
        Books fairyTail100 = db.bookDAO().getBookById(9);
        if (fairyTail100 != null) {
            insertCrossRef(db, fairyTail100, mangaCategory);
            insertCrossRef(db, fairyTail100, adventureCategory);
            insertCrossRef(db, fairyTail100, actionCategory);
            insertCrossRef(db, fairyTail100, shounenCategory);
            insertCrossRef(db, fairyTail100, fantasyCategory);

        }

        Books maclenin = db.bookDAO().getBookById(10);
        if (maclenin != null) {
            insertCrossRef(db, maclenin, philosophyCategory);
            insertCrossRef(db, maclenin, cultureCategory);

        }


    }

    private void insertCrossRef(BookDatabase db, Books book, Categories category) {
        if (category != null && book != null) {
            if (db.bookCategoryCrossRefDAO().getCrossRefByBookAndCategory(book.getBookId(), category.getCategoryId()) == null) {
                db.bookCategoryCrossRefDAO().insertBookCategoryCrossRef(new BookCategoryCrossRef(category.getCategoryId(),book.getBookId()));
            }
        }
    }

    private void insertBooks(BookDatabase db) {
        List<Books> booksList = new ArrayList<>();

        booksList.add(new Books(1, "Rừng Na Uy", "Haruki Murakami",
                "https://upload.wikimedia.org/wikipedia/vi/thumb/2/28/Norwegian-wood_poster.jpg/330px-Norwegian-wood_poster.jpg",
                2, "Câu chuyện bắt đầu từ một chuyến bay trong ngày đông...",
                100, 1));

        booksList.add(new Books(2, "Kafka bên bờ biển", "Haruki Murakami",
                "https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_32831.jpg",
                2, "Kafka bên bờ biển có hai câu chuyện song song...",
                79, 1));

        booksList.add(new Books(3, "Sự im lặng của bầy cừu", "Thomas Harris",
                "https://upload.wikimedia.org/wikipedia/vi/8/86/The_Silence_of_the_Lambs_poster.jpg",
                2, "Những cuộc phỏng vấn ở xà lim với kẻ ăn thịt người Hannibal Lecter...",
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
                1, " Fujimiya Amane sống một mình trong căn hộ của mình, và cô gái xinh nhất ở trường cậu...",
                111, 1));
        booksList.add(new Books(8, "Horimiya", "Hagiwara Daisuke",
                "https://translate.google.com/website?sl=en&tl=vi&hl=vi&client=srp&u=https://upload.wikimedia.org/wikipedia/en/4/46/Hori-san_to_Miyamura-kun_volume_1_cover.jpg",
                1, " Câu chuyện chủ yếu xoay quanh hai học sinh trung học: Kyouko Hori, một học sinh thông minh và nổi tiếng và Izumi Miyamura...",
                222, 1));
        booksList.add(new Books(9, "Fairy Tail: Nhiệm vụ trăm năm", "Hiro Mashima",
                "https://upload.wikimedia.org/wikipedia/en/e/e8/Fairy_Tail_100_Years_Quest%2C_Vol_1.jpg",
                1, "Tuyện tiếp nối chap 545 của Fairy Tail, khi nhóm Natsu đi làm nhiệm vụ trăm năm.\n" +
                "Đây chỉ là một phần nhỏ trong hành trình của Hội Pháp Sư Nhiệm Vụ Trăm Năm – câu chuyện còn ẩn chứa nhiều bất ngờ và kịch tính hơn nữa, chắc chắn sẽ khiến bạn không thể rời mắt.",
                321, 1));
        booksList.add(new Books(10, "Giáo trình kinh tế chính trị Mác-Lênin", "PGS.TS. Ngô Tuấn Nghĩa (Chủ biên)",
                "https://images.sachquocgia.vn/Picture/2024/3/21/image-20240321140730843.jpg",
                1, "Nội dung giáo trình gồm 6 chương: - Chương 1: Đối tượng, phương pháp nghiên cứu và chức năng của kinh tế chính trị Mác - Lênin; - Chương 2: Hàng hóa, thị trường và vai trò của các chủ thể tham gia thị trường; - Chương 3: Giá trị thặng dư trong nền kinh tế thị trường; - Chương 4: Cạnh tranh và độc quyền trong nền kinh tế thị trường; - Chương 5: Kinh tế thị trường định hướng xã hội chủ nghĩa và các quan hệ lợi ích kinh tế ở Việt Nam; - Chương 6: Công nghiệp hóa, hiện đại hoá và hội nhập kinh tế quốc tế của Việt Nam. Bên cạnh đó, cuối mỗi chương các tác giả tóm tắt lại nội dung của chương và đưa ra các thuật ngữ cần ghi nhớ, vấn đề thảo luận, câu hỏi ôn tập, giúp sinh viên nắm chắc và vận dụng các kiến thức đã học.",
                222, 1));
        for (Books book : booksList) {
            Books existingBook = db.bookDAO().getBookByNameAndAuthor(book.getBookName(), book.getBookAuthor());
            if (existingBook == null) {
                db.bookDAO().insertBook(book);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            String username = getIntent().getStringExtra("username");
            intent.putExtra("username", username);
            startActivity(intent);
        } else if (id == R.id.nav_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            String username = getIntent().getStringExtra("username");
            intent.putExtra("username", username);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @OptIn(markerClass = UnstableApi.class)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
            searchView.setSearchableInfo(searchableInfo);
        } else {
            androidx.media3.common.util.Log.e("HomeActivity", "SearchView is null");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.top_view) {
            // Navigate to TopViewActivity when "Top View" is clicked
            Intent intent = new Intent(this, TopViewActivity.class);
            String username = getIntent().getStringExtra("username");
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationHeader(String username) {
        // Inflate the navigation header view
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.head_username); // Make sure this ID matches your layout

        // Set the username to the TextView
        if (usernameTextView != null && username != null) {
            usernameTextView.setText(username);
        }
    }
}