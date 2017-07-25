package nz.co.alan.floatingapplication;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Aland on 16/02/2017.
 */

public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView capture;
    private ImageView close;
    private ImageView grab;
    private LinearLayout layout;
    public void onCreate() {
        super.onCreate();
        capture = new ImageView(this);
        close = new ImageView(this);
        grab = new ImageView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //a face floating bubble as imageView
        capture.setImageResource(R.drawable.ic_barcode_scan_black_48dp);
        close.setImageResource(R.drawable.ic_window_close_black_48dp);
        grab.setImageResource(R.drawable.ic_cursor_pointer_black_48dp);

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        int iLayoutParam = 2002; //2002 for TYPE_PHONE and 2005 for TYPE_TOAST

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            iLayoutParam = 2005;
        }
        //here is all the science of params
        final LayoutParams myParams = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                iLayoutParam,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        // add a floatingfacebubble icon in window
        windowManager.addView(layout, myParams);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
                windowManager.removeView(layout);
                stopSelf();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(layout);
                Toast.makeText(getApplicationContext(), "Exiting Skaneet!", Toast.LENGTH_LONG).show();
                stopSelf();
            }
        });

        layout.addView(grab);
        layout.addView(capture);
        layout.addView(close);

        //windowManager.addView(floatingFaceBubble2, myParams);
        try{
            //for moving the picture on touch and slide
            layout.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    //if(System.currentTimeMillis()-touchStartTime>ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
                    //    windowManager.removeView(layout);
                    //    stopSelf();
                    //    return false;
                    //}
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
