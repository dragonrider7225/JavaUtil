package util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import util.function.ExFunction;

/**
 * A class that represents the possibility of the lack of a value.
 * @param <T> the type of the value that may be present
 */
public class Maybe<T> implements Iterable<@NonNull T> {
    private static final Maybe<?> NOTHING = new Maybe<>();
    @Nullable
    private final T value;

    Maybe() {
        this.value = null;
    }

    Maybe(final T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * @return an object which contains no value
     */
    @SuppressWarnings("unchecked")
    public static <@NonNull T> Maybe<T> nothing() {
        return (Maybe<T>) Maybe.NOTHING;
    }

    /**
     * @param value the value of the returned object
     * @param <T> the type of the value
     * @return an object which contains the given value
     */
    public static <@NonNull T> Maybe<T> just(final T value) {
        return new Maybe<>(value);
    }

    /**
     * @param value a potential value
     * @param <T> the type of the value
     * @return if value != null then {@link #just(Object) just(value)} else {@link #nothing()}
     */
    public static <@NonNull T> Maybe<T> maybe(@Nullable final T value) {
        if (value == null) {
            return Maybe.nothing();
        }
        return Maybe.just(value);
    }

    /**
     * Convert the unannotated {@link java.util.Optional} to the annotated Maybe.
     * @param wrapper the value to convert
     * @param <T> the type of the value contained by wrapper
     * @return if wrapper != null && {@link Optional#isPresent() wrapper.isPresent()} then {@link #just(Object) just(wrapper.get())}
     * else {@link #nothing()}
     */
    @SuppressWarnings("null")
    public static <@NonNull T> Maybe<T> fromOptional(final @Nullable Optional<T> wrapper) {
        if (wrapper == null) {
            return Maybe.nothing();
        }
        return wrapper.map(Maybe::just).orElseGet(Maybe::nothing);
    }

    /**
     * @return whether this object does not contain any value
     */
    public final boolean isNothing() {
        return this.value == null;
    }

    /**
     * @return whether this object contains a value
     */
    public final boolean isJust() {
        return this.value != null;
    }

    /**
     * @return if this object contains a value then the value contained by this object else throws an IllegalStateException
     * @throws IllegalStateException if {@link #isNothing()}
     */
    public final T fromJust() {
        @Nullable
        final T ret = this.value;
        if (ret == null) {
            throw new IllegalStateException("Tried to extract value from Nothing"); //$NON-NLS-1$
        }
        return ret;
    }

    /**
     * @param defaultValue the value to return if this object does not contain a value
     * @return if {@link #isJust()} then {@link #fromJust()} else defaultValue
     */
    @SuppressWarnings({"null", "unused"}) // Can't return null because fromMaybeNullable returns null only if argument is null
    public final T fromMaybe(final T defaultValue) {
        if (defaultValue == null) { // If user respects nullness annotations, always false
            throw new IllegalArgumentException("Expected object, found null"); //$NON-NLS-1$
        }
        return (@NonNull T) this.fromMaybeNullable(defaultValue);
    }

    /**
     * @deprecated Use {@link #fromMaybeGet(Supplier) fromMaybeGet(Supplier&lt;? extends T&gt;)}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public final T fromMaybe(final Supplier<? extends T> defaultSupplier) {
        return this.fromMaybeGet(defaultSupplier);
    }

    /**
     * @param defaultSupplier the supplier of the value to return if {@link #isNothing()}
     * @return if {@link #isNothing()} then {@link Supplier#get() defaultSupplier.get()} else {@link #fromJust()}
     */
    @SuppressWarnings("null")
    public final T fromMaybeGet(final Supplier<? extends T> defaultSupplier) {
        if (this.isNothing()) {
            return defaultSupplier.get();
        }
        return this.fromJust();
    }

    /**
     * @param defaultValue the value to return if {@link #isNothing()}
     * @return if {@link #isNothing()} then defaultValue else {@link #fromJust()}
     */
    public final @Nullable T fromMaybeNullable(@Nullable final T defaultValue) {
        if (this.isNothing()) {
            return defaultValue;
        }
        return this.fromJust();
    }

    /**
     * @param exSupplier the supplier of the value to return if {@link #isNothing()}
     * @return if {@link #isNothing()} then throws {@link Supplier#get() exSupplier.get()} else {@link #fromJust()}
     * @throws X the type of the value that is thrown
     */
    @SuppressWarnings("null")
    public <X extends Throwable> T tryGet(final Supplier<X> exSupplier) throws X {
        if (this.isJust()) {
            return this.fromJust();
        }
        throw exSupplier.get();
    }

    /**
     * @param f the mapping function
     * @return if {@link #isNothing()} then {@link #nothing()} else f.apply(fromJust())
     */
    public <@NonNull U> Maybe<U> bind(final Function<? super T, Maybe<U>> f) {
        return this.tryBind(f::apply);
    }

    /**
     * @param f the mapping function. May throw <tt>X</tt>
     * @return if {@link #isNothing()} then {@link #nothing()} else f.apply(fromJust())
     * @throws X if the mapper throws X
     */
    public <@NonNull U, X extends Throwable> Maybe<U> tryBind(final ExFunction<? super T, Maybe<U>, X> f) throws X {
        if (this.isNothing()) {
            return Maybe.nothing();
        }
        return f.apply(this.fromJust());
    }

    /**
     * @param f the mapping function
     * @return if {@link #isNothing()} then {@link #nothing()} else {@link #just(Object) just(f.apply(fromJust()))}
     */
    @SuppressWarnings("null")
    public <@NonNull U> Maybe<U> map(final Function<? super T, U> f) {
        return this.bind(f.andThen(Maybe::maybe));
    }

    /**
     * @param f the mapper. May throw <tt>X</tt>
     * @param <U> the output type that will get wrapped in a maybe
     * @param <X> the type of exception that <tt>f</tt> may throw
     * @return <tt>this.{@link #tryBind(ExFunction) tryBind}({@link #maybe(Object) maybe}∘f)</tt>
     * @throws X if <tt>f</tt> throws <tt>X</tt>
     */
    @SuppressWarnings("null")
    public <@NonNull U, X extends Throwable> Maybe<U> tryMap(final ExFunction<? super T, U, X> f) throws X {
        return this.tryBind(ExFunction.compose(f, Maybe::maybe));
    }

    /**
     * @param other the other Maybe
     * @return whether this and other contain the same value according to ==
     */
    public boolean shallowEquals(final Maybe<?> other) {
        if (this.isNothing()) {
            return other.isNothing();
        }
        if (other.isNothing()) {
            return false;
        }
        return this.value == other.value;
    }

    @Override
    @SuppressWarnings("null")
    public String toString() {
        if (this.isNothing()) {
            return "Nothing"; //$NON-NLS-1$
        }
        return MessageFormat.format("Just ({0})", this.fromJust().toString()); //$NON-NLS-1$
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (this == Maybe.NOTHING || o == Maybe.NOTHING) {
            return false;
        }
        if (o instanceof Maybe) {
            return this.fromJust().equals(((Maybe<?>) o).fromJust());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this == Maybe.NOTHING) {
            return 0;
        }
        return this.fromJust().hashCode();
    }

    /**
     * @param filter the filter on this <tt>Maybe</tt>
     * @return this if this is of the form <tt>Just x</tt> where <tt>filter.test(x)</tt>, </tt>Nothing</tt> otherwise
     */
    public Maybe<T> filter(final Predicate<? super T> filter) {
        if (this.isJust() && filter.test(this.fromJust())) {
            return this;
        }
        return Maybe.nothing();
    }

    /**
     * @param cls the type to test
     * @return whether this is of the form <tt>Just x</tt> where <tt>x</tt> is an instance of the type represented by <tt>cls</tt>
     */
    public final boolean isInstance(final Class<?> cls) {
        return this.filter(cls::isInstance).isJust();
    }

    /**
     * If this object is of the form {@code Just x} and {@code x} is an instance of the passed class, cast the value of this object to that
     * type. Otherwise return Nothing.
     * @param cls the type of the contained value to return
     * @param <U> the type to cast the wrapped value to
     * @return <tt>Nothing</tt> if this is <tt>Nothing</tt> or <tt>!(this instanceof U)</tt>, <tt>Just ((U) this.fromJust())</tt> otherwise
     */
    public final <@NonNull U> Maybe<U> cast(final Class<? extends U> cls) {
        return this.filter(cls::isInstance).map(cls::cast);
    }

    /**
     * Applies the given consumer to the value contained in this Maybe object iff <tt>{@link #isJust() this.isJust()}</tt>.
     * @param f the consumer to apply
     * @return this
     */
    public Maybe<T> ifJust(final Consumer<? super T> f) {
        if (this.isJust()) {
            f.accept(this.fromJust());
        }
        return this;
    }

    /**
     * Runs the given Runnable iff <tt>{@link #isNothing() this.isNothing()}</tt>.
     * @param f the runnable to run
     * @return this
     */
    public Maybe<T> ifNothing(final Runnable f) {
        if (this.isNothing()) {
            f.run();
        }
        return this;
    }

    /**
     * Filters out the given values.
     * @param values the values to filter out
     * @return this if this is <tt>Nothing</tt> or <tt>Just x</tt> where <tt>x</tt> is not in <tt>values</tt>, Nothing otherwise
     */
    public Maybe<T> except(final Object... values) {
        return this.filter(_value -> Arrays.stream(values).anyMatch(__argValue -> _value == __argValue));
    }

    /**
     * Like {@link #except(Object...)} except that equality is checked by {@link Object#equals(Object)} instead of <tt>==</tt>.
     * @param values the values to filter out
     * @return this if this is <tt>Nothing</tt> or <tt>Just x</tt> where <tt>x</tt> is not in <tt>values</tt>, Nothing otherwise
     */
    public Maybe<T> exceptDeep(final Object... values) {
        return this.filter(_value -> Arrays.stream(values).anyMatch(_value::equals));
    }

    /**
     * Filters out any value other than the given values.
     * @param values the values to keep
     * @return this if this is <tt>Just value</tt> for some <tt>value</tt> in <tt>values</tt> or <tt>Nothing</tt>, <tt>Nothing</tt>
     * otherwise
     */
    public Maybe<T> only(final Object... values) {
        return this.filter(_value -> Arrays.stream(values).anyMatch(__argValue -> __argValue == _value));
    }

    /**
     * Like {@link #only(Object...)} except that equality is checked by {@link Object#equals(Object)} instead of <tt>==</tt>.
     * @param values the values to keep
     * @return this if this is <tt>Just value</tt> for some <tt>value</tt> in <tt>values</tt> or <tt>Nothing</tt>, <tt>Nothing</tt>
     * otherwise
     */
    public Maybe<T> onlyDeep(final Object... values) {
        return this.filter(_value -> Arrays.stream(values).anyMatch(_value::equals));
    }

    @Override
    public Iterator<@NonNull T> iterator() {
        return new Iterator<>() {
            public boolean nextGotten = false;

            public final boolean hasNext() {
                return !this.nextGotten;
            }

            public final T next() {
                if (this.hasNext()) {
                    this.nextGotten = true;
                    return Maybe.this.fromJust();
                }
                throw new NoSuchElementException();
            }
        };
    }

    /**
     * @return if this is <tt>Just x</tt>, a stream with <tt>x</tt> as its sole element, otherwise the empty stream
     */
    @SuppressWarnings("null")
    public Stream<T> stream() {
        return this.isJust() ? Stream.of(this.fromJust()) : Stream.empty();
    }
}
