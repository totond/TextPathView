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
import android.graphics.Typeface;
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
    private Path mFontPath, mDst, mPaintPath;
    private float mAnimatorValue = 0, mLength = 0;
    private PathMeasure mPathMeasure = new PathMeasure();
    private ValueAnimator mAnimator;
    private float mStop = 0;

    /**
     * 要刻画的字符
     */
    protected String mText;
    protected int mTextSize = 94;

    /**
     * 每ms绘画速度
     */
    protected float speed = 0.2f;

    private float[] mCurPos = new float[2];

    private boolean showPaint = false;

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
//        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(2);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mFontPath = new Path();
        mPaintPath = new Path();
        mText = "GIEC asdas asdasgbgjut";
        mTextPaint.getTextPath(mText,0,mText.length(),100,mTextPaint.getFontSpacing()+ 100, mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();
        Log.d(TAG, "initPaint: " + mPathMeasure.getLength());

        mDst = new Path();
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                drawPaths((Float) valueAnimator.getAnimatedValue());
            }
        });


        mAnimator.addListener(new AbstractAnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                mDrawPaint.setColor(Color.RED);
//                mDrawPaint.setStyle(Paint.Style.FILL);
//                mPathMeasure.setPath(mFontPath,true);
//                postInvalidate();
                showPaint = false;
                drawPaths(1);
            }
        });
        mAnimator.setDuration(2500);
        mAnimator.setInterpolator(new LinearInterpolator());
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mAnimator.start();
        Log.d(TAG, "initPaint: ");
    }

    public void drawPaths(float progress){
        mAnimatorValue = progress;
        Log.d(TAG, "mAnimatorValue: " + mAnimatorValue);

        mPathMeasure.setPath(mFontPath,false);
        mDst.reset();
        mPaintPath.reset();
        while (mPathMeasure.nextContour()) {
            mLength = mPathMeasure.getLength();
            mStop = mLength * mAnimatorValue;
            mPathMeasure.getSegment(0, mStop, mDst, true);
            if (showPaint) {
                mPathMeasure.getPosTan(mStop, mCurPos, null);
                drawPaintPath(mCurPos[0],mCurPos[1],mPaintPath);
            }
        }
        postInvalidate();
    }

    public void drawPaintPath(float x, float y, Path paintPath) {
        paintPath.addCircle(x, y, 3, Path.Direction.CCW);
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
        canvas.drawPath(mPaintPath,mDrawPaint);
        canvas.drawPath(mDst, mDrawPaint);

//        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
//        mAnimator.cancel();
        showPaint = true;
        mDst = new Path();
        mAnimator.start();
    }
}

