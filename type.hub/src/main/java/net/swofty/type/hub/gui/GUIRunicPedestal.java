package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.attribute.attributes.ItemAttributeRuneInfusedWith;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RefreshAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.RuneComponent;
import net.swofty.types.generic.item.components.RuneableComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GUIRunicPedestal extends SkyBlockAbstractInventory {
    private static final String STATE_HAS_LEFT_ITEM = "has_left_item";
    private static final String STATE_HAS_RIGHT_ITEM = "has_right_item";
    private static final String STATE_FUSING = "is_fusing";
    private static final String STATE_VALID_MERGE = "valid_merge";
    private static final String STATE_INVALID_MERGE = "invalid_merge";

    private static final int MAX_RUNE_LEVEL = 3;
    private static final int[] BOTTOM_SLOTS = {45, 46, 47, 48, 50, 51, 52, 53};
    private static final int[] LEFT_RUNIC_SLOTS = {10, 11, 12};
    private static final int[] RIGHT_RUNIC_SLOTS = {14, 15, 16};
    private static final int[] CONNECTOR_RUNIC_SLOTS = {22};

    private SkyBlockItem itemOnLeft = null;
    private SkyBlockItem itemOnRight = null;
    private SkyBlockItem outputItem = null;
    private CompletableFuture<Boolean> fusingAnimation = null;

    public GUIRunicPedestal() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Runic Pedestal")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        setGlassPanes(BOTTOM_SLOTS, Material.WHITE_STAINED_GLASS_PANE);
        setGlassPanes(LEFT_RUNIC_SLOTS, Material.WHITE_STAINED_GLASS_PANE);
        setGlassPanes(RIGHT_RUNIC_SLOTS, Material.WHITE_STAINED_GLASS_PANE);
        setGlassPanes(CONNECTOR_RUNIC_SLOTS, Material.WHITE_STAINED_GLASS_PANE);

        // Rune removal button
        attachItem(GUIItem.builder(44)
                .item(ItemStackCreator.getStack("§eRune Removal", Material.CAULDRON, 1,
                        "§7Sometimes, simplicity is most beautiful.").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRuneRemoval());
                    return true;
                })
                .build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        setupLeftSlot();
        setupRightSlot();
        setupDisplaySlots();
    }

    private void setupLeftSlot() {
        attachItem(GUIItem.builder(19)
                .item(() -> itemOnLeft != null ?
                        PlayerItemUpdater.playerUpdate(owner, itemOnLeft.getItemStack()).build() :
                        ItemStack.of(Material.AIR))
                .onClick((ctx, item) -> {
                    if (hasState(STATE_FUSING)) {
                        ctx.player().sendMessage("§cYou cannot remove the item on the left while fusing!");
                        return false;
                    }

                    if (hasState(STATE_HAS_LEFT_ITEM)) {
                        ctx.player().addAndUpdateItem(itemOnLeft);
                        itemOnLeft = null;
                        doAction(new RemoveStateAction(STATE_HAS_LEFT_ITEM));
                        updateMergeState();
                        return true;
                    }

                    if (!ctx.cursorItem().isAir()) {
                        itemOnLeft = new SkyBlockItem(ctx.cursorItem());
                        doAction(new AddStateAction(STATE_HAS_LEFT_ITEM));
                        updateMergeState();
                    }
                    return true;
                })
                .build());
    }

    private void setupRightSlot() {
        attachItem(GUIItem.builder(25)
                .item(() -> itemOnRight != null ?
                        PlayerItemUpdater.playerUpdate(owner, itemOnRight.getItemStack()).build() :
                        ItemStack.of(Material.AIR))
                .onClick((ctx, item) -> {
                    if (hasState(STATE_FUSING)) {
                        ctx.player().sendMessage("§cYou cannot remove the item on the right while fusing!");
                        return false;
                    }

                    if (hasState(STATE_HAS_RIGHT_ITEM)) {
                        ctx.player().addAndUpdateItem(itemOnRight);
                        itemOnRight = null;
                        doAction(new RemoveStateAction(STATE_HAS_RIGHT_ITEM));
                        updateMergeState();
                        return true;
                    }

                    if (!ctx.cursorItem().isAir()) {
                        itemOnRight = new SkyBlockItem(ctx.cursorItem());
                        doAction(new AddStateAction(STATE_HAS_RIGHT_ITEM));
                        updateMergeState();
                    }
                    return true;
                })
                .build());
    }

    private void setupDisplaySlots() {
        // Info display
        attachItem(GUIItem.builder(13)
                .item(ItemStackCreator.getStack("§aApply a Rune or Fuse Two Runes",
                        Material.END_PORTAL_FRAME, 1,
                        "§7Add the rune to your provided item",
                        "§7or provide two runes to attempt to",
                        "§7fuse them.").build())
                .requireAnyState(STATE_HAS_LEFT_ITEM, STATE_HAS_RIGHT_ITEM)
                .build());

        // Result display
        attachItem(GUIItem.builder(31)
                .item(() -> {
                    if (!hasState(STATE_HAS_LEFT_ITEM) || !hasState(STATE_HAS_RIGHT_ITEM)) {
                        return ItemStackCreator.getStack("§cRunic Pedestal", Material.BARRIER, 1,
                                "§7Place a target item in the left slot",
                                "§7and a sacrifice rune in the right slot",
                                "§7to add the two runes effect's to the item",
                                "§7or add two runes to attempt to fuse",
                                "§7them!").build();
                    }

                    RunicMerge merge = getValidMerge();
                    if (merge == null) {
                        return ItemStackCreator.getStack("§cError!", Material.BARRIER, 1,
                                "§7You cannot combine those items!").build();
                    }

                    SkyBlockItem result = merge.merge(itemOnLeft, itemOnRight);
                    java.util.List<String> lore = new ArrayList<>(result.getLore());
                    lore.add("§8§m-------------------");
                    lore.add("§aThis is the item you will get.");
                    lore.add("§aClick the §cPORTAL FRAME ABOVE §ato");
                    lore.add("§acombine.");

                    return ItemStackCreator.getStack(result.getDisplayName(),
                            result.getMaterial(), 1, lore.toArray(new String[0])).build();
                })
                .onClick((ctx, item) -> {
                    if (hasState(STATE_VALID_MERGE)) {
                        ctx.player().sendMessage("§cYou must click the Portal Frame above to combine the two items!");
                    }
                    return true;
                })
                .build());

        // Merge action
        attachItem(GUIItem.builder(13)
                .requireStates(new String[]{STATE_HAS_LEFT_ITEM, STATE_HAS_RIGHT_ITEM, STATE_VALID_MERGE})
                .item(() -> ItemStackCreator.getStack("§aCombine Items", Material.END_PORTAL_FRAME, 1,
                        "§7Combine the provided items.",
                        " ",
                        "§eClick to combine!").build())
                .onClick((ctx, item) -> {
                    if (hasState(STATE_FUSING)) {
                        ctx.player().sendMessage("§cYou cannot apply a rune while fusing!");
                        return false;
                    }

                    RunicMerge merge = getValidMerge();
                    if (merge == null) return false;

                    doAction(new AddStateAction(STATE_FUSING));
                    outputItem = merge.merge(itemOnLeft, itemOnRight);
                    itemOnLeft = null;
                    itemOnRight = null;

                    startFusingAnimation().thenAccept((hasDisconnected) -> {
                        if (hasDisconnected) return;

                        doAction(new RemoveStateAction(STATE_FUSING));
                        ctx.player().addAndUpdateItem(outputItem);
                        merge.onSuccess(ctx.player(), outputItem);
                        outputItem = null;
                        updateMergeState();
                    });

                    return true;
                })
                .build());
    }

    private void updateMergeState() {
        doAction(new RemoveStateAction(STATE_VALID_MERGE));
        doAction(new RemoveStateAction(STATE_INVALID_MERGE));

        if (itemOnLeft != null && itemOnRight != null) {
            RunicMerge merge = getValidMerge();
            if (merge != null) {
                doAction(new AddStateAction(STATE_VALID_MERGE));
            } else {
                doAction(new AddStateAction(STATE_INVALID_MERGE));
            }
        }

        updateGlassPanes();
        doAction(new RefreshAction());
    }

    private void updateGlassPanes() {
        if (hasState(STATE_FUSING)) {
            setGlassPanes(BOTTOM_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            setGlassPanes(LEFT_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            setGlassPanes(RIGHT_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            setGlassPanes(CONNECTOR_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
        } else if (hasState(STATE_VALID_MERGE)) {
            setGlassPanes(BOTTOM_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            setGlassPanes(CONNECTOR_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            setGlassPanes(LEFT_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            setGlassPanes(RIGHT_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
        } else {
            setGlassPanes(BOTTOM_SLOTS, Material.WHITE_STAINED_GLASS_PANE);
            setGlassPanes(LEFT_RUNIC_SLOTS, Material.WHITE_STAINED_GLASS_PANE);
            setGlassPanes(RIGHT_RUNIC_SLOTS, Material.WHITE_STAINED_GLASS_PANE);
            setGlassPanes(CONNECTOR_RUNIC_SLOTS, Material.WHITE_STAINED_GLASS_PANE);

            if (hasState(STATE_HAS_LEFT_ITEM)) {
                setGlassPanes(LEFT_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            }
            if (hasState(STATE_HAS_RIGHT_ITEM)) {
                setGlassPanes(RIGHT_RUNIC_SLOTS, Material.PURPLE_STAINED_GLASS_PANE);
            }
        }
    }

    private RunicMerge getValidMerge() {
        if (itemOnLeft == null || itemOnRight == null) return null;
        return RunicMerge.getMerges().stream()
                .filter(merge -> merge.isValidType(itemOnLeft, itemOnRight))
                .findFirst()
                .orElse(null);
    }

    private void setGlassPanes(int[] slots, Material material) {
        for (int slot : slots) {
            attachItem(GUIItem.builder(slot)
                    .item(ItemStackCreator.createNamedItemStack(material, "").build())
                    .build());
        }
    }

    public CompletableFuture<Boolean> startFusingAnimation() {
        fusingAnimation = new CompletableFuture<>();
        Thread.startVirtualThread(() -> {
            int duration = 1500;
            int interval = 30;
            int cycles = 5;

            int totalSteps = duration / interval;
            int stepsPerCycle = totalSteps / cycles;

            Material[] colors = {
                    Material.PURPLE_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.PINK_STAINED_GLASS_PANE
            };

            for (int currentStep = 0; currentStep < totalSteps; currentStep++) {
                int cycleIndex = currentStep / stepsPerCycle;
                double progress = (double) (currentStep % stepsPerCycle) / (stepsPerCycle - 1);

                Material color;
                if (cycleIndex % 2 == 0) {
                    int colorIndex = (int) Math.floor(progress * (colors.length - 1));
                    color = colors[colorIndex];
                } else {
                    int colorIndex = (int) Math.floor((1 - progress) * (colors.length - 1));
                    color = colors[colorIndex];
                }

                setGlassPanes(BOTTOM_SLOTS, color);
                setGlassPanes(CONNECTOR_RUNIC_SLOTS, color);
                setGlassPanes(LEFT_RUNIC_SLOTS, color);
                setGlassPanes(RIGHT_RUNIC_SLOTS, color);

                doAction(new RefreshAction());

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    fusingAnimation.complete(true);
                    return;
                }
            }

            fusingAnimation.complete(false);
        });
        return fusingAnimation;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        returnItemsToPlayer(player);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        returnItemsToPlayer(player);
    }

    private void returnItemsToPlayer(SkyBlockPlayer player) {
        if (itemOnLeft != null) player.addAndUpdateItem(itemOnLeft);
        if (itemOnRight != null) player.addAndUpdateItem(itemOnRight);
        if (outputItem != null) player.addAndUpdateItem(outputItem);

        if (fusingAnimation != null && !fusingAnimation.isDone()) {
            fusingAnimation.complete(true);
        }
    }

    public static abstract class RunicMerge {
        public abstract SkyBlockItem merge(SkyBlockItem itemOnLeft, SkyBlockItem itemOnRight);
        public abstract boolean isValidType(SkyBlockItem itemOnLeft, SkyBlockItem itemOnRight);
        public abstract void onSuccess(SkyBlockPlayer player, SkyBlockItem outputItem);

        public static List<RunicMerge> getMerges() {
            return List.of(new RuneRunicMerge(), new RuneWithItemRunicMerge());
        }
    }

    public static class RuneRunicMerge extends RunicMerge {
        @Override
        public SkyBlockItem merge(SkyBlockItem itemOnLeft, SkyBlockItem itemOnRight) {
            int runeLevelOnLeft = itemOnLeft.getAttributeHandler().getRuneLevel();
            int runeLevelOnRight = itemOnRight.getAttributeHandler().getRuneLevel();

            SkyBlockItem toReturn = itemOnLeft.clone();
            toReturn.getAttributeHandler().setRuneLevel(runeLevelOnLeft + runeLevelOnRight);
            return toReturn;
        }

        @Override
        public boolean isValidType(SkyBlockItem itemOnLeft, SkyBlockItem itemOnRight) {
            if (!(itemOnLeft.hasComponent(RuneComponent.class)) || !(itemOnRight.hasComponent(RuneComponent.class))) {
                return false;
            }
            String runeTypeOnLeft = itemOnLeft.getAttributeHandler().getTypeAsString();
            String runeTypeOnRight = itemOnRight.getAttributeHandler().getTypeAsString();
            if (!runeTypeOnLeft.equals(runeTypeOnRight))
                return false;

            int runeLevelOnLeft = itemOnLeft.getAttributeHandler().getRuneLevel();
            int runeLevelOnRight = itemOnRight.getAttributeHandler().getRuneLevel();

            return runeLevelOnLeft + runeLevelOnRight <= MAX_RUNE_LEVEL;
        }

        @Override
        public void onSuccess(SkyBlockPlayer player, SkyBlockItem outputItem) {
            int level = outputItem.getAttributeHandler().getRuneLevel();
            player.getSkills().increase(player, SkillCategories.RUNECRAFTING, 15D);
            sendSuccessMessage(player, "Combining Runes to Level " + level);
        }
    }

    public static class RuneWithItemRunicMerge extends RunicMerge {
        @Override
        public SkyBlockItem merge(SkyBlockItem itemOnLeft, SkyBlockItem itemOnRight) {
            SkyBlockItem runeItem = itemOnLeft.hasComponent(RuneComponent.class) ? itemOnLeft : itemOnRight;
            SkyBlockItem item = itemOnLeft.hasComponent(RuneComponent.class) ? itemOnRight : itemOnLeft;

            SkyBlockItem toReturn = item.clone();
            ItemAttributeRuneInfusedWith.RuneData runeData = toReturn.getAttributeHandler().getRuneData();
            runeData.setRuneType(runeItem.getAttributeHandler().getPotentialType());
            runeData.setLevel(runeItem.getAttributeHandler().getRuneLevel());
            toReturn.getAttributeHandler().setRuneData(runeData);
            return toReturn;
        }

        @Override
        public boolean isValidType(SkyBlockItem itemOnLeft, SkyBlockItem itemOnRight) {
            if (!(itemOnLeft.hasComponent(RuneComponent.class)) && !(itemOnLeft.hasComponent(RuneableComponent.class)))
                return false;
            if (!(itemOnRight.hasComponent(RuneComponent.class)) && !(itemOnRight.hasComponent(RuneableComponent.class)))
                return false;
            if (itemOnLeft.hasComponent(RuneComponent.class) && itemOnRight.hasComponent(RuneComponent.class))
                return false;
            if (itemOnLeft.hasComponent(RuneableComponent.class) && itemOnRight.hasComponent(RuneableComponent.class))
                return false;

            SkyBlockItem runableItem = itemOnLeft.hasComponent(RuneableComponent.class) ? itemOnLeft : itemOnRight;
            ItemAttributeRuneInfusedWith.RuneData runeData = runableItem.getAttributeHandler().getRuneData();

            return !runeData.hasRune();
        }

        @Override
        public void onSuccess(SkyBlockPlayer player, SkyBlockItem outputItem) {
            player.getSkills().increase(player, SkillCategories.RUNECRAFTING, 15D);

            ItemType appliedRune = outputItem.getAttributeHandler().getRuneData().getRuneType();
            Rarity rarity = appliedRune.rarity;

            sendSuccessMessage(player, "Applying " + StringUtility.toNormalCase(appliedRune.name()) +
                    " (" + rarity.getDisplay() + "§d)");
        }
    }

    private static void sendSuccessMessage(SkyBlockPlayer player, String action) {
        player.sendMessage("§d-§515 §dRunecrafting XP §7- §d" + action);
    }
}