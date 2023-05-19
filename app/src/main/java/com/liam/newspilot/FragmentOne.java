package com.liam.newspilot;

import android.os.Bundle;
import android.os.Handler;
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

import java.util.HashSet;
import java.util.Set;

public class FragmentOne extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loadingSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        loadingSpinner = view.findViewById(R.id.loading_spinner);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Perform the desired action here
            //TODO api call

            // Hide the spinner
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout cardContainer = view.findViewById(R.id.card_container);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < 10; i++) {
            //TODO instantiate with proper images and texts once we have the api working
            View cardView = inflater.inflate(R.layout.card_item, cardContainer, false);

            ImageView image = cardView.findViewById(R.id.image);
            TextView title = cardView.findViewById(R.id.title);
            TextView description = cardView.findViewById(R.id.description);
            ImageButton heartButton = cardView.findViewById(R.id.heart_button);
            //TODO set tag with proper id
            cardView.setTag(22);
            heartButton.setTag(22);

            // Set image, title, and description based on the data
            // image.setImageResource(...);
            title.setText("Title " + i);
            description.setText("Description " + i);

            // What happens when the hearth in the card item is clicked
            heartButton.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                Set<String> currentFavourites = new HashSet<>(MainActivity.sharedPrefGet.getStringSet("favourites", new HashSet<String>()));
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
    }

}