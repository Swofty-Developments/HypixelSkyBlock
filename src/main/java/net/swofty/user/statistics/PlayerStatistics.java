package net.swofty.user.statistics;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;

import java.util.ArrayList;

public class PlayerStatistics {

    private final SkyBlockPlayer player;
    @Setter
    @Getter
    private double healthRegenerationPercentBonus;
    @Setter
    @Getter
    private double manaRegenerationPercentBonus;

    public PlayerStatistics(SkyBlockPlayer player) {
        this.player = player;
    }

    public ItemStatistics allArmorStatistics() {
        ArrayList<SkyBlockItem> armorPieces = new ArrayList<>();

        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        if (SkyBlockItem.isSkyBlockItem(helmet)) armorPieces.add(new SkyBlockItem(helmet));
        if (SkyBlockItem.isSkyBlockItem(chestplate)) armorPieces.add(new SkyBlockItem(chestplate));
        if (SkyBlockItem.isSkyBlockItem(leggings)) armorPieces.add(new SkyBlockItem(leggings));
        if (SkyBlockItem.isSkyBlockItem(boots)) armorPieces.add(new SkyBlockItem(boots));

        ItemStatistics total = ItemStatistics.builder().build();
        for (SkyBlockItem item : armorPieces) {
            total = total.add(item.getAttributeHandler().getStatistics());
            total = getReforgeStatistics(item, total);
        }
        return total;
    }

    public ItemStatistics mainHandStatistics() {
        ItemStack mainhand = player.getInventory().getItemInMainHand();
        if (!SkyBlockItem.isSkyBlockItem(mainhand)) return ItemStatistics.builder().build();

        SkyBlockItem item = new SkyBlockItem(mainhand);
        ItemStatistics statistics = item.getAttributeHandler().getStatistics();
        statistics = getReforgeStatistics(item, statistics);

        return statistics;
    }

    private ItemStatistics getReforgeStatistics(SkyBlockItem item, ItemStatistics statistics) {
        if (item.getAttributeHandler().getReforge() != null) {
            ItemStatistics.ItemStatisticsBuilder builder = ItemStatistics.builder();
            for (ItemStatistic statistic : item.getAttributeHandler().getReforge().getStatistics()) {
                builder.with(statistic, item.getAttributeHandler().getReforge().getBonusCalculation(
                        statistic,
                        item.getAttributeHandler().getRarity().ordinal()));
            }
            statistics = statistics.add(builder.build());
        }
        return statistics;
    }

    public void boostManaRegeneration(double percent, int ticks) {
        manaRegenerationPercentBonus += percent;

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            manaRegenerationPercentBonus -= percent;
        }, TaskSchedule.tick(ticks), TaskSchedule.stop());
    }

    public void boostHealthRegeneration(double percent, int ticks) {
        healthRegenerationPercentBonus += percent;

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            healthRegenerationPercentBonus -= percent;
        }, TaskSchedule.tick(ticks), TaskSchedule.stop());
    }

    public static void run() {
        barLoop();
        healthLoop();
        manaLoop();
    }

    public static void barLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlock.getLoadedPlayers().forEach(player -> {
                float absorption = player.getAdditionalHearts();
                String heartsColour = absorption > 0.0f ? "§6" : "§c";

                String healthText = heartsColour + Math.round(player.getHealth() + absorption) + "/" +
                        Math.round(player.getMaxHealth()) + "❤";
                String defenseText = player.getDefense() == 0 ? "" : "§a" + Math.round(player.getDefense()) + "❈ Defense";
                String manaText = "§b" + Math.round(player.getMana()) + "/" + Math.round(player.getMaxMana()) + "✎ Mana";

                if (player.getManaDisplayReplacement() != null) {
                    manaText = player.getManaDisplayReplacement().getDisplay();
                }

                if (player.getDefenseDisplayReplacement() != null) {
                    defenseText = player.getDefenseDisplayReplacement().getDisplay();
                }

                player.sendActionBar(Component.text(
                        healthText + "     " + defenseText + "     " + manaText
                ));
            });
            return TaskSchedule.tick(4);
        });
    }

    public static void healthLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlock.getLoadedPlayers().forEach(player -> {
                if (player.getHealth() > player.getMaxHealth())
                    player.setHealth(player.getMaxHealth());

                if (player.getHealth() <= player.getMaxHealth()) {
                    player.setHealth((float) Math.min(player.getMaxHealth(), player.getHealth() + 1.5 + ((int) player.getMaxHealth() * 0.01) +
                            ((1.5 + ((int) player.getMaxHealth() * 0.01)) * player.getStatistics().getHealthRegenerationPercentBonus())));
                }
            });
            return TaskSchedule.tick(30);
        });
    }

    public static void manaLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlock.getLoadedPlayers().forEach(player -> {
                if (player.getMana() > player.getMaxMana())
                    player.setMana(player.getMaxMana());
                if (player.getMana() <= player.getMaxMana()) {
                    float manaPool = player.getMaxMana();
                    player.setMana(Math.min(manaPool, Math.min(manaPool, player.getMana() + (manaPool / 50) +
                            (int) ((manaPool / 50) * player.getStatistics().getManaRegenerationPercentBonus()))));
                }
            });
            return TaskSchedule.seconds(1);
        });
    }
}


