package algonquin.cst2335.mylab5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// new classes stuff
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @author Tianle Liang
 * @student Number: 040922323
 * @Date 23-July-2021
 * @version : 1.0
*/
public class MainActivity extends AppCompatActivity {


    String title, year, rating, runtime, mainActors, plot, URLofPoster;

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
                        String movieName = et.getText().toString();
                        String serverURL = "http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t="
                                + URLEncoder.encode(movieName, "UTF-8");  // whatever typed into EditText

                    URL url = new URL(serverURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        // This converts to a String
//                        text = (new BufferedReader(
//                                new InputStreamReader(in, StandardCharsets.UTF_8)))
//                                .lines()
//                                .collect(Collectors.joining("\n"));

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        //ignore namespace
                        factory.setNamespaceAware(false);
                        //create the object
                        XmlPullParser xpp = factory.newPullParser();
                        //read from input, like a scanner
                        xpp.setInput( in  , "UTF-8"); //one element a time

                        //move xpp to first element
                        while(xpp.next() != XmlPullParser.END_DOCUMENT)
                        {
                            switch(xpp.getEventType()){
                                case     XmlPullParser.START_DOCUMENT:
                                break;
                                case     XmlPullParser.END_DOCUMENT:
                                break;
                                case     XmlPullParser.START_TAG:
                                //look for temperature  "temperature"
                               if(xpp.getName().equals("movie"))  // which opening tag are we looking at?!
                               {
                                   title = xpp.getAttributeValue(null, "title"); // always String
                                   year = xpp.getAttributeValue(null, "year");
                                   rating = xpp.getAttributeValue(null, "imdbRating");
                                   runtime = xpp.getAttributeValue(null, "runtime");
                                   mainActors = xpp.getAttributeValue(null, "actors");
                                   plot = xpp.getAttributeValue(null, "plot");
                                   URLofPoster = xpp.getAttributeValue(null, "poster");

                               }
                               else if(xpp.getName().equals("weather")){

                               }
                               break;
                                case   XmlPullParser.END_TAG:
                                break;
                                case XmlPullParser.TEXT:
                                break;

                            }

                        }

                        runOnUiThread( (  )  -> {
                            TextView tv2 = findViewById(R.id.movieTitle);
                            tv2 = findViewById(R.id.movieTitle);
                            tv2.setText("Title: "  +  title );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.year);
                            tv2.setText("Year: "  +  year );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.runtime);
                            tv2.setText("Runtime: "  +  runtime );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.actors);
                            tv2.setText("Main Actors: "  +  mainActors );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.plot);
                            tv2.setText("Plot:  "  +  plot );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.URL);
                            tv2.setText("URL of Poster:  "  +  URLofPoster );
                            tv2.setVisibility(View.VISIBLE);

                            tv2 = findViewById(R.id.information);
                            tv2.setText("Movie Information below : " );
                            tv2.setVisibility(View.VISIBLE);

                        });


//                        JSONObject theDocument = new JSONObject( text ); //this converts the String to JSON Object. The whole page!!
//                        JSONObject main = theDocument.getJSONObject("main");
//                        JSONArray weatherArray = theDocument.getJSONArray("weather");
//                        JSONObject position0 = weatherArray.getJSONObject(0);
//                        description = position0.getString("description");
//                        String iconName = position0.getString("icon");
//
//                        currentTemp = main.getDouble("temp");
//                        maxTemp = main.getDouble("temp_max");
//                        minTemp = main.getDouble("temp_min");
//                        humidi = main.getDouble("humidity");
//                        String locationCity = et.getText().toString();
//                        JSONObject sys = theDocument.getJSONObject("sys");
//                        String locationCountry = sys.getString("country");
//
//                        Bitmap image;
//                        File file = new File(getFilesDir(), iconName + ".png");
//                        if(file.exists()){
//                            image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
//                        }
//                        else {
//                           URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
//                            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
//                            connection.connect();
//                            int responseCode = connection.getResponseCode();
//                            if (responseCode == 200) {
//                                image = BitmapFactory.decodeStream(connection.getInputStream());
//                                image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
//                                runOnUiThread( (  )  -> {
//                                ImageView iv = findViewById(R.id.icon);
//                                iv.setImageBitmap(image);
//                                iv.setVisibility(View.VISIBLE); });
//                            }
//                        }
//
//                        runOnUiThread( (  )  -> {
//
//                            TextView tv2 = findViewById(R.id.temp);
//                            tv2.setText("The current temperature is: "  +  currentTemp );
//                            tv2.setVisibility(View.VISIBLE);
//
//                            tv2 = findViewById(R.id.maxTemp);
//                            tv2.setText("The Max temperature is: "  +  maxTemp );
//                            tv2.setVisibility(View.VISIBLE);
//
//                            tv2 = findViewById(R.id.minTemp);
//                            tv2.setText("The Min temperature is: "  + minTemp );
//                            tv2.setVisibility(View.VISIBLE);
//
//                            tv2 = findViewById(R.id.humitidy);
//                            tv2.setText("The humidity: "  +  humidi + "%");
//                            tv2.setVisibility(View.VISIBLE);
//
//                            tv2 = findViewById(R.id.description);
//                            tv2.setText(description );
//                            tv2.setVisibility(View.VISIBLE);
//
//                            tv2 = findViewById(R.id.location);
//                            tv2.setText(locationCity + " ," +  locationCountry);
//                            tv2.setVisibility(View.VISIBLE);
//                        });

                    } // try

                    catch (IOException | XmlPullParserException ex) {
                        ex.printStackTrace();
                    }

                } ); // runnable, run() function, run on different cpu

        });

    }

}