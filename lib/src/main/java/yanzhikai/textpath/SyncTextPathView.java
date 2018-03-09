package yanzhikai.textpath;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/08
 * desc   : 所有笔画按顺序绘画的TextPathView
 */

public class SyncTextPathView extends TextPathView {
    public static final String TAG = "yjkTestView";

    //画笔特效
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

        //初始化画笔
        initPaint();

        //初始化文字路径
        initTextPath();

        //是否自动播放动画
        if (mAutoStart) {
            startAnimation(0,1);
        }

        //是否一开始就显示出完整的文字路径
        if (mShowInStart){
            drawPath(1);
        }
    }

    @Override
    protected void initTextPath() {
        mDst.reset();
        mFontPath.reset();
        mTextPaint.getTextPath(mText, 0, mText.length(), 0, mTextPaint.getTextSize(), mFontPath);
        mPathMeasure.setPath(mFontPath, false);
        mLengthSum = mPathMeasure.getLength();
        //获取所有路径的总长度
        while (mPathMeasure.nextContour()) {
            mLengthSum += mPathMeasure.getLength();
        }
    }


    /**
     * 绘画文字路径的方法
     * @param progress 绘画进度，0-1
     */
    @Override
    public void drawPath(float progress) {
        if (!isProgressValid(progress)){
            return;
        }
        mAnimatorValue = progress;
        mStop = mLengthSum * progress;

        checkFill(progress);

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

        Log.d(TAG, "drawPath: showPainterActually " + showPainterActually);

        //绘画画笔效果
        if (showPainterActually) {
            mPathMeasure.getPosTan(mStop, mCurPos, null);
            drawPaintPath(mCurPos[0], mCurPos[1], mPaintPath);
        }

        //绘画路径
        postInvalidate();
    }


    private void drawPaintPath(float x, float y, Path paintPath) {
        if (mPainter != null) {
            mPainter.onDrawPaintPath(x, y, paintPath);
        }
    }

    //设置文字内容
    public void setText(String text) {
        mText = text;
        initTextPath();
        clear();
        requestLayout();
    }

    @Override
    public void startAnimation(float start, float end, RepeatAnimation animationStyle, int repeatCount) {
        super.startAnimation(start, end, animationStyle, repeatCount);
        if (mPainter != null) {
            mPainter.onStartAnimation();
        }
    }

    //设置画笔特效
    public void setTextPainter(SyncTextPainter listener) {
        this.mPainter = listener;
    }

    public interface SyncTextPainter extends TextPainter {
        //开始动画的时候执行
        void onStartAnimation();

        /**
         * 绘画画笔特效时候执行
         * @param x 当前绘画点x坐标
         * @param y 当前绘画点y坐标
         * @param paintPath 画笔Path对象，在这里画出想要的画笔特效
         */
        @Override
        void onDrawPaintPath(float x, float y, Path paintPath);
    }
}
