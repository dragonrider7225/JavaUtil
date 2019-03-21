package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;

import util.function.TriFunction;

public final class MoreLists {
    private MoreLists() {
        throw new UnsupportedOperationException();
    }

    /**
     * A specialization of {@linkplain #zipWith(BiFunction, List, List)} where the joiner is
     * {@link Pair#Pair(Object, Object) new Pair(T, U)}
     */
    public static <@NonNull T, @NonNull U> List<Pair<T, U>> zip(final List<T> fst, final List<U> snd) {
        return MoreLists.zipWith(Pair::new, fst, snd);
    }

    /**
     * A specialization ,f {@linkplain #zipIndexWith(TriFunction, List, List)} where the joiner is
     * {@code (i, f, s) -> joiner.apply(f, s)}.
     */
    public static <T, U, V> List<V> zipWith(
            final BiFunction<T, U, V> joiner,
            final List<T> fst,
            final List<U> snd) {

        return MoreLists.zipIndexWith((i, f, s) -> joiner.apply(f, s), fst, snd);
    }

    /**
     * A specialization of {@linkplain #zipIndexWith(TriFunction, List, List)} where the joiner is
     * {@code (i, f, s) -> new Pair<>(i, new Pair<>(f, s))}.
     */
    public static <@NonNull T, @NonNull U> List<Pair<Integer, Pair<T, U>>> zipIndex(
            final List<T> fst,
            final List<U> snd) {

        return MoreLists.zipIndexWith(
                (i, f, s) -> new Pair<>(i, new Pair<>(f, s)), fst, snd);
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
    public static <T, U, V> List<V> zipIndexWith(
            final TriFunction<Integer, T, U, V> joiner,
            final List<T> fst,
            final List<U> snd) {

        final List<V> ret = new ArrayList<>();
        for (int i = 0; i < fst.size() && i < snd.size(); i++) {
            ret.add(joiner.apply(i, fst.get(i), snd.get(i)));
        }
        return ret;
    }

    public static <T, U> List<U> mapIndex(final List<T> xs, final BiFunction<Integer, T, U> mapper) {
        final List<U> ret = new ArrayList<>(xs.size());
        for (int i = 0; i < xs.size(); i++) {
            ret.set(i, mapper.apply(i, xs.get(i)));
        }
        return ret;
    }

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
