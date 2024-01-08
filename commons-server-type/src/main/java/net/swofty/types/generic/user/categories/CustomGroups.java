package net.swofty.types.generic.user.categories;

import net.kyori.adventure.key.Key;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.adventure.audience.PacketGroupingAudience;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class CustomGroups {

    public static Collection<Player> staffMembers = new ArrayList<>();

    public static void registerAudiences() {
        Audiences.registry().register(Key.key("skyblock:staff"), PacketGroupingAudience.of(staffMembers));
    }
}
