package com.liam.newspilot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class OptionsActivity extends AppCompatActivity {

    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Change main appbar
        appbar = findViewById(R.id.appbar_included);
        appbar.setTitle("NewsPilot - Options");
        setSupportActionBar(appbar);

        //Set light theme only
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_down_up, R.transition.slide_down_up);
    }


}