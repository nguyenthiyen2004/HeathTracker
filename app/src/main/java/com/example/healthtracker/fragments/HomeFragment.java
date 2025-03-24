package com.example.healthapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthapp.R;
import com.example.healthapp.databinding.ActivityDashboardBinding;
import com.example.healthapp.fragments.ExerciseFragment;
import com.example.healthapp.fragments.HeartRateFragment;
import com.example.healthapp.fragments.HomeFragment;
import com.example.healthapp.fragments.SleepFragment;
import com.example.healthapp.fragments.StepsFragment;
import com.example.healthapp.fragments.WaterFragment;
import com.example.healthapp.services.StepCounterService;
import com.example.healthapp.utils.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityDashboardBinding binding;
    private PreferenceManager preferenceManager;
    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        setSupportActionBar(binding.toolbar);

        // Setup drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        // Set user name in drawer header
        View headerView = binding.navView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.tv_username);
        tvUsername.setText(preferenceManager.getUserName());

        // Setup bottom navigation
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_steps) {
                selectedFragment = new StepsFragment();
            } else if (itemId == R.id.nav_heart) {
                selectedFragment = new HeartRateFragment();
            } else if (itemId == R.id.nav_sleep) {
                selectedFragment = new SleepFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });

        // Load default fragment
        loadFragment(new HomeFragment());

        // Request permissions and start step counter service
        requestPermissionsAndStartService();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (itemId == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (itemId == R.id.nav_water) {
            selectedFragment = new WaterFragment();
        } else if (itemId == R.id.nav_exercise) {
            selectedFragment = new ExerciseFragment();
        } else if (itemId == R.id.nav_logout) {
            logout();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        preferenceManager.setLoggedIn(false);
        preferenceManager.clearAll();
        stopService(new Intent(this, StepCounterService.class));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void requestPermissionsAndStartService() {
        // For step counter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
            } else {
                startStepCounterService();
            }
        } else {
            startStepCounterService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStepCounterService();
            } else {
                Toast.makeText(this, "Cần quyền để theo dõi bước chân", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startStepCounterService() {
        Intent serviceIntent = new Intent(this, StepCounterService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }
}