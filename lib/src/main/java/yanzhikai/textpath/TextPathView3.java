package yanzhikai.textpath;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/01/22
 * desc   :
 */

public class TextPathView3 extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {
    public static final String TAG = "yjkTextPathView3";
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private DrawThread mDrawThread;
    private boolean mIsDrawing = false;

    private Paint mTextPaint;
    private Paint mDrawPaint;
    private Path mFontPath, mDst;
    private float mAnimatorValue = 0, mLength = 0;
    private PathMeasure mPathMeasure = new PathMeasure();
//    private ValueAnimator mAnimator;
    /**
     * 要刻画的字符
     */
    protected String mText;
    protected int mTextSize = 94;

    /**
     * 每ms绘画速度
     */
    protected int speed = 400;

    private float mStart = 0, mStop = 0;

    public TextPathView3(Context context) {
        super(context);
        init(context);
    }

    public TextPathView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextPathView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setFocusable(true);
        this.mContext = context;
        //获取对象实例
        mSurfaceHolder = this.getHolder();
        // 给SurfaceView添加回调
        mSurfaceHolder.addCallback(this);
        //创建工作线程
        mDrawThread = new DrawThread(mSurfaceHolder);
        initPaint();
    }

    private void initPaint() {
        setOnClickListener(this);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(2);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mFontPath = new Path();
        mText = "你个沙雕";
        mTextPaint.getTextPath(mText, 0, mText.length(), 100, mTextPaint.getFontSpacing() + 100, mFontPath);
        mPathMeasure.setPath(mFontPath, false);
        mLength = mPathMeasure.getLength();
        Log.d(TAG, "initPaint: " + mPathMeasure.getLength());

        mDst = new Path();
//        mAnimator = ValueAnimator.ofFloat(0, 1);
//        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
//                Log.d(TAG, "mAnimatorValue: " + mAnimatorValue);
//
//                mPathMeasure.setPath(mFontPath,false);
//
//
//                while (mPathMeasure.nextContour()) {
//                    mLength = mPathMeasure.getLength();
//                    mStop = mLength * mAnimatorValue;
//                    mPathMeasure.getSegment(0, mStop, mDst, true);
//                }
////                Log.d(TAG, "onAnimationUpdate: start" + mStart);
////                mStart = mStop;
//
//                invalidate();
//            }
//        });
//        mAnimator.addListener(new AbstractAnimatorListener() {
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                if (mPathMeasure.nextContour()){
//                    mLength = mPathMeasure.getLength();
//                    mAnimator.setDuration((long) (mLength / speed));
//                    Log.d(TAG, "time" + (mLength / speed));
//                    mAnimator.start();
//                }else {
//                    mAnimator.cancel();
//                }
//            }
//        });
//        mAnimator.setDuration(2500);
//        mAnimator.setInterpolator(new LinearInterpolator());
////        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mAnimator.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: ");
        mIsDrawing = true;
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {

    }

    private class DrawThread extends Thread {
        private Canvas mCanvas;
        private SurfaceHolder mSurfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            super.run();
            while (mIsDrawing) {
//                mPathMeasure.setPath(mFontPath, false);
//                mLength = mPathMeasure.getLength();
                Log.d(TAG, "time" + (mLength / speed));
                drawSomething();
            }
        }

        //绘图逻辑
        private void drawSomething() {
            try {
                //获得canvas对象
                mCanvas = mSurfaceHolder.lockCanvas();
                //绘制背景
                mCanvas.drawColor(Color.WHITE);
                //绘图
                getAnimatedValue();
                Log.d(TAG, "mAnimatorValue: " + mAnimatorValue);

                mPathMeasure.setPath(mFontPath, false);
//                sleep(5);

                while (mPathMeasure.nextContour()) {
                    mLength = mPathMeasure.getLength();
                    mStop = mLength * mAnimatorValue;
                    mPathMeasure.getSegment(0, mStop, mDst, true);
                }
                mCanvas.drawPath(mDst, mDrawPaint);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    //释放canvas对象并提交画布
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }

        private void getAnimatedValue() {
            if (mAnimatorValue <= 1) {
                mAnimatorValue += 0.003f;
            } else {
                Log.d(TAG, "getAnimatedValue: end");
                mIsDrawing = false;
            }
        }

    }
}
