package com.reg.user.regis;

/**
 * Created by USER on 17-07-2017.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.v7.app.AlertDialog;
import android.hardware.SensorEventListener;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.reg.user.regis.StepListener.StepListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import static com.reg.user.regis.R.layout.tab1fragment;

/**
 * Created by USER on 16-07-2017.
 */

public class Tab1Fragment extends Fragment implements SensorEventListener, StepListener, TextToSpeech.OnInitListener {
    private static final String TAG = "Tab1Fragment";

    //added from SimpleStepCounter
    private TextView TvSteps,TvDist, TvSpeed, Tvcal;
    private Button BtnStart,BtnStop,BtnPause,BtnResume;
    private long timeWhenStopped = 0;
    private boolean stopClicked;
    private Chronometer chronometer;

    private double BMR;
    private double STRIDE_LENGTH;


    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private Timer timer;


    MySQLiteAdapter mySQLiteAdapter;
    Context context;

    private TextToSpeech textToSpeech;

    String db_date;
    String db_duration;
    int db_steps;
    double db_distance;
    double db_calories;

    ImageButton fb,wa;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view= inflater.inflate(tab1fragment, container, false);



        context=getContext();
        mySQLiteAdapter = new MySQLiteAdapter(context);

        // Get an instance of the SensorManager

        SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("sharedprefs", 0);
        String name= sharedPrefs.getString("name_dt", " ");
        double age= Double.parseDouble(sharedPrefs.getString("age_dt", " "));
        String gender= sharedPrefs.getString("gender_dt", " ");
        double weight= Double.parseDouble(sharedPrefs.getString("weight_dt", " "));
        double ht_feet= Double.parseDouble(sharedPrefs.getString("feet_dt", " "));
        double ht_inches= Double.parseDouble(sharedPrefs.getString("inches_dt", " "));


        if(gender.equalsIgnoreCase("Male")){
            double cm=30.48*ht_feet+2.54*ht_inches;
            BMR= (13.75*weight)+(5*cm)-(6.76*age)+66;
            STRIDE_LENGTH=0.9;
        }
        else{
            double cm=30.48*ht_feet+2.54*ht_inches;
            BMR= (9.56*weight)+(1.85*cm)-(4.68*age)+655;
            STRIDE_LENGTH=0.6;
        }

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) view.findViewById(R.id.tv_steps);
        TvDist = (TextView) view.findViewById(R.id.tv_dist);
        TvSpeed = (TextView) view.findViewById(R.id.tv_walkspeed);
        Tvcal = (TextView) view.findViewById(R.id.tv_cal);


        wa=(ImageButton)view.findViewById(R.id.whatsappbtn);


        BtnStart = (Button) view.findViewById(R.id.btn_start);
        BtnStop = (Button) view.findViewById(R.id.btn_stop);
        BtnPause = (Button) view.findViewById(R.id.btn_pause);
        BtnResume = (Button) view.findViewById(R.id.btn_resume);

        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        chronometer.setText("");

        textToSpeech= new TextToSpeech(getActivity(), this);



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("start walking",0,null);

                numSteps = 0;
                sensorManager.registerListener(Tab1Fragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                chronometer.setBase(SystemClock.elapsedRealtime() );

                chronometer.start();
                stopClicked = false;




            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("stop walking",0,null);
                sensorManager.unregisterListener(Tab1Fragment.this);
                if (!stopClicked) {

                    chronometer.stop();
                    stopClicked = true;

                }

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("DATA STORE PROMPT");
                builder.setMessage("Do you want to save this walking session ?");
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        //Toast.makeText(getContext(),"Data Saved",Toast.LENGTH_SHORT).show();
                        //insert database operations over here...it will be a database insrt operation

                        db_date=new SimpleDateFormat("dd-MMM-yyyy").format(new Date()).toString();

                        //MyToastMessage.myMessage(context,(db_date+","+db_duration+","+db_steps+","+db_distance+","+db_calories).toString());



                       long response_from_db=mySQLiteAdapter.InsertRecord(db_date,db_duration,db_steps,db_distance,db_calories);

                        if (response_from_db<0) {
                            MyToastMessage.myMessage(context,"Unsuccessful insertion of records..."+id);
                        } else {
                            MyToastMessage.myMessage(context,"Successful insertion of records...");

                        }




                    }
                })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int which){
                                Toast.makeText(getContext(),"Data Not Saved",Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        });


                builder.create().show();

            }
        });

        BtnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                textToSpeech.speak("paused walking",0,null);
                sensorManager.unregisterListener(Tab1Fragment.this);
                if (!stopClicked) {

                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    int seconds = (int) timeWhenStopped / 1000;

                    chronometer.stop();
                    stopClicked = true;

                }


            }
        });

        BtnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("resumed walking",0,null);

                sensorManager.registerListener(Tab1Fragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);

                chronometer.start();
                stopClicked = false;

            }



        });


        wa.setOnClickListener(new View.OnClickListener(){

            public void onClick(View arg0){
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String msg="Hey friend ! I just walked "+TvDist.getText()+" and burned "+Tvcal.getText()+"ories. Download Walky and start beating my stats.";
                sharingIntent.putExtra(Intent.EXTRA_TEXT,msg);
                sharingIntent.setPackage("com.whatsapp");
                startActivity(sharingIntent);
            }
        });


        return view;

    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        db_steps=numSteps;
        TvSteps.setText(numSteps+" Steps");



        db_distance=STRIDE_LENGTH*numSteps;
        TvDist.setText(String.format("%.2f",(double)(db_distance))+" meters");

        String time= String.valueOf(chronometer.getText());
        db_duration=time;
        int hh,mm,ss;
        if(time.length()==5){
            mm=Integer.parseInt(time.substring(0,2));
            ss=Integer.parseInt(time.substring(3))+60*mm;
        }
        else{
            hh=Integer.parseInt(time.substring(0,2));
            mm=Integer.parseInt(time.substring(3,5));
            ss=Integer.parseInt(time.substring(7))+60*mm+60*60*hh;
        }


      TvSpeed.setText(String.format("%.2f",(double)(0.9*numSteps)/ss)+" m/sec");
        double calorie=0;
        calorie=(BMR/(24*60*60))*4*(ss);
        db_calories=Double.parseDouble(String.format("%.6f",calorie));
        Tvcal.setText(String.format("%.6f",calorie)+" cal");



    }

    @Override
    public void onInit(int status) {

    }

}
