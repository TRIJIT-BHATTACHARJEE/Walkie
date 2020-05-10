package com.reg.user.regis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Tab4Fragment extends Fragment {
    TextView textView;
    Button displayBtn;

    MySQLiteAdapter mySQLiteAdapter;
    Context context;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab4fragment, container, false);
        textView = (TextView) view.findViewById(R.id.textView4);
        displayBtn = (Button) view.findViewById(R.id.display);

        context = getContext();
        mySQLiteAdapter = new MySQLiteAdapter(context);


        displayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String allrecords = mySQLiteAdapter.DisplayAllRecords();

                if (allrecords.isEmpty()) {
                    MyToastMessage.myMessage(context, "No record found for display...");
                } else {
                    //MyToastMessage.myMessage(context,allrecords);
                    textView.setText(allrecords);

                }

            }
        });

        return view;
    }

}
