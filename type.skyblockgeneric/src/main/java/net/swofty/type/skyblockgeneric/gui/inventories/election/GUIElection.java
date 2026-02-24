package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIElection extends HypixelInventoryGUI {

    public GUIElection() {
        super("Election", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        ElectionData data = ElectionManager.getElectionData();

        if (!data.isElectionOpen() || data.getCandidates().isEmpty()) {
            set(new GUIItem(13) {
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
        int[] slots = getCandidateSlots(candidates.size());

        for (int i = 0; i < candidates.size(); i++) {
            ElectionData.CandidateData candidate = candidates.get(i);
            SkyBlockMayor mayor = candidate.getMayorEnum();
            if (mayor == null) continue;

            int slot = slots[i];
            boolean isVotedFor = candidate.getMayorName().equals(playerVote);
            long votes = tally.getOrDefault(candidate.getMayorName(), 0L);
            String voteStr = formatVotes(votes);
            String pctStr = totalVotes > 0
                    ? String.format("%.1f%%", (votes * 100.0) / totalVotes) : "0%";

            int yearsSince = data.getYearsSinceLastElected(candidate.getMayorName(), currentYear);

            set(new GUIClickableItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    List<String> lore = buildCandidateLore(
                            mayor, candidate, data.getElectionYear(),
                            yearsSince, voteStr, pctStr, isVotedFor
                    );
                    return ItemStackCreator.getStackHead(
                            mayor.getColoredName(),
                            new PlayerSkin(mayor.getTexture(), mayor.getSignature()),
                            1,
                            lore.toArray(new String[0])
                    );
                }

                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer player) {
                    ElectionManager.castVote(player.getUuid(), candidate.getMayorName());
                    player.sendMessage("§aYou voted for " + mayor.getColoredName() + "§a!");
                    new GUIElection().open(player);
                }
            });
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
            lore.add("§8You may change your vote at any");
            lore.add("§8time until the election ends!");
            lore.add("");
            lore.add("§eClick to vote for " + mayor.getDisplayName() + "!");
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

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
