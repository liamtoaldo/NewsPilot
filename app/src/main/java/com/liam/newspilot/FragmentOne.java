package com.liam.newspilot;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FragmentOne extends Fragment implements APIWrapperCallback {
    public LinearLayout cardContainer;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ProgressBar loadingSpinner;
    private APIHandler apiHandler;
    private View view;
    private final Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);

        //create api handler
        apiHandler = new APIHandler(this);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        loadingSpinner = view.findViewById(R.id.loading_spinner);
        cardContainer = view.findViewById(R.id.card_container);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            cardContainer.setVisibility(View.GONE);
            loadingSpinner.setVisibility(View.VISIBLE);
            if(!MainActivity.lastQuery.equals("")) {
                apiHandler.FetchEverything(MainActivity.lastQuery, MainActivity.sharedPrefGet.getString("language", "en"));
            } else {
                apiHandler.FetchTopHeadlines(MainActivity.sharedPrefGet.getString("country", "us"));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //first query
        apiHandler.FetchTopHeadlines(MainActivity.sharedPrefGet.getString("country", "us"));
    }


    @Override
    public void onAPIWrapperPostExecute(ArrayList<Article> articles) {
        updateCardViews(articles);
        // Hide the spinner
        swipeRefreshLayout.setRefreshing(false);
    }

    //Method to instantiate the cards with the respective articles
    private void updateCardViews(ArrayList<Article> articles) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Remove all current card views
        cardContainer.removeAllViews();

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);

            View cardView = inflater.inflate(R.layout.card_item, cardContainer, false);
            cardView.setOnClickListener(v -> {
                FragmentWebView fragmentWebView = new FragmentWebView();
                Bundle bundle = new Bundle();
                bundle.putString("url", article.url);
                fragmentWebView.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentWebView)
                        .addToBackStack(null)
                        .commit();
            });

            ImageView image = cardView.findViewById(R.id.image);
            TextView title = cardView.findViewById(R.id.title);
            TextView description = cardView.findViewById(R.id.description);
            ImageButton heartButton = cardView.findViewById(R.id.heart_button);

            // Set image, title, and description based on the data
            Glide.with(view.getContext())
                    .load(article.urlToImage)
                    .placeholder(R.drawable.loading) // Optional: Show a placeholder image while the actual image is loading
                    .error(R.drawable.error_image) // Optional: Show an error image if the image fails to load
                    .into(image);

            title.setText(article.title);
            description.setText(article.description);

            // What happens when the hearth in the card item is clicked
            heartButton.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                Set<String> currentFavourites = new LinkedHashSet<>(MainActivity.sharedPrefGet.getStringSet("favourites", new LinkedHashSet<>()));

                //Add datetime of saving for better order in the favourites page
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    article.savedDateTime = System.currentTimeMillis();
                }

                // Save as json to shared preferences
                String articleJsonToSave = gson.toJson(article);

                if(v.isSelected() && !articleExists(currentFavourites, article)) {
                    currentFavourites.add(articleJsonToSave);
                    MainActivity.sharedPrefSet.putStringSet("favourites", currentFavourites);
                } else if(!v.isSelected()){
                    currentFavourites.remove(articleJsonToSave);
                    MainActivity.sharedPrefSet.putStringSet("favourites", currentFavourites);
                }
                MainActivity.sharedPrefSet.apply();
            });
            cardContainer.addView(cardView);
        }
        //hide the loading spinner
        loadingSpinner.setVisibility(View.GONE);
        //show the card container
        cardContainer.setVisibility(View.VISIBLE);
    }

    private boolean articleExists(Set<String> favourites, Article article) {
        boolean res = false;
        for (String json : favourites) {
            Article existingArticle = gson.fromJson(json, Article.class);
            if (existingArticle.equals(article)) {
                res = true;
                break;
            }
        }
        return res;
    }
}