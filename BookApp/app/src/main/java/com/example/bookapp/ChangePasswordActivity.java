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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordInput, newPasswordInput, reNewPasswordInput;
    private Button changePasswordButton;
    private UserDAO userDAO;
    private String username; // Tên đăng nhập hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordInput = findViewById(R.id.t_oldpassword);
        newPasswordInput = findViewById(R.id.t_newpassword);
        reNewPasswordInput = findViewById(R.id.t_renewpassword);
        changePasswordButton = findViewById(R.id.button_change_password);

        userDAO = BookDatabase.getInstance(this).userDAO();

        // Nhận tên đăng nhập từ Intent
        username = getIntent().getStringExtra("username");

        if (username == null) {
            Toast.makeText(this, "Cannot found username", Toast.LENGTH_SHORT).show();
            finish();
        }

        changePasswordButton.setOnClickListener(v -> changePassword());

        TextView backToHome = findViewById(R.id.textView4);
        backToHome.setOnClickListener(v -> navigateToHome());
    }

    private void changePassword() {
        String oldPassword = oldPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String reNewPassword = reNewPasswordInput.getText().toString().trim();

        if (newPassword.isEmpty() || !newPassword.equals(reNewPassword)) {
            Toast.makeText(this, "New password does not match or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            Users user = userDAO.getUserByUsername(username);

            if (user != null) {
                if (user.getPassword().equals(oldPassword)) {
                    user.setPassword(newPassword);

                    userDAO.updateUser(user);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Change password successfully", Toast.LENGTH_SHORT).show();
                        navigateToHome(); // Navigate to HomeActivity with the username
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Old password incorrect", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Cannot found user", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
        intent.putExtra("username", username); // Pass the username back to HomeActivity
        startActivity(intent);
        finish(); // Optionally finish the current activity
    }
}
