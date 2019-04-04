package yanzhikai.textpath.painter;

import android.graphics.Path;

import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.VelocityCalculator;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/09
 * desc   : 箭头画笔特效，根据传入的当前点与上一个点之间的速度方向，来调整箭头方向
 */

public class ArrowPainter implements SyncPathPainter {
    private VelocityCalculator mVelocityCalculator = new VelocityCalculator();
    //箭头长度
    private float radius = 60;
    //箭头夹角
    private double angle = Math.PI / 8;

    public ArrowPainter(){
    }

    public ArrowPainter(int radius, double angle){
        this.radius = radius;
        this.angle = angle;
    }

    @Override
    public void onDrawPaintPath(float x, float y, Path paintPath) {
        mVelocityCalculator.calculate(x, y);
        double angleV = Math.atan2(mVelocityCalculator.getVelocityY(), mVelocityCalculator.getVelocityX());
        double delta = angleV - angle;
        double sum = angleV + angle;
        double rr = radius / (2 * Math.cos(angle));
        float x1 = (float) (rr * Math.cos(sum));
        float y1 = (float) (rr * Math.sin(sum));
        float x2 = (float) (rr * Math.cos(delta));
        float y2 = (float) (rr * Math.sin(delta));

        paintPath.moveTo(x, y);
        paintPath.lineTo(x - x1, y - y1);
        paintPath.moveTo(x, y);
        paintPath.lineTo(x - x2, y - y2);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void onStartAnimation() {
        mVelocityCalculator.reset();
    }
}
