package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText ;
    TextView result;


    public void getWeather(View view){

        downloadTask dt = new downloadTask();
        dt.execute("https://openweathermap.org/data/2.5/weather?q="+ editText.getText().toString() +"&appid=439d4b804bc8187953eb36d2a8c26a02");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken() , 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.showTextView);
        editText = (EditText) findViewById(R.id.inputTextView);

    }
    public class downloadTask extends AsyncTask<String , Void , String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;


            try{
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                String result = "";
                while(data != -1){

                    char curr = (char) data;
                    result += curr;
                    data = reader.read();

                }

                return result;



            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather info" , weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                String msg ="";
                for(int i = 0 ; i<jsonArray.length() ; ++i){
                    JSONObject current = jsonArray.getJSONObject(i);

                    String main = current.getString("main");
                    String desc = current.getString("description");

                    if(!main.equals("") && !desc.equals("")){
                        msg += main + ": " + desc + "\r\n";
                    }

                }

                if(!msg.equals("")){
                    result.setText(msg);
                }


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}