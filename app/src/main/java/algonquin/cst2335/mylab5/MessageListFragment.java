package algonquin.cst2335.mylab5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageListFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View chatLayayout = inflater.inflate(R.layout.activity_main,container,false);


        return chatLayayout;


    }
}
