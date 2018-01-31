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
    private float mStart = 0,mStop = 0;

    private float[] mCurPos = new float[2];

    /**
     * 要刻画的字符
     */
    protected String mText;
    protected int mTextSize = 94;

    /**
     * 每ms绘画速度
     */
    protected float speed = 0.3f;


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
        mDrawPaint.setStrokeWidth(2);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mFontPath = new Path();
        mText = "O(∩_∩)O ！";
        mTextPaint.getTextPath(mText,0,mText.length(),100,mTextPaint.getFontSpacing()+ 100, mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();

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

                mStop = mLength * mAnimatorValue;
                mPathMeasure.getPosTan(mStop, mCurPos,null);
                mPathMeasure.getSegment(mStart, mStop, mDst, true);
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
//        mAnimator.setDuration(1000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);

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

        canvas.drawCircle(mCurPos[0],mCurPos[1],5,mDrawPaint);
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
        mAnimator.setDuration((long) (mLength / speed));
        mDst = new Path();
        mAnimator.start();
    }
}
