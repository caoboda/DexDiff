package com.example.view_day_03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
/**
 * Created by cbd on 2021/4/8 16:19
 */
public class QQStepView extends View {
    private  Paint mOutPaint;
    private  Paint mInnerPaint ;
    private  Paint mTextPaint;
    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20;// 20px
    private int mStepTextSize;
    private int mStepTextColor;


    // 总共的，当前的步数
    private int mCurrentStep = 0;
    private int mStepMax = 0;

    public QQStepView(Context context) {
       this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 1.分析效果；
        // 2.确定自定义属性，编写attrs.xml
        // 3.在布局中使用
        // 4.在自定义View中获取自定义属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.QQStepView);
        mOuterColor=typedArray.getColor(R.styleable.QQStepView_outerColor,mOuterColor);
        mInnerColor=typedArray.getColor(R.styleable.QQStepView_innerColor,mInnerColor);
        mBorderWidth=(int)typedArray.getDimension(R.styleable.QQStepView_borderWidth,mBorderWidth);
        mStepTextSize=typedArray.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize,mStepTextSize);
        mStepTextColor=typedArray.getColor(R.styleable.QQStepView_stepTextColor,mStepTextColor);
        typedArray.recycle();

        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStyle(Paint.Style.STROKE);//空心
        //setStrokeCap
        //设置画笔的线冒样式：
        //Paint.Cap.BUTT：无
        //Paint.Cap.SQUARE：方形
        //Paint.Cap.ROUND： 半圆形
        //注意： Paint.Cap.ROUND、Paint.Cap.SQUARE 会在线长度的基础上首尾添加一个通过 setStrokeWidth 设置的宽度。
        //示例如下：依次为 无设置、Paint.Cap.BUTT、Paint.Cap.SQUARE、Paint.Cap.ROUND。
        //可以看到 ROUND 和 SQUARE 样式的明显长一点。
        //原文链接：https://blog.csdn.net/lxk_1993/article/details/102936227
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);//空心
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
        // 5.onMeasure()
        // 6.画外圆弧 ，内圆弧 ，文字
        // 7.其他
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 调用者在布局文件中可能  wrap_content
        // 获取模式 AT_MOST

        // 宽度高度不一致 取最小值，确保是个正方形
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        //取最小值
        setMeasuredDimension(width>height?height:width,width>height?height:width);
    }

    //6.画外圆弧 ，内圆弧 ，文字
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 6.1 画外圆弧    分析：圆弧闭合了  思考：边缘没显示完整  描边有宽度 mBorderWidth  圆弧
         int center = getWidth()/2;
         int radius = getWidth()/2 - mBorderWidth/2;
         RectF oval = new RectF(center-radius,center-radius
         ,center+radius,center+radius);
        //float left, float top, float right, float bottom
       // RectF oval=new RectF(mBorderWidth/2,mBorderWidth/2,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(oval,135,270,false,mOutPaint);

        // 6.2 画内圆弧  怎么画肯定不能写死  百分比  是使用者设置的从外面传
        if(mStepMax==0){
            return;
        }
        float sweepAngleRatio=(float) mCurrentStep/mStepMax;
        canvas.drawArc(oval,135,270*sweepAngleRatio,false,mInnerPaint);
        // 6.3 画文字
        String mText=mCurrentStep+"";
        Rect textBounds =new Rect();
        mTextPaint.getTextBounds(mText,0,mText.length(),textBounds);
        //  getWidth()控件宽 getHeight()控件高
        int dx=getWidth()/2-textBounds.width()/2;

        // 基线 baseLine
        Paint.FontMetricsInt  fontMetrics = mTextPaint.getFontMetricsInt();
        int dy=(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom;
        int baseLine=getHeight()/2+dy;

        canvas.drawText(mText,dx,baseLine,mTextPaint);
    }




    public synchronized void setStepMax(int mStepMax) {
        this.mStepMax = mStepMax;
    }


    public synchronized void setCurrentStep(int mCurrentStep) {
        this.mCurrentStep = mCurrentStep;
        // 不断绘制  onDraw()
        invalidate();
    }
}
