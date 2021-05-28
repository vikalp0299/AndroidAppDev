package com.example.connect;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.connect.fragments.AssignmentFragment;
import com.example.connect.fragments.CalendarFragment;
import com.example.connect.fragments.ChatFragment;
import com.example.connect.fragments.NotificationFragment;
import com.example.connect.fragments.TeamsFragment;
import com.example.connect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    final Fragment fragment1 = new NotificationFragment();
    final Fragment fragment2 = new ChatFragment();
    final Fragment fragment3 = new TeamsFragment();
    final Fragment fragment4 = new AssignmentFragment();
    final Fragment fragment5 = new CalendarFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        fm.beginTransaction().add(R.id.Framelayout, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.Framelayout, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.Framelayout, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.Framelayout, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.Framelayout,fragment1, "1").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.notification:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.Chats:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.Teams:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.Assignment:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
                case R.id.Calender:
                    fm.beginTransaction().hide(active).show(fragment5).commit();
                    active = fragment5;
                    return true;
                default:
                    return false;
            }
        });
    }
}


