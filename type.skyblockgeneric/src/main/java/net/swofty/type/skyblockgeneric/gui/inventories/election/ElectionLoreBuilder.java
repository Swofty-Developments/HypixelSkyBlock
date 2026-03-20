package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.swofty.commons.StringUtility;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ElectionLoreBuilder {

    private ElectionLoreBuilder() {}

    public static List<String> build(Locale l, SkyBlockMayor mayor, ElectionData.CandidateData candidate,
                                     int electionYear, int yearsSince,
                                     String voteStr, String pctStr,
                                     boolean votedFor, boolean isLeader) {
        String color = candidate.getColor();
        List<String> lore = new ArrayList<>();
        lore.add(I18n.string("gui_election.view.candidate.year", l, Map.of("year", String.valueOf(electionYear))));
        lore.add("");
        lore.add(I18n.string("gui_election.view.candidate.votes", l, Map.of(
                "color", color, "votes", voteStr, "percentage", pctStr)));
        if (isLeader) {
            lore.add(I18n.string("gui_election.stats.leader", l, Map.of("color", color)));
        }
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
            lore.add(I18n.string("gui_election.view.candidate.click_vote", l, Map.of("name", mayor.getDisplayName())));
        }

        return lore;
    }
}
