package yanzhikai.textpath;

import android.animation.Animator;

/**
 * author : yany
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/08
 * desc   : a AnimatorListener for TextPathView]
 */

public class TextPathAnimatorListener implements Animator.AnimatorListener {
    private TextPathView mTextPathView;
    protected boolean isCancel = false;

    protected void setTarget(TextPathView textPathView) {
        this.mTextPathView = textPathView;
    }

    @Override
    public void onAnimationStart(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation) {
        isCancel = false;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mTextPathView.setShowPainterActually(false);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isCancel = true;
    }
}
