package algonquin.cst2335.mylab5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @author Tianle Liang  Student Number: 040922323
 */

public class ChatRoom extends AppCompatActivity {
    ArrayList<ChatMessage> messages = new ArrayList<>();
    RecyclerView chatList;
    int MyViewType;
    MyChatAdapter adt;
    ChatMessage removedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout); // load a layout

        EditText messageType  = findViewById(R.id.messageEdit); // getting typed message from phone keyboard
        Button send = findViewById(R.id.button); // for send button
        Button RC   = findViewById(R.id.button2);
        RecyclerView chatList = findViewById(R.id.myrecycler);
        // MyChatAdapter adt, adtSecond ;
        chatList = findViewById(R.id.myrecycler);

        MyOpenHelper opener = new MyOpenHelper( this );
        ///new added on it


        // The adapter object is an object that tells the List how to build the items
        chatList.setAdapter(adt = new MyChatAdapter());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
       //// chatList.setLayoutManager(layoutManager);

        chatList.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener( click ->
                {
                    ChatMessage nextMessage = new ChatMessage( messageType.getText().toString());
                    nextMessage.setSendOrReceive(1);  /////
                    messages.add(nextMessage);
                    messageType.setText("");//clear the edittext
        adt.notifyItemInserted( messages.size() - 1 ); // insert the new row
                    MyViewType = 1;
                }
                );
        RC.setOnClickListener( click ->
                {
                    ChatMessage nextMessage = new ChatMessage( messageType.getText().toString());
                    nextMessage.setSendOrReceive(0); /////
                    messages.add(nextMessage);
                    messageType.setText("");//clear the edittext
                    adt.notifyItemInserted( messages.size() - 1 ); // insert the new row
                    MyViewType = 2;
                }

        );

       // ChatMessage thisMessage = new ChatMessage(  );

        //messages.add( thisMessage );
     }

     private class MyRowViews extends RecyclerView.ViewHolder{

        TextView messageText; // declare two variables
        TextView timeText;  // because our rows have two TextViews
         int position = -1;

         public MyRowViews(View itemView) {
             super(itemView);

             itemView.setOnClickListener(
             click ->{
             AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
             builder.setMessage("Do you want to delete this message: " + messageText.getText() )
             .setTitle("Question:")
             .setNegativeButton("No", (dialog, cl) -> {   })
             .setPositiveButton("Yes", (dialog, cl) -> {
                 position = getAbsoluteAdapterPosition(); // update position relates which has been deleted
                 removedMessage = messages.get(position);
                messages.remove( position);
                adt.notifyItemRemoved(position);
                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                .setAction("Undo", clk -> {
                    messages.add(position, removedMessage);
                    adt.notifyItemInserted(position); // show back
                })    // give an option for Snackbar
                        .show();
             }) // for choice YES

             .create().show();
             });
             messageText = itemView.findViewById(R.id.message);
             timeText = itemView.findViewById(R.id.time);
         }
         public void setPosition(int p){ position = p; }
     }

     private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {

         @Override
         public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

             LayoutInflater inflater = getLayoutInflater();
             int layoutID;
             viewType = MyViewType;
             if (viewType == 1)  //send
             {
                 layoutID = R.layout.sent_message;
                 View loadedRow = inflater.inflate(layoutID, parent, false);
                 MyViewType = 0;
                 return new MyRowViews(loadedRow);
             } else{
                 layoutID = R.layout.receive_message;
                 View loadedRow = inflater.inflate(layoutID, parent, false);
                 MyViewType = 0;
                 return new MyRowViews(loadedRow);
             }
             //return new MyRowViews(getLayoutInflater().inflate(R.layout.sent_message,parent,false));
               //return new MyRowViews(loadedRow);
         }


        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText( messages.get(position).getMessage() );
            holder.timeText.setText( messages.get(position).getTimeSent() );
            holder.setPosition(position);
           /// MyRowViews thisRowlayout = (MyRowViews)holder;
           /// thisRowlayout.messageText.setText(  "This is a row" + position);
            ChatMessage thisRow = messages.get(position);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        } // how many items to show
    }


     private class ChatMessage{  // Data model
        String message;
        int sendOrReceive;
        Date timeSent;

         public ChatMessage(String message) { //, int sendOrReceive, Date timeSent
             this.message = message;
             this.sendOrReceive = sendOrReceive;
             this.timeSent = timeSent;
         }

         public String getMessage() {
             return message;
         }


         public int getSendOrReceive() {
             return sendOrReceive;
         }

         public String getTimeSent() {
             SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a", Locale.getDefault());
             String currentDateandTime = sdf.format(new Date());
             return currentDateandTime;
         }

         public void setSendOrReceive(int sendOrReceive) {
             this.sendOrReceive = sendOrReceive;
         }
     }


}
