package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.rewards;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.causes.LevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILevelEmblemRewards extends SkyBlockAbstractInventory {
    private static final String STATE_EMBLEM_UNLOCKED = "emblem_unlocked";
    private static final String STATE_EMBLEM_LOCKED = "emblem_locked";

    private static final int[] SLOTS = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    public GUILevelEmblemRewards() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Emblem Rewards")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        setupCloseButton();
        setupBackButton();
        setupPrefixEmblemsButton();
        setupInfoDisplay(player);
        setupEmblemSlots(player);
    }

    private void setupCloseButton() {
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());
    }

    private void setupBackButton() {
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Emblem Rewards").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelEmblemRewards());
                    return true;
                })
                .build());
    }

    private void setupPrefixEmblemsButton() {
        attachItem(GUIItem.builder(50)
                .item(ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
                        "§7Add some spice by having an emblem",
                        "§7next to your name in chat and in tab!",
                        " ",
                        "§7Emblems are unlocked through various",
                        "§7activities such as leveling up",
                        "§7or completing achievements!",
                        " ",
                        "§7Emblems also show important data",
                        "§7associated with them in chat!",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIEmblems());
                    return true;
                })
                .build());
    }

    private void setupInfoDisplay(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(14)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Emblems to show next to your name");
                    lore.add("§7that signify special achievements.");
                    lore.add(" ");
                    lore.add("§7Next Reward:");

                    List<SkyBlockEmblems.SkyBlockEmblem> levelEmblems = SkyBlockEmblems.getEmblemsWithLevelCause();
                    SkyBlockEmblems.SkyBlockEmblem nextEmblem = null;
                    for (SkyBlockEmblems.SkyBlockEmblem emblem : levelEmblems) {
                        if (player.getSkyBlockExperience().hasExperienceFor(emblem.cause())) continue;
                        nextEmblem = emblem;
                        break;
                    }

                    if (nextEmblem == null) {
                        lore.add("§cNo more rewards!");
                    } else {
                        lore.add("§f" + nextEmblem.displayName() + " " + nextEmblem.emblem());
                        lore.add("§8at Level " + ((LevelCause) nextEmblem.cause()).getLevel());
                    }

                    lore.add(" ");
                    lore.addAll(GUILevelRewards.getAsDisplay(
                            player.getSkyBlockExperience().getOfType(LevelCause.class).size(),
                            levelEmblems.size()
                    ));

                    return ItemStackCreator.getStack("§aEmblem Rewards",
                            Material.NAME_TAG, 1, lore).build();
                })
                .build());
    }

    private void setupEmblemSlots(SkyBlockPlayer player) {
        int index = 0;
        for (SkyBlockEmblems.SkyBlockEmblem emblem : SkyBlockEmblems.getEmblemsWithLevelCause()) {
            int slot = SLOTS[index];
            boolean unlocked = player.getSkyBlockExperience().hasExperienceFor(emblem.cause());

            if (unlocked) {
                doAction(new AddStateAction(STATE_EMBLEM_UNLOCKED + "_" + index));
            } else {
                doAction(new AddStateAction(STATE_EMBLEM_LOCKED + "_" + index));
            }

            attachItem(GUIItem.builder(slot)
                    .item(() -> ItemStackCreator.getStack(emblem.displayName() + " " + emblem.emblem(),
                            Material.NAME_TAG, 1,
                            "§8Level " + ((LevelCause) emblem.cause()).getLevel(),
                            " ",
                            "§7Preview: " + player.getFullDisplayName(emblem),
                            " ",
                            (unlocked ? "§aYou have unlocked this reward!" : "§7Levels left to unlock: §3" +
                                    (((LevelCause) emblem.cause()).getLevel() - player.getSkyBlockExperience().getLevel().asInt()))).build())
                    .requireState(unlocked ? STATE_EMBLEM_UNLOCKED + "_" + index : STATE_EMBLEM_LOCKED + "_" + index)
                    .build());
            index++;
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}