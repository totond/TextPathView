package yanzhikai.textpath;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/07
 * desc   : 文字路径动画自定义View总父类
 */

public abstract class TextPathView extends View {
    public static final String TAG = "yjkTextPathView";
    //用于获取文字的画笔
    protected Paint mTextPaint;
    //路径的画笔
    protected Paint mDrawPaint;
    //画笔特效的画笔
    protected Paint mPaint;
    //文字装载路径、文字绘画路径、画笔特效路径
    protected Path mFontPath = new Path(), mDst = new Path(), mPaintPath = new Path();
    //属性动画
    protected ValueAnimator mAnimator;
    //动画进度值
    protected float mAnimatorValue = 0;

    //绘画部分长度
    protected float mStop = 0;
    //是否展示画笔特效:
    //showPainter代表动画绘画时是否展示
    //showPainterActually代表所有时候是否展示，由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false
    protected boolean showPainter = false, showPainterActually = false;
    //当前绘画位置
    protected float[] mCurPos = new float[2];
    //当前点tan值,暂时无用
//    protected float[] mCurTan = new float[2];
    //文本宽高
    protected float mTextWidth = 0, mTextHeight = 0;

    protected int mDuration = 10000;

    protected PathMeasure mPathMeasure = new PathMeasure();

    //要刻画的字符
    protected String mText;
    //要刻画的字符字体大小
    protected int mTextSize = 108;

    //文字路径的粗细，画笔粗细
    protected int mTextStrokeWidth = 5, mPaintStrokeWidth = 3;
    //文字路径的颜色，画笔路径颜色
    protected int mTextStrokeColor = Color.BLACK, mPaintStrokeColor = Color.BLACK;
    //是否自动开始动画
    protected boolean mAutoStart = false;
    //文字是否居中
    protected boolean mTextInCenter = false;
    //文字是否一开始显示
    protected boolean mShowInStart = false;
    //文字是否填充颜色
    protected boolean mFillColor = false;

    protected RepeatAnimation mRepeatStyle = RepeatAnimation.NONE;
    //动画监听
    protected TextPathAnimatorListener mAnimatorListener;


