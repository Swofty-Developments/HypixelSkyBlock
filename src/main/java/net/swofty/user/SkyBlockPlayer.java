package net.swofty.user;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.SkyBlock;
import net.swofty.data.DataHandler;
import net.swofty.item.impl.ItemStatistic;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SkyBlockPlayer extends Player {

    @Getter
    private float mana = 100;
    public float hearts = 100;
    public long joined = 0;

    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        if (SkyBlock.offlineUUIDs.contains(uuid)) {
            this.setUsernameField("cracked" + username);
            SkyBlock.offlineUUIDs.remove(uuid);
        }

        joined = System.currentTimeMillis();
    }

    public DataHandler getDataHandler() {
        return DataHandler.getUser(this.uuid);
    }

    public PlayerStatistics getStatistics() {
        return new PlayerStatistics(this);
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    @Override
    public float getMaxHealth() {
        float maxHealth = 100;

        PlayerStatistics statistics = this.getStatistics();
        maxHealth += statistics.allArmorStatistics().get(ItemStatistic.HEALTH);
        maxHealth += statistics.mainHandStatistics().get(ItemStatistic.HEALTH);

        return maxHealth;
    }

    public float getMaxMana() {
        float maxMana = 100;

        PlayerStatistics statistics = this.getStatistics();
        maxMana += statistics.allArmorStatistics().get(ItemStatistic.INTELLIGENCE);
        maxMana += statistics.mainHandStatistics().get(ItemStatistic.INTELLIGENCE);

        return maxMana;
    }

    @Override
    public float getHealth() {
        return this.hearts;
    }

    public float getDefence() {
        float defence = 0;

        PlayerStatistics statistics = this.getStatistics();
        defence += statistics.allArmorStatistics().get(ItemStatistic.DEFENSE);
        defence += statistics.mainHandStatistics().get(ItemStatistic.DEFENSE);

        return defence;
    }

    public void setHearts(float hearts) {
        this.hearts = hearts;
        this.sendPacket(new UpdateHealthPacket(hearts, 20, 20));
    }

    @Override
    public void setHealth(float health) {
        if ((System.currentTimeMillis() - joined) < 3000)
            return;
        this.hearts = health;
        this.sendPacket(new UpdateHealthPacket(health, 20, 20));
    }
}
