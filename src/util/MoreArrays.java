package util;

import edu.umd.cs.findbugs.annotations.Nullable;

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

import util.function.ByteSupplier;
import util.function.CharSupplier;
import util.function.FloatSupplier;
import util.function.ShortSupplier;
import util.function.ToByteFunction;
import util.function.ToCharFunction;
import util.function.ToFloatFunction;
import util.function.ToShortFunction;

/**
 * Array utilities not present in {@link java.util.Arrays}
 */
public final class MoreArrays {
    private MoreArrays() {
        throw new UnsupportedOperationException("Can't instantiate MoreArrays"); //$NON-NLS-1$
    }

    /**
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static <T> int indexOf(final T[] xs, @Nullable final T x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to booleans.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final boolean[] xs, final boolean x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to characters.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final char[] xs, final char x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to bytes.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final byte[] xs, final byte x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to shorts.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final short[] xs, final short x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to ints.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final int[] xs, final int x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to longs.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final long[] xs, final long x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to floats.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final float[] xs, final float x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Specialization of {@link #indexOf(Object[], Object)} to doubles.
     * @param xs the array of values to search
     * @param x the value to search for
     * @return the least value i such that xs[i] == x if such an i exists, -1 otherwise
     */
    public static int indexOf(final double[] xs, final double x) {
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] == x) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set every element of array to {@link Supplier#get() supplier.get()}. Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static <T> void fill(final T[] array, final Supplier<? extends T> supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = Maybe.just(supplier.get()).fromJust();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to booleans. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final boolean[] array, final BooleanSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsBoolean();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to characters. Set every element of array to
     * {@link Supplier#get() supplier.get()}. Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final char[] array, final CharSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to bytes. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final byte[] array, final ByteSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to shorts. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final short[] array, final ShortSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to ints. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final int[] array, final IntSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsInt();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to longs. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final long[] array, final LongSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsLong();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to floats. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final float[] array, final FloatSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
    }

    /**
     * Specialization of {@link #fill(Object[], Supplier)} to doubles. Set every element of array to {@link Supplier#get() supplier.get()}.
     * Order of setting elements of array is unspecified.
     * @param array the array to fill
     * @param supplier the supplier of elements for the array
     */
    public static void fill(final double[] array, final DoubleSupplier supplier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.getAsDouble();
        }
    }

    /**
     * Build and return an array of length <tt>len</tt> generated by <tt>arraySupplier</tt> with each element generated by
     * {@link Supplier#get() valueSupplier.get()}. Order of element generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @param arraySupplier the source of the frame of the returned array
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static <A> A[] replicate(final int len, final Supplier<? extends A> valueSupplier, final IntFunction<A[]> arraySupplier) {
        return Stream.generate(valueSupplier).limit(len).toArray(arraySupplier);
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to booleans. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static boolean[] replicateb(final int len, final BooleanSupplier valueSupplier) {
        final boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.getAsBoolean();
        }
        return ret;
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to characters. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static char[] replicatec(final int len, final CharSupplier valueSupplier) {
        final char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to bytes. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static byte[] replicateb(final int len, final ByteSupplier valueSupplier) {
        final byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to shorts. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static short[] replicates(final int len, final ShortSupplier valueSupplier) {
        final short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to ints. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static int[] replicatei(final int len, final IntSupplier valueSupplier) {
        return Maybe.maybe(IntStream.generate(valueSupplier).limit(len).toArray()).fromJust();
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to longs. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static long[] replicatel(final int len, final LongSupplier valueSupplier) {
        return Maybe.maybe(LongStream.generate(valueSupplier).limit(len).toArray()).fromJust();
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to floats. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static float[] replicatef(final int len, final FloatSupplier valueSupplier) {
        final float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            ret[i] = valueSupplier.get();
        }
        return ret;
    }

    /**
     * Specialization of {@link #replicate(int, Supplier, IntFunction)} to doubles. Build and return an array of length <tt>len</tt>
     * generated by <tt>arraySupplier</tt> with each element generated by {@link Supplier#get() valueSupplier.get()}. Order of element
     * generation is unspecified.
     * @param len the length of the returned array
     * @param valueSupplier the source of values
     * @return an array of the specified length such that each element was generated by valueSupplier
     */
    public static double[] replicated(final int len, final DoubleSupplier valueSupplier) {
        return Maybe.maybe(DoubleStream.generate(valueSupplier).limit(len).toArray()).fromJust();
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Boolean[] boxed(final boolean[] xs) {
        final Boolean[] ret = new Boolean[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static boolean[] unboxed(final Boolean[] xs) {
        final boolean[] ret = new boolean[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Character[] boxed(final char[] xs) {
        final Character[] ret = new Character[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static char[] unboxed(final Character[] xs) {
        final char[] ret = new char[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Byte[] boxed(final byte[] xs) {
        final Byte[] ret = new Byte[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static byte[] unboxed(final Byte[] xs) {
        final byte[] ret = new byte[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Short[] boxed(final short[] xs) {
        final Short[] ret = new Short[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static short[] unboxed(final Short[] xs) {
        final short[] ret = new short[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Integer[] boxed(final int[] xs) {
        final Integer[] ret = new Integer[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static int[] unboxed(final Integer[] xs) {
        final int[] ret = new int[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Long[] boxed(final long[] xs) {
        final Long[] ret = new Long[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static long[] unboxed(final Long[] xs) {
        final long[] ret = new long[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Float[] boxed(final float[] xs) {
        final Float[] ret = new Float[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
    public static float[] unboxed(final Float[] xs) {
        final float[] ret = new float[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Box each element of the specified array.
     * @param xs the array to box
     * @return an array of boxed values
     */
    public static Double[] boxed(final double[] xs) {
        final Double[] ret = new Double[xs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = xs[i];
        }
        return ret;
    }

    /**
     * Unbox each element of the specified array.
     * @param xs the array to unbox
     * @return an array of unboxed values
     */
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
    public static byte randEl(final  byte[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static short randEl(final  short[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static int randEl(final  int[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static long randEl(final  long[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static float randEl(final  float[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param var1 the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static double randEl(final  double[] var1, final Random rng) {
        return var1[rng.nextInt(var1.length)];
    }

    /**
     * @param arr the array to get a random element from
     * @param rng the random number generator to use
     * @return a randomly selected element of the given array
     */
    public static <E> E randEl(final E[] arr, final Random rng) {
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
            ret[i] = Maybe.just(f.apply(xs[i])).fromJust();
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

    /**
     * Stream a float array.
     * @param xs the array to stream
     * @return a stream based on the specified array
     */
    public static Stream<Float> stream(final float[] xs) {
        return Arrays.stream(MoreArrays.boxed(xs));
    }
}