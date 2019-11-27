package com.example.forrestgump;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {
    Context parent;
    final int UPDATE_MILIS=30;
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
    Debris[] debrises = new Debris[10];
    Random rand = new Random();
    Paint paint = new Paint();
    SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();



    boolean alive = true;
    int dWidth, dHeight;
    int forrestFrame = 0;
    int forrestX, forrestY;
    int bg1y, bg2y;
    int moving;
    int frameID = 0;
    int score = 0;
    int oldScore = 0;

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
        for(int i = 0; i < 10; i++){
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(alive){

            super.onDraw(canvas);
            frameID++;

            if(frameID==7){
                frameID=0;
                sound.playStepSound();

            }





            if(moving>0){
                switch(moving){
                    case 1:
                        forrestX-=20;
                        break;
                    case 2:
                        forrestX+=20;
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
                debrises[i].y+=10;
                helperRect = new Rect(debrises[i].x,debrises[i].y,debrises[i].x+500,debrises[i].y+330);
                canvas.drawBitmap(debris[debrises[i].type],null, helperRect,null);

                if(forrestX>debrises[i].x && forrestX<debrises[i].x+500 && debrises[i].y+250 > forrestY && debrises[i].y+230 < forrestY){
                    alive = false;
                    sound.playCrashSound();
                    handler.removeCallbacksAndMessages(null);
                  //  handler.postDelayed(runnable,2000);

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

}

