package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import yanzhikai.textpath.painter.AsyncPathPainter;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/10
 * desc   : 所有笔画异步一起绘画的TextPathView
 */

public class AsyncTextPathView extends TextPathView {
    //分段路径长度
    private float mLength = 0;

    //画笔特效
    private AsyncPathPainter mPainter;

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
        setLayerType(LAYER_TYPE_SOFTWARE,null);

        initPaint();
        initPath();
        if (mAutoStart) {
            startAnimation(0,1);
        }
        if (mShowInStart){
            drawPath(1);
        }

    }

    //初始化文字路径
    @Override
    protected void initPath(){
        mDst.reset();
        mFontPath.reset();

        if (!TextUtils.isEmpty(mText)) {
            //获取宽高
            mTextWidth = Layout.getDesiredWidth(mText, mTextPaint);
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            mTextHeight = metrics.bottom - metrics.top;

            mTextPaint.getTextPath(mText, 0, mText.length(), 0f, -metrics.ascent, mFontPath);
            mPathMeasure.setPath(mFontPath, false);
            mLength = mPathMeasure.getLength();
        }

    }

    @Override
    public void drawPath(float start, float end) {
        mStart = validateProgress(start);
        mStop = validateProgress(end);

        checkFill(end);

        //重置路径
        mPathMeasure.setPath(mFontPath, false);
        mDst.reset();
        mPaintPath.reset();

        //重置路径
        mPathMeasure.setPath(mFontPath,true);
        mDst.reset();
        mPaintPath.reset();

        //根据进度获取路径
        while (mPathMeasure.nextContour()) {
            mLength = mPathMeasure.getLength();
//            Log.d(TAG, "drawPath: length:" + mLength);

            mStartValue = mLength * mStart;
            mEndValue = mLength * mStop;

//            Log.d(TAG, "drawPath: mEndValue:" + mEndValue);
//            Log.d(TAG, "drawPath: mLength:" + mLength);
//            Log.d(TAG, "drawPath: close? " + mPathMeasure.isClosed());
            mPathMeasure.getSegment(mStartValue, mEndValue, mDst, true);

            //绘画画笔效果
            if (showPainterActually) {
                mPathMeasure.getPosTan(mEndValue, mCurPos, null);
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
        initPath();
        clear();
        requestLayout();
    }

    //设置画笔特效
    public void setPathPainter(AsyncPathPainter listener) {
        this.mPainter = listener;
    }


}

