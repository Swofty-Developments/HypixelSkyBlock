package net.swofty.commons.skyblock.command;

import net.swofty.commons.skyblock.user.categories.Rank;

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
