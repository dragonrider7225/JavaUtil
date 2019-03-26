package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import util.function.TriFunction;

/**
 * Utilities not present in {@link List}.
 */
public final class Lists {
    private Lists() {
        throw new UnsupportedOperationException();
    }

    /**
     * A specialization of {@linkplain #zipWith(BiFunction, List, List)} where the joiner is
     * {@link Pair#Pair(Object, Object) new Pair(T, U)}
     * @param fst the first elements of the pairs in the returned list
     * @param snd the second elements of the pairs in the returned list
     * @return the list of c<sub>i</sub>=(fst.get(i), snd.get(i)) such that 0 &le; i &lt; min(fst.size(), snd.size())
     */
    public static <T, U> List<Pair<T, U>> zip(final List<T> fst, final List<U> snd) {
        return Lists.zipWith(Pair::new, fst, snd);
    }

    /**
     * A specialization of {@linkplain #zipIndexWith(TriFunction, List, List)} where the joiner is
     * {@code (i, f, s) -> joiner.apply(f, s)}.
     * @param joiner the function to turn the pairs of elements into a single object
     * @param fst the list of first arguments to joiner
     * @param snd the list of second arguments to joiner
     * @return the list of c<sub>i</sub>=joiner(fst.get(i), snd.get(i)) such that 0 &le; i &lt; min(fst.size(), snd.size())
     */
    public static <T, U, V> List<V> zipWith(final BiFunction<T, U, V> joiner, final List<T> fst, final List<U> snd) {
        return Lists.zipIndexWith((i, f, s) -> joiner.apply(f, s), fst, snd);
    }

    /**
     * A specialization of {@linkplain #zipIndexWith(TriFunction, List, List)} where the joiner is
     * {@code (i, f, s) -> new Pair<>(i, new Pair<>(f, s))}.
     * @param fst the first elements of the pairs in the returned list
     * @param snd the second elements of the pairs in the returned list
     * @return the list of c<sub>i</sub>=(i, (fst.get(i), snd.get(i))) such that 0 &le; i &lt; min(fst.size(), snd.size())
     */
    public static <T, U> List<Pair<Integer, Pair<T, U>>> zipIndex(final List<T> fst, final List<U> snd) {
        return Lists.zipIndexWith((i, f, s) -> new Pair<>(i, new Pair<>(f, s)), fst, snd);
    }

    /**
     * Build and return a list {@code ret} where {@code ret.get(i) == joiner.apply(i, fst.get(i),
     * snd.get(i))}.
     * @param <T> the type of the first arguments
     * @param <U> the type of the second arguments
     * @param <V> the type of the elements of the returned list
     * @param joiner the joiner function
     * @param fst the list of first arguments to the joiner function
     * @param snd the list of second arguments to the joiner function
     * @return a list of values which are the result of applying joiner to each index and pair of
     * corresponding values in fst and snd. The length of the returned list is
     * {@code Math.min(fst.size(), snd.size())}.
     */
    public static <T, U, V> List<V> zipIndexWith(final TriFunction<Integer, T, U, V> joiner, final List<T> fst, final List<U> snd) {
        final int size = Math.min(fst.size(), snd.size());
        final List<V> ret = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ret.add(joiner.apply(i, fst.get(i), snd.get(i)));
        }
        return ret;
    }

    /**
     * Create and return a list such that each element is a function of its index and the value at its index in the specified list.
     * @param xs the list to map
     * @param mapper the mapping function
     * @return the mapped list
     */
    public static <T, U> List<U> mapIndex(final List<T> xs, final BiFunction<Integer, T, U> mapper) {
        final List<U> ret = new ArrayList<>(xs.size());
        for (int i = 0; i < xs.size(); i++) {
            ret.set(i, mapper.apply(i, xs.get(i)));
        }
        return ret;
    }

    /**
     * @param values the bytes to represent as a list
     * @return a list representation of the sequence of bytes
     */
    public static List<Byte> asByteList(final byte... values) {
        final List<Byte> ret = new ArrayList<>();
        for (final byte b : values) {
            ret.add(b);
        }
        return ret;
    }

    /**
     * @param xs the list to get a random element from.
     * @param rng the random number generator to use.
     * @return a random element of the given list, chosen by rng.
     * @throws IllegalStateException if the element selected by rng is null.
     */
    public static <T> T randEl(final List<T> xs, final Random rng) {
        return Maybe.maybe(xs.get(rng.nextInt(xs.size()))).fromJust();
    }
}
