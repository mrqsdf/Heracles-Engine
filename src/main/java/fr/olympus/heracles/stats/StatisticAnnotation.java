package fr.olympus.heracles.stats;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes related to statistics mechanics.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StatisticAnnotation {

    /**
     * Gets the unique identifier for the statistic.
     * @return A string representing the unique identifier for the statistic.
     */
    String id();

}
