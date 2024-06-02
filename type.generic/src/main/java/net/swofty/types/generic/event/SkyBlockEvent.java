package net.swofty.types.generic.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SkyBlockEvent {
    EventNodes node();
    boolean requireDataLoaded();
    boolean isAsync() default false;
}