package util.function;

/**
 * A function that maps values of type T to shorts.
 * @param <T> the type of value that this <tt>ToShortFunction</tt> accepts
 */
@FunctionalInterface
public interface ToShortFunction<T> {
    /**
     * Since this function is pure, implementations should not produce side effects.
     * @param t the value to map to a short
     * @return a short value
     */
    short applyAsPrimitive(T t);
}
