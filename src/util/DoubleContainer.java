package util;

/**
 * A specialization of {@link ObjectContainer} to double with primitive versions of {@link #get()} and {@link #set(Double)}.
 */
public final class DoubleContainer extends ObjectContainer<Double> {
    /**
     * Create a new DoubleContainer with the given initial value.
     * @param value the initial value
     */
    public DoubleContainer(final double value) {
        super(value, double.class);
    }

    /**
     * @return the value inside this container as a primitive
     */
    public double getAsPrimitive() {
        return this.get();
    }

    /**
     * @param value the new value of this container
     * @return this
     */
    public DoubleContainer setAsPrimitive(final double value) {
        return (DoubleContainer) this.set(value);
    }
}
