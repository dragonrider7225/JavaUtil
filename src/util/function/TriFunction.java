package util.function;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

@FunctionalInterface
public interface TriFunction<@NonNull T, U, V, R>
        extends Function<T, BiFunction<U, V, R>>,
        BiFunction<T, U, Function<V, R>> {

    R apply(T fst, U snd, V thrd);

    @Override
    default BiFunction<U, V, R> apply(final T t) {
        return (u, v) -> this.apply(t, u, v);
    }

    @Override
    default Function<V, R> apply(final T t, final U u) {
        return v -> this.apply(t, u, v);
    }
}
