package com.liam.newspilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

    Toolbar appbar;
    public String language;
    SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change main appbar
        appbar = findViewById(R.id.appbar_included);
        setSupportActionBar(appbar);
        appbar.inflateMenu(R.menu.menu);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(this);

        //Set light theme only
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //load default language saving to shared preferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        language = sharedPref.getString(getString(R.string.language), "it");

        APIHandler api = new APIHandler();
        api.FetchEverything("bitcoin", language);

        // Set the initial fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new FragmentOne()).commit();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.options) {
            //TODO decide if to destroy or keep
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemSelect(int i) {
        Fragment selectedFragment = null;

        switch (i) {
            //home
            case 0:
                selectedFragment = new FragmentOne();
                break;
            //settings
            case 1:
                selectedFragment = new FragmentTwo();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    }

}