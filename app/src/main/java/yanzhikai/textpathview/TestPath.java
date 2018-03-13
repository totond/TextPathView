package yanzhikai.textpathview;

import android.graphics.Path;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/13
 * desc   :
 */

public class TestPath extends Path {
    public TestPath(){
        init();
    }

    private void init() {
        addCircle(300,300,150,Direction.CCW);
        addCircle(300,300,100,Direction.CW);
        addCircle(300,300,50,Direction.CCW);
        moveTo(300,300);
        lineTo(400,400);
    }
}
