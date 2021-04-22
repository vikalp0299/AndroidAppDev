package com.example.navigationbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceControl;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.navigationbar.fragments.AssignmentFragment;
import com.example.navigationbar.fragments.CalendarFragment;
import com.example.navigationbar.fragments.ChatFragment;
import com.example.navigationbar.fragments.NotificationFragment;
import com.example.navigationbar.fragments.TeamsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notification:
                        showFragment(NotificationFragment.newInstance("",""));
                        return true;
                    case R.id.Chats:
                        showFragment(ChatFragment.newInstance("",""));
                        return true;
                    case R.id.Teams:
                        showFragment(TeamsFragment.newInstance("",""));
                        return true;
                    case R.id.Assignment:
                        showFragment(AssignmentFragment.newInstance("",""));
                        return true;
                    case R.id.Calender:
                        showFragment(CalendarFragment.newInstance("",""));
                        return true;

                    default:
                        return false;
                }
            }
        });
        showFragment(NotificationFragment.newInstance("",""));
    }

    private void makeText(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Framelayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


