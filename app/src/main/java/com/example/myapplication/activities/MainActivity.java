package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.services.ExampleService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.myapplication.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R. id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(MainActivity.this, ExampleService.class);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

             startActivity(new Intent(MainActivity.this, AddElementActivity.class));
            }
        });
    }
}