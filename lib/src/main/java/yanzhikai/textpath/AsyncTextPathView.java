package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/10
 * desc   : 所有笔画异步一起绘画的TextPathView
 */

public class AsyncTextPathView extends TextPathView {
    //分段路径长度
    private float mLength = 0;

    //画笔特效
    private AsyncTextPainter mPainter;

    public AsyncTextPathView(Context context) {
        super(context);
        init();
    }

    public AsyncTextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AsyncTextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        initPaint();
        initTextPath();
        if (mAutoStart) {
            startAnimation(0,1);
        }
        if (mShowInStart){
            drawPath(1);
        }

    }

    //初始化文字路径
    @Override
    protected void initTextPath(){
        mDst.reset();
        mFontPath.reset();
        mTextPaint.getTextPath(mText,0,mText.length(),0,mTextPaint.getTextSize(), mFontPath);
        mPathMeasure.setPath(mFontPath,false);
        mLength = mPathMeasure.getLength();
    }


    /**
     * 绘画文字路径的方法
     * @param progress 绘画进度，0-1
     */
    @Override
    public void drawPath(float progress){
        if (!isProgressValid(progress)){
            return;
        }
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

    private void drawPaintPath(float x, float y, Path paintPath) {
        if (mPainter != null){
            mPainter.onDrawPaintPath(x,y,paintPath);
        }
    }

    //设置文字内容
    public void setText(String text) {
        mText = text;
        initTextPath();
        clear();
        requestLayout();
    }

    /**
     * 开始绘制文字路径动画
     * @param start 路径比例，范围0-1
     * @param end 路径比例，范围0-1
     */
    public void startAnimation(float start, float end){
        if (!isProgressValid(start) || !isProgressValid(end)){
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        initAnimator(start, end);
        initTextPath();
        canShowPainter = showPainter;
        mAnimator.start();
    }

    //设置画笔特效
    public void setTextPainter(AsyncTextPainter listener) {
        this.mPainter = listener;
    }

    public interface AsyncTextPainter extends TextPainter{
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

