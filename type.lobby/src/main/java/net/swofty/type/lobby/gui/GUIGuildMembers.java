package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildMember;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GUIGuildMembers extends PaginatedView<GuildMember, GUIGuildMembers.MembersState> {

    private static final int[] MEMBER_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<MembersState> configuration() {
        return new ViewConfiguration<>("Guild Members", InventoryType.CHEST_6_ROW);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return MEMBER_SLOTS;
    }

    @Override
    protected int getNextPageSlot() {
        return 44;
    }

    @Override
    protected int getPreviousPageSlot() {
        return 36;
    }

    @Override
    protected void layoutCustom(ViewLayout<MembersState> layout, MembersState state, ViewContext ctx) {
        layout.slot(49, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Guild"
        ), (click, viewCtx) -> viewCtx.navigator().pop());
    }

    @Override
    protected ItemStack.Builder renderItem(GuildMember member, int index, HypixelPlayer player) {
        String displayName = HypixelPlayer.getDisplayName(member.getUuid());
        NumberFormat nf = NumberFormat.getInstance();

        return ItemStackCreator.getStack(
            displayName,
            Material.PLAYER_HEAD,
            1,
            "§7Guild Rank: §b" + member.getRankName(),
            "§7Member since: §b" + formatDuration(System.currentTimeMillis() - member.getJoinedAt()),
            "§7Weekly GEXP: §6" + nf.format(member.getWeeklyGexp()),
            "§7Total GEXP: §6" + nf.format(member.getTotalGexp())
        );
    }

    @Override
    protected void onItemClick(ClickContext<MembersState> click, ViewContext ctx, GuildMember item, int index) {
    }

    @Override
    protected boolean shouldFilterFromSearch(MembersState state, GuildMember item) {
        return false;
    }

    public static MembersState createState(GuildData guild) {
        return new MembersState(guild.getMembers(), 0);
    }

    private static String formatDuration(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        if (days > 0) return days + " days, " + hours + " hours ago";
        if (hours > 0) return hours + " hours ago";
        return "Just now";
    }

    public record MembersState(List<GuildMember> items, int page) implements PaginatedState<GuildMember> {
        @Override
        public PaginatedState<GuildMember> withPage(int page) {
            return new MembersState(items, page);
        }

        @Override
        public PaginatedState<GuildMember> withItems(List<GuildMember> items) {
            return new MembersState(items, page);
        }
    }
}
