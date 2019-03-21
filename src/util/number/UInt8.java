package util.number;

import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.Nullable;

import util.Pair;

/**
 * An unsigned 8-bit integer.
 */
public final class UInt8 extends Number implements Comparable<UInt8> {
    /**
     * Structure v1.0
     */
    private static final long serialVersionUID = 5325678272360427796L;
    private static final byte MIN_SIGNED = (byte) 0x80;
    private static final byte MAX_SIGNED = (byte) 0x7F;
    @SuppressWarnings("null")
    private static final UInt8[] cache = IntStream.rangeClosed(UInt8.MIN_SIGNED, UInt8.MAX_SIGNED).mapToObj(UInt8::new)
            .toArray(UInt8[]::new);
    /**
     * Zero
     */
    public static final UInt8 ZERO = UInt8.asUnsigned((byte) 0);
    private static final UInt8 ONE = UInt8.asUnsigned((byte) 1);
    private static final UInt8 WIDTH = UInt8.asUnsigned((byte) 8);
    private final byte value;

    /**
     * Get the unsigned 8-bit integer equivalent to the given value.
     * @param value the value to convert to an unsigned byte
     * @return the UInt8 represented by the same sequence of bits as the given value
     */
    public static UInt8 asUnsigned(final byte value) {
        return UInt8.cache[value - UInt8.MIN_SIGNED];
    }

    private UInt8(final int value) {
        this.value = (byte) value;
    }

    /**
     * The 8-bit signed integer value that is represented by the same bits (in two's-complement) as this value.
     */
    @Override
    public byte byteValue() {
        return this.value;
    }

    /**
     * The 16-bit integer value that is equivalent to this unsigned value.
     */
    @Override
    public short shortValue() {
        return (short) this.intValue();
    }

    /**
     * The 32-bit integer value that is equivalent to this unsigned value.
     */
    @Override
    public int intValue() {
        return this.byteValue() & 0xFF;
    }

    /**
     * The 64-bit integer value that is equivalent to this unsigned value.
     */
    @Override
    public long longValue() {
        return this.intValue();
    }

    /**
     * The 32-bit floating point value that is equivalent to this unsigned value.
     */
    @Override
    public float floatValue() {
        return this.intValue();
    }

    /**
     * The 64-bit floating point value that is equivalent to this unsigned value.
     */
    @Override
    public double doubleValue() {
        return this.intValue();
    }

    @Override
    public int compareTo(final UInt8 other) {
        // this == other iff this.value == other.value
        if (this.value == other.value) {
            return 0;
        }
        // this > other iff
        // this.value and other.value have different signs and this.value < other.value
        // or this.value and other.value have the same sign and other.value < this.value
        if (this.value < 0 && 0 <= other.value || (this.value < 0 || 0 <= other.value) && other.value < this.value) {
            return 1;
        }
        return -1;
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return whether this < other
     */
    public boolean lessThan(final UInt8 other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return this <= other
     */
    public boolean lte(final UInt8 other) {
        return !this.greaterThan(other);
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return this > other
     */
    public boolean greaterThan(final UInt8 other) {
        return other.lessThan(this);
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return this >= other
     */
    public boolean gte(final UInt8 other) {
        return !this.lessThan(other);
    }

    /**
     * @return this > 0
     */
    public boolean isPositive() {
        return this.greaterThan(UInt8.ZERO);
    }

    /**
     * @return the UInt8 representing the bitwise complement of this value
     */
    public UInt8 not() {
        return UInt8.asUnsigned((byte) ~this.byteValue());
    }

    /**
     * @param other the other value
     * @return the UInt8 representing the bitwise and of this value and the other value
     */
    public UInt8 and(final UInt8 other) {
        return UInt8.asUnsigned((byte) (this.byteValue() & other.byteValue()));
    }

    /**
     * @param other the other value
     * @return the UInt8 representing the bitwise or of this value and the other value
     */
    public UInt8 or(final UInt8 other) {
        return UInt8.asUnsigned((byte) (this.byteValue() | other.byteValue()));
    }

    /**
     * @param other the other value
     * @return the UInt8 representing the bitwise xor of this value and the other value
     */
    public UInt8 xor(final UInt8 other) {
        return UInt8.asUnsigned((byte) (this.byteValue() ^ other.byteValue()));
    }

    /**
     * @param distance the number of bits to shift left
     * @return this << distance
     */
    public UInt8 shiftLeft(final UInt8 distance) {
        if (distance.gte(UInt8.WIDTH)) {
            return UInt8.ZERO;
        }
        return UInt8.asUnsigned((byte) (this.value << distance.value));
    }

    /**
     * @param distance the number of bits to shift right
     * @return this >> distance
     */
    public UInt8 shiftRight(final UInt8 distance) {
        if (distance.gte(UInt8.WIDTH)) {
            return UInt8.ZERO;
        }
        return UInt8.asUnsigned((byte) (this.value >>> distance.value));
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        return this.value == ((UInt8) other).value;
    }

    @Override
    public int hashCode() {
        return this.value & 0x0000_00FF;
    }

    /**
     * @return (floor((this + 1) / (2**8)), (this + 1) mod 2**8)
     */
    public Pair<Boolean, UInt8> plusOne() {
        return this.plus(UInt8.ONE);
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return (floor((this + other) / (2**8)), (this + other) mod 2**8)
     */
    public Pair<Boolean, UInt8> plus(final UInt8 other) {
        final byte newValue = (byte) (this.value + other.value);
        final boolean overflows = this.value < 0 != other.value < 0 && 0 <= newValue;
        return new Pair<>(overflows, UInt8.asUnsigned(newValue));
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return (this - other < 0, (this - other) mod 2**8)
     */
    public Pair<Boolean, UInt8> minus(final UInt8 other) {
        if (this == UInt8.ZERO) {
            return new Pair<>(false, this);
        }
        final Pair<Boolean, UInt8> negativeOther = other.not().plusOne();
        final boolean underflows = other.greaterThan(this);
        return this.plus(negativeOther.snd()).mapFst(none -> underflows);
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return (floor((this * other) / (2**8)) mod 2**8, (this * other) mod 2**8)
     */
    public Pair<UInt8, UInt8> times(final UInt8 other) {
        final short ret = (short) (this.value * other.value);
        return new Pair<>(UInt8.asUnsigned((byte) (ret >>> 8)), UInt8.asUnsigned((byte) ret));
    }

    /**
     * @param other the other 8-bit unsigned integer
     * @return (floor(this / other), this % other)
     */
    public Pair<UInt8, UInt8> divMod(final UInt8 other) {
        return new Pair<>(
                UInt8.asUnsigned((byte) (this.shortValue() / other.shortValue())),
                UInt8.asUnsigned((byte) (this.shortValue() % other.shortValue())));
    }
}
