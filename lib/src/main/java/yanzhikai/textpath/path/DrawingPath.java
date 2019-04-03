package yanzhikai.textpath.path;

import android.graphics.Path;
import android.graphics.PathMeasure;

public abstract class DrawingPath {

    protected PathMeasure mPathMeasure = new PathMeasure();

    //要绘画的路径
    protected Path mPath;
    //文字装载路径、文字绘画路径、画笔特效路径
    protected Path mDst = new Path(), mPaintPath = new Path();
    //路径开始、结束百分比
    protected float mStart = 0, mStop = 0;

    //绘画部分终点
    protected float mEndValue = 0;

    //绘画部分起点
    protected float mStartValue = 0;

    //当前绘画位置
    protected float[] mCurPos = new float[2];

    //是否展示画笔特效:
    //showPainter代表动画绘画时是否展示
    //showPainterActually代表所有时候是否展示，由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false
    protected boolean showPainter = true, showPainterActually = false;

    public abstract void initPath();

    public abstract void calculatePath(float start, float end);
}
