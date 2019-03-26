package util;

import java.util.function.UnaryOperator;

import util.function.BooleanUnaryOperator;

/**
 * A specialization of {@link ObjectContainer} to boolean, with the addition of methods for use with primitives.
 */
public final class BooleanContainer extends ObjectContainer<Boolean> {
    /**
     * @param value the initial value of this container
     */
    public BooleanContainer(final Boolean value) {
        super(value, Boolean.class);
    }

    /**
     * Specialization of {@link #get()} to primitive.
     * @return this container's value as a primitive
     */
    public boolean getAsPrimitive() {
        return this.get();
    }

    /**
     * Specialization of {@link #set(Boolean)} to primitive.
     * @param value the value to set this container to
     * @return this
     */
    public BooleanContainer setAsPrimitive(final boolean value) {
        return (BooleanContainer) this.set(value);
    }

    @Override
    public BooleanContainer atomicUpdate(final UnaryOperator<Boolean> op) {
        if (op instanceof BooleanUnaryOperator) {
            return this.atomicUpdate((BooleanUnaryOperator) op);
        }
        return this.atomicUpdate(op::apply);
    }

    /**
     * Specialization of {@link #atomicUpdate(UnaryOperator)}.
     * @param op the operator
     * @return this
     */
    public BooleanContainer atomicUpdate(final BooleanUnaryOperator op) {
        if (op == null) {
            throw new IllegalArgumentException("Expected BooleanUnaryOperator, found null"); //$NON-NLS-1$
        }
        synchronized (this) {
            this.set(op.applyAsPrimitive(this.get()));
        }
        return this;
    }
}
