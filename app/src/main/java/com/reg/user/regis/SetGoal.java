package com.reg.user.regis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.reg.user.regis.R;

public class SetGoal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);
        EditText editText=(EditText)findViewById(R.id.entergoal);
        Button button=(Button)findViewById(R.id.savegoal);
    }

    public void clickSet(View view) {
    }
}
