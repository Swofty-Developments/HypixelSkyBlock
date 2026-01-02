package net.swofty.spark;

import me.lucko.spark.common.command.sender.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

public class MinestomCommandSender extends AbstractCommandSender<CommandSender> {
    public MinestomCommandSender(CommandSender delegate) {
        super(delegate);
    }

    @Override
    public String getName() {
        if (this.delegate instanceof Player player) {
            return player.getUsername();
        } else if (this.delegate instanceof ConsoleSender) {
            return "Console";
        } else {
            return "unknown:" + this.delegate.getClass().getSimpleName();
        }
    }

    @Override
    public UUID getUniqueId() {
        if (super.delegate instanceof Player player) {
            return player.getUuid();
        }
        return null;
    }

    @Override
    public void sendMessage(Component message) {
        this.delegate.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        if (delegate instanceof Player player) {
            return HypixelGenericLoader.getFromUUID(player.getUuid()).getDataHandler()
                    .get(HypixelDataHandler.Data.RANK, DatapointRank.class)
                    .getValue() == Rank.STAFF;
        }
        return false;

    }
}
