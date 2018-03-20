package yanzhikai.textpath;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/07
 * desc   : 文字路径动画自定义View父类
 */

public abstract class TextPathView extends PathView {
    public static final String TAG = "yjkTextPathView";
    //用于获取文字的画笔
    protected TextPaint mTextPaint;
    //文字装载路径;
    protected Path mFontPath = new Path();
    //文本宽高
    protected float mTextWidth = 0, mTextHeight = 0;
    //要刻画的字符
    protected String mText;
    //要刻画的字符字体大小
    protected int mTextSize = 108;

    //是否自动开始动画
    protected boolean mAutoStart = false;
    //文字是否居中
    protected boolean mTextInCenter = false;
    //文字是否一开始显示
    protected boolean mShowInStart = false;
    //文字是否填充颜色
    protected boolean mFillColor = false;
    //字体
    protected Typeface mTypeface = null;



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

    @Override
    protected void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TextPathView);
        mText = typedArray.getString(R.styleable.TextPathView_text);
        if (mText == null){
            mText = "Test";
        }
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TextPathView_textSize,mTextSize);
        mDuration = typedArray.getInteger(R.styleable.TextPathView_duration,mDuration);
        showPainter = typedArray.getBoolean(R.styleable.TextPathView_showPainter, showPainter);
        showPainterActually = typedArray.getBoolean(R.styleable.TextPathView_showPainterActually,showPainterActually);
        mPathStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_pathStrokeWidth, mPathStrokeWidth);
        mTextStrokeColor = typedArray.getColor(R.styleable.TextPathView_pathStrokeColor,mTextStrokeColor);
        mPaintStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_paintStrokeWidth,mPaintStrokeWidth);
        mPaintStrokeColor = typedArray.getColor(R.styleable.TextPathView_paintStrokeColor,mPaintStrokeColor);
        mAutoStart = typedArray.getBoolean(R.styleable.TextPathView_autoStart,mAutoStart);
        mTextInCenter = typedArray.getBoolean(R.styleable.TextPathView_textInCenter,mTextInCenter);
        mShowInStart = typedArray.getBoolean(R.styleable.TextPathView_showInStart,mShowInStart);
        mRepeatStyle = typedArray.getInt(R.styleable.TextPathView_repeat, 0);
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    @Override
    protected void initPaint(){
        super.initPaint();
        mTextPaint = new TextPaint();
//        TextPaint textPaint = new TextPaint(mTextPaint);
//        mTextPaint.setTypeface();

        mTextPaint.setTextSize(mTextSize);

        if (mTextInCenter){
            mDrawPaint.setTextAlign(Paint.Align.CENTER);
        }
        if (mTypeface != null){
            mTextPaint.setTypeface(mTypeface);
        }

    }


    /**
     * 重写onMeasure方法使得WRAP_CONTENT生效
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int hSpeSize = MeasureSpec.getSize(heightMeasureSpec);
//        int hSpeMode = MeasureSpec.getMode(heightMeasureSpec);
//        int wSpeSize = MeasureSpec.getSize(widthMeasureSpec);
//        int wSpeMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

//        mTextWidth = TextUtil.getTextWidth(mTextPaint,mText);
        mTextWidth = Layout.getDesiredWidth(mText,mTextPaint);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        mTextHeight = metrics.bottom - metrics.top;

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT){
            width = (int) mTextWidth + 1;
        }
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT){
            height = (int) mTextHeight + 1;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mTextInCenter){
            canvas.translate((getWidth() - mTextWidth) / 2, (getHeight() - mTextHeight) / 2);
        }
        //画笔效果绘制
        if (showPainterActually) {
            canvas.drawPath(mPaintPath, mPaint);
        }
        //文字路径绘制
        if (mAnimatorValue < 1) {
            canvas.drawPath(mDst, mDrawPaint);
        }else {
            canvas.drawPath(mFontPath, mDrawPaint);
        }

    }



//    //设置自定义动画监听
//    public void setAnimatorListener(PathAnimatorListener animatorListener) {
//        mAnimatorListener = animatorListener;
//        mAnimatorListener.setTarget(this);
//        if (mAnimator != null) {
//            mAnimator.removeAllListeners();
//            mAnimator.addListener(mAnimatorListener);
//        }
//    }

    //设置文字内容
    public void setText(String text) {
        mText = text;
        try {
            initPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clear();
        requestLayout();
    }

    public void setTypeface(Typeface typeface){
        mTypeface = typeface;
        initPaint();
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




}
