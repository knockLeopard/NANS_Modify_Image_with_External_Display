package com.project.user.makephoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {
    static MainActivity mainActivity;
    public static Context mContext;
    SeekBar left_brightness;
    SeekBar left_opacity;
    SeekBar right_brightness;
    SeekBar right_opacity;

    int click_grey_left = 0;
    int click_grey_right = 0;
    int click_cont_left = 0;
    int click_cont_right = 0;
    int curr_progress_leftB = 127;
    int curr_progress_leftO = 255;
    int curr_progress_rightB = 127;
    int curr_progress_rightO = 255;
    final int REQ_CODE_SELECT_IMAGE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        mContext = this;
        setContentView(R.layout.activity_main);

        Button button_select = (Button) findViewById(R.id.button);
        Button button_left_grey = (Button) findViewById(R.id.button_left_grey);
        Button button_right_grey = (Button) findViewById(R.id.button_right_grey);
        Button button_left_cont = (Button) findViewById(R.id.button_left_contrast);
        Button button_right_cont = (Button) findViewById(R.id.button_right_contrast);
        Button button_save_left = (Button) findViewById(R.id.saveLeft);
        Button button_save_right = (Button) findViewById(R.id.saveRight);


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

        button_left_cont.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                click_cont_left++;
                click_grey_left = 0;
                ((ControlActivity)ControlActivity.mContext).qq(click_cont_left, click_grey_left, curr_progress_leftB, curr_progress_leftO);
            }
        });

        button_right_cont.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                click_cont_right++;
                click_grey_right = 0;
                ((ControlActivity2)ControlActivity2.mContext).qq(click_cont_right, click_grey_right, curr_progress_rightB, curr_progress_rightO);
            }
        });

        button_left_grey.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                click_grey_left++;
                click_cont_left = 0;
                ((ControlActivity)ControlActivity.mContext).qq(click_cont_left, click_grey_left, curr_progress_leftB, curr_progress_leftO);
            }
        });

        button_right_grey.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                click_grey_right++;
                click_cont_right = 0;
                ((ControlActivity2)ControlActivity2.mContext).qq(click_cont_right, click_grey_right, curr_progress_rightB, curr_progress_rightO);
            }
        });


        button_save_left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ((ControlActivity)ControlActivity.mContext).savePicture();
            }
        });
        button_save_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ((ControlActivity2)ControlActivity2.mContext).savePicture();
            }
        });

        qqleft();
        qqright();

    }
    public void qqleft(){

        left_opacity = (SeekBar) findViewById(R.id.left_opacity);
        left_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                ((ControlActivity)ControlActivity.mContext).qq(click_cont_left, click_grey_left, curr_progress_leftB, curr_progress_leftO);
               // ControlActivity.selected_image.setImageBitmap(SetSaturation(image_bitmap, current_progress_s));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                curr_progress_leftO=progress;
            }
        });
        left_brightness = (SeekBar) findViewById(R.id.left_brightness);
        left_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                ((ControlActivity)ControlActivity.mContext).qq(click_cont_left, click_grey_left, curr_progress_leftB, curr_progress_leftO);
                //selected_image.setImageBitmap(SetBrightness(image_bitmap, current_progress_l));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                curr_progress_leftB=progress;
            }
        });
    }


    public void qqright(){

        right_opacity = (SeekBar) findViewById(R.id.right_opacity);
        right_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                ((ControlActivity2)ControlActivity2.mContext).qq(click_cont_right, click_grey_right, curr_progress_rightB, curr_progress_rightO);
                // ControlActivity.selected_image.setImageBitmap(SetSaturation(image_bitmap, current_progress_s));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                curr_progress_rightO=progress;
            }
        });
        right_brightness = (SeekBar) findViewById(R.id.right_brightness);
        right_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                ((ControlActivity2)ControlActivity2.mContext).qq(click_cont_right, click_grey_right, curr_progress_rightB, curr_progress_rightO);
                //selected_image.setImageBitmap(SetBrightness(image_bitmap, current_progress_l));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                curr_progress_rightB=progress;
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


