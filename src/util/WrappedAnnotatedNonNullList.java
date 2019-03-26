package util;

import edu.umd.cs.findbugs.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import util.number.UInt32;

final class WrappedAnnotatedNonNullList<T> implements List<T> {
    private final AnnotatedNonNullList<?, T> base;

    WrappedAnnotatedNonNullList(final AnnotatedNonNullList<?, T> base) {
        this.base = base;
    }

    @Override
    public int size() {
        return this.base.size().intValue();
    }

    @Override
    public boolean isEmpty() {
        return this.base.isEmpty();
    }

    @Override
    public boolean contains(@Nullable final Object o) {
        if (o == null) {
            return false;
        }
        return this.base.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.base.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.base.toArray();
    }

    @Override
    public <A> A[] toArray(final A[] a) {
        if (a == null) {
            throw new IllegalArgumentException("Expected array, found null"); //$NON-NLS-1$
        }
        @SuppressWarnings("unchecked")
        final A[] ret = this.base.toArray(len -> (A[]) Array.newInstance(a.getClass(), len));
        if (a.length < ret.length) {
            return ret;
        }
        System.arraycopy(ret, 0, a, 0, ret.length);
        if (ret.length != a.length) {
            a[ret.length] = null;
        }
        return a;
    }

    @Override
    public boolean add(final T e) {
        this.base.add(e);
        return true;
    }

    @Override
    public boolean remove(final Object o) {
        if (o == null) {
            return false;
        }
        return this.base.remove(o).snd().isJust();
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        if (c == null) {
            throw new IllegalArgumentException("Expected Collection, found null"); //$NON-NLS-1$
        }
        return this.base.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        if (c == null) {
            throw new IllegalArgumentException("Expected Collection, found null"); //$NON-NLS-1$
        }
        this.base.addAll(c);
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        if (c == null) {
            throw new IllegalArgumentException("Expected Collection, found null"); //$NON-NLS-1$
        }
        this.base.addAll(UInt32.asUnsigned(index), c);
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        if (c == null) {
            throw new IllegalArgumentException("Expected Collection, found null"); //$NON-NLS-1$
        }
        final int oldSize = this.size();
        this.base.removeAll(c);
        return this.size() != oldSize;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        if (c == null) {
            throw new IllegalArgumentException("Expected Collection, found null"); //$NON-NLS-1$
        }
        final int oldSize = this.size();
        this.base.retainAll(c);
        return this.size() != oldSize;
    }

    @Override
    public void clear() {
        this.base.clear();
    }

    @Override
    public @Nullable T get(final int index) {
        return this.base.get(UInt32.asUnsigned(index)).fromMaybeNullable(null);
    }

    @Override
    public @Nullable T set(final int index, final T element) {
        return this.base.set(UInt32.asUnsigned(index), element).snd().fromMaybeNullable(null);
    }

    @Override
    public void add(final int index, final T element) {
        this.base.insert(UInt32.asUnsigned(index), element);
    }

    @Override
    public @Nullable T remove(final int index) {
        return this.base.remove(UInt32.asUnsigned(index)).snd().fromMaybeNullable(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int indexOf(final Object o) {
        return Maybe.maybe(o)
                .bind(_var1 -> {
                    try {
                        return Maybe.just((T) _var1);
                    } catch (final ClassCastException ex) {
                        return Maybe.nothing();
                    }
                })
                .bind(this.base::indexOf)
                .map(UInt32::intValue)
                .fromMaybe(-1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int lastIndexOf(final Object o) {
        return Maybe.maybe(o)
                .bind(_var1 -> {
                    try {
                        return Maybe.just((T) o);
                    } catch (final ClassCastException ex) {
                        return Maybe.nothing();
                    }
                })
                .bind(this.base::lastIndexOf)
                .map(UInt32::intValue)
                .fromMaybe(-1);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.base.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return this.base.listIterator(UInt32.ZERO);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.base.subList(UInt32.asUnsigned(fromIndex), UInt32.asUnsigned(toIndex));
    }
}
