package yanzhikai.textpath.calculator;

public class AroundCalculator extends PathCalculator {
    @Override
    public boolean calculate(float progress) {
        boolean isEnd = false;
        if (progress < 1){
            end = progress;
            if (progress < 0.75f){
                start = progress / 3;
            }else {
                start = 1 - progress;
            }
        }else {
            start = 0;
            end = 1;
            isEnd = true;
        }
        return isEnd;
    }
}
