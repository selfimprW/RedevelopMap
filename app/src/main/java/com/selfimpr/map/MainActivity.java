package com.selfimpr.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RoutePlanFragment routePlanFragment;
    private EmptyFragment emptyFragment;
    private MapFragment mapFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routePlanFragment = new RoutePlanFragment();
        mapFragment = new MapFragment();
        emptyFragment = new EmptyFragment();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        switchFragment(routePlanFragment);
    }

    private void switchFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(routePlanFragment);
                    return true;
                case R.id.navigation_dashboard:
                    switchFragment(mapFragment);
                    return true;
                case R.id.navigation_notifications:
                    switchFragment(emptyFragment);
                    return true;
            }
            return false;
        }

    };
}
