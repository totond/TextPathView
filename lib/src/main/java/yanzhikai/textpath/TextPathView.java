package yanzhikai.textpath;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

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
    private Path mFontPath, mDst,mPath;
    private float mAnimatorValue = 0, mLength = 0;
    private PathMeasure mPathMeasure = new PathMeasure();
    private ValueAnimator mAnimator;
    private PathEffect mEffect;
    private float fraction = 0;
    private float mStart = 0,mStop = 0;

    /**
     * 每ms绘画速度
     */
    private float speed = 0.2f;


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
        mTextPaint.setTextSize(94);
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(2);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mFontPath = new Path();
        String text = "O(∩_∩)O ！";
        mTextPaint.getTextPath(text,0,text.length(),100,mTextPaint.getFontSpacing()+ 100, mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();
        Log.d(TAG, "initPaint: " + mPathMeasure.getLength());

        mDst = new Path();
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
                    invalidate();
//                }
            }
        });
        mAnimator.addListener(new AbstractAnimatorListener() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if (mPathMeasure.nextContour()){
                    mLength = mPathMeasure.getLength();
                    mAnimator.setDuration((long) (mLength / speed));
                    Log.d(TAG, "time" + (mLength / speed));
                    mAnimator.start();
                }else {
                    mAnimator.cancel();
                }
            }
        });
        mAnimator.setDuration(1000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    private void initDPaint(){
//        mPath = new Path();
//        mPath.reset();
//        mPath.moveTo(100, 100);
//        mPath.lineTo(100, 500);
//        mPath.lineTo(400, 300);
//        mPath.close();

        mTextPaint = new Paint();
        mTextPaint.setTextSize(84);
        mPath = new Path();
        mTextPaint.getTextPath("GG",0,1,100,mTextPaint.getFontSpacing()+ 100, mPath);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPathMeasure = new PathMeasure(mPath, false);
        final float length = mPathMeasure.getLength();

        mAnimator = ValueAnimator.ofFloat(1, 0);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(10000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = (float) valueAnimator.getAnimatedValue();
                mEffect = new DashPathEffect(new float[]{length, length}, fraction * length);
                mPaint.setPathEffect(mEffect);
                postInvalidate();
            }
        });
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();

    }

    private void nextOutline(){
        if (mPathMeasure.nextContour()){
            mLength = mPathMeasure.getLength();
            Log.d(TAG, "nextOutline: reset" + mAnimatorValue);
            mAnimator.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawText("GT",0,100,mDrawPaint);
//        canvas.drawPath(mFontPath,mDrawPaint);

//        mDst.reset();
        // 硬件加速的BUG
//        mDst.lineTo(0,0);
        mStop = mLength * mAnimatorValue;
        mPathMeasure.getSegment(mStart, mStop, mDst, true);
        canvas.drawPath(mDst, mDrawPaint);
        mStart = mStop;

//        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        mAnimator.cancel();
        initPaint();
    }
}
