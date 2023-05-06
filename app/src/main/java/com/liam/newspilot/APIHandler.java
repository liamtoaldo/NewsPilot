package com.liam.newspilot;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class used to call the APIWrapper, it just changes the URLS depending on what it calls
 */
public class APIHandler {
    private final String APIKey = "3dfc1d849ff54868885b2421f9809351";
    private final APIWrapper apiWrapper;
    public APIHandler() {
        apiWrapper = new APIWrapper();
    }

    /** Fetch from 'everything' in the NewsAPI
     * @param query the given query, it's necessary
     * @param language short name code, like 'it', 'en'
     * @param sources the sources, that can be found at https://newsapi.org/docs/endpoints/sources, e.g.: 'ansa'
     */
    public void FetchEverything(String query, String language, ArrayList<String> sources) {
        try {
            StringBuilder sourcesBuilder = new StringBuilder();
            sources.forEach((String s) -> {
                if (sourcesBuilder.length() > 0) {
                    sourcesBuilder.append(",");
                }
                sourcesBuilder.append(s);
            });

            String sourcesString = sourcesBuilder.toString();
            URL url = new URL("https://newsapi.org/v2/everything?q=" + query + "&language=" + language + "&sources=" + sourcesString + "&apiKey=" + APIKey);
            apiWrapper.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FetchEverything(String query, String language) {
        try {
            URL url = new URL("https://newsapi.org/v2/everything?q=" + query + "&language=" + language + "&apiKey=" + APIKey);
            apiWrapper.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FetchTopHeadlines() {
        //TODO
    }
}
