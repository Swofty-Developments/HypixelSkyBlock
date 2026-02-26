package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElectionViewStatsView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Election, Year " + SkyBlockCalendar.getYear(), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);

        ElectionData data = ElectionManager.getElectionData();

        if (!data.isElectionOpen() || data.getCandidates().isEmpty()) {
            layout.slot(22, (s, c) -> ItemStackCreator.getStack(
                "§cNo Active Election",
                Material.BARRIER,
                1,
                "§7There is no active election",
                "§7at this time."
            ));
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

        for (int i = 0; i < candidates.size() && i < 5; i++) {
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

            int col = i * 2;

            for (int row = 0; row < 6; row++) {
                int slot = row * 9 + col;

                int finalRow = row;
                layout.slot(slot, (s, c) -> {
                    String playerVote = ElectionManager.getPlayerVote(c.player().getUuid());
                    boolean isVotedFor = candidateName.equals(playerVote);

                    List<String> lore = buildCandidateLore(
                        mayor, candidate, data.getElectionYear(),
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
                    String playerVote = ElectionManager.getPlayerVote(c.player().getUuid());
                    if (candidateName.equals(playerVote)) {
                        c.player().sendMessage("§aYou voted for this candidate.");
                        return;
                    }
                    ElectionManager.castVote(c.player().getUuid(), candidateName);
                    c.player().sendMessage("§c-----------------------------------------------------");
                    c.player().sendMessage("§eYou cast §c1 vote §efor " + candidate.getColoredName() + " §ein the §bYear " + data.getElectionYear() + " Elections§e!");
                    c.player().sendMessage("  §bNew player §eFame Rank §a+1 vote");
                    c.player().sendMessage(candidate.getColoredName() + " §enow has §c" + pctStr + " §eof votes with §c" + voteStr + " votes§e!");
                    c.player().sendMessage("§c-----------------------------------------------------");
                    c.replace(new ElectionViewStatsView());
                });
            }
        }
    }

    private List<String> buildCandidateLore(SkyBlockMayor mayor, ElectionData.CandidateData candidate,
                                            int electionYear, int yearsSince,
                                            String voteStr, String pctStr,
                                            boolean votedFor, boolean isLeader) {
        List<String> lore = new ArrayList<>();
        lore.add("§8Year " + electionYear + " Candidate");
        lore.add("");
        lore.add("§7Votes: " + candidate.getColor() + voteStr + " §7(" + candidate.getColor() + pctStr + "§7)");
        if (isLeader) {
            lore.add(candidate.getColor() + "Leading in votes!");
        }
        if (yearsSince >= 0) {
            lore.add("§7Last elected: " + candidate.getColor() + yearsSince + "y ago");
        } else {
            lore.add("§7Last elected: " + candidate.getColor() + "Never");
        }
        lore.add("");
        lore.add("§8§m--------------------------");

        List<SkyBlockMayor.Perk> activePerks = candidate.getActivePerkEnums();
        for (int j = 0; j < activePerks.size(); j++) {
            SkyBlockMayor.Perk perk = activePerks.get(j);
            if (j == 0) {
                lore.add("§6✯ " + candidate.getColor() + perk.getDisplayName());
            } else {
                lore.addAll(StringUtility.splitByWordAndLengthKeepLegacyColor(
                    candidate.getColor() + perk.getDisplayName(), 35));
            }
            lore.addAll(StringUtility.splitByWordAndLengthKeepLegacyColor(
                perk.getDescription(), 35));
            if (j < activePerks.size() - 1) lore.add("");
        }

        lore.add("§8§m--------------------------");

        if (!mayor.isSpecial()) {
            lore.add("");
            lore.add("§6✯ " + candidate.getColor() + "Minister Perks §7are also granted if");
            lore.add("§7this mayor wins second place!");
        }

        lore.add("");
        if (votedFor) {
            lore.add("§aYou voted for this candidate!");
        } else {
            lore.add("§eClick to vote for " + mayor.getDisplayName() + "!");
        }

        return lore;
    }

    private String formatVotes(long votes) {
        if (votes >= 1_000_000) return String.format("%.1fM", votes / 1_000_000.0);
        if (votes >= 1_000) return String.format("%.1fk", votes / 1_000.0);
        return String.valueOf(votes);
    }
}
