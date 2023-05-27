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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
            //TODO

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
        Set<String> favourites = MainActivity.sharedPrefGet.getStringSet("favourites", new LinkedHashSet<>());

        //Treat the hashset as an array, so that we can keep the order.
        ArrayList<Article> articles = new ArrayList<>();
        //Gson deserializer
        Gson gson = new Gson();

        for(String json: favourites) {
            //Deserialize the object
            Article article;
            try {
                article = gson.fromJson(json, Article.class);
                articles.add(article);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), "Error deserializing a news: " + e, Toast.LENGTH_SHORT).show();
            }
        }
        Comparator<Article> comparator = (a, b) -> Long.compare(b.savedDateTime, a.savedDateTime);
        articles.sort(comparator);
        for (Article article: articles) {
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

            // What happens when the heart in the card item is clicked
            heartButton.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                Set<String> currentFavourites = new LinkedHashSet<>(favourites);

                if (!v.isSelected()) {
                    //Remove the item from the favourites saved set
                    currentFavourites.remove(gson.toJson(article));
                    MainActivity.sharedPrefSet.putStringSet("favourites", currentFavourites);

                    // Animation when deleting favourite
                    TransitionManager.beginDelayedTransition(cardContainer, new ChangeBounds());
                    // Get the parent of the parent of the parent and remove it from the card container (linear layout)
                    View parent = (View) v.getParent().getParent().getParent();

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
