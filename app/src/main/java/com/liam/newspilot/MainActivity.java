package com.liam.newspilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

    public static SharedPreferences sharedPrefGet;
    public static SharedPreferences.Editor sharedPrefSet;
    public static String lastQuery = "";
    Toolbar appbar;
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
        sharedPrefGet = this.getPreferences(Context.MODE_PRIVATE);
        sharedPrefSet = sharedPrefGet.edit();

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
                //Reset the query
                lastQuery = "";
                return true;
            }
        };

            menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setQueryHint("Search topic...");

            // Set the query text listener
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.equals("")) {
                        return false;
                    }
                    lastQuery = query;
                    String language = sharedPrefGet.getString(getString(R.string.language), "en");

                    // Handle the submit button here
                    FragmentOne currentFragment = (FragmentOne) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (currentFragment != null) {
                        // Call the FetchEverything method of APIHandler to fetch news articles based on the query
                        APIHandler api = new APIHandler(currentFragment);
                        api.FetchEverything(query, language);
                        currentFragment.loadingSpinner.setVisibility(View.VISIBLE);
                        currentFragment.cardContainer.setVisibility(View.GONE);
                    }

                    //Hide the keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    View currentFocusedView = getCurrentFocus();
                    if (currentFocusedView != null) {
                        inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Handle the text change here
                    // For example, update the search suggestions based on the new text
                    return false;
                }
            });

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Hide search menu if it's not fragment one (home)
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof FragmentOne) {
            menu.findItem(R.id.search).setVisible(true);
        } else {
            menu.findItem(R.id.search).setVisible(false);
        }
        return true;
    }
    //Function that executes when we select a different item on the bottom nav bar
    @Override
    public boolean onItemSelect(int i) {
        Fragment selectedFragment = null;
        switch (i) {
            //home
            case 0:
                selectedFragment = new FragmentOne();
                break;
            //favorite
            case 1:
                selectedFragment = new FragmentTwo();
                break;
            //settings
            case 2:
                selectedFragment = new FragmentThree();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commitNow();
        invalidateOptionsMenu();
        return true;
    }

}