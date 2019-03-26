package util;

import java.util.function.Function;

/**
 * A pair of values.
 * @param <T> the type of the first value
 * @param <U> the type of the second value
 */
public final class Pair<T, U> {
    private final T fst;
    private final U snd;

    /**
     * @param fst the first element of the pair
     * @param snd the second element of the pair
     */
    public Pair(final T fst, final U snd) {
        if (fst == null) {
            throw new IllegalArgumentException("Expected object for first element of pair, found null"); //$NON-NLS-1$
        }
        if (snd == null) {
            throw new IllegalArgumentException("Expected object for second element of pair, found null"); //$NON-NLS-1$
        }
        this.fst = fst;
        this.snd = snd;
    }

    /**
     * @return the first element of this pair
     */
    public T fst() {
        return this.fst;
    }

    /**
     * @return the second element of this pair
     */
    public U snd() {
        return this.snd;
    }

    /**
     * @param f the function that maps this pair's first element to the first element of the returned pair
     * @return a pair which is the same as this pair except that the first element is the result of mapping this pair's first element by the
     * specified function
     */
    @SuppressWarnings("unchecked")
    public <V> Pair<V, U> mapFst(final Function<? super T, ? extends V> f) {
        final V retFst = f.apply(this.fst);
        if (retFst == this.fst) {
            return (Pair<V, U>) this; // if retFst is this.fst, then this.fst is an instance of V, so it's safe to interpret the field as V
                                      // when reading, and since fst is immutable, it can't be written.
        }
        return new Pair<>(retFst, this.snd);
    }

    /**
     * @param f the function that maps this pair's second element to the second element of the returned pair
     * @return a pair which is the same as this pair except that the second element is the result of mapping this pair's second element by
     * the specified function
     */
    @SuppressWarnings("unchecked")
    public <V> Pair<T, V> mapSnd(final Function<? super U, ? extends V> f) {
        final V retSnd = f.apply(this.snd);
        if (retSnd == this.snd) {
            return (Pair<T, V>) this;
        }
        return new Pair<>(this.fst, retSnd);
    }

    /**
     * @param fFst the function that maps this pair's first element to the first element of the returned pair
     * @param fSnd the function that maps this pair's second element to the second element of the returned pair
     * @return the pair (fFst(this.fst), fSnd(this.snd))
     */
    @SuppressWarnings("unchecked")
    public <V, W> Pair<V, W> map(final Function<? super T, ? extends V> fFst, final Function<? super U, ? extends W> fSnd) {
        final V retFst = fFst.apply(this.fst);
        final W retSnd = fSnd.apply(this.snd);
        if (retFst == this.fst && retSnd == this.snd) {
            return (Pair<V, W>) this;
        }
        return new Pair<>(retFst, retSnd);
    }

    /**
     * @param f the function that maps this pair to the returned pair
     * @return the result of applying the specified function to this pair
     */
    @SuppressWarnings("unchecked")
    public <V, W> Pair<V, W> map(final Function<Pair<? super T, ? super U>, Pair<? extends V, ? extends W>> f) {
        final Pair<? extends V, ? extends W> ret = f.apply(this);
        if (ret.fst == this.fst && ret.snd == this.snd) {
            return (Pair<V, W>) this;
        }
        return (Pair<V, W>) ret;
    }
}
