package yanzhikai.textpath;

import android.graphics.Path;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/09
 * desc   :
 */

public class ArrowPainter implements TextPathView.TextPathPainter {
    private VelocityCalculator mVelocityCalculator = new VelocityCalculator();
    private float radius = 40;
    private double angle = Math.PI / 12;

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

    @Override
    public void onInit() {

    }

    @Override
    public void onStartAnimation() {
        mVelocityCalculator.reset();
    }
}
