package yanzhikai.textpath.calculator;

public class DefaultCalculator extends PathCalculator {

    @Override
    public void calculate(float progress) {
        if (progress < 1){
            setStart(0);
            setEnd(progress);
        }else {
            setStart(0);
            setEnd(1);
        }
    }
}
