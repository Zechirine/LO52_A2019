package com.utbm.keepit.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.utbm.keepit.R;
import com.utbm.keepit.backend.dao.UserDao;
import com.utbm.keepit.ui.home.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int id = getIntent().getIntExtra("id", 0);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

//        DaoSession daoSession = ((MyApp) getApplication()).getDaoSession();
//        userDao = daoSession.getUserDao();

//        System.out.println("添加测试user admin admin");
//        User testu = new User("adminxxx","adminxxx");
//        userDao.insert(testu);
//        System.out.println("插入完毕");
//        List<User> lu = userDao.loadAll();
//        for(User u : lu){
//            System.out.println(u.toString());
//        }
            System.out.println("----------------------------------------------------------------");
            System.out.println(id);
//        if (id == 1) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.nav_host_fragment,new HomeFragment())
//                    .addToBackStack(null)
//                    .commit();
//        }
    }





}
