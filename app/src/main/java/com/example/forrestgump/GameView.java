package com.example.forrestgump;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    final int UPDATE_MILIS=30;
    Handler handler;
    Runnable runnable;
    Bitmap background;
    Display display;
    Point point;
    Rect rect1,rect2;
    Bitmap[] forrests;
    SoundPlayer sound;

    int dWidth, dHeight;
    int forrestFrame = 0;
    int forrestX, forrestY;
    int bg1y, bg2y;
    int moving;
    int frameID = 0;

    public GameView(Context context){
        super(context);
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
        forrestX = dWidth/2-forrests[0].getWidth()/2;
        forrestY = dHeight/2-forrests[0].getHeight()/2;
        bg1y = 0;
        bg2y = point.y;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        frameID++;

        if(frameID==7){
            frameID=0;
            sound.playStepSound();

        }



        if(moving>0){
            switch(moving){
                case 1:
                    forrestX-=10;
                    break;
                case 2:
                    forrestX+=10;
            }
        }

        rect1.top+=5;
        rect1.bottom+=5;
        rect2.top+=5;
        rect2.bottom+=5;
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

      //  canvas.drawBitmap(background,null,rect,null);



        if(forrestFrame == 0 && frameID%4 ==0){
            forrestFrame = 1;
        }else{
            forrestFrame = 0;
        }
        canvas.drawBitmap(forrests[forrestFrame],forrestX,forrestY,null);



        handler.postDelayed(runnable,UPDATE_MILIS);
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

