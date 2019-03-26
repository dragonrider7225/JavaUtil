package util.function;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A function of three arguments. This function can be partially applied by supplying fewer than three arguments.
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <V> the type of the third argument
 * @param <R> the type of the returned value
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> extends Function<T, BiFunction<U, V, R>>, BiFunction<T, U, Function<V, R>> {
    /**
     * @param fst the first argument
     * @param snd the second argument
     * @param thd the third argument
     * @return an object
     */
    R apply(T fst, U snd, V thd);

    @Override
    default BiFunction<U, V, R> apply(final T t) {
        return (u, v) -> this.apply(t, u, v);
    }

    @Override
    default Function<V, R> apply(final T t, final U u) {
        return v -> this.apply(t, u, v);
    }
}
