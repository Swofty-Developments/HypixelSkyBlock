package net.swofty.type.lobby.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildMember;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.guild.GuildManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GUIGuild implements View<GUIGuild.GuildState> {

    @Override
    public ViewConfiguration<GuildState> configuration() {
        return new ViewConfiguration<>("Guild", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<GuildState> layout, GuildState state, ViewContext ctx) {
        if (state.guild() == null) {
            layoutNoGuild(layout, ctx);
        } else {
            layoutWithGuild(layout, state.guild(), ctx);
        }
    }

    private void layoutNoGuild(ViewLayout<GuildState> layout, ViewContext ctx) {
        HypixelPlayer player = ctx.player();
        int level = player.getExperienceHandler().getLevel();
        int achievementPoints = player.getAchievementHandler().getTotalPoints();

        layout.slot(2, ItemStackCreator.getStackHead(
                player.getFullDisplayName(),
                player.getSkin(),
                1,
                "§7Hypixel Level: §6" + level,
                "§7Achievement Points: §e" + StringUtility.commaify(achievementPoints),
                "§7Guild: §bNone"
        ));
        layout.slot(3, ItemStackCreator.getStackHead(
                "§aFriends",
                "e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84",
                1,
                "§7View your Hypixel friends' profiles,",
                "§7and interact with your online friends!"
        ));
        layout.slot(4, ItemStackCreator.getStackHead(
                "§aParty",
                "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                1,
                "§7Create a party and join up with",
                "§7other players to play games",
                "§7together!"
        ));
        layout.slot(5, ItemStackCreator.getStackHead(
                "§aGuild",
                "fe8b59f8cce510809427c3843cf575fae8fe6a8b7d1560dd46958d148563815",
                1,
                "§7Form a guild with other Hypixel",
                "§7players to conquer game modes and",
                "§7work towards common Hypixel",
                "§7rewards."
        ));
        layout.slot(6, ItemStackCreator.getStackHead(
                "§aRecent Players",
                "9993a356809532d696841a37a0549b81b159b79a7b2919cff4e5abdfea83d66",
                1,
                "§7View players you have played recent",
                "§7games with."
        ));

        if (canCreateGuild(player)) {
            layout.slot(29, ItemStackCreator.getStack(
                    "§aCreate Guild",
                    Material.OAK_SIGN,
                    1,
                    "§7Create a guild with your own tag,",
                    "§7settings and progression.",
                    "",
                    "§eClick to create!"
            ), (click, viewCtx) -> new HypixelSignGUI(viewCtx.player())
                    .open(new String[]{"Guild Name", "Enter guild name"})
                    .thenAccept(name -> {
                        if (name == null || name.isBlank()) {
                            return;
                        }
                        GuildManager.createGuild(viewCtx.player(), name.trim());
                    }));
        } else {
            layout.slot(29, ItemStackCreator.getStack(
                    "§cCreate Guild",
                    Material.OAK_SIGN,
                    1,
                    "§7Only players with §aVIP§6+§7 or higher can",
                    "§7create guilds, but anybody can join",
                    "§7them."
            ));
        }

        layout.slot(31, ItemStackCreator.getStack(
                "§aGuild Finder",
                Material.PAPER,
                1,
                "§7Find a Guild you can join based on",
                "§7your favorite games.",
                "",
                "§eClick to browse!"
        ));
        layout.slot(33, ItemStackCreator.getStack(
                "§aSearch Guilds",
                Material.BOOK,
                1,
                "§7Click here to search guilds you can",
                "§7join on the Hypixel Network website!"
        ), (_, context) -> context.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                .clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock"))));
    }

    private void layoutWithGuild(ViewLayout<GuildState> layout, GuildData guild, ViewContext ctx) {
        HypixelPlayer player = ctx.player();
        NumberFormat nf = NumberFormat.getInstance();
        int level = player.getExperienceHandler().getLevel();
        int achievementPoints = player.getAchievementHandler().getTotalPoints();

        layout.slot(2, ItemStackCreator.getStackHead(
                player.getFullDisplayName(),
                player.getSkin(),
                1,
                "§7Hypixel Level: §6" + level,
                "§7Achievement Points: §e" + StringUtility.commaify(achievementPoints),
                "§7Guild: §b" + guild.getName()
        ));
        layout.slot(3, ItemStackCreator.getStackHead(
                "§aFriends",
                "e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84",
                1,
                "§7View your Hypixel friends' profiles,",
                "§7and interact with your online friends!"
        ));
        layout.slot(4, ItemStackCreator.getStackHead(
                "§aParty",
                "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                1,
                "§7Create a party and join up with",
                "§7other players to play games",
                "§7together!"
        ));
        layout.slot(5, ItemStackCreator.getStackHead(
                "§aGuild",
                "fe8b59f8cce510809427c3843cf575fae8fe6a8b7d1560dd46958d148563815",
                1,
                "§7Form a guild with other Hypixel",
                "§7players to conquer game modes and",
                "§7work towards common Hypixel",
                "§7rewards."
        ));
        layout.slot(6, ItemStackCreator.getStackHead(
                "§aRecent Players",
                "9993a356809532d696841a37a0549b81b159b79a7b2919cff4e5abdfea83d66",
                1,
                "§7View players you have played recent",
                "§7games with."
        ));

        GuildMember self = guild.getMember(player.getUuid());
        String rankName = self != null ? self.getRankName() : "Unknown";

        layout.slot(18, ItemStackCreator.getStack(
                "§aInvite Player",
                Material.WRITABLE_BOOK,
                1,
                "§7Click here to invite a player to your",
                "§7Guild."
        ), (click, viewCtx) -> new HypixelSignGUI(viewCtx.player())
                .open(new String[]{"Invite Player", "Enter username"})
                .thenAccept(name -> {
                    if (name == null || name.isBlank()) {
                        return;
                    }
                    GuildManager.invitePlayer(viewCtx.player(), name.trim());
                }));

        layout.slot(19, ItemStackCreator.getStack(
                "§aGuild Information",
                Material.PAINTING,
                1,
                "§7Name: §6" + guild.getName(),
                "§7Rank: §6" + rankName,
                "§7Daily Exp: §6" + nf.format(0),
                "§7Members: §6" + guild.getMembers().size() + "§b/§6" + GuildData.MAX_MEMBERS
        ));

        layout.slot(20, ItemStackCreator.getStack(
                "§aGuild Settings",
                Material.COMPARATOR,
                1,
                "§7Edit settings such as your tag,",
                "§7permissions and guild finder options",
                "",
                "§eClick to configure!"
        ), (click, viewCtx) -> viewCtx.push(new GUIGuildSettings(), new GUIGuildSettings.GuildSettingsState(guild)));

        layout.slot(21, ItemStackCreator.getStack(
                "§aWeekly Guild Quest",
                Material.ENCHANTED_BOOK,
                1,
                "§eTo complete the quest, Guild Members",
                "§eneed to complete Challenges in any",
                "§egame.",
                "§7Tier 1: §60§7/25",
                "§7Tier 2: §60§7/100",
                "§7Tier 3: §60§7/500",
                "§7Tier 4: §60§7/1500",
                "",
                "§7Reward: §250,000 Guild Experience",
                "§eResets in: 0 hours, 0 minutes"
        ));

        long expIntoCurrentLevel = getExpIntoCurrentLevel(guild);
        long expNeededForNext = guild.getGexpForLevel(guild.getLevel() + 1);
        long expRemaining = Math.max(0, expNeededForNext - expIntoCurrentLevel);
        double levelProgress = expNeededForNext <= 0 ? 1.0 : Math.min(1.0, (double) expIntoCurrentLevel / (double) expNeededForNext);
        int progressPercent = (int) Math.round(levelProgress * 100.0);

        layout.slot(22, ItemStackCreator.getStack(
                "§aGuild Leveling",
                Material.BREWING_STAND,
                1,
                "§7Guild Level: §6" + guild.getLevel(),
                "§6" + guild.getLevel() + " " + createProgressBar(levelProgress, 40) + " §6" + (guild.getLevel() + 1),
                "§7Exp until next level: §6" + nf.format(expRemaining) + " §7(§6" + progressPercent + "%§7)",
                "",
                "§7Today's exp: §6" + nf.format(0),
                "§7The guild is earning exp at §6100%§7 rate!",
                "",
                "§6Today's exp < 200,000 → 100%",
                "§7Today's exp >= 200,000 → 10%",
                "§7Today's exp >= 250,000 → 3%",
                "",
                "§eClick to view leveling rewards!"
        ), (click, viewCtx) -> viewCtx.push(new GUIGuildLevelingRewards(), GUIGuildLevelingRewards.createState(guild)));

        layout.slot(23, ItemStackCreator.getStack(
                "§aGuild Achievements",
                Material.DIAMOND,
                1,
                "§7Achievements completed: §e" + countCompletedAchievements(guild) + "§7/26",
                "",
                "§eClick to view!"
        ), (click, viewCtx) -> viewCtx.push(new GUIGuildAchievements(), new GUIGuildAchievements.GuildAchievementsState(guild)));

        layout.slot(24, ItemStackCreator.getStackHead(
                "§aGuild Discord",
                "7873c12bffb5251a0b88d5ae75c7247cb39a75ff1a81cbe4c8a39b311ddeda",
                1,
                "§7Your Guild has a Discord",
                "§7server that Guild Members can",
                "§7join.",
                "",
                "§eClick to view Invite Link",
                "§eRight-click to modify"
        ), (click, viewCtx) -> {
            if (click.click() instanceof Click.Right) {
                new HypixelSignGUI(viewCtx.player())
                        .open(new String[]{"Discord Link", "Paste invite URL"})
                        .thenAccept(link -> {
                            if (link == null || link.isBlank()) {
                                return;
                            }
                            GuildManager.changeSetting(viewCtx.player(), "discord", link.trim());
                        });
                return;
            }

            String discordLink = guild.getDiscordLink();
            if (discordLink == null || discordLink.isBlank()) {
                viewCtx.player().sendMessage("§cYour guild does not have a Discord link set.");
                return;
            }

            viewCtx.player().sendMessage(Component.text("§eClick here to open your guild Discord invite")
                    .clickEvent(ClickEvent.openUrl(discordLink)));
        });

        layout.slot(25, ItemStackCreator.getStack(
                "§aGuild Finder",
                Material.PAPER,
                1,
                "§7Find a Guild you can join based on",
                "§7your favorite games.",
                "",
                "§eClick to browse!"
        ));

        layout.slot(33, ItemStackCreator.getStack(
                "§aChange sort",
                Material.HOPPER,
                1,
                "§7Current sort: §bLast Online",
                "§7Sorting order: §bNormal",
                "",
                "§bLast Online§7: Sorts by who was",
                "§7most recently online",
                "§bGuild Rank§7: Shows highest Guild",
                "§7Rank first",
                "§bVeterancy§7: How long they've",
                "§7been in the guild",
                "§bAlphabetical§7: Show everyone",
                "§7listed from A-Z",
                "§bAP§7: Sort by Achievement Points",
                "§bLevel§7: Sort by Hypixel Level",
                "",
                "§eLEFT CLICK§7 to change between",
                "§7all the available sorting options.",
                "",
                "§eRIGHT CLICK§7 to reverse the",
                "§7current order!"
        ));

        layout.slot(34, ItemStackCreator.getStack(
                "§aSearch Players",
                Material.OAK_SIGN,
                1
        ), (click, viewCtx) -> viewCtx.push(new GUIGuildMembers(), GUIGuildMembers.createState(guild)));

        layout.slot(35, ItemStackCreator.getStack(
                "§aNext Page",
                Material.ARROW,
                1,
                "§eLEFT CLICK§7 to go to the next",
                "§7page",
                "§eRIGHT CLICK§7 to go to the last",
                "§7page",
                "§7Page 1/" + Math.max(1, (int) Math.ceil(guild.getMembers().size() / 18.0))
        ), (click, viewCtx) -> viewCtx.push(new GUIGuildMembers(), GUIGuildMembers.createState(guild)));

        int[] previewSlots = {36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        List<GuildMember> members = guild.getMembers();
        for (int i = 0; i < previewSlots.length && i < members.size(); i++) {
            layout.slot(previewSlots[i], buildMemberPreview(members.get(i)));
        }
    }

    private boolean canCreateGuild(HypixelPlayer player) {
        return player.getRank().isEqualOrHigherThan(Rank.VIP_PLUS);
    }

    private ItemStack.Builder buildMemberPreview(GuildMember member) {
        UUID uuid = member.getUuid();
        String displayName = HypixelPlayer.getDisplayName(uuid);
        String memberSince = formatDuration(System.currentTimeMillis() - member.getJoinedAt());

        HypixelPlayer loadedPlayer = HypixelGenericLoader.getLoadedPlayers().stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);

        String[] lore;
        PlayerSkin skin;

        if (loadedPlayer != null) {
            lore = new String[]{
                    "§7Hypixel Level: §6" + loadedPlayer.getExperienceHandler().getLevel(),
                    "§7Achievement Points: §e" + StringUtility.commaify(loadedPlayer.getAchievementHandler().getTotalPoints()),
                    "§7Guild Rank: §b" + member.getRankName(),
                    "§7Member since: §b" + memberSince,
                    "",
                    "§7Online Status: §bOnline"
            };
            skin = loadedPlayer.getSkin();
        } else {
            lore = new String[]{
                    "§7Hypixel Level: §6?",
                    "§7Achievement Points: §e?",
                    "§7Guild Rank: §b" + member.getRankName(),
                    "§7Member since: §b" + memberSince,
                    "",
                    "§7Last Online: §bUnknown"
            };
            skin = resolveOfflineSkin(uuid);
        }

        if (skin != null) {
            return ItemStackCreator.getStackHead(displayName, skin, 1, lore);
        }
        return ItemStackCreator.getStack(displayName, Material.PLAYER_HEAD, 1, lore);
    }

    private PlayerSkin resolveOfflineSkin(UUID uuid) {
        try {
            HypixelDataHandler dataHandler = HypixelDataHandler.getOfOfflinePlayer(uuid);
            String texture = dataHandler.get(HypixelDataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
            String signature = dataHandler.get(HypixelDataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();
            if (texture == null || signature == null || texture.equals("null") || signature.equals("null")) {
                return null;
            }
            return new PlayerSkin(texture, signature);
        } catch (Exception ignored) {
            return null;
        }
    }

    private long getExpIntoCurrentLevel(GuildData guild) {
        long accumulated = 0;
        for (int level = 1; level <= guild.getLevel(); level++) {
            accumulated += guild.getGexpForLevel(level);
        }
        return Math.max(0, guild.getTotalGexp() - accumulated);
    }

    private int countCompletedAchievements(GuildData guild) {
        int completed = 0;
        int level = guild.getLevel();
        long gexp = guild.getTotalGexp();
        int memberCount = guild.getMembers().size();

        int[] prestigeTiers = {20, 40, 60, 80, 100};
        int[] expKingTiers = {50000, 100000, 150000, 200000, 250000, 275000, 300000};
        int[] familyTiers = {5, 15, 30, 40, 50, 60, 70};

        for (int tier : prestigeTiers) {
            if (level >= tier) completed++;
        }
        for (int tier : expKingTiers) {
            if (gexp >= tier) completed++;
        }
        for (int tier : familyTiers) {
            if (memberCount >= tier) completed++;
        }

        return completed;
    }

    private String createProgressBar(double progress, int length) {
        int filled = (int) Math.round(progress * length);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            bar.append(i < filled ? "|" : "§7|");
        }
        return bar.toString();
    }

    private String formatDuration(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        if (days > 0) return days + " days, " + hours + " hours ago";
        if (hours > 0) return hours + " hours ago";
        return "Just now";
    }

    public record GuildState(@Nullable GuildData guild) { }
}
