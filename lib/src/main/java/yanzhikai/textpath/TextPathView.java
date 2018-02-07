package yanzhikai.textpath;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/02/07
 * desc   :
 */

public class TextPathView extends View {
    public static final String TAG = "TextPathView";
    //用于获取文字的画笔
    private Paint mTextPaint;
    //路径的画笔
    private Paint mDrawPaint;
    //画笔的画笔
    private Paint mPaint;


    public TextPathView(Context context) {
        super(context);
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
