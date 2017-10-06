package com.example.george.radonquote;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    String randQuote;
    String author;
    TextView quoteTextView;
    ImageView bgImageView;
    ImageView nextImageView;
    String url = "http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1&_jsonp=mycallback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the ActionBar for the activity
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        quoteTextView = (TextView) findViewById(R.id.quote_text);
        nextImageView = (ImageView) findViewById(R.id.next_quote);

        /*onclick listener for next quote*/
        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchQuote fetchQuote = new FetchQuote();
                fetchQuote.execute();
            }
        });
    }

    /*
    * async class to get random quote in background
    */
    class FetchQuote extends AsyncTask<Void, Void, Void> {
        String quote = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Fetching Quote!",Toast.LENGTH_LONG).show();
        }

        /*
        * making connection and parsing json data
        */
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL uri = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null) {
                    Log.v("line: ",line);
                    line = bf.readLine();
                    quote += line;
                }
            }
            catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            try {
                JSONArray ja = new JSONArray(quote);
                JSONObject jo = (JSONObject) ja.get(0);
                randQuote = jo.get("content").toString().replaceAll("\\<[^>]*>","");
                author = jo.get("title").toString();
                Log.v("QUOTE", randQuote+" "+author);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Log.v("Post Exec", randQuote);
            quoteTextView.setText(randQuote);

        }
    }
}
