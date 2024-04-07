package net.swofty.types.generic.user;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.MinecraftVersion;
import net.swofty.commons.ServerType;
import net.swofty.packer.SkyBlockTexture;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.event.actions.player.ActionPlayerChangeSkyBlockMenuDisplay;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.impl.ArrowImpl;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.noteblock.SkyBlockSongsHandler;
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
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class SkyBlockPlayer extends Player {
    @Setter
    private float mana = 100;
    public float health = 100;
    public long joined;
    @Setter
    public boolean hasAuthenticated = true;
    @Setter
    public boolean bypassBuild = false;
    @Setter
    public boolean isBankDelayed = false;

    private StatisticDisplayReplacement manaDisplayReplacement = null;
    private StatisticDisplayReplacement defenseDisplayReplacement = null;
    private StatisticDisplayReplacement coinsDisplayReplacement = null;

    private final PlayerAbilityHandler abilityHandler = new PlayerAbilityHandler();

    @Setter
    private SkyBlockIsland skyBlockIsland;

    @Setter
    private MinecraftVersion version = MinecraftVersion.MINECRAFT_1_20_3;

    @Getter
    private PlayerHookManager hookManager;

    @Getter
    private final PlayerStatistics statistics = new PlayerStatistics(this);

    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        joined = System.currentTimeMillis();
        hookManager = new PlayerHookManager(this, new HashMap<>());
    }

    public DataHandler getDataHandler() {
        return DataHandler.getUser(this.uuid);
    }

    public DatapointToggles.Toggles getToggles() {
        return getDataHandler().get(DataHandler.Data.TOGGLES, DatapointToggles.class).getValue();
    }

    public SkyBlockSongsHandler getSongHandler() {
        return new SkyBlockSongsHandler(this);
    }

    public AntiCheatHandler getAntiCheatHandler() {
        return new AntiCheatHandler(this);
    }

    public FairySoulHandler getFairySoulHandler() { return new FairySoulHandler(this); }

    public LogHandler getLogHandler() {
        return new LogHandler(this);
    }

    public PlayerProfiles getProfiles() {
        return PlayerProfiles.get(this.getUuid());
    }

    public DatapointPetData.UserPetData getPetData() {
        return getDataHandler().get(DataHandler.Data.PET_DATA, DatapointPetData.class).getValue();
    }

    public MissionData getMissionData() {
        MissionData data = getDataHandler().get(DataHandler.Data.MISSION_DATA, DatapointMissionData.class).getValue();
        data.setSkyBlockPlayer(this);
        return data;
    }

    public String getFullDisplayName() {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();
        SkyBlockEmblems.SkyBlockEmblem emblem = experience.getEmblem();
        return getFullDisplayName(emblem, experience.getLevel().getColor());
    }

    public String getFullDisplayName(String levelColor) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();
        return getFullDisplayName(experience.getEmblem(), levelColor);
    }

    public String getFullDisplayName(SkyBlockEmblems.SkyBlockEmblem displayEmblem) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();
        return getFullDisplayName(displayEmblem, experience.getLevel().getColor());
    }

    public String getFullDisplayName(SkyBlockEmblems.SkyBlockEmblem displayEmblem, String levelColor) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();

        return "§8[" + levelColor + experience.getLevel() + "§8] " +
                (displayEmblem == null ? "" : displayEmblem.emblem() + " ") +
                getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                this.getUsername();
    }

    public PlayerShopData getShoppingData() {
        return getDataHandler().get(DataHandler.Data.SHOPPING_DATA, DatapointShopData.class).getValue();
    }

    public DatapointCollection.PlayerCollection getCollection() {
        return getDataHandler().get(DataHandler.Data.COLLECTION, DatapointCollection.class).getValue();
    }

    public DatapointSkills.PlayerSkills getSkills() {
        return getDataHandler().get(DataHandler.Data.SKILLS, DatapointSkills.class).getValue();
    }

    public boolean isOnIsland() {
        return getInstance() != null
                && getInstance() != SkyBlockConst.getInstanceContainer()
                && getInstance() != SkyBlockConst.getEmptyInstance();
    }

    public @Nullable SkyBlockItem getArrow() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.getGenericInstance() != null && item.getGenericInstance() instanceof ArrowImpl) {
                return item;
            }
        }

        if (!hasCustomCollectionAward(CustomCollectionAward.QUIVER)) return null;

        DatapointQuiver.PlayerQuiver quiver = getQuiver();
        return quiver.getFirstItemInQuiver();
    }

    public @Nullable SkyBlockItem getAndConsumeArrow() {
        // Check Inventory first
        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.getGenericInstance() != null && item.getGenericInstance() instanceof ArrowImpl) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    getInventory().setItemStack(i, PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
                    ActionPlayerChangeSkyBlockMenuDisplay.runCheck(this);
                    return item;
                } else {
                    getInventory().setItemStack(i, ItemStack.of(Material.AIR));
                    return item;
                }
            }
        }

        if (!hasCustomCollectionAward(CustomCollectionAward.QUIVER)) return null;

        DatapointQuiver.PlayerQuiver quiver = getQuiver();
        SkyBlockItem item = quiver.getFirstItemInQuiver();
        if (item == null) return null;

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            quiver.setFirstItemInQuiver(item);
            getDataHandler().get(DataHandler.Data.QUIVER, DatapointQuiver.class).setValue(quiver);
            ActionPlayerChangeSkyBlockMenuDisplay.runCheck(this);
            return item;
        } else {
            quiver.setFirstItemInQuiver(null);
            getDataHandler().get(DataHandler.Data.QUIVER, DatapointQuiver.class).setValue(quiver);
            ActionPlayerChangeSkyBlockMenuDisplay.runCheck(this);
            return item;
        }
    }

    public boolean hasTalisman(Talisman talisman) {
        return getTalismans().stream().anyMatch(talisman1 -> talisman1.getClass() == talisman.getClass());
    }

    public List<Talisman> getTalismans() {
        List<Talisman> inInventory = Stream.of(getAllInventoryItems())
                .filter(item -> item.getGenericInstance() != null)
                .filter(item -> item.getGenericInstance() instanceof Talisman)
                .map(item -> (Talisman) item.getGenericInstance()).toList();

        List<Talisman> inAccessoryBag = getAccessoryBag().getAllAccessories().stream()
                .filter(item -> item.getGenericInstance() != null)
                .filter(item -> item.getGenericInstance() instanceof Talisman)
                .map(item -> (Talisman) item.getGenericInstance()).toList();

        return Stream.concat(inInventory.stream(), inAccessoryBag.stream()).collect(Collectors.toList());
    }

    public SkyBlockItem[] getAllInventoryItems() {
        return Stream.of(getInventory().getItemStacks())
                .map(SkyBlockItem::new)
                .toArray(SkyBlockItem[]::new);
    }

    public Map<Integer, Integer> getAllOfTypeInInventory(ItemType type) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.getAttributeHandler().getItemTypeAsType() == null) continue;

            if (item.getAttributeHandler().getItemTypeAsType() == type) {
                map.put(i, stack.amount());
            }
        }

        return map;
    }

    public void setItemInHand(@Nullable SkyBlockItem item) {
        if (item == null) {
            getInventory().setItemInHand(Hand.MAIN, ItemStack.of(Material.AIR));
            return;
        }

        getInventory().setItemInHand(Hand.MAIN,
                PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
    }

    public int getAmountInInventory(ItemType type) {
        return getAllOfTypeInInventory(type).values().stream().mapToInt(Integer::intValue).sum();
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
        if (isOnIsland())
            return SkyBlockRegion.getIslandRegion();
        return SkyBlockRegion.getRegionOfPosition(this.getPosition());
    }

    public void addAndUpdateItem(SkyBlockItem item) {
        if (item.isNA()) return;
        ItemStack toAdd = PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build();
        this.getInventory().addItemStack(toAdd);
    }

    public void addAndUpdateItem(ItemType item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    public void addAndUpdateItem(ItemStack item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    public void takeItem(ItemType type, int amount) {
        Map<Integer, Integer> map = getAllOfTypeInInventory(type);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (amount <= 0) return;

            int slot = entry.getKey();
            int currentAmount = entry.getValue();

            ItemStack item = getInventory().getItemStack(slot)
                            .consume(amount);

            getInventory().setItemStack(slot, item);
            amount -= Math.min(amount, currentAmount);
        }
    }

    public DatapointQuiver.PlayerQuiver getQuiver() {
        return getDataHandler().get(DataHandler.Data.QUIVER, DatapointQuiver.class).getValue();
    }

    public DatapointAccessoryBag.PlayerAccessoryBag getAccessoryBag() {
        return getDataHandler().get(DataHandler.Data.ACCESSORY_BAG, DatapointAccessoryBag.class).getValue();
    }

    public String getShortenedDisplayName() {
        return "§" + getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().getTextColor().asHexString()
                + this.getUsername();
    }

    public float getMaxMana() {
        return (float) (100 + getStatistics().allStatistics().get(ItemStatistic.INTELLIGENCE));
    }

    public boolean hasCustomCollectionAward(CustomCollectionAward award) {
        Map.Entry<ItemType, Integer> entry = CustomCollectionAward.AWARD_CACHE.get(award);
        if (entry == null) return false;

        return getCollection().get(entry.getKey()) > entry.getValue();
    }

    public boolean hasCustomLevelAward(CustomLevelAward award) {
        return getSkyBlockExperience().getLevel().asInt() >= CustomLevelAward.getLevel(award);
    }

    public DatapointFairySouls.PlayerFairySouls getFairySouls() {
        return getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointFairySouls.class).getValue();
    }

    public DatapointSkyBlockExperience.PlayerSkyBlockExperience getSkyBlockExperience() {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience =
                getDataHandler().get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class)
                        .getValue();
        experience.setAttachedPlayer(this);
        return experience;
    }

    public boolean hasUnlockedXPCause(SkyBlockLevelCauseAbstr cause) {
        return getSkyBlockExperience().hasExperienceFor(cause);
    }

    public PlayerEnchantmentHandler getEnchantmentHandler() {
        return new PlayerEnchantmentHandler(this);
    }

    public Double getMiningSpeed() {
        return this.getStatistics().allStatistics().get(ItemStatistic.MINING_SPEED);
    }

    public void sendTo(ServerType type) {
        sendTo(type, false, false);
    }

    public void sendTo(ServerType type, boolean force) {
        sendTo(type, force, false);
    }

    public void sendTo(ServerType type, boolean force, boolean authenticationBypass) {
        if (!authenticationBypass && !hasAuthenticated) return;
        ProxyPlayer player = asProxyPlayer();

        if (type == SkyBlockConst.getTypeLoader().getType() && !force) {
            this.teleport(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition());
            return;
        }

        SkyBlockConst.getTypeLoader().getTablistManager().nullifyCache(this);

        showTitle(Title.title(
                Component.text(SkyBlockTexture.FULL_SCREEN_BLACK.toString()),
                Component.empty(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofMillis(300), Duration.ZERO)
        ));

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            player.transferTo(type);
        }, TaskSchedule.tick(20), TaskSchedule.stop());
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

    public double getCoins() {
        return getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
    }

    public void setCoins(double coins) {
        getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins);
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
        return statistics.allStatistics().get(ItemStatistic.HEALTH).floatValue();
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
            PlayerProfiles profiles = new UserDatabase(uuid).getProfiles();
            if (profiles.getProfiles().isEmpty()) {
                Document document = ProfilesDatabase.collection.find(Filters.eq("_owner", uuid.toString())).first();
                if (document == null)
                    return "§7Unknown";
                DataHandler handler = DataHandler.fromDocument(document);
                return handler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                        handler.get(DataHandler.Data.IGN, DatapointString.class).getValue();
            }

            UUID selected = profiles.getProfiles().getFirst();

            DataHandler handler = DataHandler.fromDocument(new ProfilesDatabase(selected.toString()).getDocument());
            return handler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                    handler.get(DataHandler.Data.IGN, DatapointString.class).getValue();
        }
    }
}
