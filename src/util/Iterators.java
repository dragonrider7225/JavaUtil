package util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Collection of utility functions for {@link Iterator}s.
 */
public final class Iterators {
    private static final Iterator<?> EMPTY = new Iterator<>() {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }
    };

    private Iterators() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Can't instantiate Iterators"); //$NON-NLS-1$
    }

    /**
     * Concatenate the two iterators. If either iterator contains null, the concatenation will throw {@link IllegalStateException} upon
     * reaching that element in the iteration.
     * @param fst the first iterator
     * @param snd the second iterator
     * @return an iterator that iterates through the arguments in order
     */
    public static <T> Iterator<T> concat(final Iterator<? extends T> fst, final Iterator<? extends T> snd) {
        return new Iterator<>() {
            public boolean hasNext() {
                return fst.hasNext() || snd.hasNext();
            }

            public T next() {
                if (fst.hasNext()) {
                    return Maybe.maybe(fst.next()).fromJust();
                }
                return Maybe.maybe(snd.next()).fromJust();
            }
        };
    }

    /**
     * Generalization of {@link #concat(Iterator, Iterator)}.
     * @param iters the iterators to concatenate
     * @return the concatenation of the argument iterators in the order specified
     */
    @SafeVarargs
    @SuppressWarnings("null")
    public static <T> Iterator<T> concat(final Iterator<T>... iters) {
        return Arrays.stream(iters).reduce(Iterators.empty(), Iterators::concat);
    }

    /**
     * @param <T> the type of element for the iterator
     * @return the empty iterator
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> empty() {
        return (Iterator<T>) Iterators.EMPTY;
    }

    /**
     * Lazily apply the given function to the given iterator.
     * @param f the function to apply
     * @param i the iterator to map
     * @return an iterator which produces the application of the function to the elements of the iterator in the same order
     */
    public static <T, U> Iterator<U> map(final Function<? super T, ? extends U> f, final Iterator<T> i) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @SuppressWarnings("null")
            @Override
            public U next() {
                return f.apply(i.next());
            }
        };
    }
}
