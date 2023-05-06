package com.liam.newspilot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change main appbar
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        appbar.inflateMenu(R.menu.menu);

        //Set light theme only
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        APIHandler api = new APIHandler();
        api.FetchEverything("bitcoin", "en");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Put menu on the bar
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //Create listener for search bar
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search topic...");

        return true;
    }
}