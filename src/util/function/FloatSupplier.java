package util.function;

/**
 * A supplier of floats.
 */
@FunctionalInterface
public interface FloatSupplier {
    /**
     * @return a float
     */
    float get();
}
