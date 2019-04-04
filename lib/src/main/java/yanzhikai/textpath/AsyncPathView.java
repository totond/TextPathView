package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import yanzhikai.textpath.painter.AsyncPathPainter;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/14
 * desc   : 所有路径一起绘画
 */

public class AsyncPathView extends PathView {
    //分段路径长度
    private float mLength = 0;

    //画笔特效
    private AsyncPathPainter mPainter;

    public AsyncPathView(Context context) {
        super(context);
        init();
    }

    public AsyncPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AsyncPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        initPaint();
    }

    //初始化文字路径
    @Override
    protected void initPath(){
        if (mPath == null){
            throw new RuntimeException("PathView can't work without setting a path!");
        }
        mDst.reset();
        mPathMeasure.setPath(mPath, false);
    }


    @Override
    public void drawPath(float start, float end) {
        mStart = validateProgress(start);
        mStop = validateProgress(end);

        checkFill(mStop - mStart);

        //重置路径
        mPathMeasure.setPath(mPath,false);
        mDst.reset();
        mPaintPath.reset();

        boolean hasMore = true;
        //根据进度获取路径
        while (hasMore) {
            mLength = mPathMeasure.getLength();
            mStartValue = mLength * mStart;
            mEndValue = mLength * mStop;
            mPathMeasure.getSegment(mStartValue, mEndValue, mDst, true);

            //绘画画笔效果
            if (showPainterActually) {
                mPathMeasure.getPosTan(mEndValue, mCurPos, null);
                drawPaintPath(mCurPos[0],mCurPos[1],mPaintPath);
            }
            hasMore = mPathMeasure.nextContour();
        }

        //绘画路径
        postInvalidate();
    }

    private void drawPaintPath(float x, float y, Path paintPath) {
        if (mPainter != null){
            mPainter.onDrawPaintPath(x,y,paintPath);
        }
    }


    //设置画笔特效
    public void setPainter(AsyncPathPainter painter) {
        this.mPainter = painter;
    }


}

