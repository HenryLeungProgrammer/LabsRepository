package algonquin.cst2335.mylab5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// new classes stuff
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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

    /** Here is the login button */
    Button btn;
    /** This holds the texts from keyboards showing middle of screen */
    EditText et;
    /** This is Hello world TextView */
    TextView tv;


    boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.editText); // editText
        tv = findViewById(R.id.textView); // textView
        btn = findViewById(R.id.button); // button
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        btn.setOnClickListener( click -> { // button control

            // in red because throwing exception

                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute( ()-> {

                    URL url = null;  // connect to the server

                    try{
                        String serverURL = "https://api.openweathermap.org/data/2.5/weather?q="
                                + URLEncoder.encode(et.getText().toString(), "UTF-8")  // whatever typed into EditText
                                + "&appid=7e943c97096a9784391a981c4d878b22&Units=Metric";

                        url = new URL(serverURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        // This converts to a String
                        String text = (new BufferedReader(
                                new InputStreamReader(in, StandardCharsets.UTF_8)))
                                .lines()
                                .collect(Collectors.joining("\n"));
                        JSONObject theDocument = new JSONObject( text ); //this converts the String to JSON Object.
                        JSONObject coord =  theDocument.getJSONObject("coord");
                        double lat = coord.getDouble("lat");
                        double lon = coord.getDouble("lon");

                        String base = theDocument.getString("base");
                        theDocument.getInt("visibility");

                        //For the Array
                        JSONArray weatherArray = theDocument.getJSONArray("weather");
                        JSONObject object0 = weatherArray.getJSONObject(0);
                        JSONObject main = theDocument.getJSONObject("main");
                        double currentTemp = main.getDouble("temp");
                        double min = main.getDouble("temp_min");
                        double max = main.getDouble("temp_max");



                    } // try

                    catch (MalformedURLException e) { // two exceptions could be combined together using ||
                        e.printStackTrace();
                    }

                    catch (IOException | JSONException ex) {
                        ex.printStackTrace();
                    }

                } ); // runnable, run() function, run on different cpu

        });

    }


    /**
     * This function checks if the ABC contained
     * @return true if contains ABC
     */
    private boolean aFunction(String pw){
        return pw.contains("ABC"); // case sensitive
    }

    /**
     * This function checks if the Uppercase letter contained
     * @return true if it does contain
     */
    private boolean isUpperCase(char c){
        return Character.isUpperCase(c);
    }

    /**
     * This function checks if the Lowercase letter contained
     * @return true if it does contain
     */
    private boolean isLowerCase(char c){
        return Character.isLowerCase(c);
    }

    /**
     * This function checks if the number contained
     * @return true if it does contain
     */
    private boolean isDigit(char c){
        return Character.isDigit(c);
    }

    /**
     * This function checks if special character contained
     * @return true if it does contain
     */
    private boolean isSpecialCharacter( char c ){

        switch(c){
            case '#':
            case '?':
            case '*':
            case '$':
            case '%':
            case '^':
            case '&':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }

    }

    /**
     * This function checks whether the password meet all requirements
     * @return true if it is acceptable
     */
    private boolean checkPasswordComplexity(String pw){
        boolean isRight;

        for(int i =0; i< pw.length(); i++){

            if(isUpperCase( pw.charAt(i) )){
                foundUpperCase = true; }

            else if(isLowerCase(pw.charAt(i))){
                foundLowerCase = true; }

            else if(isDigit(pw.charAt(i))){
                foundNumber = true;}

            else //(isSpecialCharacter(pw.charAt(i)))
            {
                foundSpecial = true;  }
        }

        isRight = foundUpperCase && foundLowerCase && foundNumber && foundSpecial ;

        if(isRight == true){ Toast.makeText(MainActivity.this, "Your password is perfect!", Toast.LENGTH_LONG).show();}
        return isRight;
    }
}