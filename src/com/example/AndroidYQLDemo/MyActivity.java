package com.example.AndroidYQLDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.squareup.okhttp.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;

interface ApiListener {
    public void onApiSuccess(Object result);
    public void onApiFailure();
    public void onApiError();
}

public class MyActivity extends Activity implements ApiListener {

    public OkHttpClient client;
    public Button carButton;
    public Button aptButton;
    public Button bedButton;
    public Button tvButton;

    public static final String PATH_BASE = "http://query.yahooapis.com/v1/public/yql?q=%s";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        client = new OkHttpClient();

        carButton = (Button)findViewById(R.id.car_button);
        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yqlQuery = "select * from craigslist.search where location=\"newyork\" and type=\"sss\" and query=\"mustang convertible\"&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=";
                String fullPath = String.format(PATH_BASE, yqlQuery);
                URL queryUrl = encodeURLString(fullPath);
                JSONFetchTask task = new JSONFetchTask(MyActivity.this);
                task.execute(queryUrl);
            }
        });

        aptButton = (Button)findViewById(R.id.apt_button);
        aptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yqlQuery = "select * from craigslist.search where location=\"newyork\" and type=\"nfa\" and query=\"studio\"&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=";
                String fullPath = String.format(PATH_BASE, yqlQuery);
                URL queryUrl = encodeURLString(fullPath);
                JSONFetchTask task = new JSONFetchTask(MyActivity.this);
                task.execute(queryUrl);
            }
        });

        bedButton = (Button)findViewById(R.id.bed_button);
        bedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yqlQuery = "select * from craigslist.search where location=\"newyork\" and type=\"sss\" and query=\"bed\"&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=";
                String fullPath = String.format(PATH_BASE, yqlQuery);
                URL queryUrl = encodeURLString(fullPath);
                JSONFetchTask task = new JSONFetchTask(MyActivity.this);
                task.execute(queryUrl);
            }
        });


        tvButton = (Button)findViewById(R.id.tv_button);
        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yqlQuery = "select * from craigslist.search where location=\"newyork\" and type=\"sss\"and query=\"flat screen tv\"&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=";
                String fullPath = String.format(PATH_BASE, yqlQuery);
                URL queryUrl = encodeURLString(fullPath);
                JSONFetchTask task = new JSONFetchTask(MyActivity.this);
                task.execute(queryUrl);
            }
        });
    }

    @Override
    public void onApiSuccess(Object result){
        String jsonString = (String) result;

        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray results = object.getJSONObject("query").getJSONObject("results")
                                      .getJSONObject("RDF").getJSONArray("item");

            ArrayList<String> titleList = new ArrayList<String>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);
                titleList.add(item.getString("title"));
            }
            if (titleList.size() > 0) {
                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putStringArrayListExtra("results", titleList);
                startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onApiFailure() {

    }

    @Override
    public void onApiError() {

    }

    /**
     * Convenience method for URL encoding HTTP strings.
     * Takes in a string paramter and returns a properly encoded URL with escape slashes and so forth.

     * @param fullPath the unencoded URL path string
     * @return an encoded URL object
     */


    public URL encodeURLString(String fullPath) {
        try {
            URL queryUrl = new URL(fullPath);
            URI queryURI = new URI(queryUrl.getProtocol(), queryUrl.getUserInfo(),
                    queryUrl.getHost(), queryUrl.getPort(), queryUrl.getPath(),
                    queryUrl.getQuery(), queryUrl.getRef());
            queryUrl = queryURI.toURL();
            return queryUrl;
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    class JSONFetchTask extends AsyncTask<URL, Void, String> {

        ApiListener listener;
        public JSONFetchTask(ApiListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(URL... params) {

            URL url = params[0];
            HttpURLConnection connection = client.open(url);
            InputStream in = null;
            try {
                // Read the response.
                in = connection.getInputStream();
                byte[] response = readFully(in);
                return new String(response, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                listener.onApiError();
                e.printStackTrace();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            if (s != null){
                listener.onApiSuccess(s);
            } else {
                Toast.makeText(MyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                listener.onApiError();
            }
        }
    }

    byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

}
