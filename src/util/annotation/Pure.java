package util.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * When applied to a function, marks the annotated function as pure, meaning that it does not modify the state of the universe outside of the
 * function invocation. Given a function <tt>f</tt> annotated with <tt>\@Pure</tt>, the blocks {}, {f();}, and {f(); f(); f(); f(); ...}
 * should appear identical by any metric that does not take execution time or processor load into account. As a result, a pure function must
 * produce the same value for any particular set of arguments.
 *
 * When applied to a parameter, marks the applied parameter as unmodified by the function.
 */
@Documented
@Retention(CLASS)
@Target({METHOD, CONSTRUCTOR, PARAMETER})
public @interface Pure {
    // No members
}
