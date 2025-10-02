package sb.justwindow;

import javafx.animation.Interpolator;
import java.util.function.DoubleUnaryOperator;

public class CustomInterpolator extends Interpolator {
    private final DoubleUnaryOperator easingFunction;

    public CustomInterpolator(DoubleUnaryOperator easingFunction) {
        this.easingFunction = easingFunction;
    }

    @Override
    protected double curve(double t) {
        return easingFunction.applyAsDouble(t);
    }
}
