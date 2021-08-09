package com.alcanzar.cynapse.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;

import static android.widget.Toast.LENGTH_SHORT;

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
