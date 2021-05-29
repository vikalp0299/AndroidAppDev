package com.example.connect.adapters.RoomSectionAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.connect.fragments.RoomActivityFragments.FilesFragment;
import com.example.connect.fragments.RoomActivityFragments.MembersFragment;
import com.example.connect.fragments.RoomActivityFragments.PostsFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private final int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PostsFragment();
            case 1:
                return new FilesFragment();
            case 2:
                return new MembersFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

