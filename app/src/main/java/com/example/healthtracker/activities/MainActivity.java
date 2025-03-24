package com.example.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.example.healthapp.databinding.ActivityMainBinding;
import com.example.healthapp.utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        // Check if user is already logged in
        if (preferenceManager.isLoggedIn()) {
            startDashboardActivity();
            finish();
            return;
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // For demo purposes, any username/password is accepted
                loginUser(username, password);
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In a real app, we would open a registration activity here
                Toast.makeText(MainActivity.this, "Đăng ký chức năng sẽ được thêm sau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String username, String password) {
        // In a real app, this would validate against a backend
        preferenceManager.setLoggedIn(true);
        preferenceManager.saveUserName(username);

        startDashboardActivity();
        finish();
    }

    private void startDashboardActivity() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}