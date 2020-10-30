package com.roseproduction.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //https://api.openweathermap.org/data/2.5/weather?q=sleman,id&appid=0046d9555446c9f7229a77af0df602ab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvWeather = findViewById(R.id.tv_weather);
        final TextView tvTemp = findViewById(R.id.tv_temp);
        final ImageView img = findViewById(R.id.imageView);
        final TextView tvDesc = findViewById(R.id.tv_desc);

        String url = "https://api.openweathermap.org/data/2.5/weather?" +
                "q=sleman,id&appid=0046d9555446c9f7229a77af0df602ab";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(getApplicationContext(),
                        "Tidak dapat terhubung server", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                try {
                    JSONObject objData = new JSONObject(responseData);
                    final JSONArray arrayWeather = objData.getJSONArray("weather");
                    final JSONObject objWeather = new JSONObject(arrayWeather.get(0).toString());
                    final JSONObject objTemp = new JSONObject(objData.get("main").toString());
                    double temp = (objTemp.getDouble("temp")) - 273.15;
                    final String celcius = String.valueOf(String.format("%.1f", temp));

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tvWeather.setText(objWeather.get("main").toString());
                                tvDesc.setText(objWeather.get("description").toString());
                                tvTemp.setText(celcius + "°C");

                                String urlIcon = "http://openweathermap.org/img/w/" + objWeather.get("icon") + ".png";
                                Glide.with(MainActivity.this)
                                        .load(urlIcon)
                                        .into(img);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}