    public TextPathView(Context context) {
        super(context);
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context,attrs);
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
    }

    private void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TextPathView);
        mText = typedArray.getString(R.styleable.TextPathView_text);
        if (mText == null){
            mText = "Test";
        }
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TextPathView_textSize,mTextSize);
        mDuration = typedArray.getInteger(R.styleable.TextPathView_duration,mDuration);
        showPainter = typedArray.getBoolean(R.styleable.TextPathView_showPainter, showPainter);
        showPainterActually = typedArray.getBoolean(R.styleable.TextPathView_showPainterActually,showPainterActually);
        mTextStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_textStrokeWidth,mTextStrokeWidth);
        mTextStrokeColor = typedArray.getColor(R.styleable.TextPathView_textStrokeColor,mTextStrokeColor);
        mPaintStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_paintStrokeWidth,mPaintStrokeWidth);
        mPaintStrokeColor = typedArray.getColor(R.styleable.TextPathView_paintStrokeColor,mPaintStrokeColor);
        mAutoStart = typedArray.getBoolean(R.styleable.TextPathView_autoStart,mAutoStart);
        mTextInCenter = typedArray.getBoolean(R.styleable.TextPathView_textInCenter,mTextInCenter);
        mShowInStart = typedArray.getBoolean(R.styleable.TextPathView_showInStart,mShowInStart);
        int repeatStyle = typedArray.getInt(R.styleable.TextPathView_repeat, 0);
        switch (repeatStyle) {
            case 1:
                mRepeatStyle = RepeatAnimation.RESTART;
                break;
            case 2:
                mRepeatStyle = RepeatAnimation.REVERSE;
                break;
            default:
                mRepeatStyle = RepeatAnimation.NONE;
        }
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    protected void initPaint(){
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);

        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(mTextStrokeColor);
        mDrawPaint.setStrokeWidth(mTextStrokeWidth);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        if (mTextInCenter){
            mDrawPaint.setTextAlign(Paint.Align.CENTER);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintStrokeColor);
        mPaint.setStrokeWidth(mPaintStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initAnimator(float start, float end, RepeatAnimation animationStyle, int repeatCount) {
        mAnimator = ValueAnimator.ofFloat(start, end);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                drawPath(mAnimatorValue);
            }
        });
        if (mAnimatorListener == null){
            mAnimatorListener = new TextPathAnimatorListener();
        }
        mAnimator.addListener(mAnimatorListener);

        mAnimator.setDuration(mDuration);
        mAnimator.setInterpolator(new LinearInterpolator());
        if (animationStyle == RepeatAnimation.RESTART) {
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(repeatCount);
        } else if (animationStyle == RepeatAnimation.REVERSE) {
            mAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mAnimator.setRepeatCount(repeatCount);
        }
    }

    /**
     * 开始绘制文字路径动画
     * @param start 路径比例，范围0-1
     * @param end 路径比例，范围0-1
     */
    public void startAnimation(float start, float end) {
        startAnimation(start, end, mRepeatStyle, ValueAnimator.INFINITE);
    }

    public void startAnimation(float start, float end, RepeatAnimation animationStyle, int repeatCount) {
        if (!isProgressValid(start) || !isProgressValid(end)){
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        initAnimator(start, end, animationStyle, repeatCount);
        initTextPath();
        showPainterActually = showPainter;
        mAnimator.start();
    }

    /**
     * 绘画文字路径的方法
     * @param progress 绘画进度，0-1
     */
    public abstract void drawPath(float progress);

    protected abstract void initTextPath();

    /**
     * 重写onMeasure方法使得WRAP_CONTENT生效
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int hSpeSize = MeasureSpec.getSize(heightMeasureSpec);
//        int hSpeMode = MeasureSpec.getMode(heightMeasureSpec);
        int wSpeSize = MeasureSpec.getSize(widthMeasureSpec);
//        int wSpeMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = wSpeSize;
        int height = hSpeSize;

        mTextWidth = TextUtil.getTextWidth(mTextPaint,mText);
        mTextHeight = mTextPaint.getFontSpacing() + 1;

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT){
            width = (int) mTextWidth;
        }
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT){
            height = (int) mTextHeight;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTextInCenter){
            canvas.translate((getWidth() - mTextWidth) / 2, (getHeight() - mTextHeight) / 2);
        }
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

    //清除画面
    public void clear(){
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
    public void setAnimatorListener(TextPathAnimatorListener animatorListener) {
        mAnimatorListener = animatorListener;
        mAnimatorListener.setTarget(this);
        if (mAnimator != null) {
            mAnimator.removeAllUpdateListeners();
            mAnimator.addListener(mAnimatorListener);
        }
    }

    //设置文字内容
    public void setText(String text) {
        mText = text;
        initTextPath();
        clear();
        requestLayout();
    }

    //直接显示填充好颜色了的全部文字
    public void showFillColorText(){
        mFillColor = true;
        mDrawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPath(1);
    }

    protected void checkFill(float progress){
        if (progress != 1 && mFillColor){
            mFillColor = false;
            mDrawPaint.setStyle(Paint.Style.STROKE);
        }
    }

    protected boolean isProgressValid(float progress){
        if (progress < 0 || progress > 1){
            try {
                throw new Exception("Progress is invalid!");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public interface TextPainter {
        /**
         * 绘画画笔特效时候执行
         * @param x 当前绘画点x坐标
         * @param y 当前绘画点y坐标
         * @param paintPath 画笔Path对象，在这里画出想要的画笔特效
         */
        void onDrawPaintPath(float x, float y, Path paintPath);
    }


    public enum RepeatAnimation {
        NONE, RESTART, REVERSE
    }

}
