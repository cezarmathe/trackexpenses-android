package com.cezarmathe.trackexpenses;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Dashboard extends AppCompatActivity {

    private BottomNavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.add_bottom_navigation_item:
                    return true;
                case R.id.history_bottom_navigation_item:
                    return true;
                case R.id.statistics_bottom_navigation_item:
                    return true;
                default:
                    return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navigationView = findViewById(R.id.bottom_navigation_view);

        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        
    }
}
