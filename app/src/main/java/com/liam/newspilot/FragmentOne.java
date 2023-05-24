package com.liam.newspilot;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FragmentOne extends Fragment implements APIWrapperCallback {
    public LinearLayout cardContainer;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ProgressBar loadingSpinner;
    private APIHandler apiHandler;
    private View view;

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

        //TODO make this work based on the last query provided
        swipeRefreshLayout.setOnRefreshListener(() -> {
            cardContainer.setVisibility(View.GONE);
            loadingSpinner.setVisibility(View.VISIBLE);
            apiHandler.FetchTopHeadlines(MainActivity.sharedPrefGet.getString("country", "us"));

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

            //TODO instantiate with proper images and texts once we have the api working
            View cardView = inflater.inflate(R.layout.card_item, cardContainer, false);

            ImageView image = cardView.findViewById(R.id.image);
            TextView title = cardView.findViewById(R.id.title);
            TextView description = cardView.findViewById(R.id.description);
            ImageButton heartButton = cardView.findViewById(R.id.heart_button);
            //TODO set tag with proper id (THERE IS NO ID IN THE NEWS!!!)
            cardView.setTag(22);
            heartButton.setTag(22);

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
                Set<String> currentFavourites = new HashSet<>(MainActivity.sharedPrefGet.getStringSet("favourites", new HashSet<>()));
                if(v.isSelected()) {
                    //TODO add with proper id
                    currentFavourites.add(v.getTag().toString());
                    MainActivity.sharedPrefSet.putStringSet("favourites", currentFavourites);
                } else {
                    //TODO remove with proper id
                    currentFavourites.remove(v.getTag().toString());
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

}