package util.function;

import java.util.function.ToDoubleFunction;

/**
 * A function from float to double.
 */
@FunctionalInterface
public interface FloatToDoubleFunction extends ToDoubleFunction<Float> {
    /**
     * @param var1 the argument
     * @return a double
     */
    double applyAsPrimitive(float var1);

    @Override
    default double applyAsDouble(final Float var1) {
        return this.applyAsPrimitive(var1);
    }
}
