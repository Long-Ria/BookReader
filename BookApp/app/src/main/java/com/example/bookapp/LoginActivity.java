package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

import DAL.BookDatabase;
import DAL.UserDAO;
import Models.Users;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private UserDAO userDAO;
    BookDatabase dbBookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Using activity_login.xml layout

        usernameInput = findViewById(R.id.editTextTextPersonName);
        passwordInput = findViewById(R.id.editTextTextPersonName2);
        loginButton = findViewById(R.id.button_login);

        userDAO = dbBookDatabase.getInstance(this).userDAO();

        loginButton.setOnClickListener(v -> authenticateUser());

        // Inside onCreate method of LoginActivity
        TextView signUpText = findViewById(R.id.textView4);
        signUpText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

    }

    private void authenticateUser() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        Executors.newSingleThreadExecutor().execute(() -> {
            Users user = userDAO.getUserByUsername(username);
            runOnUiThread(() -> {
                if (user != null && user.getPassword().equals(password)) {
                    Intent intent;
                    if (user.getRole() == 2) {  // Giả sử vai trò 0 = người dùng
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                    } else {  // vai trò 1 = admin
                        intent = new Intent(LoginActivity.this, AdminActivity.class);
                    }
                    // Truyền tên đăng nhập sang ChangePasswordActivity nếu cần
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Login fail", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}
