package yanzhikai.textpath;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/07
 * desc   :
 */

public abstract class TextPathView extends View {
    public static final String TAG = "TextPathView";
    //用于获取文字的画笔
    protected Paint mTextPaint;
    //路径的画笔
    protected Paint mDrawPaint;
    //画笔特效的画笔
    protected Paint mPaint;
    //文字装载路径、文字绘画路径、画笔特效路径
    protected Path mFontPath, mDst,mPaintPath;
    //属性动画
    protected ValueAnimator mAnimator;
    //动画进度值
    protected float mAnimatorValue = 0;

    //绘画部分长度
    protected float mStop = 0;
    //是否展示画笔
    protected boolean showPaint = true;
    //当前绘画位置
    protected float[] mCurPos = new float[2];
    //当前点tan值
    protected float[] mCurTan = new float[2];

    protected int mDuration = 10000;

    protected PathMeasure mPathMeasure = new PathMeasure();

    //要刻画的字符
    protected String mText = "丢";
    //要刻画的字符字体大小
    protected int mTextSize = 124;

    //文字路径的粗细，画笔粗细
    protected int mTextStrokeWidth = 2, mPaintStrokeWidth = 2;


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
        showPaint = typedArray.getBoolean(R.styleable.TextPathView_showPaint,showPaint);
        mTextStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_textStrokeWidth,mTextStrokeWidth);
        mPaintStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_paintStrokeWidth,mPaintStrokeWidth);
        typedArray.recycle();
    }
}
