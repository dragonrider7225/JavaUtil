package util;

public final class MoreStrings {
    private MoreStrings() {
        // No instantiation
    }

    /**
     * @param len the maximum length of the returned CharSequence
     * @param cs the CharSequence to take at most <tt>len</tt> chars from.
     * @return the longest prefix of the given CharSequence with length less than or equal to <tt>len</tt>
     */
    @SuppressWarnings("null")
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
        return (String) MoreStrings.take(len, (CharSequence) str);
    }
}
