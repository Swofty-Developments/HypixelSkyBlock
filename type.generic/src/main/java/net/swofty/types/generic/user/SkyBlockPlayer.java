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

    /**
     * Constructs a new instance of the SkyBlockPlayer class.
     *
     * <p>This constructor initializes a player with a UUID, username,
     * and a connection to the player. It also records the time the player joined
     * and initializes the hook manager for player-specific events.</p>
     *
     * @param uuid             the unique identifier of the player
     * @param username         the username of the player
     * @param playerConnection  the connection object associated with the player
     */
    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        joined = System.currentTimeMillis();
        hookManager = new PlayerHookManager(this, new HashMap<>());
    }

    /**
     * Retrieves the {@link DataHandler} associated with the current player.
     *
     * @return The {@link DataHandler} instance for the player.
     */
    public DataHandler getDataHandler() {
        return DataHandler.getUser(this.getUuid());
    }

    /**
     * Retrieves the museum data associated with the player.
     *
     * @return The {@link DatapointMuseum.MuseumData} associated with the player.
     */
    public DatapointMuseum.MuseumData getMuseumData() {
        return getDataHandler().get(DataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).getValue();
    }

    /**
     * Retrieves the toggle settings associated with the player.
     *
     * @return The {@link DatapointToggles.Toggles} associated with the player.
     */
    public DatapointToggles.Toggles getToggles() {
        return getDataHandler().get(DataHandler.Data.TOGGLES, DatapointToggles.class).getValue();
    }

    /**
     * Creates a new instance of {@link SkyBlockSongsHandler} for the player.
     *
     * @return A new instance of {@link SkyBlockSongsHandler} for the player.
     */
    public SkyBlockSongsHandler getSongHandler() {
        return new SkyBlockSongsHandler(this);
    }

    /**
     * Creates a new instance of {@link AntiCheatHandler} for the player.
     *
     * @return A new instance of {@link AntiCheatHandler} for the player.
     */
    public AntiCheatHandler getAntiCheatHandler() {
        return new AntiCheatHandler(this);
    }

    /**
     * Creates a new instance of {@link FairySoulHandler} for the player.
     *
     * @return A new instance of {@link FairySoulHandler} for the player.
     */
    public FairySoulHandler getFairySoulHandler() {
        return new FairySoulHandler(this);
    }

    /**
     * Creates a new instance of {@link LogHandler} for the player.
     *
     * @return A new instance of {@link LogHandler} for the player.
     */
    public LogHandler getLogHandler() {
        return new LogHandler(this);
    }

    /**
     * Retrieves the {@link PlayerProfiles} associated with the player.
     *
     * @return The {@link PlayerProfiles} for the player.
     */
    public PlayerProfiles getProfiles() {
        return PlayerProfiles.get(this.getUuid());
    }

    /**
     * Retrieves the pet data associated with the player.
     *
     * @return An instance of {@link DatapointPetData.UserPetData} containing the player's pet data.
     */
    public DatapointPetData.UserPetData getPetData() {
        return getDataHandler().get(DataHandler.Data.PET_DATA, DatapointPetData.class).getValue();
    }

    /**
     * Retrieves the mission data associated with the player.
     *
     * @return An instance of {@link MissionData} containing the player's mission data.
     */
    public MissionData getMissionData() {
        MissionData data = getDataHandler().get(DataHandler.Data.MISSION_DATA, DatapointMissionData.class).getValue();
        data.setSkyBlockPlayer(this);
        return data;
    }

    /**
     * Constructs the full display name for the player by combining their emblem and experience level color.
     *
     * @return A string representing the player's full display name, including their emblem and experience level color.
     */
    public String getFullDisplayName() {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();
        SkyBlockEmblems.SkyBlockEmblem emblem = experience.getEmblem();
        return getFullDisplayName(emblem, experience.getLevel().getColor());
    }

    /**
     * Constructs the full display name for the player using a specified level color and the player's emblem.
     *
     * @param levelColor The color representing the player's experience level.
     * @return A string representing the player's full display name, including their emblem and specified level color.
     */
    public String getFullDisplayName(String levelColor) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();
        return getFullDisplayName(experience.getEmblem(), levelColor);
    }

    /**
     * Constructs the full display name for the player using a specified display emblem and the player's level color.
     *
     * @param displayEmblem The emblem to be displayed alongside the player's name.
     * @return A string representing the player's full display name, including the specified emblem and the player's level color.
     */
    public String getFullDisplayName(SkyBlockEmblems.SkyBlockEmblem displayEmblem) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();
        return getFullDisplayName(displayEmblem, experience.getLevel().getColor());
    }

    /**
     * Constructs the full display name for the player using a specified display emblem and level color.
     *
     * @param displayEmblem The emblem to be displayed alongside the player's name.
     * @param levelColor The color representing the player's level.
     * @return A string representing the player's full display name, formatted with the level, emblem, rank prefix, and username.
     */
    public String getFullDisplayName(SkyBlockEmblems.SkyBlockEmblem displayEmblem, String levelColor) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getSkyBlockExperience();

        return "§8[" + levelColor + experience.getLevel() + "§8] " +
                (displayEmblem == null ? "" : displayEmblem.emblem() + " ") +
                getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
                this.getUsername();
    }

    /**
     * Retrieves the shopping data for the player.
     *
     * @return An instance of {@link PlayerShopData} containing the player's shopping information.
     */
    public PlayerShopData getShoppingData() {
        return getDataHandler().get(DataHandler.Data.SHOPPING_DATA, DatapointShopData.class).getValue();
    }

    /**
     * Retrieves the collection data for the player.
     *
     * @return An instance of {@link DatapointCollection.PlayerCollection} containing the player's collection information.
     */
    public DatapointCollection.PlayerCollection getCollection() {
        return getDataHandler().get(DataHandler.Data.COLLECTION, DatapointCollection.class).getValue();
    }

    /**
     * Retrieves the skills data for the player.
     *
     * @return An instance of {@link DatapointSkills.PlayerSkills} containing the player's skills information.
     */
    public DatapointSkills.PlayerSkills getSkills() {
        return getDataHandler().get(DataHandler.Data.SKILLS, DatapointSkills.class).getValue();
    }

    /**
     * Checks if the player is currently on their island.
     *
     * @return {@code true} if the player is on their island; {@code false} otherwise.
     */
    public boolean isOnIsland() {
        return getInstance() != null
                && getInstance() != SkyBlockConst.getInstanceContainer()
                && getInstance() != SkyBlockConst.getEmptyInstance();
    }

    /**
     * Retrieves the player's arrow item.
     *
     * This method checks the player's inventory for an arrow. If no arrow is found in the inventory,
     * it checks if the player has the custom collection award for a quiver.
     * If the player has a quiver, it returns the first item from the quiver.
     *
     * @return An instance of {@link SkyBlockItem} representing the arrow,
     *         or {@code null} if no arrow is found in the inventory or the quiver.
     */
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

    /**
     * Counts the number of empty slots in the player's inventory.
     *
     * This method iterates through the player's inventory slots and counts how many are empty.
     * An empty slot is defined as a slot containing air (i.e., no item).
     *
     * @return The number of empty slots in the player's inventory.
     */
    public int getAmountOfEmptySlots() {
        int amountOfEmptySlots = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = getInventory().getItemStack(i);
            if (stack.material() == Material.AIR) amountOfEmptySlots++;
        }
        return amountOfEmptySlots;
    }

    /**
     * Checks if the player's inventory has a specified number of empty slots.
     *
     * @param amount The number of empty slots to check for.
     * @return True if the player has at least the specified number of empty slots, false otherwise.
     */
    public boolean hasEmptySlots(int amount) {
        return getAmountOfEmptySlots() >= amount;
    }

    /**
     * Retrieves an arrow from the player's inventory or quiver and consumes it.
     *
     * @return The {@link SkyBlockItem} representing the consumed arrow, or null if no arrows
     *         are available in the inventory or quiver.
     */
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

    /**
     * Checks if the player has a specific talisman.
     *
     * @param talisman The talisman to check for.
     * @return True if the player has the talisman, false otherwise.
     */
    public boolean hasTalisman(Talisman talisman) {
        return getTalismans().stream().anyMatch(talisman1 -> talisman1.getClass() == talisman.getClass());
    }

    /**
     * Checks if the player has the specified tiered talisman.
     *
     * @param talisman The tiered talisman to check for.
     * @return True if the player has the talisman, false otherwise.
     */
    public boolean hasTalisman(TieredTalisman talisman) {
        return getTalismans().stream().anyMatch(talisman1 -> talisman1.getClass() == talisman.getClass());
    }

    /**
     * Retrieves all talismans from both the player's inventory and accessory bag.
     *
     * @return A list of all talismans found in the inventory and accessory bag.
     */
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

    /**
     * Calculates and returns the player's total magical power based on the accessories
     * stored in the accessory bag. Each accessory contributes to the magical power based
     * on its rarity.
     *
     * @return The total magical power from all unique accessories.
     */
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

    /**
     * Updates the player's museum data by setting the given {@link DatapointMuseum.MuseumData}.
     * This method stores the provided museum data into the player's data handler.
     *
     * @param data The new {@link DatapointMuseum.MuseumData} to be saved.
     */
    public void setMuseumData(DatapointMuseum.MuseumData data) {
        getDataHandler().get(DataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).setValue(data);
    }

    /**
     * Retrieves all the items from the player's inventory and converts them into SkyBlockItems.
     * This method looks at every slot in the inventory, takes the raw item stack from each slot,
     * and wraps it into a {@link SkyBlockItem}.
     *
     * @return An array of {@link SkyBlockItem} objects, representing all the items currently in the player's inventory.
     *         If the inventory is empty, an empty array is returned.
     */
    public SkyBlockItem[] getAllInventoryItems() {
        return Stream.of(getInventory().getItemStacks())
                .map(SkyBlockItem::new)
                .toArray(SkyBlockItem[]::new);
    }

    /**
     * Finds all items of a specific type in the player's inventory and returns them as a map.
     * It goes through each slot in the inventory and checks if the item in that slot matches
     * the given type. If it does, it adds the slot number and the quantity of the item to a map.
     *
     * @param type The {@link ItemTypeLinker} which should be searched for.
     * @return A {@link Map} where the keys are the slots in the inventory and the values are the
     *         quantities of the items found in those slots. If no items of the specified
     *         type are found, an empty map is returned.
     */
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

    /**
     * Sets the item in the player's main hand to the specified {@link SkyBlockItem}.
     *
     * If the provided item is null, the method clears the item in the player's main hand by
     * setting it to air. If an item is provided, it updates the main hand with the specified
     * item after processing it through the player item updater.
     *
     * @param item The {@link SkyBlockItem} to set in the player's hand. If null, the hand
     *             will be cleared.
     */
    public void setItemInHand(@Nullable SkyBlockItem item) {
        if (item == null) {
            getInventory().setItemInHand(Hand.MAIN, ItemStack.of(Material.AIR));
            return;
        }

        getInventory().setItemInHand(Hand.MAIN, PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
    }

    /**
     * Retrieves the total amount of items of the specified {@link ItemType} present in the player's inventory.
     *
     * This method converts the given {@link ItemType} to an {@link ItemTypeLinker} and
     * then calls the overloaded method to get the actual amount in the inventory.
     *
     * @param type The {@link ItemType} for which to check the inventory amount.
     * @return The total number of items of the specified type present in the inventory.
     */
    public int getAmountInInventory(ItemType type) {
        return getAmountInInventory(ItemTypeLinker.fromType(type));
    }

    /**
     * Calculates the total amount of items of the specified {@link ItemTypeLinker} present in the player's inventory.
     *
     * This method retrieves a map of item stacks in the inventory matching the given type and sums their amounts.
     *
     * @param type The {@link ItemTypeLinker} representing the type of items to count.
     * @return The total number of items of the specified type present in the inventory.
     */
    public int getAmountInInventory(ItemTypeLinker type) {
        return getAllOfTypeInInventory(type).values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Checks if the player is in a coop.
     *
     * @return {@code true} if the player is in coop, {@code false} otherwise.
     */
    public boolean isCoop() {
        return getDataHandler().get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue();
    }

    /**
     * Retrieves the coop information for the player.
     *
     * @return An instance of {@link CoopDatabase.Coop} containing the player's coop details.
     */
    public CoopDatabase.Coop getCoop() {
        return CoopDatabase.getFromMember(getUuid());
    }

    /**
     * Retrieves the armor set currently equipped by the player.
     *
     * @return An instance of {@link ArmorSetRegistry} representing the player's armor set,
     *         or {@code null} if no armor set is equipped.
     */
    public @Nullable ArmorSetRegistry getArmorSet() {
        ItemTypeLinker helmet = new SkyBlockItem(getInventory().getHelmet()).getAttributeHandler().getPotentialClassLinker();
        ItemTypeLinker chestplate = new SkyBlockItem(getInventory().getChestplate()).getAttributeHandler().getPotentialClassLinker();
        ItemTypeLinker leggings = new SkyBlockItem(getInventory().getLeggings()).getAttributeHandler().getPotentialClassLinker();
        ItemTypeLinker boots = new SkyBlockItem(getInventory().getBoots()).getAttributeHandler().getPotentialClassLinker();

        return ArmorSetRegistry.getArmorSet(boots, leggings, chestplate, helmet);
    }

    /**
     * Gets the player's current rune level based on the unlocked runes in the runecrafting skill.
     *
     * @return An integer representing the player's rune level.
     */
    public int getRuneLevel() {
        return RunecraftingSkill.getUnlockedRune(this);
    }

    /**
     * Checks if the player is currently wearing a specific armor piece.
     *
     * @param item The {@link SkyBlockItem} to check against the player's equipped armor.
     * @return {@code true} if the player is wearing the item, {@code false} otherwise.
     */
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

    /**
     * Retrieves the player's equipped armor pieces.
     *
     * @return An array of {@link SkyBlockItem} representing the player's helmet, chestplate, leggings, and boots.
     */
    public SkyBlockItem[] getArmor() {
        return new SkyBlockItem[] {
                new SkyBlockItem(getInventory().getHelmet()),
                new SkyBlockItem(getInventory().getChestplate()),
                new SkyBlockItem(getInventory().getLeggings()),
                new SkyBlockItem(getInventory().getBoots())
        };
    }

    /**
     * Converts the current player instance to a {@link ProxyPlayer}.
     *
     * @return A new instance of {@link ProxyPlayer} that wraps the current player.
     */
    public ProxyPlayer asProxyPlayer() {
        return new ProxyPlayer(this);
    }

    /**
     * Updates a {@link SkyBlockItem} based on the specified origin and update logic.
     *
     * @param origin The origin of the item being updated.
     * @param update A consumer that defines how to update the {@link SkyBlockItem}.
     * @return The updated {@link SkyBlockItem}, or null if the item to update was not found.
     */
    public SkyBlockItem updateItem(PlayerItemOrigin origin, Consumer<SkyBlockItem> update) {
        ItemStack toUpdate = origin.getStack(this);
        if (toUpdate == null) return null;

        SkyBlockItem item = new SkyBlockItem(toUpdate);
        update.accept(item);

        origin.setStack(this, PlayerItemUpdater.playerUpdate(this, item.getItemStack()).build());
        return item;
    }

    /**
     * Retrieves the {@link SkyBlockRegion} associated with the player.
     *
     * <p>If the player is on their island, the island region is returned.
     * Otherwise, the region corresponding to the player's current position is returned.</p>
     *
     * @return The {@link SkyBlockRegion} the player is in, or null if no region is found.
     */
    public @Nullable SkyBlockRegion getRegion() {
        if (isOnIsland()) return SkyBlockRegion.getIslandRegion();
        return SkyBlockRegion.getRegionOfPosition(this.getPosition());
    }

    /**
     * Adds a {@link SkyBlockItem} to the player's inventory and updates it.
     *
     * <p>If the provided item is null or not applicable (NA), the method does nothing.</p>
     *
     * @param item The {@link SkyBlockItem} to add to the inventory.
     */
    public void addAndUpdateItem(@Nullable SkyBlockItem item) {
        if (item == null) return;
        if (item.isNA()) return;

        ItemStack toAdd = PlayerItemUpdater.playerUpdate(
                this,
                item.getItemStack(),
                true).build();
        this.getInventory().addItemStack(toAdd);
    }

    /**
     * Adds and updates an {@link UnderstandableSkyBlockItem} to the player's inventory.
     *
     * <p>This method creates a new {@link SkyBlockItem} from the provided
     * {@link UnderstandableSkyBlockItem} and calls the
     * {@link #addAndUpdateItem(SkyBlockItem)} method.</p>
     *
     * @param item The {@link UnderstandableSkyBlockItem} to add to the inventory.
     */
    public void addAndUpdateItem(UnderstandableSkyBlockItem item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    /**
     * Adds and updates an {@link ItemTypeLinker} to the player's inventory.
     *
     * <p>This method creates a new {@link SkyBlockItem} from the provided
     * {@link ItemTypeLinker} and calls the
     * {@link #addAndUpdateItem(SkyBlockItem)} method.</p>
     *
     * @param item The {@link ItemTypeLinker} to add to the inventory.
     */
    public void addAndUpdateItem(ItemTypeLinker item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    /**
     * Adds and updates an {@link ItemStack} to the player's inventory.
     *
     * <p>This method creates a new {@link SkyBlockItem} from the provided
     * {@link ItemStack} and calls the
     * {@link #addAndUpdateItem(SkyBlockItem)} method.</p>
     *
     * @param item The {@link ItemStack} to add to the inventory.
     */
    public void addAndUpdateItem(ItemStack item) {
        addAndUpdateItem(new SkyBlockItem(item));
    }

    /**
     * Attempts to remove a specified {@link SkyBlockItem} from the player's inventory.
     *
     * <p>This method searches through the player's inventory for an item that
     * matches the specified {@link SkyBlockItem}. If found, the item is removed
     * from the inventory, and the method returns true. If the item is not found,
     * it returns false.</p>
     *
     * @param itemToTake The {@link SkyBlockItem} to remove from the inventory.
     * @return {@code true} if the item was successfully removed;
     *         {@code false} if the item was not found in the inventory.
     */
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

    /**
     * Attempts to remove a specified amount of items of a given {@link ItemType}
     * from the player's inventory.
     *
     * <p>This method searches through the player's inventory for items that match
     * the specified {@link ItemType}. It will remove up to the specified amount
     * of items and return a list of the items that were successfully removed.</p>
     *
     * @param type   The {@link ItemType} of the items to remove from the inventory.
     * @param amount The number of items to remove.
     * @return A list of {@link SkyBlockItem} that were successfully removed.
     *         Returns {@code null} if no items were found or could be removed.
     */
    public @Nullable List<SkyBlockItem> takeItem(ItemType type, int amount) {
        return takeItem(ItemTypeLinker.fromType(type), amount);
    }

    /**
     * Attempts to remove a specified amount of items of a given {@link ItemTypeLinker}
     * from the player's inventory.
     *
     * <p>This method will search through the player's inventory for items that match
     * the specified {@link ItemTypeLinker}. It removes up to the specified amount of
     * items and returns a list of the items that were successfully removed. If not enough
     * items are available to satisfy the request, an exception is thrown.</p>
     *
     * @param type   The {@link ItemTypeLinker} of the items to remove from the inventory.
     * @param amount The number of items to remove.
     * @return A list of {@link SkyBlockItem} that were successfully removed from the inventory.
     * @throws IllegalStateException if there are not enough items to take.
     */
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

    /**
     * Retrieves the quiver data for the player.
     *
     * @return An instance of {@link DatapointQuiver.PlayerQuiver} containing the player's quiver data.
     */
    public DatapointQuiver.PlayerQuiver getQuiver() {
        return getDataHandler().get(DataHandler.Data.QUIVER, DatapointQuiver.class).getValue();
    }

    /**
     * Retrieves the accessory bag data for the player.
     *
     * @return An instance of {@link DatapointAccessoryBag.PlayerAccessoryBag} containing the player's accessory bag data.
     */
    public DatapointAccessoryBag.PlayerAccessoryBag getAccessoryBag() {
        return getDataHandler().get(DataHandler.Data.ACCESSORY_BAG, DatapointAccessoryBag.class).getValue();
    }

    /**
     * Retrieves the sack of sacks data for the player.
     *
     * @return An instance of {@link DatapointSackOfSacks.PlayerSackOfSacks} containing the player's sack of sacks data.
     */
    public DatapointSackOfSacks.PlayerSackOfSacks getSackOfSacks() {
        return getDataHandler().get(DataHandler.Data.SACK_OF_SACKS, DatapointSackOfSacks.class).getValue();
    }

    /**
     * Retrieves the items in sacks data for the player.
     *
     * @return An instance of {@link DatapointItemsInSacks.PlayerItemsInSacks} containing the player's sack items data.
     */
    public DatapointItemsInSacks.PlayerItemsInSacks getSackItems() {
        return getDataHandler().get(DataHandler.Data.ITEMS_IN_SACKS, DatapointItemsInSacks.class).getValue();
    }

    /**
     * Retrieves all sacks owned by the player.
     *
     * <p>This method collects all sacks from the player's sack of sacks
     * and also checks the player's inventory for any additional sacks.</p>
     *
     * @return A list of {@link SkyBlockItem} representing all sacks owned by the player.
     */
    public List<SkyBlockItem> getAllSacks() {
        List<SkyBlockItem> sacks = new ArrayList<>(getSackOfSacks().getAllSacks());
        for (SkyBlockItem item : getAllInventoryItems()) {
            if (item.getGenericInstance() instanceof Sack) {
                sacks.add(item);
            }
        }
        return sacks;
    }

    /**
     * Calculates the maximum storage capacity for a specified sack type by examining all sacks owned
     * by the player and aggregating the maximum storage capacity of sacks that match the category
     * of the given sack.
     *
     * @param sack The {@link ItemTypeLinker} representing the sack type
     *             for which to calculate the maximum storage.
     * @return The total maximum storage capacity for sacks of the same
     *         category, or 0 if the sack name is invalid.
     */
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

    /**
     * Checks if the specified item can be inserted into any of the player's sacks.
     *
     * <p>This method iterates through the player's sacks and checks if the specified
     * item type is allowed in any of them.</p>
     *
     * @param item The {@link ItemTypeLinker} representing the item type to check.
     * @return {@code true} if the item can be inserted into at least one sack,
     *         {@code false} otherwise.
     */
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

    /**
     * Checks if a specified amount of an item can be inserted into the player's sacks.
     *
     * <p>This method verifies if the item can be inserted into any sack and
     * calculates the maximum storage available for the specified item type.</p>
     *
     * @param item   The {@link ItemTypeLinker} representing the item type to check.
     * @param amount The amount of the item to be inserted.
     * @return {@code true} if the item can be inserted in the specified amount,
     *         {@code false} otherwise.
     */
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

    /**
     * Retrieves the shortened display name for the player.
     *
     * <p>This method combines the player's username with their rank's text color
     * to create a visually formatted display name.</p>
     *
     * @return A formatted string representing the player's shortened display name.
     */
    public String getShortenedDisplayName() {
        return StringUtility.getTextFromComponent(Component.text(this.getUsername(),
                getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().getTextColor())
        );
    }

    /**
     * Calculates the maximum mana for the player.
     *
     * @return The maximum mana as a float value.
     */
    public float getMaxMana() {
        return (float) (100 + getStatistics().allStatistics().getOverall(ItemStatistic.INTELLIGENCE));
    }

    /**
     * Checks if the player has a specific custom collection award.
     *
     * @param award The custom collection award to check for.
     * @return True if the player has the award, false otherwise.
     */
    public boolean hasCustomCollectionAward(CustomCollectionAward award) {
        Map.Entry<ItemTypeLinker, Integer> entry = CustomCollectionAward.AWARD_CACHE.get(award);
        if (entry == null) return false;

        return getCollection().get(entry.getKey()) > entry.getValue();
    }

    /**
     * Checks if the player has a specific custom level award.
     *
     * @param award The custom level award to check for.
     * @return True if the player has the award, false otherwise.
     */
    public boolean hasCustomLevelAward(CustomLevelAward award) {
        return getSkyBlockExperience().getLevel().asInt() >= CustomLevelAward.getLevel(award);
    }

    /**
     * Retrieves the fairy souls data for the player.
     *
     * @return An instance of {@link DatapointFairySouls.PlayerFairySouls} containing the player's fairy souls data.
     */
    public DatapointFairySouls.PlayerFairySouls getFairySouls() {
        return getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointFairySouls.class).getValue();
    }

    /**
     * Retrieves the SkyBlock experience data for the player.
     *
     * @return An instance of {@link DatapointSkyBlockExperience.PlayerSkyBlockExperience} containing the player's SkyBlock experience data.
     */
    public DatapointSkyBlockExperience.PlayerSkyBlockExperience getSkyBlockExperience() {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getDataHandler().get(DataHandler.Data.SKYBLOCK_EXPERIENCE,
                DatapointSkyBlockExperience.class
        ).getValue();
        experience.setAttachedPlayer(this);
        return experience;
    }

    /**
     * Checks if the player has unlocked a specific XP cause.
     *
     * @param cause The {@link SkyBlockLevelCauseAbstr} to check.
     * @return {@code true} if the player has unlocked the specified cause, {@code false} otherwise.
     */
    public boolean hasUnlockedXPCause(SkyBlockLevelCauseAbstr cause) {
        return getSkyBlockExperience().hasExperienceFor(cause);
    }

    /**
     * Retrieves the player's enchantment handler.
     *
     * @return A new instance of {@link PlayerEnchantmentHandler} for the player.
     */
    public PlayerEnchantmentHandler getEnchantmentHandler() {
        return new PlayerEnchantmentHandler(this);
    }

    /**
     * Retrieves the player's mining speed.
     *
     * @return The player's total mining speed as a {@link Double}.
     */
    public Double getMiningSpeed() {
        return this.getStatistics().allStatistics().getOverall(ItemStatistic.MINING_SPEED);
    }

    /**
     * Transfers the player to a specified server.
     *
     * @param type The {@link ServerType} to which the player will be sent.
     */
    public void sendTo(ServerType type) {
        sendTo(type, false, false);
    }

    /**
     * Transfers the player to a specified server with an option to force the transfer.
     *
     * @param type  The {@link ServerType} to which the player will be sent.
     * @param force Whether the transfer should be forced.
     */
    public void sendTo(ServerType type, boolean force) {
        sendTo(type, force, false);
    }

    /**
     * Transfers the player to a specified server with force and authentication options.
     *
     * @param type                 The {@link ServerType} to which the player will be sent.
     * @param force                Whether the transfer should be forced.
     * @param authenticationBypass Whether to bypass authentication checks before transferring.
     */
    public void sendTo(ServerType type, boolean force, boolean authenticationBypass) {
        if (!authenticationBypass && !hasAuthenticated) return;
        ProxyPlayer player = asProxyPlayer();

        if (type == SkyBlockConst.getTypeLoader().getType() && !force) {
            this.teleport(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition().apply(originServer));
            return;
        }

        SkyBlockConst.getTypeLoader().getTablistManager().nullifyCache(this);
        player.transferTo(type);
    }

    /**
     * Calculates the time required to mine a block using the specified item.
     *
     * @param item The {@link SkyBlockItem} representing the tool used for mining.
     * @param b    The {@link Block} to be mined.
     * @return The time in seconds required to mine the block, or -1 if the item cannot mine the block.
     */
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

    /**
     * Retrieves the player's defense.
     *
     * @return The player's total defense as a {@link Double}.
     */
    public @NonNull Double getDefense() {
        return this.getStatistics().allStatistics().getOverall(ItemStatistic.DEFENSE);
    }

    /**
     * Plays a success sound for the player, using a note block pling sound effect.
     */
    public void playSuccessSound() {
        playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
    }

    /**
     * @return The amount of coins the player has.
     */
    public double getCoins() {
        return getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
    }

    /**
     * Sets the player's coin balance.
     *
     * @param coins The amount of coins to set for the player.
     */
    public void setCoins(double coins) {
        getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins);
    }

    /**
     * @return The amount of bits the player has.
     */
    public Integer getBits() {
        return getDataHandler().get(DataHandler.Data.BITS, DatapointInteger.class).getValue();
    }

    /**
     * Sets the player's bit balance.
     *
     * @param bits The amount of bits to set for the player.
     */
    public void setBits(int bits) {
        getDataHandler().get(DataHandler.Data.BITS, DatapointInteger.class).setValue(bits);
    }

    /**
     * @return The amount of gems the player has.
     */
    public Integer getGems() {
        return getDataHandler().get(DataHandler.Data.GEMS, DatapointInteger.class).getValue();
    }

    /**
     * Sets the player's gem balance.
     *
     * @param gems The amount of gems to set for the player.
     */
    public void setGems(int gems) {
        getDataHandler().get(DataHandler.Data.GEMS, DatapointInteger.class).setValue(gems);
    }

    /**
     * Retrieves the expiration date of the player's booster cookie.
     *
     * @return The expiration date as a {@link Long} timestamp.
     */
    public Long getBoosterCookieExpirationDate() {
        return getDataHandler().get(DataHandler.Data.BOOSTER_COOKIE_EXPIRATION_DATE, DatapointLong.class).getValue();
    }

    /**
     * Sets the expiration date of the player's booster cookie.
     *
     * @param timestamp The expiration date as a {@code long} timestamp.
     */
    public void setBoosterCookieExpirationDate(long timestamp) {
        getDataHandler().get(DataHandler.Data.BOOSTER_COOKIE_EXPIRATION_DATE, DatapointLong.class).setValue(timestamp);
    }

    /**
     * Retrieves the player's Kat data.
     *
     * @return An instance of {@link DatapointKat.PlayerKat} containing the player's Kat-related data.
     */
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

    /**
     * Retrieves the player's maximum health.
     *
     * @return The maximum health of the player as a float.
     */
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

    /**
     * Retrieves the display name for a player based on their UUID.
     *
     * <p>If the player is currently loaded, it returns their full display name.
     * Otherwise, it fetches the player's profile data and constructs the display name
     * using their rank prefix and in-game name (IGN).</p>
     *
     * @param uuid the UUID of the player whose display name is to be retrieved
     * @return the display name of the player, including their rank prefix if offline
     */
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
