package com.liam.newspilot;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

public class FragmentThree extends Fragment implements AdapterView.OnItemSelectedListener  {
    private final String[] languages = {"🇦🇪 Arabic", "🇩🇪 German", "🇬🇧 English", "🇪🇸 Spanish", "🇫🇷 French", "🇮🇱 Hebrew", "🇮🇹 Italian", "🇳🇱 Dutch", "🇳🇴 Norwegian", "🇵🇹 Portuguese", "🇷🇺 Russian", "🇸🇪 Swedish", "🇵🇰 Urdu", "🇨🇳 Chinese"};
    //TODO fill these
    private final String[] countries = {};
    private final String[] options = {"ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "sv", "ud", "zh"};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        // Create the menu with all the options for the language
        Spinner spinner = view.findViewById(R.id.lang_menu);
        //TODO add icons or replace with proper text (e.g: Italian)
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(view.getContext(), languages);
        spinner.setAdapter(customSpinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        // Change color of the dropdown icon
        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white2), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);

        //Load the language
        String savedlang = MainActivity.sharedPrefGet.getString("language", "it");
        spinner.setSelection(Arrays.asList(options).indexOf(savedlang));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Function that gets executed when a language is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.sharedPrefSet.putString("language", options[position]);
        MainActivity.sharedPrefSet.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // Class for inflating the dropdown items
    public static class CustomSpinnerAdapter extends ArrayAdapter<String> {

        public CustomSpinnerAdapter(Context context, String[] items) {
            super(context, R.layout.selected_dropdown_item, items);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_item, parent, false);
            }
            TextView textView = convertView.findViewById(R.id.spinner_item_text);
            textView.setText(getItem(position));
            return convertView;
        }
    }
}
