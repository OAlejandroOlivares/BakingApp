package com.oolivares.bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class MyAsyncTask extends AsyncTask<String, JSONObject, JSONArray> {

    private Context context;
    private RecipieListFragment activity;
    public MyAsyncTask(Context applicationContext, RecipieListFragment mainActivity) {
        context = applicationContext;
        activity = mainActivity;
    }
    @Override
    protected JSONArray doInBackground(String... params) {
        StringBuilder sbr  = new StringBuilder(context.getString(R.string.baseUrl));
        URL url;
        JSONArray result = null;
        HttpURLConnection conn = null;
        try{
            url = new URL(sbr.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int c = in.read();
                StringBuilder sb = new StringBuilder();
                while (c != -1) {
                    sb.append((char) c);
                    c = in.read();
                }
                //sb.replace(0,1,"");
                //sb.replace(sb.length()-1,sb.length(),"");
                result = new JSONArray(String.valueOf(sb));
                Log.d("respuesta",result.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONArray jsonObject) {
        super.onPostExecute(jsonObject);
        if (activity != null) {
            activity.populateUI(jsonObject);
        }
    }
}
