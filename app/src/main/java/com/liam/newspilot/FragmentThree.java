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
    private final String[] countries = {
            "🇦🇪 United Arab Emirates",
            "🇦🇷 Argentina",
            "🇦🇹 Austria",
            "🇧🇪 Belgium",
            "🇧🇬 Bulgaria",
            "🇧🇷 Brazil",
            "🇨🇦 Canada",
            "🇨🇭 Switzerland",
            "🇨🇳 China",
            "🇨🇴 Colombia",
            "🇨🇺 Cuba",
            "🇨🇿 Czech Republic",
            "🇩🇪 Germany",
            "🇪🇬 Egypt",
            "🇫🇷 France",
            "🇬🇧 United Kingdom",
            "🇬🇷 Greece",
            "🇭🇰 Hong Kong",
            "🇭🇺 Hungary",
            "🇮🇩 Indonesia",
            "🇮🇪 Ireland",
            "🇮🇱 Israel",
            "🇮🇳 India",
            "🇮🇹 Italy",
            "🇯🇵 Japan",
            "🇰🇷 South Korea",
            "🇱🇹 Lithuania",
            "🇱🇻 Latvia",
            "🇲🇦 Morocco",
            "🇲🇽 Mexico",
            "🇲🇾 Malaysia",
            "🇳🇬 Nigeria",
            "🇳🇱 Netherlands",
            "🇳🇴 Norway",
            "🇳🇿 New Zealand",
            "🇵🇭 Philippines",
            "🇵🇱 Poland",
            "🇵🇹 Portugal",
            "🇷🇴 Romania",
            "🇷🇸 Serbia",
            "🇷🇺 Russia",
            "🇸🇦 Saudi Arabia",
            "🇸🇪 Sweden",
            "🇸🇬 Singapore",
            "🇸🇮 Slovenia",
            "🇸🇰 Slovakia",
            "🇹🇭 Thailand",
            "🇹🇷 Turkey",
            "🇹🇼 Taiwan",
            "🇺🇦 Ukraine",
            "🇺🇸 United States",
            "🇻🇪 Venezuela",
            "🇿🇦 South Africa"
    };
    private final String[] languageOptions = {"ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "sv", "ud", "zh"};
    private final String[] countryOptions = {"ae", "ar", "at", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        /* LANGUAGES DROPDOWN */
        // Create the menu with all the options for the language
        Spinner spinner = view.findViewById(R.id.lang_menu);
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(view.getContext(), languages);
        spinner.setAdapter(customSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle language spinner selection
                MainActivity.sharedPrefSet.putString("language", languageOptions[position]);
                MainActivity.sharedPrefSet.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Change color of the dropdown icon
        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white2), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);
        //Load the language
        String savedlang = MainActivity.sharedPrefGet.getString("language", "en");
        spinner.setSelection(Arrays.asList(languageOptions).indexOf(savedlang));


        /* COUNTRIES DROPDOWN */
        // Create the menu with all the options for the country
        Spinner spinnerCountries = view.findViewById(R.id.country_menu);
        CustomSpinnerAdapter customSpinnerAdapterCountries = new CustomSpinnerAdapter(view.getContext(), countries);
        spinnerCountries.setAdapter(customSpinnerAdapterCountries);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle country spinner selection
                MainActivity.sharedPrefSet.putString("country", countryOptions[position]);
                MainActivity.sharedPrefSet.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Change color of the dropdown icon
        Drawable spinnerDrawableCountries = spinnerCountries.getBackground().getConstantState().newDrawable();
        spinnerDrawableCountries.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white2), PorterDuff.Mode.SRC_ATOP);
        spinnerCountries.setBackground(spinnerDrawableCountries);

        //Load the language
        String savedCountry = MainActivity.sharedPrefGet.getString("country", "us");
        spinnerCountries.setSelection(Arrays.asList(countryOptions).indexOf(savedCountry));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Function that gets executed when a language is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
