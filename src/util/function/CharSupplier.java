package util.function;

/**
 * A supplier of characters.
 */
@FunctionalInterface
public interface CharSupplier {
    /**
     * @return a character
     */
    char get();
}
