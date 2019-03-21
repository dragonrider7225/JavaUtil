package util.number;

import java.util.stream.LongStream;

import org.eclipse.jdt.annotation.Nullable;

import util.Pair;

/**
 * An unsigned 128-bit integer.
 */
public final class UInt128 extends Number implements Comparable<UInt128> {
    /**
     * Structure v1.0
     */
    private static final long serialVersionUID = -2206240258098194755L;
    private static final long MIN_SIGNED = -0x7F;
    private static final long MAX_SIGNED = 0x7F;
    @SuppressWarnings("null")
    private static final UInt128[] cache = LongStream.rangeClosed(UInt128.MIN_SIGNED, UInt128.MAX_SIGNED)
            .mapToObj(value -> new UInt128(0, value))
            .toArray(UInt128[]::new);
    /**
     * Zero
     */
    public static final UInt128 ZERO = UInt128.asUnsigned(0, 0);
    private static final UInt128 ONE = UInt128.asUnsigned(0, 1);
    private static final UInt8 WIDTH = UInt8.asUnsigned((byte) 128);
    private final long mostSigBits;
    private final long leastSigBits;

    /**
     * Get the unsigned 128-bit integer with the same bits as the given values.
     * @param mostSigBits the most-significant bits of the u128
     * @param leastSigBits the value to convert to an unsigned 128-bit integer
     * @return the UInt128 represented by the same sequence of bits as the given values
     */
    public static UInt128 asUnsigned(final long mostSigBits, final long leastSigBits) {
        if (mostSigBits != 0 || (leastSigBits & 0xFFFF_FFFF_FFFF_FF00L) != 0) {
            return new UInt128(mostSigBits, leastSigBits);
        }
        return UInt128.cache[(int) (leastSigBits - UInt128.MIN_SIGNED)];
    }

    private UInt128(final long mostSigBits, final long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
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
        return this.leastSigBits;
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
        final double unsignedMinusSigned = 2 * Math.pow(2, 63);
        final double leastSigOfMost = Math.pow(2, 64);
        return this.leastSigBits + (this.leastSigBits < 0 ? unsignedMinusSigned : 0)
                + (this.mostSigBits + (this.mostSigBits < 0 ? unsignedMinusSigned : 0)) * leastSigOfMost;
    }

