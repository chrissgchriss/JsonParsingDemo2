package com.webappdesign.granville.jsonparsingdemo2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    Button btnHit;
    TextView txtHit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHit = (Button) findViewById(R.id.btnHit);
        txtHit = (TextView) findViewById(R.id.txtHit);

        btnHit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt");
                new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt");
            }
        });


    }// end of onCreate
    public class JSONTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) { // return null or String

            HttpURLConnection connection = null; // by making null you initalize connection
            BufferedReader reader = null; // this is part of closing and crashing if NULL...

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // what does the connection need?
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                // use what we made
                String line = "";
                while((line = reader.readLine()) !=null){
                    buffer.append(line);
                }
                String finalJason = buffer.toString();

                // This part starts describing JSON
                JSONObject parentObject = new JSONObject(finalJason);
                JSONArray parentArray = parentObject.getJSONArray("movies");

                // we need another StringBuffer???
                StringBuffer finalBufferedData = new StringBuffer();
                for( int i = 0; i<parentArray.length(); i++ ) {

                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String movieName = finalObject.getString("movie");
                    int year = finalObject.getInt("year");

                    finalBufferedData.append(movieName + " - " + year + "\n");
                }

                return finalBufferedData.toString();


            }catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // if we go through all the errors - then nothing is returned...
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txtHit.setText(result);// this used to be buffer that I cut out...
        }
    }
}
