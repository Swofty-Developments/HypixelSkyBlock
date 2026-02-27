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
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ElectionView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_election.view.title", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 22, ctx);

        ElectionData data = ElectionManager.getElectionData();

        if (!data.isElectionOpen() || data.getCandidates().isEmpty()) {
            layout.slot(13, (s, c) -> {
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

        int currentYear = SkyBlockCalendar.getYear();
        Map<String, Long> tally = data.tallyVotes();
        long totalVotes = tally.values().stream().mapToLong(Long::longValue).sum();

        List<ElectionData.CandidateData> candidates = data.getCandidates();
        int[] slots = getCandidateSlots(candidates.size());

        for (int i = 0; i < candidates.size(); i++) {
            ElectionData.CandidateData candidate = candidates.get(i);
            SkyBlockMayor mayor = candidate.getMayorEnum();
            if (mayor == null) continue;

            int slot = slots[i];
            long votes = tally.getOrDefault(candidate.getMayorName(), 0L);
            String voteStr = formatVotes(votes);
            String pctStr = totalVotes > 0
                ? String.format("%.1f%%", (votes * 100.0) / totalVotes) : "0%";
            int yearsSince = data.getYearsSinceLastElected(candidate.getMayorName(), currentYear);
            String candidateName = candidate.getMayorName();

            layout.slot(slot, (s, c) -> {
                Locale l = c.player().getLocale();
                String playerVote = ElectionManager.getPlayerVote(c.player().getUuid());
                boolean isVotedFor = candidateName.equals(playerVote);

                List<String> lore = buildCandidateLore(
                    l, mayor, candidate, data.getElectionYear(),
                    yearsSince, voteStr, pctStr, isVotedFor
                );
                return ItemStackCreator.getStackHead(
                    candidate.getColoredName(),
                    new PlayerSkin(mayor.getTexture(), mayor.getSignature()),
                    1,
                    lore
                );
            }, (_, c) -> {
                Locale l = c.player().getLocale();
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

    private List<String> buildCandidateLore(Locale l, SkyBlockMayor mayor, ElectionData.CandidateData candidate,
                                            int electionYear, int yearsSince,
                                            String voteStr, String pctStr, boolean votedFor) {
        String color = candidate.getColor();
        List<String> lore = new ArrayList<>();
        lore.add(I18n.string("gui_election.view.candidate.year", l, Map.of("year", String.valueOf(electionYear))));
        lore.add("");
        lore.add(I18n.string("gui_election.view.candidate.votes", l, Map.of(
                "color", color, "votes", voteStr, "percentage", pctStr)));
        if (yearsSince >= 0) {
            lore.add(I18n.string("gui_election.view.candidate.last_elected", l, Map.of(
                    "color", color, "years", String.valueOf(yearsSince))));
        } else {
            lore.add(I18n.string("gui_election.view.candidate.last_elected_never", l, Map.of("color", color)));
        }
        lore.add("");
        lore.add("§8§m--------------------------");

        List<SkyBlockMayor.Perk> activePerks = candidate.getActivePerkEnums();
        for (int j = 0; j < activePerks.size(); j++) {
            SkyBlockMayor.Perk perk = activePerks.get(j);
            if (j == 0) {
                lore.add("§6✯ " + color + perk.getDisplayName());
            } else {
                lore.addAll(StringUtility.splitByWordAndLengthKeepLegacyColor(
                    color + perk.getDisplayName(), 35));
            }
            lore.addAll(StringUtility.splitByWordAndLengthKeepLegacyColor(
                perk.getDescription(), 35));
            if (j < activePerks.size() - 1) lore.add("");
        }

        lore.add("§8§m--------------------------");

        if (!mayor.isSpecial()) {
            lore.add("");
            lore.add(I18n.string("gui_election.view.candidate.minister_note_1", l, Map.of("color", color)));
            lore.add(I18n.string("gui_election.view.candidate.minister_note_2", l));
        }

        lore.add("");
        if (votedFor) {
            lore.add(I18n.string("gui_election.view.candidate.voted", l));
        } else {
            lore.add(I18n.string("gui_election.view.candidate.change_vote_1", l));
            lore.add(I18n.string("gui_election.view.candidate.change_vote_2", l));
            lore.add("");
            lore.add(I18n.string("gui_election.view.candidate.click_vote", l, Map.of("name", mayor.getDisplayName())));
        }

        return lore;
    }

    private int[] getCandidateSlots(int count) {
        return switch (count) {
            case 1 -> new int[]{13};
            case 2 -> new int[]{11, 15};
            case 3 -> new int[]{11, 13, 15};
            case 4 -> new int[]{10, 12, 14, 16};
            case 5 -> new int[]{9, 11, 13, 15, 17};
            case 6 -> new int[]{9, 11, 13, 15, 17, 18};
            default -> new int[]{9, 11, 13, 15, 17};
        };
    }

    private String formatVotes(long votes) {
        if (votes >= 1_000_000) return String.format("%.1fM", votes / 1_000_000.0);
        if (votes >= 1_000) return String.format("%.1fk", votes / 1_000.0);
        return String.valueOf(votes);
    }
}
