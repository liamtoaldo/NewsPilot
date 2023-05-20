package com.liam.newspilot;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class FragmentThree extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        // Create the menu with all the options for the language
        Spinner spinner = view.findViewById(R.id.dropdown_menu);
        //TODO add icons or replace with proper text (e.g: Italian)
        String[] options = {"ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "sv", "ud", "zh"};
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(view.getContext(), options);
        spinner.setAdapter(customSpinnerAdapter);

        // Change color of the dropdown icon
        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white2), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Class for handling the dropdown items
    public static class CustomSpinnerAdapter extends ArrayAdapter<String> {

        public CustomSpinnerAdapter(Context context, String[] items) {
            super(context, R.layout.selected_dropdown_item, items);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_item, parent, false);
            }
            TextView textView = convertView.findViewById(R.id.spinner_item_text);
            textView.setText(getItem(position));
            return convertView;
        }
    }
}
