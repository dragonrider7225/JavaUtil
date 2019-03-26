package util.number;

import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.stream.LongStream;

import util.Pair;

/**
 * An unsigned 64-bit integer.
 */
public final class UInt64 extends Number implements Comparable<UInt64> {
    /**
     * Structure v1.0
     */
    private static final long serialVersionUID = 8500011781552339262L;
    private static final long MIN_SIGNED = 0xFFFF_FFFF_FFFF_FF80L;
    private static final long MAX_SIGNED = 0x0000_0000_0000_007FL;
    private static final UInt64[] cache = LongStream.rangeClosed(UInt64.MIN_SIGNED, UInt64.MAX_SIGNED).mapToObj(UInt64::new)
            .toArray(UInt64[]::new);
    /**
     * Zero
     */
    public static final UInt64 ZERO = UInt64.asUnsigned(0);
    private static final UInt64 ONE = UInt64.asUnsigned(1);
    private static final UInt8 WIDTH = UInt8.asUnsigned((byte) 64);
    private final long value;

    /**
     * Get the unsigned 64-bit integer with the same bits as the given value.
     * @param value the value to convert to an unsigned byte
     * @return the UInt64 represented by the same sequence of bits as the given value
     */
    public static UInt64 asUnsigned(final long value) {
        if ((value & 0xFFFF_FFFF_FFFF_FF00L) != 0) {
            return new UInt64(value);
        }
        return UInt64.cache[(int) (value - UInt64.MIN_SIGNED)];
    }

    private UInt64(final long value) {
        this.value = value;
    }

    /**
     * The 8-bit signed integer value that is represented by the least-significant bits of this value.
     */
    @Override
    public byte byteValue() {
        return (byte) this.longValue();
    }

    /**
     * The 16-bit signed integer value that is represented by the least-significant bits of this value.
     */
    @Override
    public short shortValue() {
        return (short) this.longValue();
    }

    /**
     * The 32-bit signed integer value that is represented by the least-significant bits of this value.
     */
    @Override
    public int intValue() {
        return (int) this.longValue();
    }

    /**
     * The 64-bit signed integer value that is equivalent to this unsigned value.
     */
    @Override
    public long longValue() {
        return this.value;
    }

    /**
     * The 32-bit floating point value that is closest to this unsigned value.
     */
    @Override
    public float floatValue() {
        return (float) this.doubleValue();
    }

    /**
     * The 64-bit floating point value that is closest to this unsigned value.
     */
    @Override
    public double doubleValue() {
        return this.value + (this.value < 0 ? Math.pow(2, 64) : 0);
    }

