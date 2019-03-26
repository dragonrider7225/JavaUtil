package util;

/**
 * Utilities not present in {@link String}
 */
public final class Strings {
    private Strings() {
        throw new UnsupportedOperationException("Can't instantiate Strings"); //$NON-NLS-1$
    }

    /**
     * @param len the maximum length of the returned CharSequence
     * @param cs the CharSequence to take at most <tt>len</tt> chars from.
     * @return the longest prefix of the given CharSequence with length less than or equal to <tt>len</tt>
     */
    public static CharSequence take(final int len, final CharSequence cs) {
        if (cs.length() <= len) {
            return cs;
        }
        return cs.subSequence(0, len);
    }

    /**
     * @param len the maximum length of the returned string
     * @param str the string to take at most <tt>len</tt> chars from
     * @return the longest prefix of the given string with length at most <tt>len</tt>
     */
    public static String take(final int len, final String str) {
        return (String) Strings.take(len, (CharSequence) str);
    }
}
