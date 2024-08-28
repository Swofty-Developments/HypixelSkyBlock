package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.user.SkyBlockPlayer;

public interface Requirement {
    boolean isMet(SkyBlockPlayer player, Item item);
    String getErrorMessage(); // Return the error message if the requirement is not met.
}
