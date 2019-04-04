package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import yanzhikai.textpath.painter.SyncPathPainter;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/08
 * desc   : 所有笔画按顺序绘画的TextPathView
 */

public class SyncTextPathView extends TextPathView {
    public static final String TAG = "yjkTestView";

    //画笔特效
    private SyncPathPainter mPainter;

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
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //初始化画笔
        initPaint();

        //初始化文字路径
        initPath();

        //是否自动播放动画
        if (mAutoStart) {
            startAnimation(0, 1);
        }

        //是否一开始就显示出完整的文字路径
        if (mShowInStart) {
            drawPath(0,1);
        }
    }

    @Override
    protected void initPath() {
        mDst.reset();
        mFontPath.reset();

        if (!TextUtils.isEmpty(mText)) {
            //获取宽高
            mTextWidth = Layout.getDesiredWidth(mText, mTextPaint);
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            mTextHeight = metrics.bottom - metrics.top;

            mTextPaint.getTextPath(mText, 0, mText.length(), 0, -metrics.ascent, mFontPath);
            mPathMeasure.setPath(mFontPath, false);
            mLengthSum = mPathMeasure.getLength();
            //获取所有路径的总长度
            while (mPathMeasure.nextContour()) {
                mLengthSum += mPathMeasure.getLength();
            }
        }

    }

    @Override
    public void drawPath(float start, float end) {
        mStart = validateProgress(start);
        mStop = validateProgress(end);

        mStartValue = mLengthSum * mStart;
        mEndValue = mLengthSum * mStop;

        checkFill(mStop - mStart);

        //重置路径
        mPathMeasure.setPath(mFontPath, false);
        mDst.reset();
        mPaintPath.reset();

        dividePath();

        //绘画画笔效果
        if (showPainterActually) {
            mPathMeasure.getPosTan(mEndValue, mCurPos, null);
            drawPaintPath(mCurPos[0], mCurPos[1], mPaintPath);
        }

        //绘画路径
        postInvalidate();
    }

    private void dividePath() {
        //每个片段的长度
        float segmentLength = mPathMeasure.getLength();
        //是否已经确定起点位置
        boolean findStart = false;
        while (true) {
            if (mEndValue <= segmentLength) {
                if (findStart) {
                    mPathMeasure.getSegment(0, mEndValue, mDst, true);
                } else {
                    mPathMeasure.getSegment(mStartValue, mEndValue, mDst, true);
                }
                break;
            } else {
                mEndValue -= segmentLength;
                if (!findStart) {
                    if (mStartValue <= segmentLength) {
                        mPathMeasure.getSegment(mStartValue, segmentLength, mDst, true);
                        findStart = true;
                    } else {
                        mStartValue -= segmentLength;
                    }
                } else {
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
    }


    private void drawPaintPath(float x, float y, Path paintPath) {
        if (mPainter != null) {
            mPainter.onDrawPaintPath(x, y, paintPath);
        }
    }

    //设置文字内容
    public void setText(String text) {
        mText = text;
        initPath();
        clear();
        requestLayout();
    }

    @Override
    public void startAnimation(float start, float end, int animationStyle, int repeatCount) {
        super.startAnimation(start, end, animationStyle, repeatCount);
        if (mPainter != null) {
            mPainter.onStartAnimation();
        }
    }

    //设置画笔特效
    public void setPathPainter(SyncPathPainter listener) {
        this.mPainter = listener;
    }

}
