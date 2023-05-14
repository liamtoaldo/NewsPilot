package com.liam.newspilot;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FragmentOne extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
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
            View cardView = inflater.inflate(R.layout.card_item, cardContainer, false);

            ImageView image = cardView.findViewById(R.id.image);
            TextView title = cardView.findViewById(R.id.title);
            TextView description = cardView.findViewById(R.id.description);

            // Set image, title, and description based on your data
            // image.setImageResource(...);
            title.setText("Title " + i);
            description.setText("Description " + i);

            cardContainer.addView(cardView);
        }
    }

}