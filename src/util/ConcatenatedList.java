package util;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.number.UInt32;

/**
 * A view of the concatenation of any number of lists.
 * @param <E> the element type of this list
 */
public final class ConcatenatedList<E> implements AnnotatedNonNullList<ConcatenatedList<E>, E> {
    private final List<?>[] lists; // All elements of type List<E>

    /**
     * @param lists the lists to concatenate
     */
    @SafeVarargs // All elements of List<?>[] lists are guaranteed to be List<E> by compiler
    public ConcatenatedList(final List<E>... lists) {
        this.lists = lists;
    }

    @SuppressWarnings("unchecked")
    private Stream<List<E>> subListStream() {
        return Arrays.stream(this.lists).<List<E>>map(list -> (List<E>) list);
    }

    @Override
    public UInt32 size() {
        return UInt32.asUnsigned(this.subListStream().mapToInt(List::size).sum());
    }

    @Override
    public boolean isEmpty() {
        return this.subListStream().allMatch(List::isEmpty);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean contains(final Object o) {
        return this.subListStream().anyMatch(list -> list.contains(o));
    }

    @Override
    public Iterator<E> iterator() {
        return this.subListStream().map(List::iterator).reduce(Iterators.empty(), Iterators::concat);
    }

    @Override
    public ConcatenatedList<E> add(final E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<ConcatenatedList<E>, Maybe<E>> remove(final UInt32 index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<ConcatenatedList<E>, Maybe<E>> remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<ConcatenatedList<E>, Maybe<E>> pureRemove(final UInt32 index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<ConcatenatedList<E>, Maybe<E>> pureRemove(final Object o) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean containsAll(final Collection<?> c) {
        return c.size() == this.stream().filter(c::contains).collect(Collectors.toSet()).size();
    }

    @Override
    public ConcatenatedList<E> addAll(final Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> addAll(final UInt32 index, final Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> pureRemoveAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> removeIf(final Predicate<? super E> p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> pureRemoveIf(final Predicate<? super E> p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> retainIf(final Predicate<? super E> p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> pureRetainIf(final Predicate<? super E> p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Maybe<E> get(final UInt32 index) {
        return Maybe
                .just(this.stream().sequential().skip(index.intValue() - 1).findFirst().orElseThrow(() -> new IndexOutOfBoundsException()));
    }

    @Override
    public Pair<ConcatenatedList<E>, Maybe<E>> set(final UInt32 index, final E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConcatenatedList<E> insert(final UInt32 index, final E element) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public Maybe<UInt32> indexOf(final Object o) {
        return Maybe
                .fromOptional(
                        this.subListStream().map(_list -> new Pair<>(_list.size(), _list.indexOf(o)))
                                .reduce((_left, _right) -> {
                                    if (0 <= _left.snd()) {
                                        return _left;
                                    }
                                    final UnaryOperator<Integer> plusLeftSize = __var1 -> __var1 + _left.fst();
                                    return _right.map(plusLeftSize, __idx -> __idx < 0 ? __idx : plusLeftSize.apply(__idx));
                                }))
                .map(Pair::snd)
                .filter(_idx -> 0 <= _idx)
                .map(UInt32::asUnsigned);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public Maybe<UInt32> lastIndexOf(@Nullable final Object o) {
        return Maybe
                .fromOptional(
                        this.subListStream().map(_list -> new Pair<>(_list.size(), _list.indexOf(o)))
                                .reduce((_left, _right) -> {
                                    final UnaryOperator<Integer> plusLeftSize = __var1 -> __var1 + _left.fst();
                                    if (0 <= _right.snd()) {
                                        return _right.map(plusLeftSize, plusLeftSize);
                                    }
                                    return _left.mapFst((UnaryOperator<Integer>) __var1 -> __var1 + _right.fst());
                                }))
                .map(Pair::snd)
                .filter(_idx -> 0 <= _idx)
                .map(UInt32::asUnsigned);
    }

    @Override
    public ListIterator<E> listIterator(final UInt32 index) {
        @SuppressWarnings("unchecked")
        final ListIterator<E>[] liters = ConcatenatedList.this.subListStream().toArray(len -> (ListIterator<E>[]) new ListIterator[len]);
        return new ListIterator<>() {
            private int actualIndex = index.intValue();
            private int list;
            private final boolean nexted = false;
            private final boolean previoused = false;

            {
                this.list = 0;
                int listIdx = index.intValue();
                while (ConcatenatedList.this.lists[this.list].size() <= listIdx) {
                    while (liters[this.list].hasNext()) {
                        liters[this.list].next();
                    }
                    listIdx -= liters[this.list].nextIndex();
                }
                for (int i = 0; i < listIdx; i++) {
                    liters[this.list].next();
                }
            }

            @Override
            public boolean hasNext() {
                if (this.list < liters.length) { // Current iterator exists
                    // Current iterator is not exhausted or next iterator exists
                    return liters[this.list].hasNext() || this.list + 1 < liters.length;
                }
                return false;
            }

            @Override
            public @NonNull E next() {
                if (this.hasNext()) {
                    if (!liters[this.list].hasNext()) {
                        this.list++;
                    }
                    this.actualIndex++;
                    return liters[this.list].next();
                }
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasPrevious() {
                if (0 <= this.list) { // Current iterator exists
                    // Current iterator is not exhausted or previous iterator exists
                    return liters[this.list].hasPrevious() || 0 <= this.list - 1;
                }
                return false;
            }

            @Override
            public @NonNull E previous() {
                if (this.hasPrevious()) {
                    if (!liters[this.list].hasPrevious()) {
                        this.list--;
                    }
                    this.actualIndex--;
                    return liters[this.list].previous();
                }
                throw new NoSuchElementException();
            }

            @Override
            public int nextIndex() {
                return this.actualIndex;
            }

            @Override
            public int previousIndex() {
                return this.actualIndex - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Can't modify structure of list view"); //$NON-NLS-1$
            }

            @Override
            public void set(@NonNull final E e) {
                if (this.nexted == this.previoused) {
                    throw new IllegalStateException();
                }
                ConcatenatedList.this.set(UInt32.asUnsigned(this.actualIndex - (this.nexted ? 1 : 0)), e);
            }

            @Override
            public void add(@NonNull final E e) {
                throw new UnsupportedOperationException("Can't structurally modify list view"); //$NON-NLS-1$
            }
        };
    }

    @Override
    public Stream<E> stream() {
        return this.subListStream().flatMap(List::stream);
    }
}
