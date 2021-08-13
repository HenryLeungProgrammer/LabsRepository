package algonquin.cst2335.mylab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Final Project in Summer Term 2021.
 *
 * @author Tianle Liang
 * @version : 1.0
 * @student Number : 040922323
 * @Date 10 -August-2021
 */
public class MainActivity extends AppCompatActivity {


    /**
     * @param title, the name of movie
     * @param year, the year movie produced
     * @param rating, rating score of this movie
     * @param runtime, the time of movie
     * @param mainActors, main actors in this movie
     * @param plot, the story of this movie
     * @param URLofPoster, movie poster URL
     * @param detailButton, the button for movie detail information
     * @param shopButton, the button for shop button
     * @param writer, the writers of the movie
     * @param country, the country where movie produced
     * @param director, movie director
     */
    String title, year, rating, runtime, mainActors, plot, URLofPoster;
    Button detailButton, shopButton;
    String writer, country, director;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // load activity_main layout

        EditText et= findViewById(R.id.editText); // editText in layout
        TextView tv = findViewById(R.id.textView); // textView in layout, content entered from keyboard
        Button btn = findViewById(R.id.button); // search button

        btn.setOnClickListener( click -> { // button control

            // in red because throwing exception
                Executor newThread = Executors.newSingleThreadExecutor(); // new Thread to compile

                newThread.execute( ()-> {

                    try{
                        String movieName = et.getText().toString(); // get the movie name
                        String serverURL = "http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t="  // UML of the movie website
                                + URLEncoder.encode(movieName, "UTF-8");  // whatever typed into EditText

                    URL url = new URL(serverURL); // url Object
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // connect Internet
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // connect

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        //ignore namespace
                        factory.setNamespaceAware(false);
                        //create the object
                        XmlPullParser xpp = factory.newPullParser();
                        //read from input, like a scanner
                        xpp.setInput( in  , "UTF-8"); //one element a time

                        //move xpp to first element
                        while(xpp.next() != XmlPullParser.END_DOCUMENT) // scanner XML content
                        {
                            switch(xpp.getEventType()){ // event type
                                case     XmlPullParser.START_DOCUMENT:
                                break;
                                case     XmlPullParser.END_DOCUMENT:
                                break;
                                case     XmlPullParser.START_TAG:

                               if(xpp.getName().equals("movie"))  // which opening tag is looking at
                               {
                                   title = xpp.getAttributeValue(null, "title"); // always String
                                   year = xpp.getAttributeValue(null, "year");  // get year from XML content
                                   rating = xpp.getAttributeValue(null, "imdbRating"); // get rating from XML content
                                   runtime = xpp.getAttributeValue(null, "runtime"); // get runtime from XML content
                                   mainActors = xpp.getAttributeValue(null, "actors"); // get actors from XML content
                                   plot = xpp.getAttributeValue(null, "plot"); // get plot from XML content
                                   URLofPoster = xpp.getAttributeValue(null, "poster"); // get URL from XML content
                                   country = xpp.getAttributeValue(null, "country"); // get country name from XML content
                                   writer = xpp.getAttributeValue(null, "writer"); // get writer from XML content
                                   director = xpp.getAttributeValue(null, "director"); // get director from XML content

                               }
                               else if(xpp.getName().equals(" ")){ // for other content except "movie"

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
                            tv2 = findViewById(R.id.movieTitle); // locate movieTitle TextView in layout
                            tv2.setText("Title: "  +  title );  // pass the title value to layout
                            tv2.setVisibility(View.VISIBLE);   // set it visible when user clicked the search button

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


                    } // try

                    catch (IOException | XmlPullParserException ex) { // catch the exception
                        ex.printStackTrace();
                    }

                } ); // runnable, run() function, run on different cpu

        });

        Toast.makeText(getApplicationContext(), "Welcome!",  // make toast notification when user open the app
                Toast.LENGTH_SHORT).show();
        detailButton = findViewById(R.id.detailButton);  // locate the detail button
        detailButton.setOnClickListener( click -> {  // when user clicked the detail button
            AlertDialog.Builder dialog = new AlertDialog.Builder(this); // create a AlertDialog Object
            dialog.setTitle("Other Details");   // the title of AlertDialog Box
            //dialog.setIcon(R.drawable.dictation2_64);  // set icon for AlertDialog Box
            dialog.setMessage("\n" + "Country: " + country + "\n\n" + "Director: " + director + "\n\n" + "Writer: " + writer); // show information

            dialog.setCancelable(true); // whether could be cancel

            dialog.show(); // show out

        });

        shopButton = findViewById(R.id.shopButton); // locate the shop button in layout file
        shopButton.setOnClickListener( click -> {  // when user clicked the shop button
            setContentView(R.layout.fragment_layout); // turn to the other page
        });

    }



}

