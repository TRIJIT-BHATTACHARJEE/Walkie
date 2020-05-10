package com.reg.user.regis;



import android.content.Context;
import android.widget.Toast;

public class MyToastMessage {
    public static void myMessage(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
