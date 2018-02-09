package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/10
 * desc   :
 */

public class TextPathView2 extends TextPathView implements View.OnClickListener {
    public static final String TAG = "TestView";
    //分段路径长度
    private float mLength = 0;

    private TextPathPainter mPainter;

    public TextPathView2(Context context) {
        super(context);
        init();
    }

    public TextPathView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextPathView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        setOnClickListener(this);
        initPaint();
        initTextPath();
        initAnimator(0,1);
        if (mPainter != null){
            mPainter.onInit();
        }
        if (mAutoStart) {
            startAnimation(0,1);
        }

    }

    //初始化文字路径
    protected void initTextPath(){
        mDst.reset();
        mFontPath.reset();
        mTextPaint.getTextPath(mText,0,mText.length(),0,mTextPaint.getTextSize(), mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();
    }


    @Override
    public void drawPath(float progress){
        mAnimatorValue = progress;

        //重置路径
        mPathMeasure.setPath(mFontPath,false);
        mDst.reset();
        mPaintPath.reset();

        //根据进度获取路径
        while (mPathMeasure.nextContour()) {
            mLength = mPathMeasure.getLength();
            mStop = mLength * mAnimatorValue;
            mPathMeasure.getSegment(0, mStop, mDst, true);

            //绘画画笔效果
            if (canShowPainter) {
                mPathMeasure.getPosTan(mStop, mCurPos, null);
                drawPaintPath(mCurPos[0],mCurPos[1],mPaintPath);
            }
        }

        //绘画路径
        postInvalidate();
    }

    public void drawPaintPath(float x, float y, Path paintPath) {
        paintPath.addCircle(x, y, 3, Path.Direction.CCW);
        if (mPainter != null){
            mPainter.onDrawPaintPath(x,y,paintPath);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canShowPainter) {
            canvas.drawPath(mPaintPath, mDrawPaint);
        }
        canvas.drawPath(mDst, mDrawPaint);

    }

    public void startAnimation(float start, float end){
        initAnimator(start, end);
        initTextPath();
        canShowPainter = showPainter;
        mAnimator.start();
        if (mPainter != null){
            mPainter.onStartAnimation();
        }
    }

    public void setListener(TextPathPainter listener) {
        this.mPainter = listener;
    }

    @Override
    public void onClick(View v) {
        startAnimation(0,1);
    }
}

