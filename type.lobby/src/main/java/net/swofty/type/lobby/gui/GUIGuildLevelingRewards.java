package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.guild.GuildData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class GUIGuildLevelingRewards extends PaginatedView<GUIGuildLevelingRewards.LevelReward, GUIGuildLevelingRewards.LevelRewardsState> {

    private static final int[] REWARD_SLOTS = IntStream.range(0, 45).toArray();

    private static final List<RewardTemplate> TEMPLATES = buildTemplates();

    @Override
    public ViewConfiguration<LevelRewardsState> configuration() {
        return new ViewConfiguration<>("Guild Leveling Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return REWARD_SLOTS;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected void layoutCustom(ViewLayout<LevelRewardsState> layout, LevelRewardsState state, ViewContext ctx) {
        layout.slot(49, ItemStackCreator.getStack(
                "§aGo Back",
                Material.ARROW,
                1,
                "§7To Guild"
        ), (click, viewCtx) -> viewCtx.navigator().pop());
    }

    @Override
    protected ItemStack.Builder renderItem(LevelReward reward, int index, HypixelPlayer player) {
        Material mat = reward.unlocked() ? reward.material() : (reward.special() ? Material.CLAY_BALL : Material.GRAY_DYE);
        String nameColor = reward.unlocked() ? reward.nameColor() : "§7";
        String unlockText = reward.unlocked()
                ? "§eUnlocked at level " + reward.level()
                : "§eUnlocks at Level " + reward.level();

        List<String> lore = new ArrayList<>(List.of(reward.description()));
        lore.add("");
        lore.add(unlockText);

        return ItemStackCreator.getStack(nameColor + reward.name(), mat, 1, lore.toArray(String[]::new));
    }

    @Override
    protected void onItemClick(ClickContext<LevelRewardsState> click, ViewContext ctx, LevelReward item, int index) {
    }

    @Override
    protected boolean shouldFilterFromSearch(LevelRewardsState state, LevelReward item) {
        return false;
    }

    public static LevelRewardsState createState(GuildData guild) {
        int guildLevel = guild.getLevel();
        List<LevelReward> rewards = TEMPLATES.stream()
                .map(t -> new LevelReward(t.level, t.name, t.nameColor, t.material, t.description, t.special, guildLevel >= t.level))
                .toList();
        return new LevelRewardsState(rewards, 0, guild);
    }

    private static List<RewardTemplate> buildTemplates() {
        List<RewardTemplate> list = new ArrayList<>();
        int expPercent = 0;
        int coinPercent = 0;

        for (int level = 1; level <= 50; level++) {
            if (level % 3 == 0) {
                coinPercent++;
                list.add(new RewardTemplate(level, coinPercent + "% Double Coins", "§6", Material.GOLD_NUGGET,
                        new String[]{"§7Whenever a Guild Member plays", "§7a game, they have a §6" + coinPercent + "% §7chance",
                                "§7of getting Double Coins every", "§7time they gain coins."}, false));
            } else {
                expPercent++;
                list.add(new RewardTemplate(level, expPercent + "% Double Exp", "§3", Material.CYAN_DYE,
                        new String[]{"§7Whenever a Guild Member plays", "§7a game, they have a §6" + expPercent + "% §7chance",
                                "§7of getting Double Exp for the", "§7entire game."}, false));
            }

            switch (level) {
                case 5 -> list.add(new RewardTemplate(5, "Gray Guild Tag", "§7", Material.LIGHT_GRAY_WOOL,
                        new String[]{"§7Guild Members can have a Gray", "§7Guild Tag displayed next to", "§7their username in lobbies."}, true));
                case 10 -> list.add(new RewardTemplate(10, "Crits and Magic Particle Packs", "§6", Material.NETHER_STAR,
                        new String[]{"§7Guild Members have the ability", "§7to use the Crits and Magic", "§7Particle Packs in lobbies."}, true));
                case 15 -> {
                    list.add(new RewardTemplate(15, "Dark Aqua Guild Tag", "§3", Material.CYAN_WOOL,
                            new String[]{"§7Guild Members can have a §3Dark", "§3Aqua§7 Guild Tag displayed next", "§7to their username in lobbies."}, true));
                    list.add(new RewardTemplate(15, "Tier 1 Forum Icon", "§6", Material.BOOK,
                            new String[]{"§7All Guild Members receive the", "§7Tier 1 Forum Icon on the", "§7Hypixel Forums."}, true));
                }
                case 20 -> list.add(new RewardTemplate(20, "Tier 1 Guild Cloak", "§6", Material.ENCHANTING_TABLE,
                        new String[]{"§7Guild Members have the ability", "§7to use the Tier 1 Guild Cloak in", "§7lobbies."}, true));
                case 25 -> list.add(new RewardTemplate(25, "Dark Green Guild Tag", "§2", Material.GREEN_WOOL,
                        new String[]{"§7Guild Members can have a §2Dark", "§2Green§7 Guild Tag displayed next", "§7to their username in lobbies."}, true));
                case 30 -> list.add(new RewardTemplate(30, "Flame and Snow Particle Packs", "§6", Material.NETHER_STAR,
                        new String[]{"§7Guild Members have the ability", "§7to use the Flame and Snow", "§7Particle Packs in lobbies."}, true));
                case 35 -> list.add(new RewardTemplate(35, "Tier 2 Forum Icon", "§6", Material.BOOK,
                        new String[]{"§7All Guild Members receive the", "§7Tier 2 Forum Icon on the", "§7Hypixel Forums."}, true));
                case 40 -> list.add(new RewardTemplate(40, "Tier 2 Guild Cloak", "§6", Material.ENCHANTING_TABLE,
                        new String[]{"§7Guild Members have the ability", "§7to use the Tier 2 Guild Cloak in", "§7lobbies."}, true));
                case 45 -> list.add(new RewardTemplate(45, "Yellow Guild Tag", "§e", Material.YELLOW_WOOL,
                        new String[]{"§7Guild Members can have a §eYellow", "§7Guild Tag displayed next", "§7to their username in lobbies."}, true));
                case 50 -> list.add(new RewardTemplate(50, "Tier 3 Forum Icon", "§6", Material.BOOK,
                        new String[]{"§7All Guild Members receive the", "§7Tier 3 Forum Icon on the", "§7Hypixel Forums."}, true));
            }
        }
        return Collections.unmodifiableList(list);
    }

    private record RewardTemplate(int level, String name, String nameColor, Material material, String[] description, boolean special) { }

    public record LevelReward(int level, String name, String nameColor, Material material, String[] description, boolean special, boolean unlocked) { }

    public record LevelRewardsState(List<LevelReward> items, int page, GuildData guild) implements PaginatedState<LevelReward> {
        @Override
        public PaginatedState<LevelReward> withPage(int page) {
            return new LevelRewardsState(items, page, guild);
        }

        @Override
        public PaginatedState<LevelReward> withItems(List<LevelReward> items) {
            return new LevelRewardsState(items, page, guild);
        }
    }
}