package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.skills;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointHOTM;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointLoadouts;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUITreeSlots;
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager;
import net.swofty.type.skyblockgeneric.skilltree.HotmService;
import net.swofty.type.skyblockgeneric.skilltree.SkillTreeDefinition;
import net.swofty.type.skyblockgeneric.skilltree.SkillTreeType;
import net.swofty.type.skyblockgeneric.skilltree.TreeNodeDefinition;
import net.swofty.type.skyblockgeneric.skilltree.TreePowder;
import net.swofty.type.skyblockgeneric.skilltree.TreeTierDefinition;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIHeartOfTheMountain implements StatefulView<GUIHeartOfTheMountain.State> {
    private static final String RESET_TEXTURE = "7c8489c03357d6d6abd9f4a3bd8824eb0f2841685ade95ff987ebe15b2e65fad";
    private static final String RNG_METER_DROP = "Divan's Alloy";

    private final boolean commandEntry;

    public GUIHeartOfTheMountain() {
        this(false);
    }

    public GUIHeartOfTheMountain(boolean commandEntry) {
        this.commandEntry = commandEntry;
    }

    @Override
    public State initialState() {
        return new State(-1);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Heart of the Mountain", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 45);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkillTreeDefinition definition = HotmService.definition();
        int topY = effectiveScroll(state, player, definition);

        for (int row = 0; row < 5; row++) {
            int y = topY + row;
            if (y > definition.maxY()) break;
            int tier = definition.tierForY(y);
            layout.slot(row * 9, tierItem(player, definition.tier(tier), tier));
            for (int x = 0; x < 7; x++) {
                TreeNodeDefinition node = definition.nodeAt(x, y);
                if (node == null) continue;
                int slot = row * 9 + x + 1;
                layout.slot(slot,
                        (s, c) -> nodeItem((SkyBlockPlayer) c.player(), node),
                        (click, c) -> handleNodeClick(node, click.click(), c));
            }
        }

        layout.slot(8, (s, c) -> scrollItem(s, (SkyBlockPlayer) c.player(), definition, true),
                (click, c) -> scroll(c, definition, true, click.click()));
        layout.slot(53, (s, c) -> scrollItem(s, (SkyBlockPlayer) c.player(), definition, false),
                (click, c) -> scroll(c, definition, false, click.click()));

        layout.slot(47, (s, c) -> treeSlotItem((SkyBlockPlayer) c.player()),
                (_, c) -> c.push(new GUITreeSlots(SkillTreeType.HOTM)));
        layoutBack(layout, ctx);
        layout.slot(49, (s, c) -> informationItem((SkyBlockPlayer) c.player()));
        layout.slot(50, crystalsItem());
        layout.slot(51, rngMeterItem(), (_, c) -> c.push(new GUIHotmRngMeter()));
        layout.slot(52, resetItem(), (_, c) -> reset((SkyBlockPlayer) c.player(), c));
    }

    private void layoutBack(ViewLayout<State> layout, ViewContext ctx) {
        if (commandEntry) {
            layout.slot(48, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Mining Skill"),
                    (_, c) -> c.replace(new GUISkillCategory(net.swofty.type.skyblockgeneric.skill.SkillCategories.MINING, 0)));
            return;
        }
        if (!Components.back(layout, 48, ctx)) Components.close(layout, 48);
    }

    private void handleNodeClick(TreeNodeDefinition node, Click click, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        if (click instanceof Click.Right || click instanceof Click.RightShift) {
            if (HotmService.toggleNode(player, node)) ctx.session(State.class).refresh();
            return;
        }
        if (!(click instanceof Click.Left) && !(click instanceof Click.LeftShift)) return;

        int current = HotmService.level(player, node);
        if (node.ability() && current >= node.maxLevel()) {
            if (HotmService.toggleAbility(player, node)) ctx.session(State.class).refresh();
            return;
        }
        int amount = click instanceof Click.LeftShift ? 10 : 1;
        if (HotmService.upgrade(player, node, amount) > 0) ctx.session(State.class).refresh();
    }

    private void scroll(ViewContext ctx, SkillTreeDefinition definition, boolean up, Click click) {
        State state = ctx.session(State.class).state();
        int current = effectiveScroll(state, (SkyBlockPlayer) ctx.player(), definition);
        boolean right = click instanceof Click.Right || click instanceof Click.RightShift;
        int next;
        if (right) {
            next = up ? 0 : definition.maxY();
        } else if (click instanceof Click.Left || click instanceof Click.LeftShift) {
            next = current + (up ? -1 : 1);
        } else {
            return;
        }
        ctx.session(State.class).update(_ -> new State(definition.clampScroll(next)));
    }

    private static int effectiveScroll(State state, SkyBlockPlayer player, SkillTreeDefinition definition) {
        if (state.topY() >= 0) return definition.clampScroll(state.topY());
        return definition.clampScroll(10 - HotmService.data(player).getTier());
    }

    private net.minestom.server.item.ItemStack.Builder tierItem(SkyBlockPlayer player, TreeTierDefinition tier, int tierNumber) {
        boolean unlocked = HotmService.data(player).getTier() >= tierNumber;
        List<String> lore = new ArrayList<>();
        if (unlocked) {
            lore.add("§7You have unlocked this tier. All");
            lore.add("§7perks and abilities on this tier are");
            lore.add("§7available for unlocking with §5Token of");
            lore.add("§5the Mountain§7.");
            lore.add("");
            lore.add("§7Rewards");
            for (String reward : tier.rewards()) lore.add("§8+§f" + reward);
            lore.add("");
            lore.add("§a§lUNLOCKED");
        } else {
            lore.add("§7Reach §5Heart of the Mountain " + tierNumber + "§7");
            lore.add("§7to unlock this tier.");
            lore.add("");
            lore.add("§c§lLOCKED");
        }
        return ItemStackCreator.getStack((unlocked ? "§a" : "§c") + "Tier " + tierNumber,
                unlocked ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, 1, lore);
    }

    private net.minestom.server.item.ItemStack.Builder nodeItem(SkyBlockPlayer player, TreeNodeDefinition node) {
        DatapointHOTM.PlayerHOTMData data = HotmService.data(player);
        int current = HotmService.level(player, node);
        int displayLevel = Math.max(1, current);
        boolean available = HotmService.available(player, node);
        boolean selected = HotmService.isSelected(player, node);
        boolean enabled = HotmService.isEnabled(player, node);
        String titleColor = current >= node.maxLevel() || selected ? "§a" : current > 0 || available ? "§e" : "§c";

        List<String> lore = new ArrayList<>();
        if (!node.ability()) lore.add("§7Level " + displayLevel + "/" + node.maxLevel());
        lore.addAll(node.renderLore(displayLevel, data.getTier(), HotmService.level(player, HotmService.node("core_of_the_mountain"))));

        if (current > 0 && current < node.maxLevel()) {
            lore.add("");
            lore.add("§a§l=====[ UPGRADE ]=====");
            if (!node.ability()) lore.add("§7Level " + (current + 1) + "/" + node.maxLevel());
            lore.add("");
            lore.addAll(node.renderLore(current + 1, data.getTier(), HotmService.level(player, HotmService.node("core_of_the_mountain"))));
        }

        if (current < node.maxLevel()) {
            lore.add("");
            lore.add("§7Cost");
            if (current == 0) {
                lore.add("§5§l1 §5Token of the Mountain");
            } else {
                TreePowder powder = node.powder(current);
                lore.add(powder.color() + StringUtility.commaify(node.cost(current)) + " " + powder.displayName());
            }
            lore.add("");
            if (!available) {
                for (String requirement : HotmService.missingRequirements(player, node)) {
                    lore.add("§cRequires " + requirement);
                }
            } else if (!HotmService.canAffordNextLevel(player, node)) {
                if (current == 0) {
                    lore.add("§cYou don't have enough Token of the Mountain!");
                } else {
                    lore.add("§cYou don't have enough " + node.powder(current).displayName() + "!");
                }
            } else if (current == 0) {
                lore.add("§eClick to unlock!");
            } else {
                lore.add(enabled ? "§a§lENABLED" : "§c§lDISABLED");
                lore.add("");
                lore.add(enabled ? "§eRight-click to §cdisable§e!" : "§eRight-click to §aenable§e!");
                lore.add("§eLeft-click to upgrade!");
                lore.add("§eShift Left-click to upgrade 10 levels!");
            }
        } else if (node.ability()) {
            lore.add("");
            lore.add(selected ? "§a§lSELECTED" : "§eClick to select!");
            if (selected) {
                lore.add("");
                lore.add("§eRight-click to §cdisable§e!");
            }
        } else {
            lore.add("");
            lore.add(enabled ? "§a§lENABLED" : "§c§lDISABLED");
            lore.add("");
            lore.add(enabled ? "§eRight-click to §cdisable§e!" : "§eRight-click to §aenable§e!");
        }

        return ItemStackCreator.getStack(titleColor + node.name(), node.material(current), 1, lore);
    }

    private net.minestom.server.item.ItemStack.Builder scrollItem(State state, SkyBlockPlayer player, SkillTreeDefinition definition, boolean up) {
        int current = effectiveScroll(state, player, definition);
        if ((up && current == 0) || (!up && current == definition.maxY())) {
            return Components.asFiller(Material.BLACK_STAINED_GLASS_PANE);
        }
        String action = up ? "up" : "down";
        String destination = up ? "top tier" : "bottom tier";
        return ItemStackCreator.getStack("§aScroll " + (up ? "Up" : "Down"), Material.ARROW, 1,
                "§eLeft-click §7to scroll " + action + "!", "", "§eRight-click §7to go to the " + destination + "!");
    }

    private net.minestom.server.item.ItemStack.Builder treeSlotItem(SkyBlockPlayer player) {
        DatapointLoadouts.LoadoutsData data = LoadoutManager.data(player);
        int active = data.getActiveHotmSlot();
        return ItemStackCreator.getStack("§aHeart of the Mountain Slot", Material.CHEST, 1,
                "§7Quickly swap between saved trees.", "", "§7Current: §a" + data.getHotmNames()[active], "",
                "§cSwapping trees has a 10m cooldown!", "", "§eClick to view!");
    }

    private net.minestom.server.item.ItemStack.Builder informationItem(SkyBlockPlayer player) {
        DatapointHOTM.PlayerHOTMData data = HotmService.data(player);
        List<String> lore = new ArrayList<>();
        lore.add("§7Token of the Mountain: §5" + data.getAvailableTokens());
        lore.add("");
        lore.add("§8Use §5Token of the Mountain §8to unlock");
        lore.add("§8perks and abilities above!");
        lore.add("");
        lore.add("§7Mithril Powder: §2" + StringUtility.commaify(data.getMithrilPowder()));
        lore.add("  §8(+§2more powder§8)");
        lore.add("§7Gemstone Powder: §d" + StringUtility.commaify(data.getGemstonePowder()));
        lore.add("§7Glacite Powder: §b" + StringUtility.commaify(data.getGlacitePowder()));
        lore.add("");
        lore.add("§7Obtain §2Mithril Powder §7by mining and");
        lore.add("§7taking part in events in the §2Dwarven Mines§7.");
        lore.add("§7Obtain §dGemstone Powder §7by mining");
        lore.add("§7Gemstones and opening Treasure Chests in the §5Crystal Hollows§7.");
        lore.add("§7Obtain §bGlacite Powder §7by mining Glacite");
        lore.add("§7and looting Frozen Corpses in the §bGlacite Tunnels§7.");
        lore.add("");
        lore.add("§8Increase your chance to gain extra");
        lore.add("§8Powder by unlocking perks, equipping");
        lore.add("§8the §2Mithril Golem Pet§8, and more!");
        return ItemStackCreator.getStackHead("§5Heart of the Mountain",
                HotmService.definition().headTexture(), 1, lore);
    }

    private net.minestom.server.item.ItemStack.Builder crystalsItem() {
        return ItemStackCreator.getStack("§5Crystal Hollows Crystals", Material.PAPER, 1,
                "§8Crystals are used to forge Gems", "§8into §dPerfect §8Gems. They can be", "§8found hidden within the §5Crystal", "§8Hollows§8.", "", "§dYour Crystal Nucleus", "  §aJade §c✖ Not Found", "  §6Amber §c✖ Not Found", "  §5Amethyst §c✖ Not Found", "  §bSapphire §c✖ Not Found", "  §eTopaz §c✖ Not Found", "", "§dYour Other Crystals", "  §cRuby §a✔ Found", "  §fOpal §c✖ Not Found", "  §9Aquamarine §c✖ Not Found", "  §2Peridot §c✖ Not Found", "  §8Onyx §c✖ Not Found", "  §4Citrine §c✖ Not Found");
    }

    private net.minestom.server.item.ItemStack.Builder rngMeterItem() {
        return ItemStackCreator.getStack("§dCrystal Nucleus RNG Meter", Material.PAPER, 1,
                "§7Your §dCrystal Nucleus RNG Meter §7fills", "§7with §91,000 Nucleus XP §7every time you", "§7complete the §dCrystal Nucleus§7!", "", "§7Selected Drop", "§6" + RNG_METER_DROP, "", "§7Progress: §d1.1%", "§d§m                         §f  §d11,000§5/§d1M", "", "§eClick to view!");
    }

    private net.minestom.server.item.ItemStack.Builder resetItem() {
        return ItemStackCreator.getStackHead("§cReset Heart of the Mountain", RESET_TEXTURE, 1,
                "§7Resets the Perks and Abilities of", "§7your §5Heart of the Mountain§7, locking", "§7them and resetting their levels.", "", "§cWARNING: This is permanent.", "§cYou can not go back after resetting!");
    }

    private void reset(SkyBlockPlayer player, ViewContext ctx) {
        if (HotmService.resetActiveTree(player) > 0) {
            player.sendMessage("§aYour Heart of the Mountain perks have been reset.");
            ctx.session(State.class).refresh();
        }
    }

    public record State(int topY) {
    }
}
