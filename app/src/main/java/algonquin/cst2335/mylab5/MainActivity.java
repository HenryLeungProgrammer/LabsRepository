package algonquin.cst2335.mylab5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// new classes stuff
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * @author Tianle Liang
 * @student Number: 040922323
 * @Date 23-July-2021
 * @version : 1.0
*/
public class MainActivity extends AppCompatActivity {

    String text;
    Double currentTemp, maxTemp, minTemp, humidi;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et= findViewById(R.id.editText); // editText
        TextView tv = findViewById(R.id.textView); // textView
        Button btn = findViewById(R.id.button); // button

        btn.setOnClickListener( click -> { // button control

            // in red because throwing exception
                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute( ()-> {

                    try{
                        String cityName = et.getText().toString();
                        String serverURL = "https://api.openweathermap.org/data/2.5/weather?q="
                                + URLEncoder.encode(cityName, "UTF-8")  // whatever typed into EditText
                                + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";


                    URL url = new URL(serverURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        // This converts to a String
                        text = (new BufferedReader(
                                new InputStreamReader(in, StandardCharsets.UTF_8)))
                                .lines()
                                .collect(Collectors.joining("\n"));
                        JSONObject theDocument = new JSONObject( text ); //this converts the String to JSON Object. The whole page!!
                        JSONObject main = theDocument.getJSONObject("main");
                        JSONArray weatherArray = theDocument.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);
                        description = position0.getString("description");
                        String iconName = position0.getString("icon");

                        currentTemp = main.getDouble("temp");
                        maxTemp = main.getDouble("temp_max");
                        minTemp = main.getDouble("temp_min");
                        humidi = main.getDouble("humidity");
                        String locationCity = et.getText().toString();
                        JSONObject sys = theDocument.getJSONObject("sys");
                        String locationCountry = sys.getString("country");

                        Bitmap image;
                        File file = new File(getFilesDir(), iconName + ".png");
                        if(file.exists()){
                            image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                        }
                        else {
                           URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                image = BitmapFactory.decodeStream(connection.getInputStream());
                                image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                runOnUiThread( (  )  -> {
                                ImageView iv = findViewById(R.id.icon);
                                iv.setImageBitmap(image);
                                iv.setVisibility(View.VISIBLE); });
                            }
                        }

                        runOnUiThread( (  )  -> {

                            TextView tv2 = findViewById(R.id.temp);
                            tv2.setText("The current temperature is: "  +  currentTemp );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.maxTemp);
                            tv2.setText("The Max temperature is: "  +  maxTemp );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.minTemp);
                            tv2.setText("The Min temperature is: "  + minTemp );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.humitidy);
                            tv2.setText("The humidity: "  +  humidi + "%");
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.description);
                            tv2.setText(description );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.location);
                            tv2.setText(locationCity + " ," +  locationCountry);
                            tv2.setVisibility(View.VISIBLE);
                        });

                    } // try

                    catch (IOException | JSONException ex) {
                        ex.printStackTrace();
                    }

                } ); // runnable, run() function, run on different cpu



        });

    }

}