package com.reg.user.regis;

/**
 * Created by USER on 17-07-2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Signin extends Activity {

    Button button;
    RadioGroup genderRadioGroup;
    EditText name;
    EditText age;
    EditText weight;
    Spinner ht_feet;
    Spinner ht_inches;
    boolean completed;
    SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);



        sharedPrefs = getSharedPreferences("sharedprefs", 0);
        completed = sharedPrefs.getBoolean("complete", false);
        if (completed == true) {
            Intent intent = new Intent();
            intent.setClass(Signin.this, hi2.class);
            startActivity(intent);
            finish();
        }


        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        weight = (EditText) findViewById(R.id.weight);
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender);
        ht_feet = (Spinner) findViewById(R.id.ft_spinner);
        ht_inches = (Spinner) findViewById(R.id.inch_spinner);
        button=(Button) findViewById(R.id.submit);



       // Spinner ht_feet = (Spinner) findViewById(R.id.ft_spinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.ft_range, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ht_feet.setAdapter(adapter1);

        //Spinner ht_inch = (Spinner) findViewById(R.id.inch_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.inch_range, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ht_inches.setAdapter(adapter);


        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString();
                String age1 = age.getText().toString();
                String weight1 = weight.getText().toString();
                String feet1 = ht_feet.getSelectedItem().toString();
                String inches1 = ht_inches.getSelectedItem().toString();

                int id = genderRadioGroup.getCheckedRadioButtonId();
                RadioButton radBut = (RadioButton) findViewById(id);
                String gender1= radBut.getText().toString();

                if (name1.equals("") || age1.equals("")
                        || weight1.equals("") || radBut.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Enter all fields",
                            Toast.LENGTH_SHORT).show();
                } else{
                    SharedPreferences.Editor editors = sharedPrefs.edit();
                    editors.putString("name_dt", name1);
                    editors.putString("age_dt", age1);
                    editors.putString("gender_dt", gender1);
                    editors.putString("weight_dt", weight1);
                    editors.putString("feet_dt", feet1);
                    editors.putString("inches_dt", inches1);
                    editors.putBoolean("complete", true);
                    editors.apply();
                    Intent reg_done=new Intent(Signin.this, hi2.class);
                    startActivity(reg_done);
                    finish();
                }
            }
        });
    }
}