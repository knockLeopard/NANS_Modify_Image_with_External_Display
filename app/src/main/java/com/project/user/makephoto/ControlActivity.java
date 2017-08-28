package com.project.user.makephoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * Created by user on 2017-08-27.
 */

public class ControlActivity extends AppCompatActivity {


    int width;
    int height;
    int length;
    // create output bitmap
    Bitmap bmOut;
    // color information
    int alpha, red, green, blue;
    int pixel;
    int gap;


    static ControlActivity controlActivity;
    public static Context mContext;

    public static ImageView selected_image;
    public static Bitmap image_bitmap;
    Bitmap image_bitmap_modified;

    final int REQ_CODE_SELECT_IMAGE = 100;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlActivity = this;
        mContext = this;

        setContentView(R.layout.sub_control);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);


    }

    public void qq(int click_cont_left, int click_grey_left, int progressB, int progressO) {

        if (click_grey_left % 2 == 1) {
            image_bitmap_modified = SetGrey(image_bitmap);
        } else {
            image_bitmap_modified = image_bitmap;
        }

        if(click_cont_left % 2 == 1){
            image_bitmap_modified = SetCont(image_bitmap_modified);
        }
        else{
            ;
        }

        image_bitmap_modified = SetBrightness(image_bitmap_modified, progressB);
        image_bitmap_modified = SetOpacity(image_bitmap_modified, progressO);

        selected_image.setImageBitmap(image_bitmap_modified);
    }


    public void autoActivityChangeTo2() {


        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        int i = controlActivity.getTaskId();
        DisplayManager mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        // enumerate the displays
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        am.setExternalDisplay(i, presentationDisplays[0], am.SET_EXTERNAL_DISPLAY_AND_STAY);

        Intent intent2 = new Intent();
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent2.setComponent(new ComponentName("com.project.user.makephoto", "com.project.user.makephoto.ControlActivity2"));

        startActivity(intent2);


      //  ((MainActivity) MainActivity.mContext).setDefaultDisplay();



        //moveTaskToBack(true);
        //    List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
        //    ComponentName topActivity = Info.get(0).topActivity;
        //    String name = topActivity.getPackageName();
        //  am.setExternalDisplay(data, ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(), am.SET_EXTERNAL_DISPLAY_AND_GO_HOME);

        //finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    image_bitmap_modified = image_bitmap;
                    selected_image = (ImageView) findViewById(R.id.imageView);

                    selected_image.setImageBitmap(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        // Change to ControlActivity2



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoActivityChangeTo2();
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 100);// 0.5초 정도 딜레이를 준 후 시작

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

    public Bitmap SetCont(Bitmap src) {

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


        src.getPixels(pixel_array, 0, width, 0, 0, width, height);

        for (int i = 0; i < length; i++) {
            //A = Color.alpha(pixel);
            A = Color.alpha(pixel_array[i]);
            R = pixel_array[i] & 0x00FF;
            G = pixel_array[i] >> 8 & 0x00FF;
            B = pixel_array[i] >> 16 & 0x00FF;



            pixel_array[i] = Color.argb(A, R, G, B);
        }
        bmOut.setPixels(pixel_array, 0, width, 0, 0, width, height);

        // return final image
        return bmOut;
    }

    public void savePicture() {
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/Download");
        File temp = new File(dir, "temp.jpg");
        if (!temp.mkdirs()) {
            //Toast.makeText(controlActivity, "SHIT", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(controlActivity, root, Toast.LENGTH_SHORT).show();
        //myDir.mkdirs();
        //String fname = "Image_fuck"+ ".jpg";
        //File file = new File(myDir, fname);
//        if (file.exists()) {
//            Toast.makeText(controlActivity, "There is..?", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(controlActivity, "There is not..?", Toast.LENGTH_SHORT).show();
//        }
        //Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(temp);
            image_bitmap_modified.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Toast.makeText(controlActivity, "file saved", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void savePicture(){
        Bitmap saveBitmap = image_bitmap_modified;
        try{

            File file = new File("test.png");
            FileOutputStream fos = openFileOutput("test.png" , 0);
            saveBitmap.compress(Bitmap.CompressFormat.PNG, 100 , fos);
            fos.flush();
            fos.close();

            Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();
        }catch(Exception e) { Toast.makeText(this, "file error", Toast.LENGTH_SHORT).show();}



    }
*/

}