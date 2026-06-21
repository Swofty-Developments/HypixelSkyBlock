package net.swofty.type.generic.collectibles;

import net.swofty.type.generic.user.HypixelPlayer;

@FunctionalInterface
public interface CollectibleUnlockResolver {
    boolean isUnlocked(HypixelPlayer player, CollectibleDefinition definition);
}
