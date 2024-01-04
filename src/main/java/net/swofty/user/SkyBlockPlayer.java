package net.swofty.user;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.SetSlotPacket;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.*;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.actions.player.fall.ActionPlayerFall;
import net.swofty.event.custom.IslandPlayerLoadedEvent;
import net.swofty.event.value.SkyBlockValueEvent;
import net.swofty.event.value.ValueUpdateEvent;
import net.swofty.event.value.events.MiningValueUpdateEvent;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.mission.MissionData;
import net.swofty.region.mining.MineableBlock;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.PlayerStatistics;
import net.swofty.user.statistics.StatisticDisplayReplacement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SkyBlockPlayer extends Player {

    @Getter
    private float mana = 100;
    public float health = 100;
    public long joined;
    @Setter
    @Getter
    public boolean bypassBuild = false;

    @Getter
    private StatisticDisplayReplacement manaDisplayReplacement = null;
    @Getter
    private StatisticDisplayReplacement defenseDisplayReplacement = null;
    @Getter
    private PlayerAbilityHandler abilityHandler = new PlayerAbilityHandler();
    @Getter
    private SkyBlockIsland skyBlockIsland;

    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        if (SkyBlock.offlineUUIDs.contains(uuid)) {
            this.setUsernameField("cracked" + username);
            SkyBlock.offlineUUIDs.remove(uuid);
        }

        joined = System.currentTimeMillis();
    }

    public void setSkyBlockIsland(SkyBlockIsland island) {
        this.skyBlockIsland = island;
    }

    public DataHandler getDataHandler() {
        return DataHandler.getUser(this.uuid);
    }

    public PlayerStatistics getStatistics() {
        return new PlayerStatistics(this);
    }

    public AntiCheatHandler getAntiCheatHandler() {
        return new AntiCheatHandler(this);
    }

    public LogHandler getLogHandler() {
        return new LogHandler(this);
    }

    public UserProfiles getProfiles() {
        return UserProfiles.get(this.getUuid());
    }

    public MissionData getMissionData() {
        MissionData data = getDataHandler().get(DataHandler.Data.MISSION_DATA, DatapointMissionData.class).getValue();
        data.setSkyBlockPlayer(this);
        return data;
    }

    public String getFullDisplayName() {
        return getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() + this.getUsername();
    }

    public PlayerShopData getShoppingData() {
        return getDataHandler().get(DataHandler.Data.SHOPPING_DATA, DatapointShopData.class).getValue();
    }

    public boolean isOnIsland() {
        return getInstance() != null && getInstance() != SkyBlock.getInstanceContainer();
    }

    public boolean isCoop() {
        return getDataHandler().get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue();
    }

    public void setDisplayReplacement(StatisticDisplayReplacement replacement, StatisticDisplayReplacement.DisplayType type) {
        // Determine which replacement to update based on type
        StatisticDisplayReplacement currentReplacement =
                (type == StatisticDisplayReplacement.DisplayType.MANA) ? this.manaDisplayReplacement :
                        this.defenseDisplayReplacement;

        // Check if the replacement needs to be updated
        if (currentReplacement == null || currentReplacement.getTicksToLast() > replacement.getTicksToLast()) {
            // Update the appropriate replacement based on type
            if (type == StatisticDisplayReplacement.DisplayType.MANA) {
                this.manaDisplayReplacement = replacement;
            } else if (type == StatisticDisplayReplacement.DisplayType.DEFENSE) {
                this.defenseDisplayReplacement = replacement;
            }

            int hashCode = replacement.hashCode();

            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                StatisticDisplayReplacement scheduledReplacement =
                        (type == StatisticDisplayReplacement.DisplayType.MANA) ? this.manaDisplayReplacement :
                                this.defenseDisplayReplacement;
                if (hashCode == scheduledReplacement.hashCode()) {
                    if (type == StatisticDisplayReplacement.DisplayType.MANA) {
                        this.manaDisplayReplacement = null;
                    } else if (type == StatisticDisplayReplacement.DisplayType.DEFENSE) {
                        this.defenseDisplayReplacement = null;
                    }
                }
            }, TaskSchedule.tick(replacement.getTicksToLast()), TaskSchedule.stop());
        }
    }

    public SkyBlockRegion getRegion() {
        if (isOnIsland()) {
            return SkyBlockRegion.getIslandRegion();
        }

        return SkyBlockRegion.getRegionOfPosition(this.getPosition());
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    public void addAndUpdateItem(SkyBlockItem item) {
        ItemStack toAdd = PlayerItemUpdater.playerUpdate(this, PlayerItemOrigin.INVENTORY_SLOT, item.getItemStack()).build();
        this.getInventory().addItemStack(toAdd);
    }

    public float getMaxMana() {
        return (float) (100 + getStatistics().allStatistics().get(ItemStatistic.INTELLIGENCE));
    }

    public int getMiningSpeed() {
        return this.getStatistics().allStatistics().get(ItemStatistic.MINING_SPEED);
    }

    public void sendToHub() {
        // Allows fall damage system to recalculate using new teleported Y value
        ActionPlayerFall.fallHeight.remove(this);

        if (getInstance() == SkyBlock.getInstanceContainer()) {
            this.teleport(new Pos(-2.5, 70, -69.5, 180, 0));
            return;
        }
        
        this.setInstance(SkyBlock.getInstanceContainer(), new Pos(-2.5, 70, -69.5, 180, 0));
    }

    public CompletableFuture<Boolean> sendToIsland() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        skyBlockIsland.getSharedInstance().thenAccept(sharedInstance -> {
            // Allows fall damage system to recalculate using new teleported Y value
            ActionPlayerFall.fallHeight.remove(this);

            if (sharedInstance != getInstance())
                this.setInstance(sharedInstance, new Pos(0.5, 100, 0.5, 0, 0));
            this.teleport(new Pos(0.5, 100, 0.5, 0, 0));

            SkyBlockEvent.callSkyBlockEvent(new IslandPlayerLoadedEvent(
                    skyBlockIsland,
                    this,
                    skyBlockIsland.getCoop() != null,
                    skyBlockIsland.getCoop() != null ? skyBlockIsland.getCoop().getOnlineMembers() : List.of(this),
                    skyBlockIsland.getCoop() != null ? skyBlockIsland.getCoop().memberProfiles() : List.of(this.getUuid())
            ));

            future.complete(true);
        });

        return future;
    }

    public double getTimeToMine(SkyBlockItem item, Block b) {
        MineableBlock block = MineableBlock.get(b);
        if (block == null) return -1;
        if (!item.getAttributeHandler().isMiningTool()) return -1;
        if (getRegion() == null) return -1;

        if (block.getMiningPowerRequirement() > item.getAttributeHandler().getBreakingPower()) return -1;
        if (block.getStrength() > 0) {
            double time = (block.getStrength() * 30) / (Math.max(getMiningSpeed(), 1));
            ValueUpdateEvent event = new MiningValueUpdateEvent(
                    this,
                    time,
                    item);

            SkyBlockValueEvent.callValueUpdateEvent(event);
            time = (double) event.getValue();

            double softcap = ((double) 20 / 3) * block.getStrength();
            if (time < 1)
                return 1;

            return Math.min(time, softcap);
        }

        return 0;
    }

    public float getDefense() {
        float defence = 0;

        PlayerStatistics statistics = this.getStatistics();
        defence += statistics.allStatistics().get(ItemStatistic.DEFENSE);

        return defence;
    }

    public void setHearts(float hearts) {
        this.health = hearts;
        this.sendPacket(new UpdateHealthPacket((hearts / getMaxHealth()) * 20, 20, 20));
    }

    public void playSuccessSound() {
        playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        return super.getInventory();
    }

    @Override
    public float getMaxHealth() {
        PlayerStatistics statistics = this.getStatistics();
        return statistics.allStatistics().get(ItemStatistic.HEALTH);
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float health) {
        if ((System.currentTimeMillis() - joined) < 3000)
            return;
        this.health = health;
        this.sendPacket(new UpdateHealthPacket((health / getMaxHealth()) * 20, 20, 20));
    }

    @Override
    public void sendMessage(@NotNull String message) {
        super.sendMessage(message.replace("&", "§"));
    }

    @Override
    public void closeInventory() {
        Inventory tempInv = this.getOpenInventory();
        super.closeInventory();
        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(this.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(this.getUuid());

            if (gui == null) return;

            gui.onClose(new InventoryCloseEvent(tempInv, this), SkyBlockInventoryGUI.CloseReason.SERVER_EXITED);
            SkyBlockInventoryGUI.GUI_MAP.remove(this.getUuid());
        }
    }

    public static String getDisplayName(UUID uuid) {
        if (SkyBlock.getLoadedPlayers().stream().anyMatch(player -> player.getUuid().equals(uuid))) {
            return SkyBlock.getLoadedPlayers().stream().filter(player -> player.getUuid().equals(uuid)).findFirst().get().getFullDisplayName();
        } else {
            UserProfiles profiles = new UserDatabase(uuid).getProfiles();
            UUID selected = profiles.getProfiles().get(0);

            if (selected == null) {
                return "§7Unknown";
            } else {
                DataHandler handler = DataHandler.fromDocument(new ProfilesDatabase(selected.toString()).getDocument());
                return handler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                        handler.get(DataHandler.Data.IGN_LOWER, DatapointString.class).getValue();
            }
        }
    }
}
