package yanzhikai.textpath.painter;

import android.graphics.Path;

import java.util.Random;

import yanzhikai.textpath.SyncPathView;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.VelocityCalculator;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/11
 * desc   : 火花特效，根据箭头引申变化而来，根据当前点与上一个点算出的速度方向来控制火花的方向
 */

public class FireworksPainter implements SyncPathPainter {
    private VelocityCalculator mVelocityCalculator = new VelocityCalculator();
    private Random random = new Random();
    //箭头长度
    private float radius = 100;
    //箭头夹角
    private double angle = Math.PI / 8;
    //同时存在箭头数
    private static final int arrowCount = 6;
    //最大线段切断数
    private static final int cutCount = 9;


    public FireworksPainter(){

    }

    public FireworksPainter(int radius, double angle){
        this.radius = radius;
        this.angle = angle;
    }

    @Override
    public void onDrawPaintPath(float x, float y, Path paintPath) {
        mVelocityCalculator.calculate(x, y);

        for (int i = 0; i < arrowCount; i++) {
            double angleV = Math.atan2(mVelocityCalculator.getVelocityY(), mVelocityCalculator.getVelocityX());
            double rAngle = (angle * random.nextDouble());
            double delta = angleV - rAngle;
            double sum = angleV + rAngle;
            double rr = radius * random.nextDouble() / (2 * Math.cos(rAngle));
            float x1 = (float) (rr * Math.cos(sum));
            float y1 = (float) (rr * Math.sin(sum));
            float x2 = (float) (rr * Math.cos(delta));
            float y2 = (float) (rr * Math.sin(delta));

            splitPath(x, y, x - x1, y - y1, paintPath, random.nextInt(cutCount) + 2);
            splitPath(x, y, x - x2, y - y2, paintPath, random.nextInt(cutCount) + 2);
        }
    }

    @Override
    public void onStartAnimation() {
        mVelocityCalculator.reset();
    }

    //分解Path为虚线
    //注意count要大于0
    private void splitPath(float startX, float startY, float endX, float endY, Path path, int count) {
        float deltaX = (endX - startX) / count;
        float deltaY = (endY - startY) / count;
        for (int i = 0; i < count; i++) {
            if (i % 3 == 0) {
                path.moveTo(startX, startY);
                path.lineTo(startX + deltaX, startY + deltaY);
            }
            startX += deltaX;
            startY += deltaY;
        }
    }
}
