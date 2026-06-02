package anotacao;

import java.lang.annotation.*;

/**
 * @author anderson.salviano
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TipoChave {

    String value();
}
