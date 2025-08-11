package net.swofty.type.generic.command;

import net.swofty.type.generic.user.categories.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    String description() default "";

    String aliases() default "";

    String usage();

    Rank permission();

    boolean allowsConsole();
}
