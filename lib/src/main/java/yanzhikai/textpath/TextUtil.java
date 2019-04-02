package yanzhikai.textpath;

import android.graphics.Paint;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/09
 * desc   : 获取文字的属性工具
 */

public class TextUtil {
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}
