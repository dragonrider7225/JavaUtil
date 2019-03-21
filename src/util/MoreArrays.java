package util;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import util.annotation.Pure;
import util.function.ByteSupplier;
import util.function.CharSupplier;
import util.function.FloatSupplier;
import util.function.ShortSupplier;
import util.function.ToByteFunction;
import util.function.ToCharFunction;
import util.function.ToFloatFunction;
import util.function.ToShortFunction;

public final class MoreArrays {
    private MoreArrays() {
        // Nothing
    }

    @Pure
    public static <@Nullable T> int indexOf(@Nullable final T[] xs, @Nullable final T x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final boolean[] xs, final boolean x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final byte[] xs, final byte x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final short[] xs, final short x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final int[] xs, final int x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final long[] xs, final long x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final float[] xs, final float x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Pure
    public static int indexOf(final double[] xs, final double x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public static <@NonNull T> void fill(final T[] array, final Supplier<? extends T> supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    public static void fill(final boolean[] array, final BooleanSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsBoolean();
        }
    }

    public static void fill(final byte[] array, final ByteSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    public static void fill(final short[] array, final ShortSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    public static void fill(final int[] array, final IntSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsInt();
        }
    }

    public static void fill(final long[] array, final LongSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsLong();
        }
    }

    public static void fill(final float[] array, final FloatSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    public static void fill(final double[] array, final DoubleSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsDouble();
        }
    }

    @SuppressWarnings("null")
    public static <A> A[] replicate(final int len, final Supplier<? extends A> valueSupplier, final IntFunction<A[]> arraySupplier) {
        return Stream.generate(valueSupplier).limit(len).toArray(arraySupplier);
    }

    public static char[] replicatec(final int len, final CharSupplier valueSupplier) {
        final char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    public static byte[] replicateb(final int len, final ByteSupplier valueSupplier) {
        final byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    public static short[] replicates(final int len, final ShortSupplier valueSupplier) {
        final short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    public static int[] replicatei(final int len, final IntSupplier valueSupplier) {
        return IntStream.generate(valueSupplier).limit(len).toArray();
    }

    public static long[] replicatel(final int len, final LongSupplier valueSupplier) {
        return LongStream.generate(valueSupplier).limit(len).toArray();
    }

    public static float[] replicatef(final int len, final FloatSupplier valueSupplier) {
        final float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    public static double[] replicated(final int len, final DoubleSupplier valueSupplier) {
        return DoubleStream.generate(valueSupplier).limit(len).toArray();
    }

    @Pure
    public static Character[] boxed(final char[] xs) {
        @SuppressWarnings("null")
        final Character[] ret = new Character[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static char[] unboxed(final Character[] xs) {
        final char[] ret = new char[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static Byte[] boxed(final byte[] xs) {
        @SuppressWarnings("null")
        final Byte[] ret = new Byte[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static byte[] unboxed(final Byte[] xs) {
        final byte[] ret = new byte[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static Short[] boxed(final short[] xs) {
        @SuppressWarnings("null")
        final Short[] ret = new Short[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static short[] unboxed(final Short[] xs) {
        final short[] ret = new short[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static Integer[] boxed(final int[] xs) {
        @SuppressWarnings("null")
        final Integer[] ret = new Integer[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static int[] unboxed(final Integer[] xs) {
        final int[] ret = new int[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static Long[] boxed(final long[] xs) {
        @SuppressWarnings("null")
        final Long[] ret = new Long[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static long[] unboxed(final Long[] xs) {
        final long[] ret = new long[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static Float[] boxed(final float[] xs) {
        @SuppressWarnings("null")
        final Float[] ret = new Float[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static float[] unboxed(final Float[] xs) {
        final float[] ret = new float[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static Double[] boxed(final double[] xs) {
        @SuppressWarnings("null")
        final Double[] ret = new Double[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    @Pure
    public static double[] unboxed(final Double[] xs) {
        final double[] ret = new double[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static byte randEl(final @Pure byte[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static short randEl(final @Pure short[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static int randEl(final @Pure int[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static long randEl(final @Pure long[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static float randEl(final @Pure float[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static double randEl(final @Pure double[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param arr the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static <@Nullable E> E randEl(final @Pure @Nullable E[] arr, final Random rng) {
        return arr[rng.nextInt(arr.length)];
    }

    /**
     * Like an eager version of {@linkplain Stream#map(Function)}.
     * @param <T> the type of the elements of xs
     * @param <U> the type of the elements of the returned array
     * @param f the mapper function
     * @param xs the array of elements to map
     * @param retGen the source of an array of U
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.apply(xs[i])</tt>.
     */
    public static <T, U> U[] map(final Function<? super T, ? extends U> f, final T[] xs, final IntFunction<U[]> retGen) {
        final U[] ret = retGen.apply(xs.length);
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.apply(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToInt(ToIntFunction)} except that the results are chars instead of ints.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsPrimitive(xs[i])</tt>.
     */
    public static <T> char[] mapToChar(final ToCharFunction<? super T> f, final T[] xs) {
        final char[] ret = new char[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsPrimitive(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToInt(ToIntFunction)} except that the results are bytes instead of ints.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsPrimitive(xs[i])</tt>.
     */
    public static <T> byte[] mapToByte(final ToByteFunction<? super T> f, final T[] xs) {
        final byte[] ret = new byte[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsPrimitive(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToInt(ToIntFunction)} except that the results are shorts instead of ints.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsPrimitive(xs[i])</tt>.
     */
    public static <T> short[] mapToShort(final ToShortFunction<? super T> f, final T[] xs) {
        final short[] ret = new short[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsPrimitive(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToInt(ToIntFunction)}.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsInt(xs[i])</tt>.
     */
    public static <T> int[] mapToInt(final ToIntFunction<? super T> f, final T[] xs) {
        final int[] ret = new int[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsInt(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToLong(ToLongFunction)}.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsLong(xs[i])</tt>.
     */
    public static <T> long[] mapToLong(final ToLongFunction<? super T> f, final T[] xs) {
        final long[] ret = new long[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsLong(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToDouble(ToDoubleFunction)} except that the results are floats instead of bytes.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsPrimitive(xs[i])</tt>.
     */
    public static <T> float[] mapToFloat(final ToFloatFunction<? super T> f, final T[] xs) {
        final float[] ret = new float[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsPrimitive(xs[i]);
        }
        return ret;
    }

    /**
     * Like an eager version of {@linkplain Stream#mapToDouble(ToDoubleFunction)}.
     * @param <T> the type of the elements of xs
     * @param f the mapper function
     * @param xs the array of elements to map
     * @return an array <tt>ret</tt> such that &forall; i &isin; [0, xs.length): <tt>ret[i]</tt> == f.applyAsDouble(xs[i])</tt>.
     */
    public static <T> double[] mapToDouble(final ToDoubleFunction<? super T> f, final T[] xs) {
        final double[] ret = new double[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ret[i] = f.applyAsDouble(xs[i]);
        }
        return ret;
    }

    @SuppressWarnings("null")
    public static Stream<Float> stream(final @Pure float[] xs) {
        return Arrays.stream(MoreArrays.boxed(xs));
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static char[] clone(final char[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static byte[] clone(final byte[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static short[] clone(final short[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static int[] clone(final int[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static long[] clone(final long[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static float[] clone(final float[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static double[] clone(final double[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }

    /**
     * Make and return a copy of the given array. For all positive <tt>i</tt> less than the length of <tt>xs</tt>, <tt>xs[i] == ys[i]</tt>,
     * where <tt>ys = clone(xs)</tt>.
     * @param xs the source array
     * @return a copy of the source array
     */
    @Pure
    @SuppressWarnings("null")
    public static <T> T[] clone(final T[] xs) {
        return Arrays.copyOf(xs, xs.length);
    }
}