package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;
import java.util.concurrent.Executors;

import DAL.BookDatabase;
import DAL.UserDAO;
import Models.Users;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput, rePasswordInput, emailInput, phoneNumberInput;
    private Button signUpButton;
    private UserDAO userDAO;
    BookDatabase dbBookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize EditText fields
        usernameInput = findViewById(R.id.t_username);
        passwordInput = findViewById(R.id.t_password);
        rePasswordInput = findViewById(R.id.t_repassword);

        signUpButton = findViewById(R.id.button_login);  //  sign-up button

        userDAO = dbBookDatabase.getInstance(this).userDAO();

        // Set up sign-up button click listener
        signUpButton.setOnClickListener(v -> registerUser());

        TextView signInText = findViewById(R.id.textView4);
        signInText.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String rePassword = rePasswordInput.getText().toString().trim();


        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty() ||
                password.contains(" ") || rePassword.contains(" ")) {
            Toast.makeText(this, "Username and passwords cannot be empty or contain spaces", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            // Check if the username already exists
            Users existingUser = userDAO.getUserByUsername(username);

            if (existingUser != null) {
                // If user already exists, show a message
                runOnUiThread(() -> Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show());
            } else {
                // Proceed with registration if username is unique
                Users newUser = new Users();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setEmail("");  // Update as needed if other fields are added
                newUser.setPhoneNumber("");
                newUser.setCreatedDate(new Date());
                newUser.setRole(2);  // Default role for new user

                userDAO.insertUser(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                });
            }
        });
    }



}
