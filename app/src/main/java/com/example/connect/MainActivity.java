package com.example.connect;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.connect.fragments.BottomNavigationFragments.AssignmentFragment;
import com.example.connect.fragments.BottomNavigationFragments.ProfileFragment;
import com.example.connect.fragments.BottomNavigationFragments.ChatFragment;
import com.example.connect.fragments.BottomNavigationFragments.NotificationFragment;
import com.example.connect.fragments.BottomNavigationFragments.RoomFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    final Fragment fragment1 = new NotificationFragment();
    final Fragment fragment2 = new ChatFragment();
    final Fragment fragment3 = new RoomFragment();
    final Fragment fragment4 = new AssignmentFragment();
    final Fragment fragment5 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();

    Fragment active = fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Rooms);

        fm.beginTransaction().add(R.id.Framelayout, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.Framelayout, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.Framelayout, fragment3, "3").commit();
        fm.beginTransaction().add(R.id.Framelayout, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.Framelayout,fragment1, "1").hide(fragment1).commit();

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
                case R.id.Rooms:
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
//
//    public void changeColorToPurple(){
//        getWindow().setStatusBarColor(this.getColor(R.color.purple_500));
//    }
}


