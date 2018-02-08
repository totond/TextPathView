package yanzhikai.textpath;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/08
 * desc   :
 */

public class VelocityCalculator {
    private float mLastX = 0;
    private float mLastY = 0;
    private long mLastTime = 0;
    private boolean first = true;

    private float mVelocityX = 0;
    private float mVelocityY = 0;

    public void reset(){
        mLastX = 0;
        mLastY = 0;
        mLastTime = 0;
        first = true;
    }

    public void calculate(float x, float y){
        long time = System.currentTimeMillis();
        if (!first){
            float deltaTime = time - mLastTime;
            mVelocityX = (x - mLastX) / deltaTime;
            mVelocityY = (y - mLastY) / deltaTime;
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
