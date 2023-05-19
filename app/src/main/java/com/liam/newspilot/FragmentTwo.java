package com.liam.newspilot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.util.ArraySet;
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
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import java.util.HashSet;
import java.util.Set;

public class FragmentTwo extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loadingSpinner;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        loadingSpinner = view.findViewById(R.id.loading_spinner_fav);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_fav);
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

        LinearLayout cardContainer = view.findViewById(R.id.card_container_fav);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Get the favourite news saved in the device
        Set<String> favouriteIds = MainActivity.sharedPrefGet.getStringSet("favourites", new HashSet<>());

        for (int i = 0; i < favouriteIds.size(); i++) {
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
                Set<String> currentFavourites = new HashSet<>(favouriteIds);
                if(v.isSelected()) {
                    //TODO add with proper id
                    currentFavourites.add(v.getTag().toString());
                    MainActivity.sharedPrefSet.putStringSet("favourites", currentFavourites);
                } else {
                    //TODO remove with proper id
                    currentFavourites.remove(v.getTag().toString());
                    MainActivity.sharedPrefSet.putStringSet("favourites", currentFavourites);

                    // Animation when deleting favourite
                    TransitionManager.beginDelayedTransition(cardContainer, new ChangeBounds());
                    // Get the parent of the parent of the parent and remove it from the card container (linear layout)
                    View parent = (View)v.getParent().getParent().getParent();

                    // Create the "explode" animation
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
                    PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
                    ObjectAnimator explodeAnimator = ObjectAnimator.ofPropertyValuesHolder(parent, scaleX, scaleY, alpha);
                    explodeAnimator.setDuration(400);

                    // Remove the parent view from the cardContainer once the animation is complete
                    explodeAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            cardContainer.removeView(parent);
                        }
                    });

                    // Start the animation
                    explodeAnimator.start();
                }
                MainActivity.sharedPrefSet.apply();
            });
            //Put the red heart for the ones in the favourites page
            heartButton.setSelected(true);
            cardContainer.addView(cardView);
        }
        //hide the loading spinner
        loadingSpinner.setVisibility(View.GONE);
    }

}
