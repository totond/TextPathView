package yanzhikai.textpath;

import android.animation.Animator;

/**
 * author : totond
 * e-mail : yanzhikai_yjk@qq.com
 * time   : 2018/03/13
 * desc   :
 */

public class PathAnimatorListener implements Animator.AnimatorListener {
    private PathView mPathView;
    protected boolean isCancel = false;

    protected void setTarget(PathView pathView) {
        this.mPathView = pathView;
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
        mPathView.setShowPainterActually(false);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isCancel = true;
    }
}
