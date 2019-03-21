package util.number;

import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.Nullable;

import util.Pair;

/**
 * An unsigned 32-bit integer.
 */
public final class UInt32 extends Number implements Comparable<UInt32> {
    /**
     * Structure v1.0
     */
    private static final long serialVersionUID = -4930812132202366806L;
    private static final int MIN_SIGNED = 0xFFFF_FF80;
    private static final int MAX_SIGNED = 0x0000_007F;
    @SuppressWarnings("null")
    private static final UInt32[] cache = IntStream.rangeClosed(UInt32.MIN_SIGNED, UInt32.MAX_SIGNED).mapToObj(UInt32::new)
            .toArray(UInt32[]::new);
    /**
     * Zero
     */
    public static final UInt32 ZERO = UInt32.asUnsigned(0);
    private static final UInt32 ONE = UInt32.asUnsigned(1);
    private static final UInt8 WIDTH = UInt8.asUnsigned((byte) 32);
    private final int value;

    /**
     * Get the unsigned 32-bit integer with the same bits as the given value.
     * @param value the value to convert to an unsigned byte
     * @return the UInt32 represented by the same sequence of bits as the given value
     */
    public static UInt32 asUnsigned(final int value) {
        if ((value & 0xFFFF_FF00) != 0) {
            return new UInt32(value);
        }
        return UInt32.cache[value - UInt32.MIN_SIGNED];
    }

    private UInt32(final int value) {
        this.value = value;
    }

    /**
     * The 8-bit signed integer value that is represented by the least-significant bits of this value.
     */
    @Override
    public byte byteValue() {
        return (byte) this.intValue();
    }

    /**
     * The 16-bit signed integer value that is represented by the least-significant bits of this value.
     */
    @Override
    public short shortValue() {
        return (short) this.intValue();
    }

    /**
     * The 32-bit signed integer value that is equivalent to this unsigned value.
     */
    @Override
    public int intValue() {
        return this.value;
    }

    /**
     * The 64-bit integer value that is equivalent to this unsigned value.
     */
    @Override
    public long longValue() {
        return this.intValue() & 0x0000_0000_FFFF_FFFF;
    }

    /**
     * The 32-bit floating point value that is equivalent to this unsigned value.
     */
    @Override
    public float floatValue() {
        return this.longValue();
    }

    /**
     * The 64-bit floating point value that is equivalent to this unsigned value.
     */
    @Override
    public double doubleValue() {
        return this.longValue();
    }

    @Override
    public int compareTo(final UInt32 other) {
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
     * @param other the other 32-bit unsigned integer
     * @return this < other
     */
    public boolean lessThan(final UInt32 other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return this <= other
     */
    public boolean lte(final UInt32 other) {
        return !this.greaterThan(other);
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return this > other
     */
    public boolean greaterThan(final UInt32 other) {
        return other.lessThan(this);
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return this >= other
     */
    public boolean gte(final UInt32 other) {
        return !this.lessThan(other);
    }

    /**
     * @return this > 0
     */
    public boolean isPositive() {
        return this.greaterThan(UInt32.ZERO);
    }

    /**
     * @return the UInt32 representing the bitwise complement of this value
     */
    public UInt32 not() {
        return UInt32.asUnsigned(~this.intValue());
    }

    /**
     * @param other the other value
     * @return the UInt32 representing the bitwise and of this value and the other value
     */
    public UInt32 and(final UInt32 other) {
        return UInt32.asUnsigned(this.intValue() & other.intValue());
    }

    /**
     * @param other the other value
     * @return the UInt32 representing the bitwise or of this value and the other value
     */
    public UInt32 or(final UInt32 other) {
        return UInt32.asUnsigned(this.intValue() | other.intValue());
    }

    /**
     * @param other the other value
     * @return the UInt32 representing the bitwise xor of this value and the other value
     */
    public UInt32 xor(final UInt32 other) {
        return UInt32.asUnsigned(this.intValue() ^ other.intValue());
    }

    /**
     * @param distance the number of bits to shift left
     * @return this << distance
     */
    public UInt32 shiftLeft(final UInt8 distance) {
        if (distance.gte(UInt32.WIDTH)) {
            return UInt32.ZERO;
        }
        return UInt32.asUnsigned(this.value << distance.byteValue());
    }

    /**
     * @param distance the number of bits to shift right
     * @return this >> distance
     */
    public UInt32 shiftRight(final UInt8 distance) {
        if (distance.gte(UInt32.WIDTH)) {
            return UInt32.ZERO;
        }
        return UInt32.asUnsigned(this.value >>> distance.byteValue());
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        return this.value == ((UInt32) other).value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    /**
     * @return (floor((this + 1) / (2**32)), (this + 1) mod 2**32)
     */
    public Pair<Boolean, UInt32> plusOne() {
        return this.plus(UInt32.ONE);
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return (floor((this + other) / (2**32)), (this + other) mod 2**32)
     */
    public Pair<Boolean, UInt32> plus(final UInt32 other) {
        final int newValue = this.value + other.value;
        final boolean overflows = this.value < 0 != other.value < 0 && 0 <= newValue;
        return new Pair<>(overflows, UInt32.asUnsigned(newValue));
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return (this - other < 0, (this - other) mod 2**32)
     */
    public Pair<Boolean, UInt32> minus(final UInt32 other) {
        if (this == UInt32.ZERO) {
            return new Pair<>(false, this);
        }
        final Pair<Boolean, UInt32> negativeOther = other.not().plusOne();
        final boolean underflows = other.greaterThan(this);
        return this.plus(negativeOther.snd()).mapFst(none -> underflows);
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return (floor((this * other) / (2**32)), (this * other) mod 2**32)
     */
    public Pair<UInt32, UInt32> times(final UInt32 other) {
        final long ret = (long) this.value * other.value;
        return new Pair<>(UInt32.asUnsigned((int) (ret >>> 32)), UInt32.asUnsigned((int) ret));
    }

    /**
     * @param other the other 32-bit unsigned integer
     * @return (floor(this / other), this % other)
     */
    public Pair<UInt32, UInt32> divMod(final UInt32 other) {
        return new Pair<>(
                UInt32.asUnsigned((int) (this.longValue() / other.longValue())),
                UInt32.asUnsigned((int) (this.longValue() % other.longValue())));
    }
}
