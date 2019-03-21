package util.function;

/**
 * A function that maps values of type T to chars.
 * @param <T> the type of value that this <tt>ToCharFunction</tt> accepts
 */
@FunctionalInterface
public interface ToCharFunction<T> {
    /**
     * Since this function is pure, implementations should not produce side effects.
     * @param t the value to map to a char
     * @return a char value
     */
    char applyAsPrimitive(T t);
}
