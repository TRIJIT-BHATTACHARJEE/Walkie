package com.reg.user.regis;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    Thread updateSeekBar;

    SeekBar sb;
    Button btPlay, btNxt, btPrv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay=(Button) findViewById(R.id.btPlay);
        btPrv=(Button) findViewById(R.id.btPrev);
        btNxt=(Button) findViewById(R.id.btNxt);
        sb= (SeekBar) findViewById(R.id.seekBar);

        btPlay.setOnClickListener(this);
        btPrv.setOnClickListener(this);
        btNxt.setOnClickListener(this);

        updateSeekBar=new Thread(){
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                //sb.setMax(totalDuration);
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i= getIntent();
        Bundle b=i.getExtras();
        mySongs= (ArrayList) b.getParcelableArrayList("songlist");
        position= b.getInt("pos",0);

        u= Uri.parse(mySongs.get(position).toString());
        mp= MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());

        updateSeekBar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    public void onClick(View v){
        int id=v.getId();
        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()){

                    btPlay.setText("Play");
                    mp.pause();
                }
                else {
                    mp.start();
                    btPlay.setText("Pause");
                }
                break;

            case R.id.btNxt:
                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                u= Uri.parse(mySongs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

            case R.id.btPrev:
                mp.stop();
                mp.release();
                position=(position-1<0)? mySongs.size()-1: position-1;
                u= Uri.parse(mySongs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;


        }
    }
}

