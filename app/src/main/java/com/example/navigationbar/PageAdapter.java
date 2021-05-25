package com.example.navigationbar;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.navigationbar.fragments.AssignmentFragment;
import com.example.navigationbar.fragments.FilesFragment;
import com.example.navigationbar.fragments.PostsFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PostsFragment();
            case 1:
                return new FilesFragment();
            case 2:
                return new AssignmentFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

