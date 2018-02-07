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

public class TextPathView1 extends View implements View.OnClickListener {
    public static final String TAG = "TestView";
    //用于获取文字的画笔
    private Paint mTextPaint;
    //路径的画笔
    private Paint mDrawPaint;
    //画笔特效的画笔
    private Paint mPaint;
    //文字装载路径、文字绘画路径、画笔特效路径
    private Path mFontPath, mDst,mPaintPath;
    //属性动画
    private ValueAnimator mAnimator;
    //动画进度值
    private float mAnimatorValue = 0;
    //绘画部分长度
    private float mStop = 0;
    //是否展示画笔
    private boolean showPaint = false;
    //当前绘画位置
    private float[] mCurPos = new float[2];
    private PathMeasure mPathMeasure = new PathMeasure();

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
            mPathMeasure.getPosTan(mStop, mCurPos, null);
            drawPaintPath(mCurPos[0],mCurPos[1],mPaintPath);
        }
        mPathMeasure.getSegment(0, mStop, mDst, true);
        postInvalidate();
    }


    public void drawPaintPath(float x, float y, Path paintPath) {
        paintPath.addCircle(0, 0, 3, Path.Direction.CCW);

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
        paintPath.offset(x,y);

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
    }
}
