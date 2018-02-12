package yanzhikai.textpath.painter;

import android.graphics.Path;

import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/11
 * desc   :
 */

public class PenPainter implements SyncTextPathView.SyncTextPainter,AsyncTextPathView.AsyncTextPainter {
    @Override
    public void onDrawPaintPath(float x, float y, Path paintPath) {
        paintPath.moveTo(x, y);
        paintPath.lineTo(x + 10, y);
        paintPath.lineTo(x, y - 10);
        paintPath.lineTo(x, y);
        paintPath.moveTo(x + 10, y);
        paintPath.lineTo(x + 80, y - 30);
        paintPath.lineTo(x + 70, y - 40);
        paintPath.lineTo(x, y - 10);
    }

    @Override
    public void onStartAnimation() {

    }
}
