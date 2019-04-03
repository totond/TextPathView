package yanzhikai.textpath.path;

import android.graphics.Path;

import yanzhikai.textpath.painter.SyncPathPainter;

public class SyncDrawingPath extends DrawingPath {
    //画笔特效
    private SyncPathPainter mPainter;

    //路径长度总数
    private float mLengthSum = 0;

    @Override
    public void initPath() {

    }

    @Override
    public void calculatePath(float start, float end) {

        mStartValue = mLengthSum * mStart;
        mEndValue = mLengthSum * mStop;

        //重置路径
        mPathMeasure.setPath(mPath, false);
        mDst.reset();
        mPaintPath.reset();

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


        //绘画画笔效果
        if (showPainterActually) {
            mPathMeasure.getPosTan(mEndValue, mCurPos, null);
            drawPaintPath(mCurPos[0], mCurPos[1], mPaintPath);
        }
    }

    public void setPainter(SyncPathPainter painter) {
        mPainter = painter;
    }

    private void drawPaintPath(float x, float y, Path paintPath) {
        if (mPainter != null){
            mPainter.onDrawPaintPath(x,y,paintPath);
        }
    }
}
