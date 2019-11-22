package com.charizard.TenorGIF;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//A very simple Tenor GIF Search API Wrapper that returns the URL.
//It can either pick a random GIF (see returnRandomGIF function) or the first GIF from the result.
//returnGIFs returns all GIFs.
//Please use the setAPIKey function before executing other functions.

public class Tenor {
private static String API_KEY = "";
    public static void setAPIKey(String apikey) {
        API_KEY = apikey;
    }
    public static String returnGIF(String searchTerm) {
        JSONObject searchResult = getSearchResults(searchTerm, 2);
      	      String url = "";
    	      ArrayList<String> list = new ArrayList<String>();
    	      JSONArray jsonArray = searchResult.getJSONArray("results");
    	      for(int i = 0 ; i < jsonArray.length(); i++) {
    	         list.add(jsonArray.getJSONObject(i).getString("url"));
    	         url = jsonArray.getJSONObject(i).getString("url");
              }
              return url;
    }
    public static String returnRandomGIF (String searchTerm, int maxResults) {
        JSONObject searchResult = getSearchResults(searchTerm, maxResults);
        Random rand = new Random();
        String url = "";
          ArrayList<String> list = new ArrayList<String>();
          JSONArray jsonArray = searchResult.getJSONArray("results");
          for(int i = 0 ; i < jsonArray.length(); i++) {
             list.add(jsonArray.getJSONObject(i).getString("url"));
          }
          url = list.get(rand.nextInt(list.size() - 1));
          return url;
    }
    public static ArrayList<String> returnGIFs(String searchTerm, int maxResults) {
        JSONObject searchResult = getSearchResults(searchTerm, maxResults);
          ArrayList<String> list = new ArrayList<String>();
          JSONArray jsonArray = searchResult.getJSONArray("results");
          for(int i = 0 ; i < jsonArray.length(); i++) {
             list.add(jsonArray.getJSONObject(i).getString("url"));
          }
          return list;
    }
    public static JSONObject getSearchResults(String searchTerm, int limit) {

        // make search request - using default locale of EN_US

        final String url = String.format("https://api.tenor.com/v1/search?q=%1$s&key=%2$s&limit=%3$s",
                searchTerm, API_KEY, limit);
        try {
            return get(url);
        } catch (IOException | JSONException ignored) {
        }
        return null;
    }
    private static JSONObject get(String url) throws IOException, JSONException {
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Handle failure
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
                System.out.println(error);
                throw new ConnectException(error);
            }

            // Parse response
            return parser(connection);
        } catch (Exception e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }

    private static JSONObject parser(HttpURLConnection connection) throws JSONException {
        char[] buffer = new char[1024 * 4];
        int n;
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(connection.getInputStream());
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            return new JSONObject(writer.toString());
        } catch (Exception IOException) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return new JSONObject("");
    }
}