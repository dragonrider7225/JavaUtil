package util;

import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @note this class has a natural ordering that is inconsistent with equals
 * @param <T> the left type
 * @param <U> the right type
 */
public class Either<@NonNull T, @NonNull U> implements Comparable<Either<T, U>> {
    private Maybe<T> left = Maybe.nothing();
    private Maybe<U> right = Maybe.nothing();

    Either(final boolean dummy, final T left) {
        this.left = Maybe.just(left);
    }

    Either(final U right) {
        this.right = Maybe.just(right);
    }

    /**
     * @param value the value contained in the returned object. Must be non-null.
     * @return a "Left" object containing the passed value
     */
    public static <@NonNull T, @NonNull U> Either<T, U> left(final T value) {
        return new Either<>(true, value);
    }

    /**
     * @param value the value contained in the returned object. Must be non-null
     * @return a "Right" object containing the passed value
     */
    public static <@NonNull T, @NonNull U> Either<T, U> right(final U value) {
        return new Either<>(value);
    }

    /**
     * @return true iff this value was created with {@link #left(Object) left(T)}
     */
    public final boolean isLeft() {
        return this.left.isJust();
    }

    /**
     * @return true iff this value was created with {@link #right(Object) right(U)}
     */
    public final boolean isRight() {
        return this.right.isJust();
    }

    /**
     * @return the non-null value held by this value with {@code this.isLeft()}
     * @throws NoSuchElementException if {@code !this.isLeft()}
     */
    public final T fromLeft() {
        return this.left.fromJust();
    }

    /**
     * @return the non-null value held by this value with {@code this.isRight()}
     * @throws NoSuchElementException if {@code !this.isRight()}
     */
    public final U fromRight() {
        return this.right.fromJust();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Either) {
            final Either<?, ?> other = (Either<?, ?>) o;
            if (other.isLeft() && this.isLeft()) {
                return this.left.equals(other.left);
            }
            if (other.isRight() && this.isRight()) {
                return this.right.equals(other.right);
            }
            return false;
        }
        return false;
    }

    public final boolean shallowEquals(final Either<T, U> other) {
        return this.left == other.left && this.right == other.right;
    }

    @Override
    public int hashCode() {
        if (this.isLeft()) {
            return this.fromLeft().hashCode();
        }
        return this.fromRight().hashCode() ^ 0x100;
    }

    @Override
    public int compareTo(final Either<T, U> o) {
        if (this.isLeft() && o.isRight()) {
            return -1;
        }
        if (this.isRight() && o.isLeft()) {
            return 1;
        }
        return 0;
    }
}
