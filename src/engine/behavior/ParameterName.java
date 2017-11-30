package engine.behavior;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Annotation for retaining the names of parameters at runtime, which is needed to aid the reflective generation of
 * game elements.
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ParameterName {
    String value();
}
