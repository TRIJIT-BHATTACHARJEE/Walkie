package com.reg.user.regis;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MyDialogFragment extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    public void ShowDialog(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("DATA STORE PROMPT");
        builder.setMessage("Do you want to save this walking session ?");
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id){
                Toast.makeText(getApplicationContext(),"OK clicked",Toast.LENGTH_SHORT).show();
                finish();
            }
        })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        Toast.makeText(getApplicationContext(),"Cancel was clicked",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        builder.create().show();
    }
}
