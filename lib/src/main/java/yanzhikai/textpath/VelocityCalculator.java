package yanzhikai.textpath;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/08
 * desc   : 计算传入的当前点与上一个点之间的速度
 */

public class VelocityCalculator {
    private float mLastX = 0;
    private float mLastY = 0;
    private long mLastTime = 0;
    private boolean first = true;

    private float mVelocityX = 0;
    private float mVelocityY = 0;

    //重置
    public void reset(){
        mLastX = 0;
        mLastY = 0;
        mLastTime = 0;
        first = true;
    }

    //计算速度
    public void calculate(float x, float y){
        long time = System.currentTimeMillis();
        if (!first){
            //因为只需要方向，不需要具体速度值，所以默认deltaTime = 1，提高效率
//            float deltaTime = time - mLastTime;
//            mVelocityX = (x - mLastX) / deltaTime;
//            mVelocityY = (y - mLastY) / deltaTime;
            mVelocityX = x - mLastX;
            mVelocityY = y - mLastY;
        }else {
            first = false;
        }

        mLastX = x;
        mLastY = y;
        mLastTime = time;

    }

    public float getVelocityX() {
        return mVelocityX;
    }

    public float getVelocityY() {
        return mVelocityY;
    }
}
