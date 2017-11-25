package engine.behavior;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ParameterName {

    String value();

}
