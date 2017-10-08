package com.example.vidit.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by vidit on 27/09/16.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News news = getItem(position);

        TextView title = (TextView) listView.findViewById(R.id.title);
        title.setText(news.getmTitle());

        TextView author = (TextView) listView.findViewById(R.id.author);
        author.setText(news.getmAuthor());

        TextView data = (TextView) listView.findViewById(R.id.news);
        data.setText(news.getmNews());


        return listView;
    }
}
