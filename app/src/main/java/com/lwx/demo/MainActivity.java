package com.lwx.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lwx.slidinglayout.SlidingLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingLayout slidingLayout = (SlidingLayout)findViewById(R.id.slidinglayout);
        slidingLayout.setMenuOpenListener(new SlidingLayout.MenuOpenListener() {
            @Override
            public void onMenuOpen(View menu) {

                Toast.makeText(MainActivity.this, "menu open", Toast.LENGTH_SHORT).show();
            }
        });

        slidingLayout.setMenuCloseListener(new SlidingLayout.MenuCloseListener() {
            @Override
            public void onMenuClose(View menu) {

                Toast.makeText(MainActivity.this, "menu close", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
