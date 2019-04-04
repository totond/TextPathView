package yanzhikai.textpath.calculator;

public class BlinkCalculator extends PathCalculator {
    private int count = 4, interval = 4;

    @Override
    public void calculate(float progress) {
        setStart(0);
        if (progress < 1) {
            if (count == interval) {
                setEnd(1);
                count = 0;
            } else {
                setEnd(progress);
                count++;
            }
        } else {
            setEnd(1);
        }
    }
}
