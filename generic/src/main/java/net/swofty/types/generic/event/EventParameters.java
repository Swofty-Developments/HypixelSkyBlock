package net.swofty.types.generic.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventParameters {
    String description() default "";

    EventNodes node();

    boolean requireDataLoaded();

    enum Location {
        ISLAND,
        HUB,
        EITHER
    }
}
