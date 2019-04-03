package yanzhikai.textpath.calculator;

public class MidCalculator extends PathCalculator {
    @Override
    public void calculate(float progress) {
        if (progress < 1){
            setStart(0.5f - progress / 2);
            setEnd(0.5f + progress / 2);
        }else {
            setStart(0);
            setEnd(1);
        }
    }
}
