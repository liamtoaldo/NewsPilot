package com.liam.newspilot;

import android.os.Build;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Article {
    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    private String publishedAt;
    public String content;
    //Saved in millis. This field is not present in newsAPI, I added it myself.
    public long savedDateTime;

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

    //These two methods don't include "savedDateTime", so that same news liked in different times can't be added.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return source.equals(article.source) && Objects.equals(author, article.author) && Objects.equals(title, article.title) && Objects.equals(description, article.description) && Objects.equals(url, article.url) && Objects.equals(urlToImage, article.urlToImage) && Objects.equals(publishedAt, article.publishedAt) && Objects.equals(content, article.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, author, title, description, url, urlToImage, publishedAt, content);
    }
}