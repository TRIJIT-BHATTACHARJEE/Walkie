package com.reg.user.regis;

/**
 * Created by USER on 17-07-2017.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.Timer;

/**
 * Created by USER on 16-07-2017.
 */

public class Tab2Fragment extends Fragment implements TextToSpeech.OnInitListener {
    private static final String TAG = "Tab2Fragment";
    private Button BtnStart,BtnStop,BtnPause,BtnFindMe,BtnTorchOn,BtnTorchOff;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private Timer timer;

    private TextToSpeech textToSpeech;

    private Camera camera;
    private Parameters parameters;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2fragment, container, false);

        BtnTorchOn = (Button) view.findViewById(R.id.btn_torch_on);
        BtnTorchOff = (Button) view.findViewById(R.id.btn_torch_off);
        BtnFindMe = (Button) view.findViewById(R.id.find_me);


        textToSpeech= new TextToSpeech(getActivity(), this );




        camera = Camera.open();
        parameters = camera.getParameters();


        BtnTorchOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /*int permission_for_camera=1;
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA);
                if(permissionCheck!=PackageManager.PERMISSION_GRANTED)
                {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            permission_for_camera);

                }


                if(permission_for_camera==1) {*/

                    Toast.makeText(getActivity(), "torch on!", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak("Torch turned on", 0, null);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();


            }



        });

        BtnTorchOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("Torch turned off",0,null);

                Toast.makeText(getActivity(), "torch off!", Toast.LENGTH_SHORT).show();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
            }



        });

        BtnFindMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                textToSpeech.speak("Finding You",0,null);
                Intent intent= new Intent(getActivity(), Find2.class);
                Toast.makeText(getActivity(), "There you are!", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });

        return view;
    }

    public void onInit(int status) {

    }
}