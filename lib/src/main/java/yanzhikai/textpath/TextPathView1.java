package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/08
 * desc   : a view with paths of text
 */

public class TextPathView1 extends TextPathView implements View.OnClickListener {
    public static final String TAG = "TestView";

    private TextPathPainter mPainter;

    //路径长度总数
    private float mLengthSum = 0;

    protected VelocityCalculator mVelocityCalculator;

    private float radius = 40;
    private double angle = Math.PI / 12;

    public TextPathView1(Context context) {
        super(context);
        init();
    }

    public TextPathView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextPathView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {

        initPaint();

        initTextPath();

//        mVelocityCalculator = new VelocityCalculator();

        initAnimator(0, 1);

        if (mPainter != null) {
            mPainter.onInit();
        }
        if (mAutoStart) {
            startAnimation(0,1);
        }
        setOnClickListener(this);
    }


    private void initTextPath() {
        mDst.reset();
        mFontPath.reset();
        mTextPaint.getTextPath(mText, 0, mText.length(), 0, mTextPaint.getTextSize(), mFontPath);
        mPathMeasure.setPath(mFontPath, false);
        mLengthSum = mPathMeasure.getLength();
        while (mPathMeasure.nextContour()) {
            mLengthSum += mPathMeasure.getLength();
        }
    }


    @Override
    public void drawPath(float progress) {
        mStop = mLengthSum * progress;

        //重置路径
        mPathMeasure.setPath(mFontPath, false);
        mDst.reset();
        mPaintPath.reset();

        //根据进度获取路径
        while (mStop > mPathMeasure.getLength()) {
            mStop = mStop - mPathMeasure.getLength();
            mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDst, true);
            if (!mPathMeasure.nextContour()) {
                break;
            }
        }
        mPathMeasure.getSegment(0, mStop, mDst, true);

        //绘画画笔效果
        if (canShowPainter) {
            mPathMeasure.getPosTan(mStop, mCurPos, null);
            drawPaintPath(mCurPos[0], mCurPos[1], mPaintPath);
        }

        //绘画路径
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

//        mVelocityCalculator.calculate(x, y);
//        double angleV = Math.atan2(mVelocityCalculator.getVelocityY(), mVelocityCalculator.getVelocityX());
//        double delta = angleV - angle;
//        double sum = angleV + angle;
//        double rr = radius / (2 * Math.cos(angle));
//        float x1 = (float) (rr * Math.cos(sum));
//        float y1 = (float) (rr * Math.sin(sum));
//        float x2 = (float) (rr * Math.cos(delta));
//        float y2 = (float) (rr * Math.sin(delta));
//
//        paintPath.moveTo(x, y);
//        paintPath.lineTo(x - x1, y - y1);
//        paintPath.moveTo(x, y);
//        paintPath.lineTo(x - x2, y - y2);

        if (mPainter != null) {
            mPainter.onDrawPaintPath(x, y, paintPath);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPaintPath, mPaint);
        canvas.drawPath(mDst, mDrawPaint);

    }

    public void setText(String text) {
        mText = text;
        initTextPath();
        clear();
        requestLayout();
    }

    public void startAnimation(float start, float end) {
        initAnimator(start, end);
        initTextPath();
        canShowPainter = showPainter;
//        mVelocityCalculator.reset();
        mAnimator.start();
        if (mPainter != null) {
            mPainter.onStartAnimation();
        }
    }

    public void setListener(TextPathPainter listener) {
        this.mPainter = listener;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        startAnimation(0, 1);
    }
}
