package yanzhikai.textpath.calculator;

public class MidCalculator extends PathCalculator {
    @Override
    public boolean calculate(float progress) {
        boolean isEnd = false;
        if (progress < 1){
            start = 0.5f - progress / 2;
            end = 0.5f + progress / 2;
        }else {
            start = 0;
            end = 1;
            isEnd = true;
        }
        return isEnd;
    }
}
