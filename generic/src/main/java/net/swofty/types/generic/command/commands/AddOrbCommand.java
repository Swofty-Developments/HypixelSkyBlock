package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.OrbDatabase;
import net.swofty.types.generic.entity.ServerOrbImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ServerOrb;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "spawnorb",
        description = "Spawns in an orb at the player's location.",
        usage = "/addorb",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AddOrbCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum<>("itemType", ItemType.class);

        command.addSyntax((sender, context) -> {
            ItemType type = context.get(itemType);
            try {
                CustomSkyBlockItem item = type.clazz.newInstance();

                // Spawn the orb
                ServerOrbImpl orb = new ServerOrbImpl(((ServerOrb) item).getOrbSpawnMaterial(), ((SkullHead) item).getSkullTexture(null, new SkyBlockItem(type)));
                orb.setInstance(((SkyBlockPlayer) sender).getInstance(), ((SkyBlockPlayer) sender).getPosition());

                new OrbDatabase().addOrb(((SkullHead) item).getSkullTexture(null, new SkyBlockItem(type)),
                        ((SkyBlockPlayer) sender).getPosition(),
                        type);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, itemType);
    }
}
