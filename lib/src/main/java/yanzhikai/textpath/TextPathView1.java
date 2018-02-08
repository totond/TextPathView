package yanzhikai.textpath;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/08
 * desc   : a view with paths of text
 */

public class TextPathView1 extends TextPathView implements View.OnClickListener {
    public static final String TAG = "TestView";


    private ArrayList<Float> mTextLengthSumArray = new ArrayList<>();
    private float mLengthSum = 0;

    protected VelocityCalculator mVelocityCalculator;

    private float radius = 40;
    private double angle = Math.PI / 6;

    public TextPathView1(Context context) {
        super(context);
        initPaint();
    }

    public TextPathView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TextPathView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);

        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(mTextStrokeWidth);
        mDrawPaint.setStyle(Paint.Style.STROKE);

        mPaintPath = new Path();

        mFontPath = new Path();

        mDst = new Path();

        initTextPath();

        mVelocityCalculator = new VelocityCalculator();

        mAnimator = ValueAnimator.ofFloat(0, 1);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                Log.d(TAG, "mAnimatorValue: " + mAnimatorValue);
//                if (mAnimatorValue == 1 && mPathMeasure.nextContour()) {
//                    mLength = mPathMeasure.getLength();
//                    Log.d(TAG, "mAnimatorValue: reset" + mAnimatorValue);
//
//                    mAnimator.start();
//                }else {

//                mStop = mLength * mAnimatorValue;
//                mPathMeasure.getPosTan(mStop, mCurPos,null);
//                mPathMeasure.getSegment(mStart, mStop, mDst, true);
//                    invalidate();
//                }
                drawPath(mAnimatorValue);
            }
        });
        mAnimator.addListener(new AbstractAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showPaint = false;
                drawPath(1);
            }
        });

        mAnimator.setDuration(mDuration);
        mAnimator.setInterpolator(new LinearInterpolator());
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);

        setOnClickListener(this);
    }

    private void initTextPath(){
        mTextPaint.getTextPath(mText,0,mText.length(),0,mTextPaint.getFontSpacing(), mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mTextLengthSumArray.clear();
        mLengthSum = mPathMeasure.getLength();
        mTextLengthSumArray.add(mLengthSum);
        while (mPathMeasure.nextContour()){
            mLengthSum += mPathMeasure.getLength();
            mTextLengthSumArray.add(mLengthSum);
        }

//        for (float f : mTextLengthSumArray){
//            Log.d(TAG, "length: " + f);
//        }
    }

    public void drawPath(float progress){
        mPathMeasure.setPath(mFontPath,false);
        mStop = mLengthSum * progress;
        mDst.reset();
        mPaintPath.reset();
        while (mStop > mPathMeasure.getLength()){
            mStop = mStop - mPathMeasure.getLength();
            mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDst, true);
            if (!mPathMeasure.nextContour()){
                break;
            }
        }
        if (showPaint) {
            mPathMeasure.getPosTan(mStop, mCurPos, mCurTan);
            drawPaintPath(mCurPos[0],mCurPos[1],mPaintPath);
        }
        mPathMeasure.getSegment(0, mStop, mDst, true);
        postInvalidate();
    }


    public void drawPaintPath(float x, float y, Path paintPath) {
//        paintPath.addCircle(0, 0, 3, Path.Direction.CCW);

//        paintPath.moveTo(x,y);
//        paintPath.lineTo(x + 20, y);
//        paintPath.lineTo(x, y - 20);
//        paintPath.lineTo(x,y);
//        paintPath.moveTo(x + 20, y);
//        paintPath.lineTo(x + 120, y - 50);
//        paintPath.lineTo(x + 100,y - 70);
//        paintPath.lineTo(x, y - 20);

//        paintPath.lineTo(20, 0);
//        paintPath.lineTo(0, -20);
//        paintPath.lineTo(0,0);
//        paintPath.moveTo(20, 0);
//        paintPath.lineTo(120, -50);
//        paintPath.lineTo(100,-70);
//        paintPath.lineTo(0, -20);
//        paintPath.offset(x,y);

        mVelocityCalculator.calculate(x,y);
//        float tan = mVelocityCalculator.getVelocityY() / mVelocityCalculator.getVelocityX();
        double angleV = Math.atan2(mVelocityCalculator.getVelocityY(),mVelocityCalculator.getVelocityX());
//        double angleV = Math.atan(tan);
//        double angle0 = Math.atan(mCurTan[0]);
//        double angle1 = Math.atan(mCurTan[1]);
//        paintPath.lineTo((float) (x - 50 * Math.cos(angle0)),(float) (y - 50 * Math.sin(angle0)));
        Log.d(TAG, "angleV: " + (angleV / Math.PI) * 180);
        double delta = angleV - angle;
        double sum = angleV + angle;
        double rr = radius / (2 * Math.cos(angle));
        float x1 = (float) (rr * Math.cos(sum));
        float y1 = (float) (rr * Math.sin(sum));
        float x2 = (float) (rr * Math.cos(delta));
        float y2 = (float) (rr * Math.sin(delta));

        paintPath.moveTo(x, y);
        paintPath.lineTo(x - x1, y - y1);
        paintPath.moveTo(x,y);
        paintPath.lineTo(x - x2, y -y2);

//        paintPath.lineTo((float) (x - radius * Math.cos(delta)), (float) (y - radius * Math.sin(delta)));

//        if (mVelocityCalculator.getVelocityX() < 0) {
//            paintPath.lineTo((float) (x + radius * Math.sin(angle) * Math.cos(delta)), (float) (y - radius * (1 - Math.sin(angle) * Math.sin(delta))));
//        }else {
//            paintPath.lineTo((float) (x - radius * Math.cos(angle) * Math.cos(delta)), (float) (y - radius * Math.sin(angle) * Math.sin(delta)));
//        }
//        paintPath.moveTo(x, y);

//        paintPath.lineTo((float) (x - radius * Math.sin(delta)), (float) (y - radius * Math.cos(delta)));
//        paintPath.lineTo((float) (x - 50 * Math.cos(angle1)),(float) (y + 50 * Math.sin(angle1)));
//        if (mVelocityCalculator.getVelocityY() < 0){
//            paintPath.lineTo((float) (x - radius * Math.sin(angle) * Math.cos(sum)), (float) (y - radius * (1 - Math.sin(angle) * Math.sin(delta))));
//        }else {
//            paintPath.lineTo((float) (x + radius * Math.sin(angle) * Math.sin(delta)), (float) (y - radius * Math.cos(angle) * Math.cos(delta)));
//        }



        Log.d(TAG, "drawPaintPath: " + mVelocityCalculator.getVelocityX());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawText("GT",0,100,mDrawPaint);
//        canvas.drawPath(mFontPath,mDrawPaint);

//        mDst.reset();
        // 硬件加速的BUG
//        mDst.lineTo(0,0);

//        canvas.drawCircle(mCurPos[0],mCurPos[1],5,mDrawPaint);
        canvas.drawPath(mPaintPath,mDrawPaint);
        canvas.drawPath(mDst, mDrawPaint);

//        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        mAnimator.cancel();
        mPathMeasure.setPath(mFontPath,false);
//        mAnimator.setDuration((long) (mLength / speed));
        mDst = new Path();
        showPaint = true;
        mAnimator.start();
        mVelocityCalculator.reset();
    }
}
