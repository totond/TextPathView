package yanzhikai.textpath.painter;

import android.graphics.Path;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/14
 * desc   :
 */

public interface AsyncPathPainter extends PathPainter {
    /**
     * 绘画画笔特效时候执行
     * @param x 当前绘画点x坐标
     * @param y 当前绘画点y坐标
     * @param paintPath 画笔Path对象，在这里画出想要的画笔特效
     */
    @Override
    void onDrawPaintPath(float x, float y, Path paintPath);
}