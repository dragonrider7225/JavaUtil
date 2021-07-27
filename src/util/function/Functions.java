package util.function;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility functions on functions.
 */
public final class Functions {
    private Functions() {
        throw new UnsupportedOperationException("Can't instantiate utility class"); //$NON-NLS-1$
    }

    /**
     * Curry the specified function of two arguments to a function of one argument that returns a function of one argument.
     * @param f the function to curry
     * @return the curried function
     */
    public static <T, U, V> Function<T, Function<U, V>> curry(final BiFunction<T, U, V> f) {
        return t -> u -> f.apply(t, u);
    }

    /**
     * Uncurry the specified function of one argument that returns a function of one argument to a function of two arguments.
     * curry(uncurry(f)) produces a function which behaves identically to f on every pair of arguments.
     * Similarly, uncurry(curry(g)) produces a function which behaves identically to g on every pair of arguments.
     * @param f the function to uncurry
     * @return the uncurried function
     */
    public static <T, U, V> BiFunction<T, U, V> uncurry(final Function<T, Function<U, V>> f) {
        return (t, u) -> f.apply(t).apply(u);
    }
}
