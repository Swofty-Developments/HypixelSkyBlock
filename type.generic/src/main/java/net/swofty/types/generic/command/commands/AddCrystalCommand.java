package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.CrystalDatabase;
import net.swofty.types.generic.entity.ServerCrystalImpl;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.ServerOrbComponent;
import net.swofty.types.generic.item.components.SkullHeadComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "spawncrystal",
        description = "Spawns in a crystal at the player's location.",
        usage = "/addcrystal",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AddCrystalCommand extends SkyBlockCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum<>("itemType", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ItemType type = context.get(itemType);
            SkyBlockItem item = new SkyBlockItem(type);

            // Spawn the orb
            ServerOrbComponent asOrb = item.getComponent(ServerOrbComponent.class);
            ServerCrystalImpl crystal = new ServerCrystalImpl(
                    asOrb.getSpawnMaterialFunction(),
                    item.getComponent(SkullHeadComponent.class).getSkullTexture(item),
                    asOrb.getValidBlocks()
            );
            crystal.setInstance(((SkyBlockPlayer) sender).getInstance(), ((SkyBlockPlayer) sender).getPosition());

            new CrystalDatabase().addCrystal(item.getComponent(SkullHeadComponent.class).getSkullTexture(item),
                    ((SkyBlockPlayer) sender).getPosition(),
                    type);
        }, itemType);
    }
}
