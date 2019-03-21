package util.function;

import util.annotation.Pure;

/**
 * A function that maps values of type T to floats.
 * @param <T> the type of value that this <tt>ToFloatFunction</tt> accepts
 */
@FunctionalInterface
public interface ToFloatFunction<T> {
    /**
     * Since this function is pure, implementations should not produce side effects.
     * @param t the value to map to a float
     * @return a float value
     */
    @Pure
    float applyAsPrimitive(T t);
}
