package yanzhikai.textpath.calculator;

public class DefaultCalculator extends PathCalculator {

    @Override
    public boolean calculate(float progress) {
        boolean isEnd = false;
        if (progress < 1){
            start = 0;
            end = progress;
        }else {
            start = 0;
            end = 1;
            isEnd = true;
        }
        return isEnd;
    }
}
