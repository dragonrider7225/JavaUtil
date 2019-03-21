package util;

/**
 * A container for integers.
 */
public final class IntContainer extends ObjectContainer<Integer> {
    /**
     * Create a container with the given initial value.
     * @param value the initial value
     */
    public IntContainer(final int value) {
        super(value, int.class);
    }

    /**
     * @return the value of this container
     */
    public int getAsPrimitive() {
        return this.get();
    }

    /**
     * @param value the new value of this container
     * @return this
     */
    public IntContainer setAsPrimitive(final int value) {
        return (IntContainer) this.set(value);
    }
}
