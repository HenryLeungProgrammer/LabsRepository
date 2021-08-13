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
    Button detailButton;
    String writer, country, director;

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
                                   country = xpp.getAttributeValue(null, "country");
                                   writer = xpp.getAttributeValue(null, "writer");
                                   director = xpp.getAttributeValue(null, "director");

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


                    } // try

                    catch (IOException | XmlPullParserException ex) {
                        ex.printStackTrace();
                    }

                } ); // runnable, run() function, run on different cpu

        });

        Toast.makeText(getApplicationContext(), "Welcome!",
                Toast.LENGTH_SHORT).show();
        detailButton = findViewById(R.id.detailButton);
        detailButton.setOnClickListener( click -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Other Details");
            //dialog.setIcon(R.drawable.dictation2_64);
            dialog.setMessage("\n" + "Country: " + country + "\n\n" + "Director: " + director + "\n\n" + "Writer: " + writer);

            // (3)设置dialog可否被取消
            dialog.setCancelable(true);

            // (4)显示出来
            dialog.show();

        });

    }



}

