package net.swofty.types.generic.command.commands;

import net.minestom.server.utils.mojang.MojangUtils;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandParameters(aliases = "forceadmin",
        description = "Literally just gives me admin",
        usage = "/adminme",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class AdminMeCommand extends SkyBlockCommand {

    private static final List<String> ADMIN_LIST = List.of(
            "Swofty",
            "Foodzz",
            "Hamza_dev",
            "ItzKatze",
            "NullPointer_Ex"
    );

    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("§8Running checks...");
            Thread.startVirtualThread(() -> {
                SkyBlockPlayer player = (SkyBlockPlayer) sender;
                UUID realUUID = player.getUuid();
                UUID crackedUUID = UUID.nameUUIDFromBytes((STR."OfflinePlayer:\{player.getName()}").getBytes(StandardCharsets.UTF_8));

                List<UUID> adminUUIDs = new ArrayList<>();
                ADMIN_LIST.forEach(admin -> adminUUIDs.add(UUID.nameUUIDFromBytes((STR."OfflinePlayer:\{admin}").getBytes(StandardCharsets.UTF_8))));
                ADMIN_LIST.forEach(admin -> {
                    try {
                        adminUUIDs.add(MojangUtils.getUUID(admin));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (!adminUUIDs.contains(realUUID) && !adminUUIDs.contains(crackedUUID)) {
                    sender.sendMessage("§cYou are not allowed to use this command.");
                    return;
                }

                player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).setValue(Rank.ADMIN);

                sender.sendMessage("§aSuccessfully set rank to §c[ADMIN]§a.");
            });
        });
    }
}
