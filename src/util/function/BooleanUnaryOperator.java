package util.function;

import java.util.function.UnaryOperator;

/**
 * A primitive specification of {@link UnaryOperator} to boolean. All valid pure boolean unary operators are defined as constants in this
 * class.
 */
@FunctionalInterface
public interface BooleanUnaryOperator extends UnaryOperator<Boolean> {
    /**
     * The boolean unary operator that ignores its argument and returns true.
     */
    BooleanUnaryOperator ALWAYS_TRUE = none -> true;
    /**
     * The boolean unary operator that ignores its argument and returns false.
     */
    BooleanUnaryOperator ALWAYS_FALSE = none -> false;
    /**
     * The boolean unary operator that returns its argument.
     */
    BooleanUnaryOperator ID = b -> b;
    /**
     * The boolean unary operator that inverts its argument.
     */
    BooleanUnaryOperator NOT = b -> !b;

    @Override
    default Boolean apply(final Boolean value) {
        if (value == null) {
            throw new IllegalArgumentException("Expected Boolean, found null"); //$NON-NLS-1$
        }
        return this.applyAsPrimitive(value);
    }

    /**
     * @param value the argument to this unary operator
     * @return the result of applying this unary operator to the argument
     */
    boolean applyAsPrimitive(boolean value);
}
