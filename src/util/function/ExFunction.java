package util.function;

/**
 * A function that may throw an object of type X.
 * @param <T> the input type
 * @param <U> the output type
 * @param <X> the exception type
 */
@FunctionalInterface
public interface ExFunction<T, U, X extends Throwable> {
    /**
     * Like {@linkplain java.util.function.Function#apply(Object)} but can throw <tt>X</tt>
     * @param t the argument
     * @return a value
     * @throws X
     */
    U apply(T t) throws X;

    /**
     * @param f the inner function
     * @param g the outer function
     * @return g ∘ f
     */
    static <T, U, V, X extends Throwable> ExFunction<T, V, X> compose(
            final ExFunction<? super T, ? extends U, ? extends X> f,
            final ExFunction<? super U, ? extends V, ? extends X> g) {

        return _var1 -> g.apply(f.apply(_var1));
    }

    /**
     * @param g the outer function
     * @param f the inner function
     * @return g ∘ f
     */
    static <T, U, V, X extends Throwable> ExFunction<T, V, X> invCompose(
            final ExFunction<? super U, ? extends V, ? extends X> g,
            final ExFunction<? super T, ? extends U, ? extends X> f) {

        return ExFunction.compose(f, g);
    }
}
