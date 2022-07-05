package com.example.winuinm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GestureView extends View {
    Path path;
    Paint paint;
    float pX;
    float pY;
    public GestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pX = event.getX();
                pY = event.getY();
                path.moveTo(event.getX(),event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                float x = (pX + event.getX()) / 2;
                float y = (pY + event.getY()) / 2;
                path.quadTo(pX,pY,x,y);
                pX = event.getX();
                pY = event.getY();
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }

    public void saveImg() {
        try {
            Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(path, paint);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            File file=new File(Environment.getExternalStorageDirectory(),simpleDateFormat.format(date)+".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reset(){
        path.reset();
        postInvalidate();
    }
}
