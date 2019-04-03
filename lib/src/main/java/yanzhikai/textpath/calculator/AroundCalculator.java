package yanzhikai.textpath.calculator;

public class AroundCalculator extends PathCalculator {
    @Override
    public void calculate(float progress) {
        if (progress < 1){
            setEnd(progress);
            if (progress < 0.75f){
                setStart(progress / 3);
            }else {
                setStart(1 - progress);
            }
        }else {
            setStart(0);
            setEnd(1);
        }
    }
}
