package yanzhikai.textpath;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/13
 * desc   : 所有路径动画自定义View的父类
 */

public abstract class PathView extends View {
    public static final String TAG = "yjkTextPathView";

    public static final int NONE = 0;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;

    @IntDef({NONE, RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface Repeat {}
    @Repeat
    protected int mRepeatStyle = NONE;

    //路径的画笔
    protected Paint mDrawPaint;
    //画笔特效的画笔
    protected Paint mPaint;
    //文字装载路径、文字绘画路径、画笔特效路径
    protected Path mDst = new Path(), mPaintPath = new Path();
    //属性动画
    protected ValueAnimator mAnimator;
    //动画进度值
    protected float mAnimatorValue = 0;

    //绘画部分长度
    protected float mStop = 0;
    //是否展示画笔特效:
    //showPainter代表动画绘画时是否展示
    //showPainterActually代表所有时候是否展示，由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false
    protected boolean showPainter = true, showPainterActually = false;
    //当前绘画位置
    protected float[] mCurPos = new float[2];
    //当前点tan值,暂时无用
//    protected float[] mCurTan = new float[2];
    //路径宽高
    protected float mPathWidth = 0, mPathHeight = 0;

    protected int mDuration = 6000;

    protected PathMeasure mPathMeasure = new PathMeasure();

    //要绘画的路径
    protected Path mPath;

    //文字路径的粗细，画笔粗细
    protected int mPathStrokeWidth = 5, mPaintStrokeWidth = 3;
    //文字路径的颜色，画笔路径颜色
    protected int mTextStrokeColor = Color.BLACK, mPaintStrokeColor = Color.BLACK;

    //文字填充颜色,后面会初始化默认为mTextStrokeColor
//    protected int mFillColor = Color.BLACK;
    //文字是否填充颜色
    protected boolean mShouldFill = false;

    //动画监听
    protected PathAnimatorListener mAnimatorListener;

    protected boolean nullPath = true;


    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    protected void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PathView);
        mDuration = typedArray.getInteger(R.styleable.PathView_duration, mDuration);
        showPainter = typedArray.getBoolean(R.styleable.PathView_showPainter, showPainter);
        showPainterActually = typedArray.getBoolean(R.styleable.PathView_showPainterActually, showPainterActually);
        mPathStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.PathView_pathStrokeWidth, mPathStrokeWidth);
        mTextStrokeColor = typedArray.getColor(R.styleable.PathView_pathStrokeColor, mTextStrokeColor);
        mPaintStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.PathView_paintStrokeWidth, mPaintStrokeWidth);
        mPaintStrokeColor = typedArray.getColor(R.styleable.PathView_paintStrokeColor, mPaintStrokeColor);
        mRepeatStyle = typedArray.getInt(R.styleable.PathView_repeat, mRepeatStyle);
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    protected void initPaint() {

        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(mTextStrokeColor);
        mDrawPaint.setStrokeWidth(mPathStrokeWidth);
        mDrawPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintStrokeColor);
        mPaint.setStrokeWidth(mPaintStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    protected void initAnimator(float start, float end, int animationStyle, int repeatCount) {
        mAnimator = ValueAnimator.ofFloat(start, end);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                drawPath(mAnimatorValue);
            }
        });
        if (mAnimatorListener == null){
            mAnimatorListener = new PathAnimatorListener();
            mAnimatorListener.setTarget(this);
        }
        mAnimator.removeAllListeners();
        mAnimator.addListener(mAnimatorListener);

        mAnimator.setDuration(mDuration);
        mAnimator.setInterpolator(new LinearInterpolator());
        if (animationStyle == RESTART) {
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(repeatCount);
        } else if (animationStyle == REVERSE) {
            mAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mAnimator.setRepeatCount(repeatCount);
        }
    }

    /**
     * 开始绘制文字路径动画
     *
     * @param start 路径比例，范围0-1
     * @param end   路径比例，范围0-1
     */
    public void startAnimation(float start, float end) {
        startAnimation(start, end, mRepeatStyle, ValueAnimator.INFINITE);
    }

    public void startAnimation(float start, float end, int animationStyle, int repeatCount) {
        if (!isProgressValid(start) || !isProgressValid(end)) {
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        initAnimator(start, end, animationStyle, repeatCount);
        try {
            initPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showPainterActually = showPainter;
        mAnimator.start();
    }

    /**
     * Stop animation
     */
    public void stopAnimation() {
        showPainterActually = false;
        clear();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    /**
     * Pause animation
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseAnimation() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }

    /**
     * Resume animation
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeAnimation() {
        if (mAnimator != null) {
            mAnimator.resume();
        }
    }

    /**
     * 绘画文字路径的方法
     *
     * @param progress 绘画进度，0-1
     */
    public abstract void drawPath(float progress);

    protected abstract void initPath() throws Exception;

    /**
     * 重写onMeasure方法使得WRAP_CONTENT生效，未成功
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int hSpeSize = View.MeasureSpec.getSize(heightMeasureSpec);
//        int hSpeMode = MeasureSpec.getMode(heightMeasureSpec);
        int wSpeSize = View.MeasureSpec.getSize(widthMeasureSpec);
//        int wSpeMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = wSpeSize;
        int height = hSpeSize;

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && !nullPath) {
            width = (int) mPathWidth;
        }
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT && !nullPath) {
            height = (int) mPathHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画笔效果绘制
        if (showPainterActually) {
            canvas.drawPath(mPaintPath, mPaint);
        }
        //文字路径绘制
        canvas.drawPath(mDst, mDrawPaint);

    }

    //获取绘画文字的画笔
    public Paint getDrawPaint() {
        return mDrawPaint;
    }

    //获取绘画画笔特效的画笔
    public Paint getPaint() {
        return mPaint;
    }

    //设置路径，必须先设置好路径在startAnimation()，不然会报错！
    public void setPath(Path path) {
        this.mPath = path;
        try {
            initPath();
            //ToDo 这里的设置只能获取Path非空白部分的宽高，不能获取整个Path的宽高，后面再寻找方法
            RectF rectF = new RectF();
            mPath.computeBounds(rectF,false);
            mPathWidth = rectF.width();
            mPathHeight = rectF.height();
            nullPath = false;
        } catch (Exception e) {
            nullPath = true;
            e.printStackTrace();
        }
    }

    //清除画面
    public void clear() {
        mDst.reset();
        mPaintPath.reset();
        postInvalidate();
    }

    //设置动画时能否显示画笔效果
    public void setShowPainter(boolean showPainter) {
        this.showPainter = showPainter;
    }

    //设置所有时候是否显示画笔效果,由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false
    public void setShowPainterActually(boolean showPainterActually) {
        this.showPainterActually = showPainterActually;
    }

    //设置自定义动画监听
    public void setAnimatorListener(PathAnimatorListener animatorListener) {
        mAnimatorListener = animatorListener;
        mAnimatorListener.setTarget(this);
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.addListener(mAnimatorListener);
        }
    }


    //直接显示填充好颜色了的全部文字
    public void showFillColorText() {
        mShouldFill = true;
        mDrawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPath(1);
    }

    //设置动画持续时间
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    //设置重复方式
    public void setRepeatStyle(int repeatStyle) {
        this.mRepeatStyle = repeatStyle;
    }

    protected void checkFill(float progress) {
        if (progress != 1 && mShouldFill) {
            mShouldFill = false;
            mDrawPaint.setStyle(Paint.Style.STROKE);
        }
    }

    protected boolean isProgressValid(float progress) {
        if (progress < 0 || progress > 1) {
            try {
                throw new Exception("Progress is invalid!");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }



}
