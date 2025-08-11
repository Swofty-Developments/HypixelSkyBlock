package net.swofty.type.generic.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HypixelEvent {
    EventNodes node();
    boolean requireDataLoaded();
    boolean isAsync() default false;
}