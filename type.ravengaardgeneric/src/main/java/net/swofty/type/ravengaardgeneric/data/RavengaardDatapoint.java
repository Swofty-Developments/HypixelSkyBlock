package net.swofty.type.ravengaardgeneric.data;

import net.minestom.server.entity.Player;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

public abstract class RavengaardDatapoint<T> extends Datapoint<T> {

    protected RavengaardDatapoint(String key, T value, Serializer<T> serializer) {
        super(key, value, serializer);
    }

    @Override
    protected boolean hasOnChange() {
        if (data instanceof HypixelDataHandler.Data hypixelData) {
            return hypixelData.onChange != null;
        }
        if (data instanceof RavengaardDataHandler.Data ravengaardData) {
            return ravengaardData.onChange != null;
        }
        return false;
    }

    @Override
    protected void triggerOnChange(Player player, Datapoint<?> datapoint) {
        if (data instanceof HypixelDataHandler.Data hypixelData && hypixelData.onChange != null) {
            hypixelData.onChange.accept(player, datapoint);
        } else if (data instanceof RavengaardDataHandler.Data ravengaardData && ravengaardData.onChange != null) {
            ravengaardData.onChange.accept((HypixelPlayer) player, datapoint);
        }
    }
}
