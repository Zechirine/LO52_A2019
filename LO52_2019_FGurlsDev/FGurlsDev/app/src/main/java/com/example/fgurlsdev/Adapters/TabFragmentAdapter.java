package com.example.fgurlsdev.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private String mTitles[];
    private List<Fragment> fragmentList;

    public TabFragmentAdapter(FragmentManager fm, String mTitles[], List<Fragment> fragmentList){
        super(fm);
        this.mTitles = mTitles;
        this.fragmentList = fragmentList;
    }

    /**
     * return current page of ViewPager
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
         return fragmentList.get(position);
    }

    /**
     * return current size of pages
     * @return
     */
    @Override
    public int getCount() {
        //return fragmentList.size();
        return mTitles.length;
    }

    /**
     * Title's name
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle (int position) {
        return mTitles[position];
    }
}
