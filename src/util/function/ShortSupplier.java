package util.function;

/**
 * A supplier of shorts.
 */
@FunctionalInterface
public interface ShortSupplier {
    /**
     * @return a short
     */
    short get();
}
