package util;

import java.text.MessageFormat;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.UnaryOperator;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A container type primarily intended for use with {@link java.util.stream.Stream#forEachOrdered}, although if the operator is commutative
 * and associative, {@link java.util.stream.Stream#forEach} can be used instead to allow any overhead in the consumer outside of the update
 * to this container to run concurrently.
 *
 * @param <T> the type of value that this container accepts
 */
public class ObjectContainer<T> {
    private boolean synchronize = false;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private T value;
    private final Class<T> cls;

    /**
     * Construct a new container with the given value.
     * @param value the value to initialize this container with
     * @param cls the class representing the type of value this container accepts
     */
    @SuppressWarnings({"null", "unused"})
    public ObjectContainer(final T value, final Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Expected Class, found null"); //$NON-NLS-1$
        }
        if (value == null) {
            throw new IllegalArgumentException(MessageFormat.format("Expected {0}, found null", cls.getSimpleName())); //$NON-NLS-1$
        }
        this.value = value;
        this.cls = cls;
    }

    /**
     * Synchronize all future accesses to the contained value.
     * @return this
     */
    public ObjectContainer<T> synchronize() {
        this.synchronize = true;
        return this;
    }

    /**
     * @return the value of this container
     */
    public final T get() {
        if (this.synchronize) {
            this.lock.readLock().lock();
                final T ret = this.value;
            this.lock.readLock().unlock();
            return ret;
        }
        return this.value;
    }

    /**
     * Sets the value of this container.
     * @param value the value to set this container to. Must not be null
     * @return this
     */
    @SuppressWarnings({"null", "unused"})
    public final ObjectContainer<T> set(final T value) {
        if (value == null) {
            throw new IllegalArgumentException(MessageFormat.format("Expected {0}, found null", this.cls.getSimpleName())); //$NON-NLS-1$
        }
        if (this.synchronize) {
            this.lock.writeLock().lock();
                this.value = value;
            this.lock.writeLock().unlock();
            return this;
        }
        synchronized (this) {
            this.value = value;
        }
        return this;
    }

    /**
     * Updates the value of this container atomically. That is, this object is guaranteed to not be modified between when the unary operator
     * is applied to this object's value and when this object's value is updated to be the result of the operator.
     * @param op the updater function. Must not return null
     * @return this
     */
    @SuppressWarnings({"null", "unused"})
    public ObjectContainer<T> atomicUpdate(final UnaryOperator<T> op) {
        if (op == null) {
            throw new IllegalArgumentException("Expected UnaryOperator, found null"); //$NON-NLS-1$
        }
        synchronized (this) {
            if (this.synchronize) {
                this.lock.writeLock().lock();
            }
                @Nullable
                final T tmpValue = op.apply(this.value);
                if (tmpValue == null) {
                    throw new IllegalArgumentException(
                            MessageFormat.format("Expected {0}, found null", this.cls.getSimpleName())); //$NON-NLS-1$
                }
                this.value = tmpValue;
            if (this.synchronize) {
                this.lock.writeLock().unlock();
            }
        }
        return this;
    }

    /**
     * Like (==) for the contained values.
     * @param other the other container to check
     * @return whether this container and the other container contain the same object (by reference)
     */
    public final boolean shallowEquals(final ObjectContainer<?> other) {
        if (this.synchronize) {
            this.lock.readLock().lock();
        }
        if (other.synchronize) {
            other.lock.readLock().lock();
        }
            final boolean ret = this.value == other.value;
        if (other.synchronize) {
            other.lock.readLock().unlock();
        }
        if (this.synchronize) {
            this.lock.readLock().unlock();
        }
        return ret;
    }

    @Override
    public final boolean equals(@Nullable final Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ObjectContainer<?> other = (ObjectContainer<?>) o;
        if (this.synchronize) {
            this.lock.readLock().lock();
        }
        if (other.synchronize) {
            other.lock.readLock().lock();
        }
            final boolean ret = this.value.equals(other.value);
        if (other.synchronize) {
            other.lock.readLock().unlock();
        }
        if (this.synchronize) {
            this.lock.readLock().unlock();
        }
        return ret;
    }

    @Override
    public final int hashCode() {
        return 31 ^ this.value.hashCode();
    }
}
