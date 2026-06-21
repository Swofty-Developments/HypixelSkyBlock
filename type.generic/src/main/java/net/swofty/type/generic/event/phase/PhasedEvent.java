package net.swofty.type.generic.event.phase;

import net.swofty.type.generic.event.EventNodes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PhasedEvent {
    EventNodes node();
    EventPhase phase() default EventPhase.GAMEPLAY;
    int order() default 0;
    boolean requireDataLoaded() default false;
    boolean isAsync() default false;
}
