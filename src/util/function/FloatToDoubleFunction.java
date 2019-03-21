package util.function;

import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface FloatToDoubleFunction extends ToDoubleFunction<Float> {
    double applyAsPrimitive(float var1);

    @Override
    default double applyAsDouble(final Float var1) {
        return this.applyAsPrimitive(var1);
    }
}
