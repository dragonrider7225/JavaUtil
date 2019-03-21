package util;

/**
 * A container for floats.
 */
public final class FloatContainer extends ObjectContainer<Float> {
    /**
     * Create a new float container with the given initial value.
     * @param value the initial value
     */
    public FloatContainer(final float value) {
        super(value, float.class);
    }

    /**
     * @return this.get() as a float
     */
    public float getAsPrimitive() {
        return this.get();
    }

    /**
     * @param value the new value of this container
     * @return this
     */
    public FloatContainer setAsPrimitive(final float value) {
        return (FloatContainer) this.set(value);
    }
}
