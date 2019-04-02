package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import yanzhikai.textpath.painter.SyncPathPainter;

/**
 * author : totond
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
    public void drawPath(float start, float end) {
        mStart = validateProgress(start);
        mStop = validateProgress(end);

        mStartValue = mLengthSum * mStart;
        mEndValue = mLengthSum * mStop;

        checkFill(end);

        //重置路径
        mPathMeasure.setPath(mPath, false);
        mDst.reset();
        mPaintPath.reset();

//每个片段的长度
        float segmentLength = mPathMeasure.getLength();
        //是否已经确定起点位置
        boolean findStart = false;
        while (true){
            Log.i(TAG, "drawPath: mEndValue: " + mEndValue + " segmentLength: " + segmentLength);
            if (mEndValue <= segmentLength) {
                Log.i(TAG, "drawPath: 找到终点，findStart: " + findStart + " mSV: " + mStartValue);
                if (findStart){
                    mPathMeasure.getSegment(0, mEndValue, mDst, true);
                }else {
                    mPathMeasure.getSegment(mStartValue, mEndValue, mDst, true);
                }
                break;
            }else {
                mEndValue -= segmentLength;
                if (!findStart) {
                    if (mStartValue <= segmentLength) {
                        Log.i(TAG, "drawPath: 找到起点");
                        mPathMeasure.getSegment(mStartValue, segmentLength, mDst, true);
                        findStart = true;
                    } else {
                        Log.i(TAG, "drawPath: 下一段找起点");
                        mStartValue -= segmentLength;
                    }
                }else {
                    Log.i(TAG, "drawPath: 找到起点后补充");
                    mPathMeasure.getSegment(0, segmentLength, mDst, true);
                }
            }
            if (!mPathMeasure.nextContour()) {
                //todo 一些精度误差处理
                break;
            } else {
                //获取下一段path长度
                segmentLength = mPathMeasure.getLength();
            }
        }


        //绘画画笔效果
        if (showPainterActually) {
            mPathMeasure.getPosTan(mEndValue, mCurPos, null);
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
        if (mPath == null) {
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
