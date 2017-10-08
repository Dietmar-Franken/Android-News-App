package com.example.vidit.newsapp;

/**
 * Created by vidit on 27/09/16.
 */

public class News {

    private String mTitle;
    private String mNews;
    private String mUrl;
    private String mAuthor;


    public News(String title, String news, String url, String author) {
        mTitle = title;
        mNews = news;
        mUrl = url;
        mAuthor = author;

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmNews() {
        return mNews;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
