package util;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import util.number.UInt32;

/**
 * A fixed size view of a section of another {@link AnnotatedNonNullList}. Non-structural mutations write through to the backing list.
 * Structural mutations fail.
 * @param <T> the type of the elements of the list that this is a view of
 */
public class Slice<T> implements AnnotatedNonNullList<Slice<T>, T> {
    private final AnnotatedNonNullList<?, T> base;
    private final UInt32 fromIdx;
    private final UInt32 toIdx;

    Slice(final AnnotatedNonNullList<?, T> base, final UInt32 fromIdx, final UInt32 toIdx) {
        this.base = base;
        this.fromIdx = fromIdx;
        this.toIdx = base.size().lessThan(toIdx) ? base.size() : toIdx;
    }

    @Override
    public UInt32 size() {
        return this.toIdx.minus(this.fromIdx).snd();
    }

    @Override
    public boolean contains(final Object object) {
        final Iterator<T> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (object.equals(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean containsAll(final Collection<?> c) {
        final Collection<?> c2 = new ArrayList<>(c);
        final Iterator<T> iterator = this.iterator();
        while (iterator.hasNext()) {
            c2.remove(iterator.next());
        }
        return c2.isEmpty();
    }

    @Override
    public @NonNull Maybe<UInt32> indexOf(@NonNull final T value) {
        final ListIterator<T> liter = this.listIterator();
        while (liter.hasNext()) {
            if (value.equals(liter.next())) {
                return Maybe.just(UInt32.asUnsigned(liter.previousIndex()));
            }
        }
        return Maybe.nothing();
    }

    @Override
    public @NonNull Maybe<UInt32> lastIndexOf(@NonNull final T value) {
        final ListIterator<T> liter = this.listIterator(this.size());
        while (liter.hasPrevious()) {
            if (value.equals(liter.previous())) {
                return Maybe.just(UInt32.asUnsigned(liter.nextIndex()));
            }
        }
        return Maybe.nothing();
    }

    @Override
    public Maybe<T> get(final UInt32 index) {
        final Pair<Boolean, UInt32> baseIdx = this.fromIdx.plus(index);
        if (baseIdx.fst() || baseIdx.snd().gte(this.toIdx)) {
            return Maybe.nothing();
        }
        return this.base.get(baseIdx.snd());
    }

    @Override
    public Slice<T> add(@NonNull final T value) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> insert(final UInt32 index, @NonNull final T value) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> addAll(final Collection<? extends T> c) {
        if (c.size() == 0) {
            return this;
        }
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> addAll(final UInt32 index, final Collection<? extends T> c) {
        if (c.size() == 0) {
            return this;
        }
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Pair<Slice<T>, Maybe<T>> set(final UInt32 index, @NonNull final T value) {
        final var baseIdx = index.plus(this.fromIdx);
        if (baseIdx.fst() || baseIdx.snd().gte(this.toIdx)) {
            return new Pair<>(this, Maybe.nothing());
        }
        return this.base.set(baseIdx.snd(), value).mapFst(none -> this);
    }

    @Override
    public Pair<Slice<T>, Maybe<T>> remove(final UInt32 index) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Pair<Slice<T>, Maybe<T>> remove(final Object o) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Pair<Slice<T>, Maybe<T>> pureRemove(final UInt32 index) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Pair<Slice<T>, Maybe<T>> pureRemove(final Object o) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> removeAll(final Collection<?> c) {
        if (c.size() == 0) {
            return this;
        }
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> pureRemoveAll(final Collection<?> c) {
        if (c.size() == 0) {
            return this;
        }
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> removeIf(final Predicate<? super T> p) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> pureClear() {
        throw new UnsupportedOperationException("Can't get slice without base list"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> pureRemoveIf(final Predicate<? super T> p) {
        throw new UnsupportedOperationException("Can't get slice without base list"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> retainAll(final Collection<?> c) {
        if (c.size() == 0) {
            return this;
        }
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> retainIf(final Predicate<? super T> p) {
        throw new UnsupportedOperationException("Can't mutate size of slice"); //$NON-NLS-1$
    }

    @Override
    public Slice<T> pureRetainIf(final Predicate<? super T> p) {
        throw new UnsupportedOperationException("Can't get slice without base list"); //$NON-NLS-1$
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<T> iter = this.base.iterator();
        return new Iterator<>() {
            private final UInt32 i = UInt32.ZERO;

            {
                while (this.i.lessThan(Slice.this.fromIdx)) {
                    iter.next();
                }
            }

            public boolean hasNext() {
                return this.i.lessThan(Slice.this.toIdx) && iter.hasNext();
            }

            public T next() {
                if (this.hasNext()) {
                    return iter.next();
                }
                throw new NoSuchElementException("Slice: end of slice"); //$NON-NLS-1$
            }
        };
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.listIterator(UInt32.ZERO);
    }

    @Override
    public ListIterator<T> listIterator(final UInt32 startIndex) {
        final UInt32 endIdx = this.size();
        final UInt32 startIdx = startIndex.lessThan(endIdx) ? startIndex : endIdx;
        // fromIdx + startIdx can't overflow because startIdx <= this.size() and fromIdx + this.size() <= base.size()
        final ListIterator<T> liter = this.base.listIterator(this.fromIdx.plus(startIdx).snd());
        return new ListIterator<>() {
            private UInt32 i = startIdx;
            private final UInt32 maxI = endIdx;

            @Override
            public boolean hasNext() {
                if (this.i.lessThan(this.maxI)) {
                    if (!liter.hasNext()) {
                        throw new IllegalStateException("Slice: Missing next element in base ListIterator"); //$NON-NLS-1$
                    }
                    return true;
                }
                return false;
            }

            @Override
            public @NonNull T next() {
                if (this.hasNext()) {
                    this.i = this.i.plusOne().snd();
                    return Maybe.maybe(liter.next()).fromJust();
                }
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasPrevious() {
                if (this.i.isPositive()) {
                    if (!liter.hasPrevious()) {
                        throw new IllegalStateException("Slice: Missing previous element in ListIterator"); //$NON-NLS-1$
                    }
                    return true;
                }
                return false;
            }

            @Override
            public @NonNull T previous() {
                if (this.hasPrevious()) {
                    this.i = this.i.plus(UInt32.asUnsigned(-1)).snd();
                    return Maybe.maybe(liter.previous()).fromJust();
                }
                throw new NoSuchElementException();
            }

            @Override
            public int nextIndex() {
                return this.i.intValue();
            }

            @Override
            public int previousIndex() {
                return this.i.intValue() - 1;
            }

            @Override
            public void remove() {
                Slice.this.remove(this.i);
            }

            @Override
            public void set(@NonNull final T e) {
                Slice.this.set(this.i, e);
            }

            @Override
            public void add(@NonNull final T e) {
                Slice.this.insert(this.i, e);
            }
        };
    }
}
