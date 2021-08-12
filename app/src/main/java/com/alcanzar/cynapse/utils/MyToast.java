package com.alcanzar.cynapse.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MyToast extends AppCompatActivity {

    private static final int  LONG_DELAY = 10000;

        //TODO: this is used for the long toast display
        public static void toastLong(Activity activity , String msg)
        {
            if(!msg.isEmpty()){
                //Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
                makeLongToast(msg,activity);
            }
        }

        //TODO: this is used for the short toast display
        public static void toastShort(Activity activity,String msg)
        {
            if(!msg.isEmpty()){
                //Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
                makeLongToast(msg,activity);
            }
        }

        public static void logMsg(String tag,String msg){
            if(!msg.isEmpty()){
                Log.e(tag,msg);
            }
        }

        public static void makeLongToast(final String msg, final Context context) {
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }

}
