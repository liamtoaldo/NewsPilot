package com.liam.newspilot;

import android.os.Build;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class Article {
    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    private String publishedAt;
    public String content;

    public OffsetDateTime publishedAt() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return OffsetDateTime.parse(publishedAt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}