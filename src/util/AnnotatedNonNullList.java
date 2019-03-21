package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.jdt.annotation.NonNull;

import util.number.UInt32;

/**
 * A port of {@link java.util.List} to support nullness annotations, since an implementation of List cannot be annotated correctly.
 * @param <C> the type of the list returned by the mutation methods
 * @param <T> the type of the elements of this list
 */
public interface AnnotatedNonNullList<C extends AnnotatedNonNullList<C, T>, T> /*extends List<T> */{
    /**
     * @return the number of elements in this list
     */
    UInt32 size();

    /**
     * @return whether this list has no elements
     */
    default boolean isEmpty() {
        return !this.size().isPositive();
    }

    /**
     * @param object the object to find
     * @return whether this list contains the given object
     */
    boolean contains(Object object);

    /**
     * @param c the collection of objects to find
     * @return whether this list contains at least as many references to each object as c does
     */
    boolean containsAll(Collection<?> c);

    /**
     * @param value the object to find
     * @return Just x for the least x such that <tt>this.get(x).equals(Maybe.just(value))</tt> if such an x exists, Nothing otherwise
     */
    Maybe<UInt32> indexOf(T value);

    /**
     * @param value the object to find
     * @return Just x for the greatest x such that <tt>this.get(x).equals(Maybe.just(value))</tt> if such an x exists, Nothing otherwise
     */
    Maybe<UInt32> lastIndexOf(T value);

    /**
     * @param index the index of this list to access
     * @return Just x where x is the object that is at the given index in this list if 0 &lt;= x &lt; {@link #size()}, Nothing otherwise
     */
    Maybe<T> get(UInt32 index);

    /**
     * Tries to add the specified value to the end of the list represented by this object. Implementations are required to return this.
     * @param value the value to add to the end of the list
     * @return this
     * @throws UnsupportedOperationException if the implementation does not permit adding elements to the end of the list
     */
    C add(T value);

    /**
     * Tries to insert the specified value to the list represented by this object at the specified index. Implementations are required to
     * return this.
     * @param index the index at which to insert the given object
     * @param value the object to insert
     * @return this
     * @throws UnsupportedOperationException if the implementation does not permit adding elements to the list
     */
    C insert(UInt32 index, T value);

    /**
     * Tries to add the specified values to the end of the list represented by this object. Implementations are required to return this.
     * @param c the values to add to the end of the list
     * @return this
     * @throws UnsupportedOperationException if the implementation does not permit adding elements to the end of the list
     */
    C addAll(Collection<? extends T> c);

    /**
     * Tries to insert the specified values to the list represented by this object at the specified index. Implementations are required to
     * return this.
     * @param index the index at which to insert the given object
     * @param c the objects to insert
     * @return this
     * @throws UnsupportedOperationException if the implementation does not permit adding elements to the list
     */
    C addAll(UInt32 index, Collection<? extends T> c);

    /**
     * Tries to replace the object at the specified index with the specified object. Implementations are required to return this.
     * @param index the index to modify
     * @param value the value to set at the specified index
     * @return a pair of this and "Just (the old value at the specified index)" if index is not greater than the size of this list,
     * otherwise a pair of this and Nothing
     * @throws UnsupportedOperationException if the implementation does not permit replacing elements of the list
     */
    Pair<C, Maybe<T>> set(UInt32 index, T value);

    /**
     * Remove the object at the given index of this list if the index refers to an object in this list and this list supports element
     * removal.
     * @param index the index of this list to access
     * @return a pair of this and "Just x where x is the object that was at the given index of this list if 0 &lt;= index &lt;
     * {@link #size()} and this implementation permits removing elements from the list, Nothing otherwise"
     * @throws UnsupportedOperationException if the implementation does not permit removing elements from the list
     */
    Pair<C, Maybe<T>> remove(UInt32 index);

    /**
     * Remove the first element x in this list such that x.equals(o) if such an element exists and this list supports element removal.
     * @param o the object to remove
     * @return a pair of this and "Just x where x is the object that was removed if such an element exists, Nothing otherwise"
     * @throws UnsupportedOperationException if the implementation does not permit removing elements from the list
     */
    Pair<C, Maybe<T>> remove(Object o);

    /**
     * Remove the object at the given index of this list if the index refers to an object in this list and this list supports element
     * removal.
     * @param index the index of this list to access
     * @return if this.size() >= index then (this, Nothing) else ("an object representing the list that this object represents except that
     * the object at the specified index in the list that this object represents is not present", Just "the object that has been removed")
     * @throws UnsupportedOperationException if the implementation does not support the creation of the returned list object
     */
    Pair<C, Maybe<T>> pureRemove(UInt32 index);

    /**
     * Remove the first element x in this list such that x.equals(o) if such an element exists and this list supports element removal.
     * @param o the object to remove
     * @return if !this.contains(o) then (this, Nothing) else ("an object representing the list that this object represents except that the
     * first object x such that x.equals(o) is not present", Just "the object that has been removed")
     * @throws UnsupportedOperationException if the implementation does not support the creation of the returned list object
     */
    Pair<C, Maybe<T>> pureRemove(Object o);

