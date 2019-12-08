package com.example.forrestgump;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static android.content.Context.SENSOR_SERVICE;

public class GameView extends View {
    Context parent;
    Twitter twitter;

    private int UPDATE_MILIS=30;
    Handler handler;
    Runnable runnable;
    Bitmap background;
    Bitmap[] forrests;
    Bitmap[] debris;
    Display display;
    Point point;
    Rect rect1,rect2;
    Rect helperRect;
    SoundPlayer sound;
    Debris[] debrises = new Debris[30];
    Random rand = new Random();
    Paint paint = new Paint();
    SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
    SensorManager sensorManager;
    Sensor accelerometer;
    SensorEventListener gyroscopeListener;
    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());


    boolean alive = true;
    int armor;
    int boots;
    int dWidth, dHeight;
    int forrestFrame = 0;
    int forrestX, forrestY;
    int bg1y, bg2y;
    int moving;
    int frameID = 0;
    int score = 0;
    int oldScore = 0;
    int timeAlive = 0;
    public GameView(Context context){

        super(context);
        parent = context;
        sound = new SoundPlayer(getContext());
        sound.playBackgroundSound();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.game_background);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect1 = new Rect(0,0,dWidth,dHeight);
        rect2 = new Rect(0,-dHeight,dWidth,0);
        forrests = new Bitmap[3];
        forrests[0] = BitmapFactory.decodeResource(getResources(),R.drawable.forrest0);
        forrests[1] = BitmapFactory.decodeResource(getResources(),R.drawable.forrest1);
        debris = new Bitmap[1];
        debris[0] = BitmapFactory.decodeResource(getResources(),R.drawable.stone);
        for(int i = 0; i < 30; i++){
            int x = rand.nextInt((500) + 1);
            int y = (-500 * i) - rand.nextInt((500) + 1) ;
           debrises[i] = new Debris(x,y,0);
        }
        forrestX = dWidth/2-forrests[0].getWidth()/2;
        forrestY = dHeight/2-forrests[0].getHeight()/2;
        bg1y = 0;
        bg2y = point.y;
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        oldScore = pref.getInt("score",-1);
        score+=oldScore;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Cursor data = databaseHelper.getItems();
        while(data.moveToNext()){
            armor =Integer.valueOf(data.getString(0));
            boots =Integer.valueOf(data.getString(1));

        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        configureTwitter();
        sendTweet(this,"Run, Forrest, run!");

        String message = "Armor: " +  String.valueOf(armor);
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.show();

        gyroscopeListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("KRUST","X: " + sensorEvent.values[0] + " Y:" + sensorEvent.values[1] + " Z:" + sensorEvent.values[2]);
              if(sensorEvent.values[0] > 0.7f){
                    moving=1;
              }else if(sensorEvent.values[0] < -0.7f){
                    moving=2;
              }else if(sensorEvent.values[0] > 0.5f || sensorEvent.values[0] < -0.5f){
                  moving = 0;
              }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(alive){
            timeAlive++;
            if(timeAlive>30){
                UPDATE_MILIS--;
                timeAlive=0;
            }
         //   if(timeAlive>100)UPDATE_MILIS=20;

            sensorManager.registerListener(gyroscopeListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            super.onDraw(canvas);
            frameID++;

            if(frameID==7){
                frameID=0;
                sound.playStepSound();

            }





            if(moving>0){
                switch(moving){
                    case 1:
                        if(forrestX > 300)
                        forrestX-=boots*6;
                        break;
                    case 2:
                        if(forrestX < dWidth-forrests[0].getWidth()-250)
                        forrestX+=boots*6;
                }
            }
            rect1.top+=10;
            rect1.bottom+=10;
            rect2.top+=10;
            rect2.bottom+=10;
            if(rect2.top>=point.y) {
                rect2.top=rect2.top-(point.y*2);
                rect2.bottom=rect2.bottom-(point.y*2);
            }
            // canvas.drawBitmap(background, 0, bg2y, null);
            canvas.drawBitmap(background,null,rect1,null);
            if(rect1.top>=point.y){
                rect1.top=rect1.top-(point.y*2);
                rect1.bottom=rect1.bottom-(point.y*2);
            }
            // canvas.drawBitmap(background, 0, bg1y, null);
            canvas.drawBitmap(background,null,rect2,null);





            for(int i = 0; i < debrises.length;i++){
                if(debrises[i].alive){
                    debrises[i].y+=10;

                    helperRect = new Rect(debrises[i].x,debrises[i].y,debrises[i].x+500,debrises[i].y+350);
                    canvas.drawBitmap(debris[debrises[i].type],null, helperRect,null);

                    if(forrestX+forrests[0].getWidth()-40>debrises[i].x &&
                            forrestX<debrises[i].x+helperRect.width()-60 &&
                            debrises[i].y+helperRect.height()-120 > forrestY &&
                            debrises[i].y-forrests[0].getHeight()+120 < forrestY){
                        debrises[i].alive = false;
                        sound.playCrashSound();
                        armor--;

                        if(armor<0){
                            handler.removeCallbacksAndMessages(null);
                            alive=false;
                            sendTweet(this,"Forrest ran " + String.valueOf(score-oldScore) + " meters");
                        }else{
                            String message = "Armor: " +  String.valueOf(armor);
                            Toast toast = Toast.makeText(getContext(),message,Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        //

                    }
                }

            }
            //  canvas.drawBitmap(background,null,rect,null);



            if(forrestFrame == 0 && frameID%4 ==0){
                forrestFrame = 1;
            }else{
                forrestFrame = 0;
            }
            canvas.drawBitmap(forrests[forrestFrame],forrestX,forrestY,null);





            canvas.drawText(String.valueOf(score), 100, 100, paint);
            score++;

            handler.postDelayed(runnable,UPDATE_MILIS);
        }else{
            editor.putInt("score", score);
            editor.commit();
            Activity activity = (Activity)getContext();
            activity.finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                Log.d("krust","down");
                if(x < 400){
                    moving = 1;
                }else{
                    moving = 2;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("krust","up");

                moving = 0;
                break;
        }

        return true;
    }

    public void configureTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("oEJEAxEPjNgWb0O35QNVaA")
                .setOAuthConsumerSecret("2TyiPmQMpnYHPE3S8ITkIQWld5fjk6jQ5eGfTsG8kg")
                .setOAuthAccessToken("927024486-4X07W3nTicx2SG0dTccqsNzraAyT1G8Ffc4VvNqN")
                .setOAuthAccessTokenSecret("neehbYt9lBY6o29UdcMLsZ1Zs9vVLPPncOpivLoyXtA");

        //cb.setUseSSL(true);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public void sendTweet(View view,String message) {

        Date d = new Date();

        try {
            Status status = twitter.updateStatus(message);
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            // oops
            e.printStackTrace();
        }

    }

}

