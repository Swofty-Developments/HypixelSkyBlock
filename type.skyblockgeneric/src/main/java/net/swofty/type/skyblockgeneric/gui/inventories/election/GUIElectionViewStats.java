package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIElectionViewStats extends HypixelInventoryGUI {

    public GUIElectionViewStats() {
        super("Election Stats", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        ElectionData data = ElectionManager.getElectionData();

        if (!data.isElectionOpen() || data.getCandidates().isEmpty()) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§cNo Active Election",
                            Material.BARRIER,
                            1,
                            "§7There is no active election",
                            "§7at this time."
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        int currentYear = SkyBlockCalendar.getYear();
        String playerVote = ElectionManager.getPlayerVote(getPlayer().getUuid());
        Map<String, Long> tally = data.tallyVotes();
        long totalVotes = tally.values().stream().mapToLong(Long::longValue).sum();

        List<ElectionData.CandidateData> candidates = data.getCandidates();

        for (int i = 0; i < candidates.size() && i < 5; i++) {
            ElectionData.CandidateData candidate = candidates.get(i);
            SkyBlockMayor mayor = candidate.getMayorEnum();
            if (mayor == null) continue;

            boolean isVotedFor = candidate.getMayorName().equals(playerVote);
            long votes = tally.getOrDefault(candidate.getMayorName(), 0L);
            String voteStr = formatVotes(votes);
            String pctStr = totalVotes > 0
                    ? String.format("%.1f%%", (votes * 100.0) / totalVotes) : "0%";
            int yearsSince = data.getYearsSinceLastElected(candidate.getMayorName(), currentYear);

            int col = i * 2;

            for (int row = 0; row < 6; row++) {
                int slot = row * 9 + col;
                int finalRow = row;

                set(new GUIItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        if (finalRow == 0) {
                            List<String> lore = buildCandidateLore(
                                    mayor, candidate, data.getElectionYear(),
                                    yearsSince, voteStr, pctStr, isVotedFor
                            );
                            return ItemStackCreator.getStack(
                                    mayor.getColoredName(),
                                    Material.GRAY_STAINED_GLASS_PANE,
                                    1,
                                    lore.toArray(new String[0])
                            );
                        }

                        int barHeight = totalVotes > 0 ? (int) Math.ceil((votes * 5.0) / totalVotes) : 0;
                        int barRow = 6 - finalRow;
                        if (barRow <= barHeight) {
                            return ItemStackCreator.getStack(
                                    mayor.getColor() + voteStr + " votes",
                                    Material.LIME_STAINED_GLASS_PANE,
                                    1,
                                    mayor.getColor() + pctStr
                            );
                        }
                        return ItemStackCreator.getStack(" ",
                                Material.BLACK_STAINED_GLASS_PANE, 1);
                    }
                });
            }
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    private List<String> buildCandidateLore(SkyBlockMayor mayor, ElectionData.CandidateData candidate,
                                            int electionYear, int yearsSince,
                                            String voteStr, String pctStr, boolean votedFor) {
        List<String> lore = new ArrayList<>();
        lore.add("§8Year " + electionYear + " Candidate");
        lore.add("");
        lore.add("§7Votes: " + mayor.getColor() + voteStr + " §7(" + mayor.getColor() + pctStr + "§7)");
        if (yearsSince >= 0) {
            lore.add("§7Last elected: " + mayor.getColor() + yearsSince + "y ago");
        } else {
            lore.add("§7Last elected: " + mayor.getColor() + "Never");
        }
        lore.add("");
        lore.add("§8§m--------------------------");

        List<SkyBlockMayor.Perk> activePerks = candidate.getActivePerkEnums();
        for (int j = 0; j < activePerks.size(); j++) {
            SkyBlockMayor.Perk perk = activePerks.get(j);
            if (j == 0) {
                lore.add("§6✯ " + mayor.getColor() + perk.getDisplayName());
            } else {
                lore.add(mayor.getColor() + perk.getDisplayName());
            }
            lore.add(perk.getDescription());
            if (j < activePerks.size() - 1) lore.add("");
        }

        lore.add("§8§m--------------------------");

        if (!mayor.isSpecial()) {
            lore.add("");
            lore.add("§6✯ " + mayor.getColor() + "Minister Perks §7are also granted if");
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

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
