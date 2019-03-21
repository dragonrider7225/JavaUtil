package util.number;

import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.Nullable;

import util.Pair;

/**
 * An unsigned 16-bit integer.
 */
public final class UInt16 extends Number implements Comparable<UInt16> {
    /**
     * Structure v1.0
     */
    private static final long serialVersionUID = 3104615960713072715L;
    private static final short MIN_SIGNED = (short) 0xFF80;
    private static final short MAX_SIGNED = (short) 0x007F;
    @SuppressWarnings("null")
    private static final UInt16[] cache = IntStream.rangeClosed(UInt16.MIN_SIGNED, UInt16.MAX_SIGNED).mapToObj(UInt16::new)
            .toArray(UInt16[]::new);
    /**
     * Zero
     */
    public static final UInt16 ZERO = UInt16.asUnsigned((short) 0);
    private static final UInt16 ONE = UInt16.asUnsigned((short) 1);
    private static final UInt8 WIDTH = UInt8.asUnsigned((byte) 16);
    private final short value;

    /**
     * Get the unsigned 16-bit integer with the same bits as the given value.
     * @param value the value to convert to an unsigned byte
     * @return the UInt16 represented by the same sequence of bits as the given value
     */
    public static UInt16 asUnsigned(final short value) {
        if ((value & 0xFF00) != 0) {
            return new UInt16(value);
        }
        return UInt16.cache[value - UInt16.MIN_SIGNED];
    }

    private UInt16(final int value) {
        this.value = (short) value;
    }

    /**
     * The 8-bit signed integer value that is represented by the least-significant bits (in two's-complement) of this value.
     */
    @Override
    public byte byteValue() {
        return (byte) this.intValue();
    }

    /**
     * The 16-bit signed integer value that is equivalent to this unsigned value.
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
        return this.value & 0xFFFF;
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
    public int compareTo(final UInt16 other) {
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
     * @param other the other 16-bit unsigned integer
     * @return this < other
     */
    public boolean lessThan(final UInt16 other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return this <= other
     */
    public boolean lte(final UInt16 other) {
        return !this.greaterThan(other);
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return this > other
     */
    public boolean greaterThan(final UInt16 other) {
        return other.lessThan(this);
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return this >= other
     */
    public boolean gte(final UInt16 other) {
        return !this.lessThan(other);
    }

    /**
     * @return this > 0
     */
    public boolean isPositive() {
        return this.greaterThan(UInt16.ZERO);
    }

    /**
     * @return the UInt16 representing the bitwise complement of this value
     */
    public UInt16 not() {
        return UInt16.asUnsigned((short) ~this.shortValue());
    }

    /**
     * @param other the other value
     * @return the UInt16 representing the bitwise and of this value and the other value
     */
    public UInt16 and(final UInt16 other) {
        return UInt16.asUnsigned((short) (this.shortValue() & other.shortValue()));
    }

    /**
     * @param other the other value
     * @return the UInt16 representing the bitwise or of this value and the other value
     */
    public UInt16 or(final UInt16 other) {
        return UInt16.asUnsigned((short) (this.shortValue() | other.shortValue()));
    }

    /**
     * @param other the other value
     * @return the UInt16 representing the bitwise xor of this value and the other value
     */
    public UInt16 xor(final UInt16 other) {
        return UInt16.asUnsigned((short) (this.shortValue() ^ other.shortValue()));
    }

    /**
     * @param distance the number of bits to shift left
     * @return this << distance
     */
    public UInt16 shiftLeft(final UInt8 distance) {
        if (distance.gte(UInt16.WIDTH)) {
            return UInt16.ZERO;
        }
        return UInt16.asUnsigned((short) (this.value << distance.byteValue()));
    }

    /**
     * @param distance the number of bits to shift right
     * @return this >> distance
     */
    public UInt16 shiftRight(final UInt8 distance) {
        if (distance.gte(UInt16.WIDTH)) {
            return UInt16.ZERO;
        }
        return UInt16.asUnsigned((short) (this.value >>> distance.byteValue()));
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        return this.value == ((UInt16) other).value;
    }

    @Override
    public int hashCode() {
        return this.value & 0x0000_FFFF;
    }

    /**
     * @return (floor((this + 1) / (2**16)), (this + 1) mod 2**16)
     */
    public Pair<Boolean, UInt16> plusOne() {
        return this.plus(UInt16.ONE);
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return (floor((this + other) / (2**16)), (this + other) mod 2**16)
     */
    public Pair<Boolean, UInt16> plus(final UInt16 other) {
        final short newValue = (short) (this.value + other.value);
        final boolean overflows = this.value < 0 != other.value < 0 && 0 <= newValue;
        return new Pair<>(overflows, UInt16.asUnsigned(newValue));
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return (this - other < 0, (this - other) mod 2**16)
     */
    public Pair<Boolean, UInt16> minus(final UInt16 other) {
        if (this == UInt16.ZERO) {
            return new Pair<>(false, this);
        }
        final Pair<Boolean, UInt16> negativeOther = other.not().plusOne();
        final boolean underflows = other.greaterThan(this);
        return this.plus(negativeOther.snd()).mapFst(none -> underflows);
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return (floor((this * other) / (2**16)), (this * other) mod 2**16)
     */
    public Pair<UInt16, UInt16> times(final UInt16 other) {
        final int ret = this.value * other.value;
        return new Pair<>(UInt16.asUnsigned((short) (ret >>> 16)), UInt16.asUnsigned((short) ret));
    }

    /**
     * @param other the other 16-bit unsigned integer
     * @return (floor(this / other), this % other)
     */
    public Pair<UInt16, UInt16> divMod(final UInt16 other) {
        return new Pair<>(
                UInt16.asUnsigned((short) (this.intValue() / other.intValue())),
                UInt16.asUnsigned((short) (this.intValue() % other.intValue())));
    }
}
