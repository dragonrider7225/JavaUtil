package util.function;

/**
 * A supplier of bytes.
 */
@FunctionalInterface
public interface ByteSupplier {
    /**
     * @return a byte
     */
    byte get();
}
