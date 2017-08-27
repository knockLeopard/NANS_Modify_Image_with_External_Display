package com.project.user.makephoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by user on 2017-08-28.
 */

public class ControlActivity2  extends AppCompatActivity {

    int width;
    int height;
    int length;
    // create output bitmap
    Bitmap bmOut;
    // color information
    int alpha, red, green, blue;
    int pixel;
    int gap;


    static ControlActivity2 controlActivity2;
    public static Context mContext;

    public static ImageView selected_image;
    Bitmap image_bitmap;
    Bitmap image_bitmap_modified;

    final int REQ_CODE_SELECT_IMAGE = 100;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlActivity2 = this;
        mContext = this;

        setContentView(R.layout.sub_control2);

        image_bitmap = ControlActivity.image_bitmap;
        image_bitmap_modified = image_bitmap;
        selected_image = (ImageView) findViewById(R.id.imageView2);
        selected_image.setImageBitmap(image_bitmap);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                autoActivityChangeToMainActivity();


                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 100);// 0.5초 정도 딜레이를 준 후 시작

    }


    public void qq(int click_grey_left, int progressB, int progressO) {

        if (click_grey_left % 2 == 1) {
            image_bitmap_modified = SetGrey(image_bitmap);
        } else {
            image_bitmap_modified = image_bitmap;
        }

        image_bitmap_modified = SetBrightness(image_bitmap_modified, progressB);
        image_bitmap_modified = SetOpacity(image_bitmap_modified, progressO);

        selected_image.setImageBitmap(image_bitmap_modified);
    }


    public void autoActivityChangeToMainActivity() {


        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        int i = controlActivity2.getTaskId();
        DisplayManager mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        // enumerate the displays
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        am.setExternalDisplay(i, presentationDisplays[1], am.SET_EXTERNAL_DISPLAY_AND_STAY);


        ((MainActivity) MainActivity.mContext).setDefaultDisplay();


        //moveTaskToBack(true);
        //    List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
        //    ComponentName topActivity = Info.get(0).topActivity;
        //    String name = topActivity.getPackageName();
        //  am.setExternalDisplay(data, ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(), am.SET_EXTERNAL_DISPLAY_AND_GO_HOME);

        //finish();
    }


    public Bitmap SetBrightness(final Bitmap src, int value) {

        // original image size
        width = src.getWidth();
        height = src.getHeight();
        length = width * height;
        // create output bitmap
        bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        int[] pixel_array = new int[width * height];
        gap = value - 127;
        Toast.makeText(getBaseContext(), "Brit Bar : " + value, Toast.LENGTH_SHORT).show();

        src.getPixels(pixel_array, 0, width, 0, 0, width, height);

        for (int i = 0; i < length; i++) {
            A = Color.alpha(pixel_array[i]);
            R = Color.red(pixel_array[i]);
            G = Color.green(pixel_array[i]);
            B = Color.blue(pixel_array[i]);

            // increase/decrease each channel
            R += gap;
            if (R > 255) {
                R = 255;
            } else if (R < 0) {
                R = 0;
            }

            G += gap;
            if (G > 255) {
                G = 255;
            } else if (G < 0) {
                G = 0;
            }

            B += gap;
            if (B > 255) {
                B = 255;
            } else if (B < 0) {
                B = 0;
            }
            pixel_array[i] = Color.argb(A, R, G, B);
        }
        bmOut.setPixels(pixel_array, 0, width, 0, 0, width, height);

        int pixel_test = bmOut.getPixel(250, 250);
        Toast.makeText(getBaseContext(), "Brit func R : " + Color.red(pixel_test) + "G : " + Color.green(pixel_test) + "B : " + Color.blue(pixel_test) + "alpha : " + Color.alpha(pixel_test), Toast.LENGTH_SHORT).show();

        // return final image
        return bmOut;

    }

    public Bitmap SetOpacity(Bitmap src, int value) {
        // original image size
        int width = src.getWidth();
        int height = src.getHeight();
        length = width * height;
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        int gap = value - 255;
        int[] pixel_array = new int[width * height];

        Toast.makeText(getBaseContext(), "Opac Bar : " + value, Toast.LENGTH_SHORT).show();
        // scan through all pixels

        src.getPixels(pixel_array, 0, width, 0, 0, width, height);

        for (int i = 0; i < length; i++) {
            //A = Color.alpha(pixel);
            A = Color.alpha(pixel_array[i]);
            R = Color.red(pixel_array[i]);
            G = Color.green(pixel_array[i]);
            B = Color.blue(pixel_array[i]);

            A += gap;
            if (A > 255) {
                A = 255;
            } else if (A < 0) {
                A = 0;
            }

            pixel_array[i] = Color.argb(A, R, G, B);
        }
        bmOut.setPixels(pixel_array, 0, width, 0, 0, width, height);

        int pixel_test = bmOut.getPixel(250, 250);


        Toast.makeText(getBaseContext(), "Opac func R : " + Color.red(pixel_test) + "G : " + Color.green(pixel_test) + "B : " + Color.blue(pixel_test) + "alpha : " + Color.alpha(pixel_test), Toast.LENGTH_SHORT).show();

        // return final image
        return bmOut;
    }


    public Bitmap SetGrey(Bitmap src) {

        // original image size
        width = src.getWidth();
        height = src.getHeight();
        length = width * height;
        // create output bitmap
        bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int avgRGB;
        int pixel;
        int[] pixel_array = new int[width * height];
        // Toast.makeText(getBaseContext(), "Brit Bar : " + value,Toast.LENGTH_SHORT).show();


        src.getPixels(pixel_array, 0, width, 0, 0, width, height);

        for (int i = 0; i < length; i++) {
            //A = Color.alpha(pixel);
            A = Color.alpha(pixel_array[i]);
            R = Color.red(pixel_array[i]);
            G = Color.green(pixel_array[i]);
            B = Color.blue(pixel_array[i]);

            avgRGB = (R + G + B) / 3;

            pixel_array[i] = Color.argb(A, avgRGB, avgRGB, avgRGB);
        }
        bmOut.setPixels(pixel_array, 0, width, 0, 0, width, height);

        // return final image
        return bmOut;
    }
}
