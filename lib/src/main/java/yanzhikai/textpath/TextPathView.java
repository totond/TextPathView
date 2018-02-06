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

import java.util.ArrayList;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/08
 * desc   : a view with paths of text
 */

public class TextPathView extends View implements View.OnClickListener {
    public static final String TAG = "TestView";
    private Paint mTextPaint, mPaint;
    private Paint mDrawPaint;
    private Path mFontPath, mDst,mPaintPath;
    private float mAnimatorValue = 0, mLength = 0;
    private PathMeasure mPathMeasure = new PathMeasure();
    private ValueAnimator mAnimator;
    private float mStart = 0,mStop = 0;

    private boolean showPaint = false;

    private float[] mCurPos = new float[2];

    /**
     * 要刻画的字符
     */
    protected String mText;
    protected int mTextSize = 144;

    /**
     * 每ms绘画速度
     */
    protected float speed = 0.3f;

    private ArrayList<Float> mTextLengthSumArray = new ArrayList<>();
    private float mLengthSum = 0;

    public TextPathView(Context context) {
        super(context);
        initPaint();
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        setOnClickListener(this);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(3);
        mDrawPaint.setStyle(Paint.Style.STROKE);

        mPaintPath = new Path();

        mFontPath = new Path();
        mText = "炎之铠";
        mTextPaint.getTextPath(mText,0,mText.length(),100,mTextPaint.getFontSpacing()+ 100, mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();

        mDst = new Path();

        initTextPath();

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

        mAnimator.setDuration(10000);
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);


    }

    private void initTextPath(){
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
        float length = mLengthSum * progress;
        mDst.reset();
        mPaintPath.reset();
        while (length > mPathMeasure.getLength()){
            length = length - mPathMeasure.getLength();
            mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDst, true);
            if (!mPathMeasure.nextContour()){
                break;
            }
        }
        if (showPaint) {
            mPathMeasure.getPosTan(length, mCurPos, null);
            drawPaintPath(mCurPos[0],mCurPos[1],mPaintPath);
        }
        mPathMeasure.getSegment(0, length, mDst, true);
        postInvalidate();
    }

//    private int findPathIndex(float length){
//        int start = 0;
//        int end = mTextLengthSumArray.size() - 1;
//        int mid;
//        while (start < end){
//            mid = (start + end) / 2;
//            float midLength = mTextLengthSumArray.get(mid);
//            if (midLength > length){
//                if (mid > 0 && mTextLengthSumArray.get(mid - 1) < length){
//                    return mid - 1;
//                }else {
//                    end = mid - 1;
//                }
//            }else{
//
//            }
//        }
//    }
//
//    private boolean checkIn(int index){
//    }

    public void drawPaintPath(float x, float y, Path paintPath) {
//        paintPath.addCircle(x, y, 3, Path.Direction.CCW);
        paintPath.moveTo(x,y);
        paintPath.lineTo(x + 20, y);
        paintPath.lineTo(x, y - 20);
        paintPath.lineTo(x,y);
        paintPath.moveTo(x + 20, y);
        paintPath.lineTo(x + 120, y - 50);
        paintPath.lineTo(x + 100,y - 70);
        paintPath.lineTo(x, y - 20);

        Log.d(TAG, "drawPaintPath: ");
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
        mStart = mStop;

//        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        mAnimator.cancel();
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();
//        mAnimator.setDuration((long) (mLength / speed));
        mDst = new Path();
        showPaint = true;
        mAnimator.start();
    }
}
