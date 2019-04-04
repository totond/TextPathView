package yanzhikai.textpath.calculator;

/**
 * 用于定义进度progress对应的路径开端和末端位置的工具，可以继承来自定义
 */
public abstract class PathCalculator {
        private float start = 0, end = 0;

        /**
         * 此方法用于定义进度progress对应的路径开端和末端位置
         * @param progress 输入的进度：0-1f
         */
        public abstract void calculate(float progress);

    public void setStart(float start) {
        this.start = start;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public float getStart() {
        return start;
    }

    public float getEnd() {
        return end;
    }
}
