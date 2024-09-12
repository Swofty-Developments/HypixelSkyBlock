package net.swofty.types.generic.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.commons.MinecraftVersion;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.PlayerShopData;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.*;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.event.actions.player.ActionPlayerChangeSkyBlockMenuDisplay;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.MaxHealthValueUpdateEvent;
import net.swofty.types.generic.event.value.events.MiningValueUpdateEvent;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ArrowImpl;
import net.swofty.types.generic.item.impl.Sack;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.noteblock.SkyBlockSongsHandler;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.region.mining.MineableBlock;
import net.swofty.types.generic.skill.skills.RunecraftingSkill;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.DeathMessageCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class SkyBlockPlayer extends Player {
    private final PlayerAbilityHandler abilityHandler = new PlayerAbilityHandler();
    @Getter
    private final PlayerStatistics statistics = new PlayerStatistics(this);
    public float health = 100;
    public long joined;
    @Setter
    public boolean hasAuthenticated = true;
    @Setter
    public boolean bypassBuild = false;
    @Setter
    public boolean isBankDelayed = false;
    @Setter
    private float mana = 100;
    @Setter
    private boolean inLaunchpad = false;
    @Setter
    private ServerType originServer = ServerType.HUB;
    @Setter
    private SkyBlockIsland skyBlockIsland;
    @Setter
    private MinecraftVersion version = MinecraftVersion.MINECRAFT_1_20_3;
    @Getter
    private PlayerHookManager hookManager;

    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        joined = System.currentTimeMillis();
        hookManager = new PlayerHookManager(this, new HashMap<>());
    }

    public DataHandler getDataHandler() {
        return DataHandler.getUser(this.getUuid());
    }

    public DatapointMuseum.MuseumData getMuseumData() {
        return getDataHandler().get(DataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).getValue();
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

    public FairySoulHandler getFairySoulHandler() {
        return new FairySoulHandler(this);
    }

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

    public int getAmountOfEmptySlots() {
        int amountOfEmptySlots = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            if (stack.material() == Material.AIR) amountOfEmptySlots++;
        }
        return amountOfEmptySlots;
    }

    public boolean hasEmptySlots(int amount) {
        return getAmountOfEmptySlots() >= amount;
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

    public boolean hasTalisman(TieredTalisman talisman) {
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

    public Integer getMagicalPower() {

        int magicalPower = 0;

        for (SkyBlockItem accessory : getAccessoryBag().getUniqueAccessories()) {
            Rarity taliRarity = accessory.getAttributeHandler().getRarity();
            switch (taliRarity) {
                case COMMON -> magicalPower += 3;
                case UNCOMMON -> magicalPower += 5;
                case RARE -> magicalPower += 8;
                case EPIC -> magicalPower += 12;
                case LEGENDARY -> magicalPower += 16;
                case MYTHIC -> magicalPower += 22;
            }
        }
        return magicalPower;
    }

    public void setMuseumData(DatapointMuseum.MuseumData data) {
        getDataHandler().get(DataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).setValue(data);
    }

    public SkyBlockItem[] getAllInventoryItems() {
        return Stream.of(getInventory().getItemStacks())
                .map(SkyBlockItem::new)
                .toArray(SkyBlockItem[]::new);
    }

    public Map<Integer, Integer> getAllOfTypeInInventory(ItemTypeLinker type) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.getAttributeHandler().getPotentialClassLinker() == null) continue;

            if (item.getAttributeHandler().getPotentialClassLinker() == type) {
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

        getInventory().setItemInHand(Hand.MAIN, PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
    }

    public int getAmountInInventory(ItemType type) {
        return getAmountInInventory(ItemTypeLinker.fromType(type));
    }

    public int getAmountInInventory(ItemTypeLinker type) {
        return getAllOfTypeInInventory(type).values().stream().mapToInt(Integer::intValue).sum();
    }

    public boolean isCoop() {
        return getDataHandler().get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue();
    }

    public CoopDatabase.Coop getCoop() {
        return CoopDatabase.getFromMember(getUuid());
    }

    public @Nullable ArmorSetRegistry getArmorSet() {
        ItemTypeLinker helmet = new SkyBlockItem(getInventory().getHelmet()).getAttributeHandler().getPotentialClassLinker();
        ItemTypeLinker chestplate = new SkyBlockItem(getInventory().getChestplate()).getAttributeHandler().getPotentialClassLinker();
        ItemTypeLinker leggings = new SkyBlockItem(getInventory().getLeggings()).getAttributeHandler().getPotentialClassLinker();
        ItemTypeLinker boots = new SkyBlockItem(getInventory().getBoots()).getAttributeHandler().getPotentialClassLinker();

        return ArmorSetRegistry.getArmorSet(boots, leggings, chestplate, helmet);
    }

    public int getRuneLevel() {
        return RunecraftingSkill.getUnlockedRune(this);
    }

    public boolean isWearingItem(SkyBlockItem item) {
        SkyBlockItem[] armor = getArmor();

        for (SkyBlockItem armorItem : armor) {
            if (armorItem == null) continue;
            if (armorItem.getAttributeHandler().getPotentialClassLinker() == item.getAttributeHandler().getPotentialClassLinker()) {
                return true;
            }
        }

        return false;
    }

    public SkyBlockItem[] getArmor() {
        return new SkyBlockItem[] {
                new SkyBlockItem(getInventory().getHelmet()),
                new SkyBlockItem(getInventory().getChestplate()),
                new SkyBlockItem(getInventory().getLeggings()),
                new SkyBlockItem(getInventory().getBoots())
        };
    }

    public ProxyPlayer asProxyPlayer() {
        return new ProxyPlayer(this);
    }

    public SkyBlockItem updateItem(PlayerItemOrigin origin, Consumer<SkyBlockItem> update) {
        ItemStack toUpdate = origin.getStack(this);
        if (toUpdate == null) return null;

        SkyBlockItem item = new SkyBlockItem(toUpdate);
        update.accept(item);

        origin.setStack(this, PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
        return item;
    }

    public @Nullable SkyBlockRegion getRegion() {
        if (isOnIsland()) return SkyBlockRegion.getIslandRegion();
        return SkyBlockRegion.getRegionOfPosition(this.getPosition());
    }

    public void addAndUpdateItem(@Nullable SkyBlockItem item) {
        if (item == null) return;
        if (item.isNA()) return;
        ItemStack toAdd = PlayerItemUpdater.playerUpdate(this, item.getItemStack(), true).build();
        this.getInventory().addItemStack(toAdd);
    }

    public void addAndUpdateItem(UnderstandableSkyBlockItem item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    public void addAndUpdateItem(ItemTypeLinker item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    public void addAndUpdateItem(ItemStack item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    public boolean takeItem(SkyBlockItem itemToTake) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.isSimilar(itemToTake)) {
                getInventory().setItemStack(i, ItemStack.of(Material.AIR));
                return true;
            }
        }

        return false;
    }

    public @Nullable List<SkyBlockItem> takeItem(ItemType type, int amount) {
        return takeItem(ItemTypeLinker.fromType(type), amount);
    }

    public @Nullable List<SkyBlockItem> takeItem(ItemTypeLinker type, int amount) {
        List<SkyBlockItem> consumedItems = new ArrayList<>();
        Map<Integer, Integer> map = getAllOfTypeInInventory(type);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (amount <= 0) break;

            int slot = entry.getKey();
            int currentAmount = entry.getValue();

            SkyBlockItem skyBlockItem = new SkyBlockItem(getInventory().getItemStack(slot));
            skyBlockItem.setAmount(Math.min(skyBlockItem.getAmount(), amount));

            consumedItems.add(skyBlockItem);
            ItemStack item = getInventory().getItemStack(slot).consume(amount);

            getInventory().setItemStack(slot, item);
            amount -= Math.min(amount, currentAmount);
        }
        if (amount > 0) throw new IllegalStateException("Not enough items to take!");
        return consumedItems;
    }

    public DatapointQuiver.PlayerQuiver getQuiver() {
        return getDataHandler().get(DataHandler.Data.QUIVER, DatapointQuiver.class).getValue();
    }

    public DatapointAccessoryBag.PlayerAccessoryBag getAccessoryBag() {
        return getDataHandler().get(DataHandler.Data.ACCESSORY_BAG, DatapointAccessoryBag.class).getValue();
    }

    public DatapointSackOfSacks.PlayerSackOfSacks getSackOfSacks() {
        return getDataHandler().get(DataHandler.Data.SACK_OF_SACKS, DatapointSackOfSacks.class).getValue();
    }

    public DatapointItemsInSacks.PlayerItemsInSacks getSackItems() {
        return getDataHandler().get(DataHandler.Data.ITEMS_IN_SACKS, DatapointItemsInSacks.class).getValue();
    }

    public List<SkyBlockItem> getAllSacks() {
        List<SkyBlockItem> sacks = new ArrayList<>(getSackOfSacks().getAllSacks());
        for (SkyBlockItem item : getAllInventoryItems()) {
            if (item.getGenericInstance() instanceof Sack) {
                sacks.add(item);
            }
        }
        return sacks;
    }

    public int getMaxSackStorage(ItemTypeLinker sack) {
        int maxStorage = 0;
        String sackCategory = "";

        Matcher matcher = Pattern.compile("^(?:(SMALL|MEDIUM|LARGE|ENCHANTED)_)?(.+?)_SACK$").matcher(sack.name());
        if (matcher.find()) {
            sackCategory = matcher.group(2);
        } else {
            System.out.println("Invalid sack name: " + sack.name());
            return 0;
        }

        for (SkyBlockItem skyBlockItem : getAllSacks()) {
            matcher = Pattern.compile("^(?:(SMALL|MEDIUM|LARGE|ENCHANTED)_)?(.+?)_SACK$").matcher(skyBlockItem.getAttributeHandler().getPotentialClassLinker().name());
            if (matcher.find()) {
                String otherCategory = matcher.group(2);
                if (sackCategory.equals(otherCategory) && skyBlockItem.getGenericInstance() instanceof Sack sackInstance) {
                    maxStorage += sackInstance.getMaximumCapacity();
                }
            }
        }
        return maxStorage;
    }

    public boolean canInsertItemIntoSacks(ItemTypeLinker item) {
        for (SkyBlockItem sack : getAllSacks()) {
            if (sack.getGenericInstance() instanceof Sack sackInstance) {
                for (ItemTypeLinker linker : sackInstance.getSackItems()) {
                    if (linker == item) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canInsertItemIntoSacks(ItemTypeLinker item, Integer amount) {
        if (!canInsertItemIntoSacks(item)) return false;

        List<Map<SkyBlockItem, Integer>> maxStorages = new ArrayList<>();
        for (SkyBlockItem sack : getAllSacks()) {
            if (sack.getGenericInstance() instanceof Sack sackInstance) {
                for (ItemTypeLinker linker : sackInstance.getSackItems()) {
                    if (linker == item) {
                        if (!maxStorages.contains(sack)) {
                            maxStorages.add(Map.of(sack, getMaxSackStorage(sack.getAttributeHandler().getPotentialClassLinker())));
                        }
                    }
                }
            }
        }

        maxStorages.sort((map1, map2) -> {
            Integer value1 = map1.values().iterator().next();
            Integer value2 = map2.values().iterator().next();
            return value2.compareTo(value1);
        });

        return maxStorages.getFirst().values().iterator().next() >= getSackItems().getAmount(item) + amount;
    }

    public String getShortenedDisplayName() {
        return StringUtility.getTextFromComponent(Component.text(this.getUsername(),
                getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().getTextColor())
        );
    }

    public float getMaxMana() {
        return (float) (100 + getStatistics().allStatistics().getOverall(ItemStatistic.INTELLIGENCE));
    }

    public boolean hasCustomCollectionAward(CustomCollectionAward award) {
        Map.Entry<ItemTypeLinker, Integer> entry = CustomCollectionAward.AWARD_CACHE.get(award);
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
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getDataHandler().get(DataHandler.Data.SKYBLOCK_EXPERIENCE,
                DatapointSkyBlockExperience.class
        ).getValue();
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
        return this.getStatistics().allStatistics().getOverall(ItemStatistic.MINING_SPEED);
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
            this.teleport(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition().apply(originServer));
            return;
        }

        SkyBlockConst.getTypeLoader().getTablistManager().nullifyCache(this);

        /*showTitle(Title.title(
                Component.text(SkyBlockTexture.FULL_SCREEN_BLACK.toString()),
                Component.empty(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofMillis(300), Duration.ZERO)
        ));*/

        player.transferTo(type);
    }

    public double getTimeToMine(SkyBlockItem item, Block b) {
        MineableBlock block = MineableBlock.get(b);
        if (block == null) return -1;
        if (!item.getAttributeHandler().isMiningTool()) return -1;
        if (getRegion() == null) return -1;

        if (block.getMiningPowerRequirement() > item.getAttributeHandler().getBreakingPower()) return -1;
        if (block.getStrength() > 0) {
            double time = Math.round(block.getStrength() * 30) / (Math.max(getMiningSpeed(), 1));
            ValueUpdateEvent event = new MiningValueUpdateEvent(this, time, item);

            SkyBlockValueEvent.callValueUpdateEvent(event);
            time = (double) event.getValue();

            return time;
        }

        return 0;
    }

    public @NonNull Double getDefense() {
        return this.getStatistics().allStatistics().getOverall(ItemStatistic.DEFENSE);
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

    public Integer getBits() {
        return getDataHandler().get(DataHandler.Data.BITS, DatapointInteger.class).getValue();
    }

    public void setBits(int bits) {
        getDataHandler().get(DataHandler.Data.BITS, DatapointInteger.class).setValue(bits);
    }

    public Integer getGems() {
        return getDataHandler().get(DataHandler.Data.GEMS, DatapointInteger.class).getValue();
    }

    public void setGems(int gems) {
        getDataHandler().get(DataHandler.Data.GEMS, DatapointInteger.class).setValue(gems);
    }

    public Boolean getPurchaseConfirmationBits() {
        return getDataHandler().get(DataHandler.Data.PURCHASE_CONFIRMATION_BITS, DatapointBoolean.class).getValue();
    }

    public void setPurchaseConfirmationBits(boolean enabled) {
        getDataHandler().get(DataHandler.Data.PURCHASE_CONFIRMATION_BITS, DatapointBoolean.class).setValue(enabled);
    }

    public Long getBoosterCookieExpirationDate() {
        return getDataHandler().get(DataHandler.Data.BOOSTER_COOKIE_EXPIRATION_DATE, DatapointLong.class).getValue();
    }

    public void setBoosterCookieExpirationDate(long timestamp) {
        getDataHandler().get(DataHandler.Data.BOOSTER_COOKIE_EXPIRATION_DATE, DatapointLong.class).setValue(timestamp);
    }

    public DatapointKat.PlayerKat getKatData() {
        return getDataHandler().get(DataHandler.Data.KAT, DatapointKat.class).getValue();
    }

    @Override
    public void kill() {
        setHealth(getMaxHealth());
        sendTo(SkyBlockConst.getTypeLoader().getType());

        if (SkyBlockConst.isIslandServer()) return;

        DeathMessageCreator creator = new DeathMessageCreator(this.lastDamage);

        sendMessage("§c☠ §7You " + creator.createPersonal());

        DatapointDouble coins = getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
        coins.setValue(coins.getValue() / 2);

        playSound(Sound.sound(Key.key("block.anvil.fall"), Sound.Source.PLAYER, 1.0f, 2.0f));

        sendMessage("§cYou died and lost " + StringUtility.decimalify(coins.getValue(), 1) + " coins!");

        if (!SkyBlockConst.getTypeLoader().getLoaderValues().announceDeathMessages()) return;

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
            if (player.getUuid().equals(getUuid())) return;
            if (player.getInstance() != getInstance()) return;

            player.sendMessage("§c☠ §7" + getFullDisplayName() + " §7" + creator.createOther());
        });
    }

    public float getMaxHealth() {
        PlayerStatistics statistics = this.getStatistics();
        float maxHealth = statistics.allStatistics().getOverall(ItemStatistic.HEALTH).floatValue();
        MaxHealthValueUpdateEvent event = new MaxHealthValueUpdateEvent(this, maxHealth);
        SkyBlockValueEvent.callValueUpdateEvent(event);
        return (float) event.getValue();
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float health) {
        if ((System.currentTimeMillis() - joined) < 3000) return;
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
            DataHandler profile = DataHandler.getSelectedOfOfflinePlayer(uuid);
            return profile.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                    profile.get(DataHandler.Data.IGN, DatapointString.class).getValue();
        }
    }
}
