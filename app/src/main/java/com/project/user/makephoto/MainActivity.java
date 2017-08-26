package com.project.user.makephoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {
    static MainActivity mainActivity;
    public static Context mContext;
    SeekBar left_luminosity;
    SeekBar left_saturation;
   // ImageView selected_image;
    Bitmap image_bitmap;
    int current_progress_l;
    int current_progress_s;
    final int REQ_CODE_SELECT_IMAGE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        mContext = this;
        setContentView(R.layout.activity_main);

        Button button_select = (Button) findViewById(R.id.button);

        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent2.setComponent(new ComponentName("com.project.user.makephoto", "com.project.user.makephoto.ControlActivity"));
                startActivity(intent2);
            }
        });
        qq();

    }
    public void qq(){

        left_saturation = (SeekBar) findViewById(R.id.left_saturation);
        left_saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                ((ControlActivity)ControlActivity.mContext).qq(left_saturation, left_luminosity);
               // ControlActivity.selected_image.setImageBitmap(SetSaturation(image_bitmap, current_progress_s));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                current_progress_s=progress;
            }
        });
        left_luminosity = (SeekBar) findViewById(R.id.left_luminosity);
        left_luminosity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                ((ControlActivity)ControlActivity.mContext).qq(left_saturation, left_luminosity);
                //selected_image.setImageBitmap(SetBrightness(image_bitmap, current_progress_l));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                current_progress_l=progress;
            }
        });
    }

    public void setDefaultDisplay(){

        Intent intent2 = new Intent();
           intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.setComponent(new ComponentName("com.project.user.makephoto", "com.project.user.makephoto.MainActivity"));
        startActivity(intent2);


        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        int i = mainActivity.getTaskId();
        DisplayManager mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        // enumerate the displays
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        //am.setExternalDisplay(i, getWindowManager().getDefaultDisplay(), am.SET_EXTERNAL_DISPLAY_AND_STAY);
        //moveTaskToBack(true);

    }
}


