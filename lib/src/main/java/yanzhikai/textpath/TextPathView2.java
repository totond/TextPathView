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
import android.view.animation.LinearInterpolator;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/10
 * desc   :
 */

public class TextPathView2 extends View implements View.OnClickListener {
    public static final String TAG = "TestView";
    private Paint mTextPaint;
    private Paint mDrawPaint;
    private Path mFontPath, mDst;
    private float mAnimatorValue = 0, mLength = 0;
    private PathMeasure mPathMeasure = new PathMeasure();
    private ValueAnimator mAnimator;
    private PathEffect mEffect;
    private float fraction = 0;
    private float mStart = 0,mStop = 0;

    /**
     * 要刻画的字符
     */
    protected String mText;
    protected int mTextSize = 94;

    /**
     * 每ms绘画速度
     */
    protected float speed = 0.2f;


    public TextPathView2(Context context) {
        super(context);
        initPaint();
    }

    public TextPathView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TextPathView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mDrawPaint.setStrokeWidth(2);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mFontPath = new Path();
        mText = "你个沙雕";
        mTextPaint.getTextPath(mText,0,mText.length(),100,mTextPaint.getFontSpacing()+ 100, mFontPath);
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

                mPathMeasure.setPath(mFontPath,false);


                while (mPathMeasure.nextContour()) {
                    mLength = mPathMeasure.getLength();
                    mStop = mLength * mAnimatorValue;
                    mPathMeasure.getSegment(0, mStop, mDst, true);
                }
//                Log.d(TAG, "onAnimationUpdate: start" + mStart);
//                mStart = mStop;

                invalidate();
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
        mAnimator.setDuration(2500);
        mAnimator.setInterpolator(new LinearInterpolator());
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

        canvas.drawPath(mDst, mDrawPaint);

//        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        mAnimator.cancel();
        initPaint();
    }
}

