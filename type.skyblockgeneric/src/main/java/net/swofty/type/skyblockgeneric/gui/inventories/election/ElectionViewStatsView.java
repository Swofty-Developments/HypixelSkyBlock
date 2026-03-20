package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ElectionViewStatsView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.withString(
                (s, ctx) -> I18n.string("gui_election.stats.title", ctx.player().getLocale(),
                        Map.of("year", String.valueOf(SkyBlockCalendar.getYear()))),
                InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);

        ElectionData data = ElectionManager.getElectionData();

        if (!data.isElectionOpen() || data.getCandidates().isEmpty()) {
            layout.slot(22, (s, c) -> {
                Locale l = c.player().getLocale();
                return ItemStackCreator.getStack(
                    I18n.string("gui_election.view.no_election", l),
                    Material.BARRIER,
                    1,
                    I18n.lore("gui_election.view.no_election.lore", l)
                );
            });
            return;
        }

        Map<String, Long> tally = data.tallyVotes();
        long totalVotes = tally.values().stream().mapToLong(Long::longValue).sum();
        int currentYear = SkyBlockCalendar.getYear();

        List<ElectionData.CandidateData> candidates = data.getCandidates();

        String leaderName = null;
        long leaderVotes = -1;
        for (ElectionData.CandidateData c : candidates) {
            long v = tally.getOrDefault(c.getMayorName(), 0L);
            if (v > leaderVotes) {
                leaderVotes = v;
                leaderName = c.getMayorName();
            }
        }

        int candidateCount = Math.min(candidates.size(), 6);
        int[] cols = candidateCount <= 5
                ? new int[]{0, 2, 4, 6, 8}
                : new int[]{0, 1, 3, 5, 7, 8};

        for (int i = 0; i < candidateCount; i++) {
            ElectionData.CandidateData candidate = candidates.get(i);
            SkyBlockMayor mayor = candidate.getMayorEnum();
            if (mayor == null) continue;

            long votes = tally.getOrDefault(candidate.getMayorName(), 0L);
            String voteStr = formatVotes(votes);
            String pctStr = totalVotes > 0
                ? String.format("%.1f%%", (votes * 100.0) / totalVotes) : "0%";
            int yearsSince = data.getYearsSinceLastElected(candidate.getMayorName(), currentYear);
            String candidateName = candidate.getMayorName();
            boolean isLeader = candidateName.equals(leaderName) && votes > 0;
            Material glassMaterial = isLeader ? Material.ORANGE_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE;

            int col = cols[i];

            for (int row = 0; row < 6; row++) {
                int slot = row * 9 + col;

                int finalRow = row;
                layout.slot(slot, (s, c) -> {
                    Locale l = c.player().getLocale();
                    String playerVote = ElectionManager.getPlayerVote(c.player().getUuid());
                    boolean isVotedFor = candidateName.equals(playerVote);

                    List<String> lore = ElectionLoreBuilder.build(
                        l, mayor, candidate, data.getElectionYear(),
                        yearsSince, voteStr, pctStr, isVotedFor, isLeader
                    );

                    if (finalRow == 5) {
                        return ItemStackCreator.getStackHead(
                            candidate.getColoredName(),
                            new PlayerSkin(mayor.getTexture(), mayor.getSignature()),
                            1,
                            lore
                        );
                    }
                    return ItemStackCreator.getStack(
                        candidate.getColoredName(),
                        glassMaterial,
                        1,
                        lore
                    );
                }, (_, c) -> {
                    Locale l = c.player().getLocale();
                    String playerVote = ElectionManager.getPlayerVote(c.player().getUuid());
                    if (candidateName.equals(playerVote)) {
                        c.player().sendMessage(I18n.string("gui_election.view.already_voted", l));
                        return;
                    }
                    ElectionManager.castVote(c.player().getUuid(), candidateName);
                    c.player().sendMessage(I18n.string("gui_election.view.vote_divider", l));
                    c.player().sendMessage(I18n.string("gui_election.view.vote_cast", l, Map.of(
                            "candidate", candidate.getColoredName(),
                            "year", String.valueOf(data.getElectionYear()))));
                    c.player().sendMessage("  " + I18n.string("gui_election.view.vote_fame", l));
                    c.player().sendMessage(I18n.string("gui_election.view.vote_result", l, Map.of(
                            "candidate", candidate.getColoredName(),
                            "percentage", pctStr,
                            "votes", voteStr)));
                    c.player().sendMessage(I18n.string("gui_election.view.vote_divider", l));
                    c.replace(new ElectionViewStatsView());
                });
            }
        }
    }

    private String formatVotes(long votes) {
        if (votes >= 1_000_000) return String.format("%.1fM", votes / 1_000_000.0);
        if (votes >= 1_000) return String.format("%.1fk", votes / 1_000.0);
        return String.valueOf(votes);
    }
}
