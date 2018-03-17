package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import yanzhikai.textpath.painter.SyncPathPainter;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/13
 * desc   : 所有路径按顺序绘画
 */

public class SyncPathView extends PathView {
    //画笔特效
    private SyncPathPainter mPainter;

    //路径长度总数
    private float mLengthSum = 0;
    public SyncPathView(Context context) {
        super(context);
        init();
    }

    public SyncPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SyncPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void drawPath(float progress) {
        if (!isProgressValid(progress)){
            return;
        }
        mAnimatorValue = progress;
        mStop = mLengthSum * progress;

        checkFill(progress);

        //重置路径
        mPathMeasure.setPath(mPath, false);
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

    @Override
    protected void initPath() throws Exception {
        if (mPath == null){
            throw new Exception("PathView can't work without setting a path!");
        }
        mDst.reset();


        mPathMeasure.setPath(mPath, false);
        mLengthSum = mPathMeasure.getLength();
        //获取所有路径的总长度
        while (mPathMeasure.nextContour()) {
            mLengthSum += mPathMeasure.getLength();
        }
    }

    protected void init() {

        //初始化画笔
        initPaint();

        //初始化路径
//        initPath();

    }

    @Override
    public void startAnimation(float start, float end, int animationStyle, int repeatCount) {
        super.startAnimation(start, end, animationStyle, repeatCount);
        if (mPainter != null) {
            mPainter.onStartAnimation();
        }
    }

    //设置画笔特效
    public void setPathPainter(SyncPathPainter painter) {
        this.mPainter = painter;
    }


}
