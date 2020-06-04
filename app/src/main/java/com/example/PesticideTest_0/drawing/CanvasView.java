package com.example.PesticideTest_0.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.PesticideTest_0.drawing.HelpDraw;
import com.example.PesticideTest_0.drawing.Utils;

public class CanvasView extends View {

    //成员变量
    private Paint mGridPaint;//网格画笔
    private Paint mRedPaint;//红色画笔
    private Point mWinSize;//屏幕尺寸
    private Point mCoo;//坐标系原点
    private static float canvas_k = (float) -2.95,canvas_b= (float) 81.75,boundary=(float)5;
    public CanvasView(Context context) {
        this(context, null);

    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    //TODO init 初始化:release：
        //准备屏幕尺寸
        mWinSize = new Point();
        mCoo = new Point(50, 850);
        Utils.loadWinSize(getContext(), mWinSize);
        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint = new Paint();
        mRedPaint.setStyle(Paint.Style.FILL);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStrokeWidth(5);
    }

    public void drawModel(float k, float b, Canvas canvas, Paint paint){
        float x0=0,y1=0,x1,y0;
        y0=(k*x0+b)*(-10);//y取反
        x1=(y1-b)/k*25;
        canvas.drawLine(mCoo.x+x0,mCoo.y+y0,mCoo.x+x1,mCoo.y+y1,mRedPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO drawGrid 绘制网格：release：
        HelpDraw.drawGrid(canvas, mWinSize, mGridPaint);
        //TODO drawCoo 绘制坐标系:release：
        HelpDraw.drawCoo(canvas, mCoo, mWinSize, mGridPaint);
        //TODO drawModel
        drawModel(canvas_k,canvas_b,canvas,mRedPaint);
    }

    public static float getK(){
        return canvas_k;
    }
    public static float getB(){
        return canvas_b;
    }
    public static float getBoundary(){
        return boundary;
    }
    public static void changeK(float k){canvas_k=k;}
    public static void changeB(float b){canvas_b=b;}
}