    @Override
    public int compareTo(final UInt128 other) {
        // If most significant bits have different signs, the "negative" one is greater
        if (this.mostSigBits < 0 && 0 <= other.mostSigBits) {
            return 1;
        }
        if (other.mostSigBits < 0 && 0 <= this.mostSigBits) {
            return -1;
        }
        // sign(this.mostSigBits) == sign(other.mostSigBits)
        if (this.mostSigBits < other.mostSigBits) {
            return -1;
        }
        if (other.mostSigBits < this.mostSigBits) {
            return 1;
        }
        // this.mostSigBits == other.mostSigBits
        // If least significant bits have different signs, the "negative" one is greater
        if (this.leastSigBits < 0 && 0 <= other.leastSigBits) {
            return 1;
        }
        if (other.leastSigBits < 0 && 0 <= this.leastSigBits) {
            return -1;
        }
        // sign(this.leastSigBits) == sign(other.leastSigBits)
        // Subtraction of signed integers with same sign can't overflow
        return (int) Math.signum(this.leastSigBits - other.leastSigBits);
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return this < other
     */
    public boolean lessThan(final UInt128 other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return this <= other
     */
    public boolean lte(final UInt128 other) {
        return !this.greaterThan(other);
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return this > other
     */
    public boolean greaterThan(final UInt128 other) {
        return other.lessThan(this);
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return this >= other
     */
    public boolean gte(final UInt128 other) {
        return !this.lessThan(other);
    }

    /**
     * @return this > 0
     */
    public boolean isPositive() {
        return this.greaterThan(UInt128.ZERO);
    }

    /**
     * @return the UInt128 representing the bitwise complement of this value
     */
    public UInt128 not() {
        return UInt128.asUnsigned(~this.mostSigBits, ~this.leastSigBits);
    }

    /**
     * @param other the other value
     * @return the UInt128 representing the bitwise and of this value and the other value
     */
    public UInt128 and(final UInt128 other) {
        return UInt128.asUnsigned(this.mostSigBits & other.mostSigBits, this.leastSigBits & other.leastSigBits);
    }

    /**
     * @param other the other value
     * @return the UInt128 representing the bitwise or of this value and the other value
     */
    public UInt128 or(final UInt128 other) {
        return UInt128.asUnsigned(this.mostSigBits | other.mostSigBits, this.leastSigBits | other.leastSigBits);
    }

    /**
     * @param other the other value
     * @return the UInt128 representing the bitwise xor of this value and the other value
     */
    public UInt128 xor(final UInt128 other) {
        return UInt128.asUnsigned(this.mostSigBits ^ other.mostSigBits, this.leastSigBits ^ other.leastSigBits);
    }

    /**
     * @param distance the number of bits to shift left
     * @return this << distance
     */
    public UInt128 shiftLeft(final UInt8 distance) {
        if (distance.gte(UInt128.WIDTH)) {
            return UInt128.ZERO;
        }
        final byte distanceSigned = distance.byteValue();
        if (63 < distanceSigned) {
            return UInt128.asUnsigned(this.leastSigBits << (distanceSigned - 64), 0);
        }
        // ret.leastSigBits != 0
        final long leastToMost = this.leastSigBits >>> (64 - distanceSigned); // logical shift brings in zeroes
        return UInt128.asUnsigned((this.mostSigBits << distanceSigned) | leastToMost, this.leastSigBits << distanceSigned);
    }

    /**
     * @param distance the number of bits to shift right
     * @return this >> distance
     */
    public UInt128 shiftRight(final UInt8 distance) {
        if (distance.gte(UInt128.WIDTH)) {
            return UInt128.ZERO;
        }
        final byte distanceSigned = distance.byteValue();
        if (63 < distanceSigned) {
            return UInt128.asUnsigned(0, this.mostSigBits >>> (distanceSigned - 64));
        }
        // ret.mostSigBits != 0
        final long mostToLeast = this.mostSigBits << (64 - distanceSigned);
        return UInt128.asUnsigned(this.mostSigBits >>> distanceSigned, mostToLeast | (this.mostSigBits >>> distanceSigned));
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return this.mostSigBits == ((UInt128) o).mostSigBits && this.leastSigBits == ((UInt128) o).leastSigBits;
    }

    @Override
    public int hashCode() {
        return UInt64.asUnsigned(this.mostSigBits).hashCode() ^ UInt64.asUnsigned(this.leastSigBits).hashCode();
    }

    /**
     * @return (floor((this + 1) / (2**128)), (this + 1) mod 2**128)
     */
    public Pair<Boolean, UInt128> plusOne() {
        return this.plus(UInt128.ONE);
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return (floor((this + other) / (2**128)), (this + other) mod 2**128)
     */
    public Pair<Boolean, UInt128> plus(final UInt128 other) {
        final long newLeastSig = this.leastSigBits + other.leastSigBits;
        final boolean leastSigOverflows = this.leastSigBits < 0 != other.leastSigBits < 0 && 0 <= newLeastSig;
        // Can't overflow twice, since a + 1 overflows if and only if signed(a) == -1
        //                         and a + b overflows only if signed(a) + signed(b) != -1
        final long newMostSig = this.mostSigBits + other.mostSigBits + (leastSigOverflows ? 1 : 0);
        final boolean mostSigOverflows = this.mostSigBits < 0 != other.mostSigBits < 0 && 0 <= newMostSig;
        return new Pair<>(mostSigOverflows, UInt128.asUnsigned(newMostSig, newLeastSig));
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return (this - other < 0, (this - other) mod 2**128)
     */
    public Pair<Boolean, UInt128> minus(final UInt128 other) {
        if (this == UInt128.ZERO) {
            return new Pair<>(false, this);
        }
        final Pair<Boolean, UInt128> negativeOther = other.not().plusOne();
        final boolean underflows = other.greaterThan(this);
        return this.plus(negativeOther.snd()).mapFst(none -> underflows);
    }

    /**
     * @param other the other 128-bit unsigned integer
     * @return (floor((this * other) / (2**128)), (this * other) mod 2**128)
     */
    public Pair<UInt128, UInt128> times(final UInt128 other) {
        // Algorithm based on UInt64.times
        final UInt64 thisLeast = UInt64.asUnsigned(this.leastSigBits);
        final UInt64 otherLeast = UInt64.asUnsigned(other.leastSigBits);
        final UInt64 thisMost = UInt64.asUnsigned(this.mostSigBits);
        final UInt64 otherMost = UInt64.asUnsigned(other.mostSigBits);

        final Pair<UInt64, UInt64> leastLeast = thisLeast.times(otherLeast);
        final Pair<UInt64, UInt64> leastMost = thisLeast.times(otherMost);
        final Pair<UInt64, UInt64> mostLeast = thisMost.times(otherLeast);
        final Pair<UInt64, UInt64> mostMost = thisMost.times(otherMost);

        final UInt64 retLeastLeast = leastLeast.snd();
        final Pair<Boolean, Pair<Boolean, UInt64>> retLeastMost = mostLeast.snd().plus(leastMost.snd()).mapSnd(leastLeast.fst()::plus);
        final Pair<Boolean, Pair<Boolean, Pair<Boolean, Pair<Boolean, UInt64>>>> retMostLeast = mostMost.snd().plus(mostLeast.fst())
                .mapSnd(leastMost.fst()::plus)
                .mapSnd(_var1 -> retLeastMost.fst() ? _var1.mapSnd(UInt64::plusOne) : _var1.mapSnd(__var1 -> __var1.plus(UInt64.ZERO)))
                .mapSnd(
                        _var1 -> retLeastMost.snd().fst()
                                ? _var1.mapSnd(__var1 -> __var1.mapSnd(UInt64::plusOne))
                                : _var1.mapSnd(__var1 -> __var1.mapSnd(___var1 -> ___var1.plus(UInt64.ZERO))));
        final UInt64 retMostMost = (retMostLeast.fst() ? mostMost.fst().plusOne() : mostMost.fst().plus(UInt64.ZERO))
                .mapSnd(_var1 -> (retMostLeast.snd().fst() ? _var1.plusOne() : _var1.plus(UInt64.ZERO)).snd())
                .mapSnd(_var1 -> (retMostLeast.snd().snd().fst() ? _var1.plusOne() : _var1.plus(UInt64.ZERO)).snd())
                .mapSnd(_var1 -> (retMostLeast.snd().snd().snd().fst() ? _var1.plusOne() : _var1.plus(UInt64.ZERO)).snd())
                .snd();

        return new Pair<>(
                UInt128.asUnsigned(retMostMost.longValue(), retMostLeast.snd().snd().snd().snd().longValue()),
                UInt128.asUnsigned(retLeastMost.snd().snd().longValue(), retLeastLeast.longValue()));
    }

    /**
     * @param divisor the other 128-bit unsigned integer
     * @return (floor(this / other), this % other)
     */
    public Pair<UInt128, UInt128> divMod(final UInt128 divisor) {
        if (divisor.equals(UInt128.ZERO)) {
            throw new ArithmeticException("/ by zero"); //$NON-NLS-1$
        }
        UInt128 dividend = this;
        UInt128 divisor_prime = divisor;
        while (divisor_prime.greaterThan(UInt128.ZERO)) {
            divisor_prime = divisor_prime.shiftLeft(UInt8.asUnsigned((byte) 1));
        }
        UInt128 quotient = UInt128.asUnsigned(0, 0);
        while (dividend.gte(divisor)) {
            quotient = quotient.times(UInt128.asUnsigned(0, 2)).snd(); // Quotient can't be wider than dividend
            if (dividend.gte(divisor_prime)) {
                quotient = quotient.plusOne().snd(); // quotient can't be wider than dividend
                dividend = dividend.minus(divisor_prime).snd(); // Can't underflow because dividend >= divisor_prime
            }
            divisor_prime = divisor_prime.shiftRight(UInt8.asUnsigned((byte) 1));
        }
        return new Pair<>(quotient, dividend);
    }
}
