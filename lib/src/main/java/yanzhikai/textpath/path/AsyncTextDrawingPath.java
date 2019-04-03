package yanzhikai.textpath.path;

import android.graphics.Path;

import yanzhikai.textpath.painter.AsyncPathPainter;

public class AsyncTextDrawingPath extends TextDrawingPath {
    //分段路径长度
    private float mLength = 0;
    //画笔特效
    private AsyncPathPainter mPainter;

    @Override
    public void initPath() {

    }

    @Override
    public void calculatePath(float start, float end) {
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
    }

    public void setPainter(AsyncPathPainter painter) {
        mPainter = painter;
    }

    private void drawPaintPath(float x, float y, Path paintPath) {
        if (mPainter != null){
            mPainter.onDrawPaintPath(x,y,paintPath);
        }
    }
}
