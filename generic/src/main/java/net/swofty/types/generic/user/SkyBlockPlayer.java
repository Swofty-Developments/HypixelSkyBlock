package net.swofty.types.generic.user;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.TranslatableComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.region.mining.MineableBlock;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.*;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.MiningValueUpdateEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.DeathMessageCreator;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private StatisticDisplayReplacement coinsDisplayReplacement = null;
    @Getter
    private PlayerAbilityHandler abilityHandler = new PlayerAbilityHandler();
    @Getter
    private SkyBlockIsland skyBlockIsland;

    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

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

    public DatapointCollection.PlayerCollection getCollection() {
        return getDataHandler().get(DataHandler.Data.COLLECTION, DatapointCollection.class).getValue();
    }

    public boolean isOnIsland() {
        return getInstance() != null && getInstance() != SkyBlockConst.getInstanceContainer();
    }

    public boolean hasTalisman(Talisman talisman) {
        return getTalismans().stream().anyMatch(talisman1 -> talisman1.getClass() == talisman.getClass());
    }

    public List<Talisman> getTalismans() {
        return Stream.of(getAllPlayerItems())
                .filter(item -> item.getGenericInstance() instanceof Talisman)
                .map(item -> (Talisman) item.getGenericInstance()).collect(Collectors.toList());
    }

    public SkyBlockItem[] getAllPlayerItems() {
        return Stream.of(getInventory().getItemStacks())
                .map(SkyBlockItem::new)
                .filter(item -> item.getGenericInstance() instanceof CustomSkyBlockItem)
                .toArray(SkyBlockItem[]::new);
    }

    public boolean isCoop() {
        return getDataHandler().get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue();
    }

    public CoopDatabase.Coop getCoop() {
        return CoopDatabase.getFromMember(getUuid());
    }

    public ArmorSetRegistry getArmorSet() {
        ItemType helmet = new SkyBlockItem(getInventory().getHelmet()).getAttributeHandler().getItemTypeAsType();
        ItemType chestplate = new SkyBlockItem(getInventory().getChestplate()).getAttributeHandler().getItemTypeAsType();
        ItemType leggings = new SkyBlockItem(getInventory().getLeggings()).getAttributeHandler().getItemTypeAsType();
        ItemType boots = new SkyBlockItem(getInventory().getBoots()).getAttributeHandler().getItemTypeAsType();

        if (helmet == null || chestplate == null || leggings == null || boots == null) return null;

        return ArmorSetRegistry.getArmorSet(boots, leggings, chestplate, helmet);
    }

    public boolean isWearingItem(SkyBlockItem item) {
        SkyBlockItem[] armor = getArmor();

        for (SkyBlockItem armorItem : armor) {
            if (armorItem == null) continue;
            if (armorItem.getAttributeHandler().getItemTypeAsType() == item.getAttributeHandler().getItemTypeAsType()) {
                return true;
            }
        }

        return false;
    }

    public SkyBlockItem[] getArmor() {
        return new SkyBlockItem[]{
                new SkyBlockItem(getInventory().getHelmet()),
                new SkyBlockItem(getInventory().getChestplate()),
                new SkyBlockItem(getInventory().getLeggings()),
                new SkyBlockItem(getInventory().getBoots())
        };
    }

    public ProxyPlayer asProxyPlayer() {
        return new ProxyPlayer(this);
    }

    public void setDisplayReplacement(StatisticDisplayReplacement replacement, StatisticDisplayReplacement.DisplayType type) {
        switch (type) {
            case MANA:
                this.manaDisplayReplacement = replacement;
                break;
            case DEFENSE:
                this.defenseDisplayReplacement = replacement;
                break;
            case COINS:
                this.coinsDisplayReplacement = replacement;
                break;
        }

        int hashCode = replacement.hashCode();

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            StatisticDisplayReplacement scheduledReplacement = switch (type) {
                case MANA -> this.manaDisplayReplacement;
                case DEFENSE -> this.defenseDisplayReplacement;
                case COINS -> this.coinsDisplayReplacement;
            };
            if (hashCode == scheduledReplacement.hashCode()) {
                switch (type) {
                    case MANA -> this.manaDisplayReplacement = null;
                    case DEFENSE -> this.defenseDisplayReplacement = null;
                    case COINS -> this.coinsDisplayReplacement = null;
                }
            }
        }, TaskSchedule.tick(replacement.getTicksToLast()), TaskSchedule.stop());
    }

    public SkyBlockItem updateItem(PlayerItemOrigin origin, Consumer<SkyBlockItem> update) {
        ItemStack toUpdate = origin.getStack(this);
        if (toUpdate == null) return null;

        SkyBlockItem item = new SkyBlockItem(toUpdate);
        update.accept(item);

        origin.setStack(this, PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
        return item;
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
        if (item.isNA()) return;
        ItemStack toAdd = PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build();
        this.getInventory().addItemStack(toAdd);
    }

    public void addAndUpdateItem(ItemStack item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    public float getMaxMana() {
        return (float) (100 + getStatistics().allStatistics().get(ItemStatistic.INTELLIGENCE));
    }

    public int getMiningSpeed() {
        return this.getStatistics().allStatistics().get(ItemStatistic.MINING_SPEED);
    }

    public void sendTo(ServerType type) {
        sendTo(type, false);
    }

    public void sendTo(ServerType type, boolean force) {
        ProxyPlayer player = asProxyPlayer();

        if (type == SkyBlockConst.getTypeLoader().getType() && !force) {
            this.teleport(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition());
            return;
        }

        player.transferTo(type);
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

    public void playSuccessSound() {
        playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
    }

    @Override
    public void kill() {
        setHealth(getMaxHealth());
        sendTo(SkyBlockConst.getTypeLoader().getType());

        DeathMessageCreator creator = new DeathMessageCreator(this.lastDamage);

        sendMessage("§c☠ §7You " + creator.createPersonal());

        DatapointDouble coins = getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
        coins.setValue(coins.getValue() / 2);

        playSound(Sound.sound(Key.key("block.anvil.fall"), Sound.Source.PLAYER, 1.0f, 2.0f));

        sendMessage("§cYou died and lost " + StringUtility.commaify(coins.getValue()) + " coins!");

        if (!SkyBlockConst.getTypeLoader().getLoaderValues().announceDeathMessages()) return;

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
            if (player.getUuid().equals(getUuid())) return;
            if (player.getInstance() != getInstance()) return;

            player.sendMessage("§c☠ §7" + getFullDisplayName() + " §7" + creator.createOther());
        });
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
        if (health < 0) {
            kill();
            return;
        }
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
        if (SkyBlockGenericLoader.getLoadedPlayers().stream().anyMatch(player -> player.getUuid().equals(uuid))) {
            return SkyBlockGenericLoader.getLoadedPlayers().stream().filter(player -> player.getUuid().equals(uuid)).findFirst().get().getFullDisplayName();
        } else {
            UserProfiles profiles = new UserDatabase(uuid).getProfiles();
            if (profiles.getProfiles().isEmpty()) {
                Document document = ProfilesDatabase.collection.find(Filters.eq("_owner", uuid.toString())).first();
                if (document == null)
                    return "§7Unknown";
                DataHandler handler = DataHandler.fromDocument(document);
                return handler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                        handler.get(DataHandler.Data.IGN, DatapointString.class).getValue();
            }

            UUID selected = profiles.getProfiles().get(0);

            DataHandler handler = DataHandler.fromDocument(new ProfilesDatabase(selected.toString()).getDocument());
            return handler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                    handler.get(DataHandler.Data.IGN, DatapointString.class).getValue();
        }
    }
}
