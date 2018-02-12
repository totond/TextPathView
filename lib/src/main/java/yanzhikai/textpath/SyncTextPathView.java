package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/08
 * desc   : 所有笔画按顺序绘画的TextPathView
 */

public class SyncTextPathView extends TextPathView {
    public static final String TAG = "yjkTestView";

    private SyncTextPainter mPainter;

    //路径长度总数
    private float mLengthSum = 0;

    public SyncTextPathView(Context context) {
        super(context);
        init();
    }

    public SyncTextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SyncTextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {

        initPaint();

        initTextPath();

        if (mAutoStart) {
            startAnimation(0,1);
        }
        if (mShowInStart){
            drawPath(1);
        }
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
        if (mPainter != null) {
            mPainter.onDrawPaintPath(x, y, paintPath);
        }

    }


    public void setText(String text) {
        mText = text;
        initTextPath();
        clear();
        requestLayout();
    }

    public void startAnimation(float start, float end) {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        initAnimator(start, end);
        initTextPath();
        canShowPainter = showPainter;
        mAnimator.start();
        if (mPainter != null) {
            mPainter.onStartAnimation();
        }
    }

    public void setTextPainter(SyncTextPainter listener) {
        this.mPainter = listener;
    }

    public interface SyncTextPainter extends TextPainter {
        void onStartAnimation();
    }
}
