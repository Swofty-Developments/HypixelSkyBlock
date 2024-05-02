package net.swofty.spark;

import me.lucko.spark.common.command.sender.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

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
        }else {
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
        if (delegate instanceof Player player){
            return ((SkyBlockPlayer) player).getDataHandler()
                    .get(DataHandler.Data.RANK, DatapointRank.class)
                    .getValue() == Rank.ADMIN;
        }
        return false;

    }
}
