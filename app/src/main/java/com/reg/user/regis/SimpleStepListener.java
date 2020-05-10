package com.reg.user.regis;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Timer;


import android.hardware.Camera.Parameters;

import com.reg.user.regis.StepListener.StepListener;

public class SimpleStepListener extends AppCompatActivity implements SensorEventListener, StepListener, TextToSpeech.OnInitListener {
    private TextView TvSteps,TvDist;
    private Button BtnStart,BtnStop,BtnPause,BtnResume,BtnTorchOn,BtnTorchOff;
    private long timeWhenStopped = 0;
    private boolean stopClicked;
    private Chronometer chronometer;



    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private Timer timer;

    private TextToSpeech textToSpeech;

    private Camera camera;
    private Parameters parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_step_listener);


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        TvDist = (TextView) findViewById(R.id.tv_dist);


        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnPause = (Button) findViewById(R.id.btn_pause);
        BtnResume = (Button) findViewById(R.id.btn_resume);
        BtnTorchOn = (Button) findViewById(R.id.btn_torch_on);
        BtnTorchOff = (Button) findViewById(R.id.btn_torch_off);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setText("");

        textToSpeech= new TextToSpeech(this, this);

        camera = Camera.open();
        parameters = camera.getParameters();


        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("start walking",0,null);

                numSteps = 0;
                sensorManager.registerListener(SimpleStepListener.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                chronometer.setBase(SystemClock.elapsedRealtime() );

                chronometer.start();
                stopClicked = false;




            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("stop walking",0,null);
                sensorManager.unregisterListener(SimpleStepListener.this);
                if (!stopClicked) {

                    chronometer.stop();
                    stopClicked = true;

                }
            }
        });

        BtnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                textToSpeech.speak("paused walking",0,null);
                sensorManager.unregisterListener(SimpleStepListener.this);
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

                sensorManager.registerListener(SimpleStepListener.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);

                chronometer.start();
                stopClicked = false;

            }



        });

        BtnTorchOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("Torch turned on",0,null);


                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();




            }



        });

        BtnTorchOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("Torch off",0,null);


                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
            }



        });




    }



    @Override
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
        TvSteps.setText("Steps : " +numSteps);
        TvDist.setText("Distance : "+((int) (0.9*numSteps)));

    }

    @Override
    public void onInit(int status) {

    }
}
