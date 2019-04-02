package yanzhikai.textpath.painter;

import android.graphics.Path;

import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/11
 * desc   : 一支笔的画笔特效，就是在绘画点旁边画多一支笔
 */

public class PenPainter implements SyncPathPainter,AsyncPathPainter {
    private static final float r_nib = 30,r_pen = 100;

    @Override
    public void onDrawPaintPath(float x, float y, Path paintPath) {
        paintPath.addCircle(x,y,3, Path.Direction.CCW);
        paintPath.moveTo(x, y);
        paintPath.lineTo(x + r_nib, y);
        paintPath.lineTo(x, y - r_nib);
        paintPath.lineTo(x, y);
        paintPath.moveTo(x + r_nib, y);
        paintPath.lineTo(x + r_nib + r_pen, y - r_pen);
        paintPath.lineTo(x + r_pen, y - r_pen - r_nib);
        paintPath.lineTo(x, y - r_nib);
    }

    @Override
    public void onStartAnimation() {

    }
}
