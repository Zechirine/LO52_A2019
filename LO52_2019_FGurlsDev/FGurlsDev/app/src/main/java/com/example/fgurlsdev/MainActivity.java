package com.example.fgurlsdev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.fgurlsdev.Adapters.TabFragmentAdapter;
import com.example.fgurlsdev.DaoUtils.DaoManager;
import com.example.fgurlsdev.Fragments.RunnerFragment;
import com.example.fgurlsdev.Fragments.GroupFragment;
import com.example.fgurlsdev.Fragments.CompetitionFragment;
import com.example.fgurlsdev.Fragments.StatisticsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;       //ViewPager
    TabLayout mytab;            //Table layout for sub-titles
    List<Fragment> fragmentList = new ArrayList<>();
    String [] mTitles = {"Coureur", "Equipe", "Course", "Statistique"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.mViewPager);
        mytab = findViewById(R.id.mytab);

        fragmentList.add(RunnerFragment.newInstance());
        fragmentList.add(GroupFragment.newInstance());
        fragmentList.add(CompetitionFragment.newInstance());
        fragmentList.add(StatisticsFragment.newInstance());

        initViewPager();
        initTabLayout();
        initDatabase();

        // From other activity to fragment
        int fragmentId = getIntent().getIntExtra("fragmentId", 0);
        switch (fragmentId){
            case 1:
                mViewPager.setCurrentItem(0);
                break;
            case 2:
                mViewPager.setCurrentItem(1);
                break;
            case 3:
                mViewPager.setCurrentItem(2);
                break;
            case 4:
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    //Initialize ViewPager
    public void initViewPager(){
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mTitles, fragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Action when a title is selected
                mytab.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Initialize TabLayout
    public void initTabLayout() {
        //mytab.setTabMode(TabLayout.MODE_SCROLLABLE); //More than 5 titles
        mytab.addTab(mytab.newTab().setText(mTitles[0]));
        mytab.addTab(mytab.newTab().setText(mTitles[1]));
        mytab.addTab(mytab.newTab().setText(mTitles[2]));
        mytab.addTab(mytab.newTab().setText(mTitles[3]));

        mytab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //When tab is selected, tablayout change with viewpage
                mViewPager.setCurrentItem(mytab.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initDatabase() {
        DaoManager daoManager = DaoManager.getInstance();
        daoManager.init(this);
    }
}