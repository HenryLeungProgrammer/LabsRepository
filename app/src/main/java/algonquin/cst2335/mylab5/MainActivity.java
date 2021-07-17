package algonquin.cst2335.mylab5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Tianle Liang
 * @Student Number: Tianle Liang
 * @Date 15-July-2021
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

        et = findViewById(R.id.editText);
        tv = findViewById(R.id.textView);
        btn = findViewById(R.id.button);
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;


        btn.setOnClickListener( click -> { // button control

            /**
             * @words words holds what were typed from the keyboard
             */
            String words = et.getText().toString();

         /*   if(aFunction(words)){
                tv.setText("I found ABC in your text");
            }
            else
                tv.setText("No ABC found");*/

            if(checkPasswordComplexity( words )){
                tv.setText( "Your password meets all the requirement" );
                }
            else//(!checkPasswordComplexity( words )){
                tv.setText( "You shall not pass! Please change another password" ); // }
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