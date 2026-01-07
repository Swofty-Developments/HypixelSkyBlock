package net.swofty.type.skywarslobby.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skywars.SoulWellRewardType;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsUnlocks;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.kit.SkywarsKit;
import net.swofty.type.skywarslobby.kit.SkywarsKitRegistry;
import net.swofty.type.skywarslobby.perk.SkywarsPerk;
import net.swofty.type.skywarslobby.perk.SkywarsPerkRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GUISoulWellRolling extends HypixelInventoryGUI {
    private static final Random RANDOM = new Random();
    private static final Material[] GLASS_COLORS = {
            Material.PINK_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE
    };

    // Wheel column positions centered for different wheel counts
    // 1 wheel: center column (4)
    private static final int[][] WHEEL_SLOTS_1 = {
            {4, 13, 22, 31, 40}    // Center column
    };

    // 2 wheels: columns 3 and 5
    private static final int[][] WHEEL_SLOTS_2 = {
            {3, 12, 21, 30, 39},   // Left of center
            {5, 14, 23, 32, 41}    // Right of center
    };

    // 3 wheels: columns 2, 4, 6
    private static final int[][] WHEEL_SLOTS_3 = {
            {2, 11, 20, 29, 38},   // Left
            {4, 13, 22, 31, 40},   // Center
            {6, 15, 24, 33, 42}    // Right
    };

    // 4 wheels: columns 1, 3, 5, 7
    private static final int[][] WHEEL_SLOTS_4 = {
            {1, 10, 19, 28, 37},   // Column 1
            {3, 12, 21, 30, 39},   // Column 3
            {5, 14, 23, 32, 41},   // Column 5
            {7, 16, 25, 34, 43}    // Column 7
    };

    // 5 wheels: columns 0, 2, 4, 6, 8
    private static final int[][] WHEEL_SLOTS_5 = {
            {0, 9, 18, 27, 36},    // Column 0
            {2, 11, 20, 29, 38},   // Column 2
            {4, 13, 22, 31, 40},   // Column 4
            {6, 15, 24, 33, 42},   // Column 6
            {8, 17, 26, 35, 44}    // Column 8
    };

    private final int wheelCount;
    private final List<SoulWellReward> finalRewards;
    private final List<List<SoulWellReward>> wheelItems;
    private boolean rewardsProcessed = false;

    public GUISoulWellRolling(int wheelCount) {
        super("Soul Well", InventoryType.CHEST_5_ROW);
        this.wheelCount = Math.min(5, Math.max(1, wheelCount));
        this.finalRewards = new ArrayList<>();
        this.wheelItems = new ArrayList<>();

        // Generate rewards for each wheel
        for (int i = 0; i < this.wheelCount; i++) {
            finalRewards.add(generateRandomReward());
            wheelItems.add(generateWheelItems());
        }
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        // Fill all slots with random glass initially
        for (int i = 0; i < 45; i++) {
            final int slot = i;
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§8Rolling...",
                            getRandomGlass(),
                            1
                    );
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());

        // Start the rolling animation
        startRollingAnimation(player);
    }

    private void startRollingAnimation(HypixelPlayer player) {
        AtomicInteger tickCount = new AtomicInteger(0);
        int totalTicks = 40;
        int[][] wheelSlots = getWheelSlots(wheelCount);
        int actualWheelCount = wheelSlots.length;

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            int tick = tickCount.getAndIncrement();

            if (!player.isOnline() || getInventory() == null) {
                return;
            }

            // Update each wheel
            for (int wheel = 0; wheel < actualWheelCount; wheel++) {
                int[] slots = wheelSlots[wheel];
                List<SoulWellReward> items = wheelItems.get(wheel);
                SoulWellReward finalReward = finalRewards.get(wheel);

                // Calculate how far through the animation we are
                double progress = (double) tick / totalTicks;

                // Wheels stop at different times
                int stopTick = totalTicks - (actualWheelCount - wheel - 1) * 5;

                if (tick >= stopTick) {
                    // This wheel has stopped - show final reward in center
                    for (int i = 0; i < slots.length; i++) {
                        int slot = slots[i];
                        if (i == 2) { // Center slot (index 2 of 5)
                            setRewardItem(slot, finalReward);
                        } else {
                            // Show nearby items
                            int offset = i - 2;
                            int itemIndex = (items.size() / 2 + offset + items.size()) % items.size();
                            setRewardItem(slot, items.get(itemIndex));
                        }
                    }
                } else {
                    // Still rolling - cycle through items
                    int offset = tick % items.size();
                    for (int i = 0; i < slots.length; i++) {
                        int slot = slots[i];
                        int itemIndex = (offset + i) % items.size();
                        setRewardItem(slot, items.get(itemIndex));
                    }
                }
            }

            // Fill remaining slots with glass
            for (int i = 0; i < 45; i++) {
                boolean isWheelSlot = false;
                for (int wheel = 0; wheel < actualWheelCount; wheel++) {
                    int[] slots = wheelSlots[wheel];
                    for (int slot : slots) {
                        if (slot == i) {
                            isWheelSlot = true;
                            break;
                        }
                    }
                    if (isWheelSlot) break;
                }

                if (!isWheelSlot) {
                    final int slot = i;
                    set(new GUIItem(slot) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer player) {
                            return ItemStackCreator.getStack(
                                    "§8Rolling...",
                                    getRandomGlass(),
                                    1
                            );
                        }
                    });
                }
            }

            updateItemStacks(getInventory(), getPlayer());

            // Animation complete
            if (tick >= totalTicks && !rewardsProcessed) {
                rewardsProcessed = true;
                // Process rewards and close after a short delay
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    processRewards(player);
                    player.closeInventory();
                }).delay(TaskSchedule.millis(1500)).schedule();
            }
        }).delay(TaskSchedule.millis(50)).repeat(TaskSchedule.millis(100)).schedule();
    }

    private void setRewardItem(int slot, SoulWellReward reward) {
        set(new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return reward.toItemStack();
            }
        });
    }

    private void processRewards(HypixelPlayer player) {
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        if (handler == null) return;

        for (SoulWellReward reward : finalRewards) {
            switch (reward.type) {
                case COINS_SMALL -> {
                    int amount = 100 + RANDOM.nextInt(401);
                    long currentCoins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                    handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).setValue(currentCoins + amount);
                    player.sendMessage("§6§lSOUL WELL! §7You received §6" + amount + " Coins§7!");
                }
                case COINS_MEDIUM -> {
                    int amount = 300 + RANDOM.nextInt(701);
                    long currentCoins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                    handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).setValue(currentCoins + amount);
                    player.sendMessage("§9§lSOUL WELL! §7You received §9" + amount + " Coins§7!");
                }
                case COINS_LARGE -> {
                    int amount = 500 + RANDOM.nextInt(1501);
                    long currentCoins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                    handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).setValue(currentCoins + amount);
                    player.sendMessage("§6§lSOUL WELL! §7You received §6" + amount + " Coins§7!");
                }
                case KIT -> {
                    if (reward.kit != null) {
                        DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                                SkywarsDataHandler.Data.UNLOCKS,
                                DatapointSkywarsUnlocks.class
                        ).getValue();
                        unlocks.unlockKit(reward.kit.getId());
                        player.sendMessage("§a§lSOUL WELL! §7You unlocked the §a" + reward.kit.getName() + " Kit§7!");
                    }
                }
                case PERK -> {
                    if (reward.perk != null) {
                        DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                                SkywarsDataHandler.Data.UNLOCKS,
                                DatapointSkywarsUnlocks.class
                        ).getValue();
                        unlocks.unlockPerk(reward.perk.getId());
                        player.sendMessage("§a§lSOUL WELL! §7You unlocked the §a" + reward.perk.getName() + " Perk§7!");
                    }
                }
            }
        }
    }

    private SoulWellReward generateRandomReward() {
        int roll = RANDOM.nextInt(100);

        if (roll < 30) {
            // 30% - Small coins
            return new SoulWellReward(RewardType.COINS_SMALL, null, null, "Small bag of coins");
        } else if (roll < 50) {
            // 20% - Medium coins
            return new SoulWellReward(RewardType.COINS_MEDIUM, null, null, "Medium bag of coins");
        } else if (roll < 60) {
            // 10% - Large coins
            return new SoulWellReward(RewardType.COINS_LARGE, null, null, "Large bag of coins");
        } else if (roll < 80) {
            // 20% - Kit (only Soul Well droppable kits)
            List<SkywarsKit> kits = SkywarsKitRegistry.getSoulWellKits();
            if (!kits.isEmpty()) {
                SkywarsKit kit = kits.get(RANDOM.nextInt(kits.size()));
                return new SoulWellReward(RewardType.KIT, kit, null, kit.getName() + " Kit");
            }
        } else {
            // 20% - Perk (only Soul Well droppable perks)
            List<SkywarsPerk> perks = SkywarsPerkRegistry.getSoulWellPerks();
            if (!perks.isEmpty()) {
                SkywarsPerk perk = perks.get(RANDOM.nextInt(perks.size()));
                return new SoulWellReward(RewardType.PERK, null, perk, perk.getName() + " Perk");
            }
        }

        // Fallback to coins
        return new SoulWellReward(RewardType.COINS_SMALL, null, null, "Small bag of coins");
    }

    private List<SoulWellReward> generateWheelItems() {
        List<SoulWellReward> items = new ArrayList<>();

        // Add a mix of items to the wheel
        for (int i = 0; i < 15; i++) {
            items.add(generateRandomReward());
        }

        return items;
    }

    private Material getRandomGlass() {
        return GLASS_COLORS[RANDOM.nextInt(GLASS_COLORS.length)];
    }

    private int[][] getWheelSlots(int count) {
        return switch (count) {
            case 1 -> WHEEL_SLOTS_1;
            case 2 -> WHEEL_SLOTS_2;
            case 3 -> WHEEL_SLOTS_3;
            case 4 -> WHEEL_SLOTS_4;
            default -> WHEEL_SLOTS_5;
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private enum RewardType {
        COINS_SMALL,
        COINS_MEDIUM,
        COINS_LARGE,
        KIT,
        PERK
    }

    private record SoulWellReward(RewardType type, SkywarsKit kit, SkywarsPerk perk, String displayName) {
        public ItemStack.Builder toItemStack() {
            return switch (type) {
                case COINS_SMALL -> ItemStackCreator.getStack("§aSmall bag of coins", Material.SUNFLOWER, 1);
                case COINS_MEDIUM -> ItemStackCreator.getStack("§9Medium bag of coins", Material.SUNFLOWER, 1);
                case COINS_LARGE -> ItemStackCreator.getStack("§6Large bag of coins", Material.SUNFLOWER, 1);
                case KIT -> {
                    if (kit != null) {
                        if (kit.hasCustomTexture()) {
                            yield ItemStackCreator.getStackHead("§a" + displayName, kit.getIconTexture(), 1);
                        }
                        yield ItemStackCreator.getStack("§a" + displayName, kit.getIconMaterial(), 1);
                    }
                    yield ItemStackCreator.getStack("§a" + displayName, Material.LEATHER_HELMET, 1);
                }
                case PERK -> {
                    if (perk != null) {
                        yield ItemStackCreator.getStack("§a" + displayName, perk.getIconMaterial(), 1);
                    }
                    yield ItemStackCreator.getStack("§a" + displayName, Material.OAK_PLANKS, 1);
                }
            };
        }
    }
}