    @Override
    public int compareTo(final UInt64 other) {
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
     * @param other the other 64-bit unsigned integer
     * @return this < other
     */
    public boolean lessThan(final UInt64 other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @param other the other 64-bit unsigned integer
     * @return this <= other
     */
    public boolean lte(final UInt64 other) {
        return !this.greaterThan(other);
    }

    /**
     * @param other the other 64-bit unsigned integer
     * @return this > other
     */
    public boolean greaterThan(final UInt64 other) {
        return other.lessThan(this);
    }

    /**
     * @param other the other 64-bit unsigned integer
     * @return this >= other
     */
    public boolean gte(final UInt64 other) {
        return !this.lessThan(other);
    }

    /**
     * @return this > 0
     */
    public boolean isPositive() {
        return this.greaterThan(UInt64.ZERO);
    }

    /**
     * @return the UInt64 representing the bitwise complement of this value
     */
    public UInt64 not() {
        return UInt64.asUnsigned(~this.longValue());
    }

    /**
     * @param other the other value
     * @return the UInt64 representing the bitwise and of this value and the other value
     */
    public UInt64 and(final UInt64 other) {
        return UInt64.asUnsigned(this.longValue() & other.longValue());
    }

    /**
     * @param other the other value
     * @return the UInt64 representing the bitwise or of this value and the other value
     */
    public UInt64 or(final UInt64 other) {
        return UInt64.asUnsigned(this.longValue() | other.longValue());
    }

    /**
     * @param other the other value
     * @return the UInt64 representing the bitwise xor of this value and the other value
     */
    public UInt64 xor(final UInt64 other) {
        return UInt64.asUnsigned(this.longValue() ^ other.longValue());
    }

    /**
     * @param distance the number of bits to shift left
     * @return this << distance
     */
    public UInt64 shiftLeft(final UInt8 distance) {
        if (distance.gte(UInt64.WIDTH)) {
            return UInt64.ZERO;
        }
        return UInt64.asUnsigned(this.value << distance.byteValue());
    }

    /**
     * @param distance the number of bits to shift right
     * @return this >> distance
     */
    public UInt64 shiftRight(final UInt8 distance) {
        if (distance.gte(UInt64.WIDTH)) {
            return UInt64.ZERO;
        }
        return UInt64.asUnsigned(this.value >>> distance.byteValue());
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        return this.value == ((UInt64) other).value;
    }

    @Override
    public int hashCode() {
        return (int) (this.value ^ (this.value >> 32));
    }

    /**
     * @return (floor((this + 1) / (2**64)), (this + 1) mod 2**64)
     */
    public Pair<Boolean, UInt64> plusOne() {
        return this.plus(UInt64.ONE);
    }

    /**
     * @param other the other 64-bit unsigned integer
     * @return (floor((this + other) / (2**64)), (this + other) mod 2**64)
     */
    public Pair<Boolean, UInt64> plus(final UInt64 other) {
        final long newValue = this.value + other.value;
        final boolean overflows = this.value < 0 != other.value < 0 && 0 <= newValue;
        return new Pair<>(overflows, UInt64.asUnsigned(newValue));
    }

    /**
     * @param other the other 64-bit unsigned integer
     * @return (this - other < 0, (this - other) mod 2**64)
     */
    public Pair<Boolean, UInt64> minus(final UInt64 other) {
        if (this == UInt64.ZERO) {
            return new Pair<>(false, this);
        }
        final Pair<Boolean, UInt64> negativeOther = other.not().plusOne();
        final boolean underflows = other.greaterThan(this);
        return this.plus(negativeOther.snd()).mapFst(none -> underflows);
    }

    /**
     * @param other the other 64-bit unsigned integer
     * @return (floor((this * other) / (2**64)), (this * other) mod 2**64)
     */
    public Pair<UInt64, UInt64> times(final UInt64 other) {
        final UInt32 thisLeast = UInt32.asUnsigned((int) (this.value & 0x0000_0000_FFFF_FFFFL));
        final UInt32 otherLeast = UInt32.asUnsigned((int) (other.value & 0x0000_0000_FFFF_FFFFL));
        final UInt32 thisMost = UInt32.asUnsigned((int) ((this.value & 0xFFFF_FFFF_0000_0000L) >> 32)); // subset of 0x0000_0000_FFFF_FFFF
        final UInt32 otherMost = UInt32.asUnsigned((int) ((other.value & 0xFFFF_FFFF_0000_0000L) >> 32)); // subset of 0x0000_0000_FFFF_FFFF
        // ret = thisLeast * (otherLeast + otherMost * 2**32) + thisMost * (otherLeast + otherMost * 2**32)
        //     = thisLeast * otherLeast + thisLeast * otherMost * 2**32 + thisMost * otherLeast * 2**32 + thisMost * otherMost * 2**64
        //     = thisMost * otherMost * 2**64 + (thisLeast * otherMost + thisMost * otherLeast) * 2**32 + thisLeast * otherLeast
        // maxPartProduct = 0xFFFF_FFFF * 0xFFFF_FFFF
        //                = (16**8 - 1) * (16**8 - 1)
        //                = 16**16 - 2 * 16**8 + 1
        //                = 0x1_0000_0000_0000_0000 - 0x0_0000_0002_0000_0000 + 1
        //                = 0xFFFF_FFFE_0000_0001
        final Pair<UInt32, UInt32> leastLeast = thisLeast.times(otherLeast);
        final Pair<UInt32, UInt32> leastMost = thisLeast.times(otherMost);
        final Pair<UInt32, UInt32> mostLeast = thisMost.times(otherLeast);
        final Pair<UInt32, UInt32> mostMost = thisMost.times(otherMost);
        // maxProduct = maxPartProduct * 16**16 + 2 * maxPartProduct * 16**8 + maxPartProduct
        //            = 0xFFFF_FFFE_0000_0001__0000_0000_0000_0000 + 2 * 0xFFFF_FFFE__0000_0001_0000_0000 + 0xFFFF_FFFE_0000_0001
        //            = 0xFFFF_FFFE_0000_0001__0000_0000_0000_0000 + 0x0001_FFFF_FFFC__0000_0002_0000_0000 + 0xFFFF_FFFE_0000_0001
        //            = 0xFFFF_FFFE_0000_0001__0000_0000_0000_0000 + 0x0001_FFFF_FFFD__0000_0000_0000_0001
        //            = 0xFFFF_FFFF_FFFF_FFFE__0000_0000_0000_0001
        // product = (mostMost.fst() * 16**8 + (mostMost.snd() + mostLeast.fst() + leastMost.fst())) * 16**16
        //              + (mostLeast.snd() + leastMost.snd() + leastLeast.fst()) * 16**8 + leastLeast.snd()
        final UInt32 retLeastLeast = leastLeast.snd();
        final Pair<Boolean, Pair<Boolean, UInt32>> retLeastMost = mostLeast.snd().plus(leastMost.snd()).mapSnd(leastLeast.fst()::plus);
        final Pair<Boolean, Pair<Boolean, Pair<Boolean, Pair<Boolean, UInt32>>>> retMostLeast = mostMost.snd().plus(mostLeast.fst())
                .mapSnd(leastMost.fst()::plus)
                .mapSnd(_var1 -> retLeastMost.fst() ? _var1.mapSnd(UInt32::plusOne) : _var1.mapSnd(__var1 -> __var1.plus(UInt32.ZERO)))
                .mapSnd(
                        _var1 -> retLeastMost.snd().fst()
                                ? _var1.mapSnd(__var1 -> __var1.mapSnd(UInt32::plusOne))
                                : _var1.mapSnd(__var1 -> __var1.mapSnd(___var1 -> ___var1.plus(UInt32.ZERO))));
        final UInt32 retMostMost = (retMostLeast.fst() ? mostMost.fst().plusOne() : mostMost.fst().plus(UInt32.ZERO))
                .mapSnd(_var1 -> (retMostLeast.snd().fst() ? _var1.plusOne() : _var1.plus(UInt32.ZERO)).snd())
                .mapSnd(_var1 -> (retMostLeast.snd().snd().fst() ? _var1.plusOne() : _var1.plus(UInt32.ZERO)).snd())
                .mapSnd(_var1 -> (retMostLeast.snd().snd().snd().fst() ? _var1.plusOne() : _var1.plus(UInt32.ZERO)).snd())
                .snd(); // Can't overflow because max mathematical value of this * other < 16**32
        return new Pair<>(
                UInt64.asUnsigned((retMostMost.longValue() << 32) + retMostLeast.snd().snd().snd().snd().longValue()),
                UInt64.asUnsigned((retLeastMost.snd().snd().longValue() << 32) + retLeastLeast.longValue()));
    }

    /**
     * @param divisor the other 64-bit unsigned integer
     * @return (floor(this / other), this % other)
     */
    public Pair<UInt64, UInt64> divMod(final UInt64 divisor) {
        if (divisor.equals(UInt64.ZERO)) {
            throw new ArithmeticException("/ by zero"); //$NON-NLS-1$
        }
        UInt64 dividend = this;
        UInt64 divisor_prime = divisor;
        while (divisor_prime.greaterThan(UInt64.ZERO)) {
            divisor_prime = divisor_prime.shiftLeft(UInt8.asUnsigned((byte) 1));
        }
        UInt64 quotient = UInt64.asUnsigned(0);
        while (dividend.gte(divisor)) {
            quotient = quotient.times(UInt64.asUnsigned(2)).snd(); // quotient can't be wider than dividend
            if (dividend.gte(divisor_prime)) {
                quotient = quotient.plusOne().snd(); // quotient can't be wider than dividend
                dividend = dividend.minus(divisor_prime).snd(); // zero not greater than or equal to divisor, since divisor must be positive
            }
            divisor_prime = divisor_prime.shiftRight(UInt8.asUnsigned((byte) 1));
        }
        return new Pair<>(quotient, dividend);
    }
}
