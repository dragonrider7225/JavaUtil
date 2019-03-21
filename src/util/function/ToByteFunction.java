package util.function;

import util.annotation.Pure;

/**
 * A function that maps values of type T to bytes.
 * @param <T> the type of value that this <tt>ToByteFunction</tt> accepts
 */
@FunctionalInterface
public interface ToByteFunction<T> {
    /**
     * Since this function is pure, implementations should not produce side effects.
     * @param t the value to map to a byte
     * @return a byte value
     */
    @Pure
    byte applyAsPrimitive(T t);
}
