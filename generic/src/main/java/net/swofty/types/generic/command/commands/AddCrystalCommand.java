package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.CrystalDatabase;
import net.swofty.types.generic.entity.ServerCrystalImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ServerOrb;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "spawncrystal",
        description = "Spawns in a crystal at the player's location.",
        usage = "/addcrystal",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AddCrystalCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum<>("itemType", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ItemType type = context.get(itemType);
            try {
                CustomSkyBlockItem item = type.clazz.newInstance();

                // Spawn the orb
                ServerOrb asOrb = (ServerOrb) item;
                ServerCrystalImpl crystal = new ServerCrystalImpl(
                        asOrb.getOrbSpawnMaterial(),
                        ((SkullHead) item).getSkullTexture(null, new SkyBlockItem(type)),
                        asOrb.getBlocksToPlaceOn()
                );
                crystal.setInstance(((SkyBlockPlayer) sender).getInstance(), ((SkyBlockPlayer) sender).getPosition());

                new CrystalDatabase().addCrystal(((SkullHead) item).getSkullTexture(null, new SkyBlockItem(type)),
                        ((SkyBlockPlayer) sender).getPosition(),
                        type);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, itemType);
    }
}
