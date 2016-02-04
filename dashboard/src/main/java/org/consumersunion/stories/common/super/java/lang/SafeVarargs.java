package java.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A Java language annotation used by code that intends to signal the
 * SafeVarargs feature in later virtual machines, while remaining
 * compatible with prior versions. This should be used on the
 * boot-strap classpath at compile-time, and NOT included as a
 * transitive dependency at runtime, as it will be provided by the
 * target environment, if the feature is available. If the feature is
 * not available, the annotation (and its lack of presence on the
 * classpath) will be ignored, per the Java language spec.
 */
@Documented
@Retention(RUNTIME)
@Target({CONSTRUCTOR, METHOD})
public @interface SafeVarargs {
}
