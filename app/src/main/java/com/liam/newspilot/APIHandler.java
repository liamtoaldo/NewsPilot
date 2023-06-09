package com.liam.newspilot;


import android.content.Context;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class used to call the APIWrapper, it just changes the URLS depending on what it calls
 */
public class APIHandler {
    private static final String APIKey = "3dfc1d849ff54868885b2421f9809351";
    private final APIWrapperCallback callback;
    private static final int pageSize = 40;
    public APIHandler(APIWrapperCallback callback ) {
        this.callback = callback;
    }

    /** Fetch from 'everything' in the NewsAPI
     * @param query the given query, it's necessary
     * @param language short name code, like 'it', 'en'
     * @param sources the sources, that can be found at https://newsapi.org/docs/endpoints/sources, e.g.: 'ansa'
     */
    public void FetchEverything(String query, String language, ArrayList<String> sources) {
        try {
            APIWrapper apiWrapper = new APIWrapper(callback);
            StringBuilder sourcesBuilder = new StringBuilder();
            sources.forEach((String s) -> {
                if (sourcesBuilder.length() > 0) {
                    sourcesBuilder.append(",");
                }
                sourcesBuilder.append(s);
            });

            String sourcesString = sourcesBuilder.toString();
            URL url = new URL("https://newsapi.org/v2/everything?q=" + query + "&language=" + language + "&sources=" + sourcesString + "&apiKey=" + APIKey+ "&pageSize=" + pageSize);
            apiWrapper.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FetchEverything(String query, String language) {
        try {
            APIWrapper apiWrapper = new APIWrapper(callback);
            URL url = new URL("https://newsapi.org/v2/everything?q=" + query + "&language=" + language + "&apiKey=" + APIKey + "&pageSize=" + pageSize);
            apiWrapper.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Fetch from 'breaking news' in the NewsAPI
     * @param query the given query, it's necessary
     * @param country short name code, like 'it', 'en'
     */
    public void FetchTopHeadlines(String query, String country) {
        try {
            APIWrapper apiWrapper = new APIWrapper(callback);
            URL url = new URL("https://newsapi.org/v2/top-headlines?q=" + query + "&country=" + country + "&apiKey=" + APIKey);
            apiWrapper.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FetchTopHeadlines(String country) {
        try {
            APIWrapper apiWrapper = new APIWrapper(callback);
            URL url = new URL("https://newsapi.org/v2/top-headlines?country=" + country + "&apiKey=" + APIKey);
            apiWrapper.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
