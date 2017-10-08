package com.example.vidit.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by vidit on 27/09/16.
 */

public class Request {

    private static final String LOG_TAG = Request.class.getSimpleName();

    private Request() {

    }

    public static List<News> fetchNews(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFromJson(String JSON) {
        if (TextUtils.isEmpty(JSON)) {
            return null;
        }

        List<News> news = new ArrayList<News>();
        try {
            JSONObject root = new JSONObject(JSON);
            JSONObject responseJSON = root.getJSONObject("response");
            JSONArray results = responseJSON.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject resultObect = results.getJSONObject(i);
                String sectionName = resultObect.getString("sectionName");
                String webTitle = resultObect.getString("webTitle");
                String webUrl = resultObect.getString("webUrl");
                JSONArray tags = resultObect.getJSONArray("tags");
                String authors = "";
                for (int j = 0; j < tags.length(); j++) {
                    JSONObject tagObject = tags.getJSONObject(j);
                    if (j > 0) {
                        authors = authors + ", "; //this is to comma-separate authors in case of multiple
                    }
                    //append the new author to "authors"
                    authors = authors + tagObject.getString("webTitle");
                }
                News data = new News(sectionName, webTitle, webUrl, authors);
                news.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

}
