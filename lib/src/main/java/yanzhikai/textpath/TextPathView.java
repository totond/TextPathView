package yanzhikai.textpath;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * author : totond
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
    //测量Path具体范围
    private RectF mPathBounds = new RectF();
    //Height是否处于wrap_content
    private boolean wrapWidth = false, wrapHeight = false;


    public TextPathView(Context context) {
        super(context);
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    @Override
    protected void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextPathView);
        mText = typedArray.getString(R.styleable.TextPathView_text);
        if (mText == null) {
            mText = "Test";
        }
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TextPathView_textSize, mTextSize);
        mDuration = typedArray.getInteger(R.styleable.TextPathView_duration, mDuration);
        showPainter = typedArray.getBoolean(R.styleable.TextPathView_showPainter, showPainter);
        showPainterActually = typedArray.getBoolean(R.styleable.TextPathView_showPainterActually, showPainterActually);
        mPathStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_pathStrokeWidth, mPathStrokeWidth);
        mTextStrokeColor = typedArray.getColor(R.styleable.TextPathView_pathStrokeColor, mTextStrokeColor);
        mPaintStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.TextPathView_paintStrokeWidth, mPaintStrokeWidth);
        mPaintStrokeColor = typedArray.getColor(R.styleable.TextPathView_paintStrokeColor, mPaintStrokeColor);
        mAutoStart = typedArray.getBoolean(R.styleable.TextPathView_autoStart, mAutoStart);
        mTextInCenter = typedArray.getBoolean(R.styleable.TextPathView_textInCenter, mTextInCenter);
        mShowInStart = typedArray.getBoolean(R.styleable.TextPathView_showInStart, mShowInStart);
        mRepeatStyle = typedArray.getInt(R.styleable.TextPathView_repeat, 0);
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    @Override
    protected void initPaint() {
        super.initPaint();
        mTextPaint = new TextPaint();
//        TextPaint textPaint = new TextPaint(mTextPaint);
//        mTextPaint.setTypeface();

        mTextPaint.setTextSize(mTextSize);

        if (mTextInCenter) {
            mDrawPaint.setTextAlign(Paint.Align.CENTER);
        }
        if (mTypeface != null) {
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

        if (mTextWidth > width) {
            handleNewLines(width);
            mTextWidth = width;
        }

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            if (mTextWidth <= width) {
                width = (int) mTextWidth + 1;
            } else {
                handleNewLines(width);
                mTextWidth = width;
            }
            wrapWidth = true;
        } else {
            wrapWidth = false;
        }

        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = (int) mTextHeight + 1;
            wrapHeight = true;
        } else {
            wrapHeight = false;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mFontPath.computeBounds(mPathBounds, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mTextInCenter) {
            if (!wrapWidth) {
                canvas.translate((getWidth() - mPathBounds.width()) / 2, 0);
            }
            if (!wrapHeight) {
                canvas.translate(0, (getHeight() - mPathBounds.height()) / 2);
            }
        }
        //画笔效果绘制
        if (showPainterActually) {
            canvas.drawPath(mPaintPath, mPaint);
        }
        //文字路径绘制
        if (mStop - mStart >= 1) {
            canvas.drawPath(mFontPath, mDrawPaint);
        } else {
            canvas.drawPath(mDst, mDrawPaint);
        }

    }

    //处理换行：拆分字符串，分别获取它们的path，再拼接
    protected void handleNewLines(float outerWidth) {
        float[] widths = new float[mText.length()];
        mTextPaint.getTextWidths(mText, widths);

        float widthSum = 0;
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float ascent = -mTextPaint.getFontMetrics().ascent;
        float height = metrics.descent + ascent;
        int start = 0, count = 0;
        mFontPath.reset();
        for (int i = 0; i < widths.length; i++) {
            float width = widths[i];
            widthSum += width;
//            Log.d(TAG, "handleNewLines: width " + width + " i: " + i);
            if (widthSum > outerWidth) {
                String text = mText.substring(start, i);
                widthSum = width;
                start = i;
                Path path = new Path();
                mTextPaint.getTextPath(text, 0, text.length(), 0, ascent, path);
                mFontPath.addPath(path, 0, height * count);
//                Log.d(TAG, "handleNewLines text: " + text);
                count++;
            }
        }
        if (start < widths.length) {
            String text = mText.substring(start, widths.length);
//            Log.d(TAG, "handleNewLines text: " + text);
            Path path = new Path();
            mTextPaint.getTextPath(text, 0, text.length(), 0, ascent, path);
            mFontPath.addPath(path, 0, height * count);
        }
        mTextHeight = height * ++count;
    }


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

    //设置字体样式
    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
        initPaint();
    }

    //直接显示填充好颜色了的全部文字
    public void showFillColorText() {
        mFillColor = true;
        mDrawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPath(1);
    }

    /**
     * 检查当前进度是否需要填充颜色
     * @param progress 输入进度值
     */
    protected void checkFill(float progress) {
        if (progress != 1 && mFillColor) {
            mFillColor = false;
            mDrawPaint.setStyle(Paint.Style.STROKE);
        }
    }


}
