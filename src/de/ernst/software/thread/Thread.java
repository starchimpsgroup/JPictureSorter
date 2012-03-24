package de.ernst.software.thread;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 18.03.12
 * Time: 10:59
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Thread {
    String value();
}