    /**
     * Remove each object in c from this list.
     * @param c the objects to remove from this list
     * @return this
     */
    C removeAll(Collection<?> c);

    /**
     * Like {@link #removeAll(Collection)} except that return value must be a new list object.
     * @param c the objects to exclude from the returned list
     * @return a new list object like this object except that each of the elements of c has been removed
     */
    C pureRemoveAll(Collection<?> c);

    /**
     * Remove each element x of this list such that p.test(x). This function must return this. For a version of this function that does not
     * modify this list, see {@link #pureRemoveIf(Predicate)}.
     * @param p the predicate to use in filtering this list
     * @return this
     */
    C removeIf(Predicate<? super T> p);

    /**
     * Functionally identical to {@link #removeIf(Predicate) removeIf(_ -> true)}.
     * @return this
     */
    default C clear() {
        return this.removeIf(none -> true);
    }

    /**
     * Create and return a list which contains all and only those elements of this list which do not match the given predicate.
     * @param p the predicate to use in filtering this list
     * @return a new list containing all and only those elements of this list which do not match the given predicate
     */
    C pureRemoveIf(Predicate<? super T> p);

    /**
     * Functionally identical to {@link #pureRemoveIf(Predicate) pureRemoveIf(_ -> true)}.
     * @return a new empty list
     */
    default C pureClear() {
        return this.pureRemoveIf(none -> true);
    }

    /**
     * Remove all elements of this list not contained in the specified collection. Result should be a permutation of the remaining elements
     * of c after c.removeAll(this).
     * @param c the list of elements to retain
     * @return this
     */
    C retainAll(Collection<?> c);

    /**
     * Remove each element x of this list such that !p.test(x). This function must return this. For a version of this function that does not
     * modify this list, see {@link #pureRetainIf(Predicate)}.
     * @param p the predicate to use in filtering this list
     * @return this
     */
    C retainIf(Predicate<? super T> p);

    /**
     * Create and return a list which contains all and only those elements of this list which match the given predicate.
     * @param p the predicate to use in filtering this list
     * @return a new list containing all and only those elements of this list which match the given predicate
     */
    C pureRetainIf(Predicate<? super T> p);

    /**
     * Produce a {@link java.util.List} view of a segment of this list that writes through.
     * @param fromIdx the first index of this list that the returned list can show
     * @param toIdx the first index of this list after fromIdx that the returned list can't show
     * @return a view of a segment of this list that writes through
     */
    default List<T> subList(final UInt32 fromIdx, final UInt32 toIdx) {
        return new WrappedAnnotatedNonNullList<>(this.slice(fromIdx, toIdx));
    }

    /**
     * Produce a {@link Slice} view of a segment of this list that writes through.
     * @param fromIdx the first index of this list that the returned list can show
     * @param toIdx the first index of this list after fromIdx that the returned list can't show
     * @return a view of a segment of this list that writes through
     */
    default Slice<T> slice(final UInt32 fromIdx, final UInt32 toIdx) {
        return new Slice<>(this, fromIdx, toIdx);
    }

    /**
     * Create and return an array representation of this list. For a method that returns an array with an element type other than Object,
     * see {@link #toArray(IntFunction)}.
     * @return an object array that is identical to the current state of this list
     */
    @SuppressWarnings("null")
    default Object[] toArray() {
        return this.stream().toArray();
    }

    /**
     * Create and return an array representation of this list.
     * @param arrayGenerator the function to generate the returned array. Must return a new array on each invocation.
     * @return a new array representing this list
     */
    @SuppressWarnings("null")
    default <A> @NonNull A[] toArray(final IntFunction<A[]> arrayGenerator) {
        return this.stream().toArray(arrayGenerator);
    }

    /**
     * @return an iterator over this list
     */
    Iterator<T> iterator();

    /**
     * @return a spliterator over this list
     */
    @SuppressWarnings("null")
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(this.iterator(), this.size().longValue(), Spliterator.ORDERED);
    }

    /**
     * A specialization of {@link #listIterator(UInt32)} with startIndex as 0.
     * @return a ListIterator over this list
     */
    default ListIterator<T> listIterator() {
        return this.listIterator(UInt32.ZERO);
    }

    /**
     * @param startIndex the initial index of the returned list iterator
     * @return a ListIterator over this list starting at the specified index
     */
    ListIterator<T> listIterator(UInt32 startIndex);

    /**
     * @return a stream over the elements of this list
     */
    @SuppressWarnings("null")
    default Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    /**
     * Convert this list to an instance of {@link java.util.List}
     * @return a {@link java.util.List} which represents the same sequence of objects
     */
    default List<T> asUnannotatedList() {
        return new WrappedAnnotatedNonNullList<>(this);
    }
}
