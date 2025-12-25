package net.swofty.type.skyblockgeneric.auction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.minestom.server.component.DataComponents;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record AuctionItemLoreHandler(AuctionItem auctionItem) {
    @JsonIgnore
    @Transient
    public List<String> getLore() {
        return getLore(null);
    }

    @JsonIgnore
    @Transient
    public List<String> getLore(SkyBlockPlayer player) {
        List<String> toReturn = new ArrayList<>();
        SkyBlockItem skyBlockItem = new SkyBlockItem(auctionItem.getItem());

        if (player == null) {
            new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem().build().get(DataComponents.LORE).forEach(loreEntry -> {
                toReturn.add(StringUtility.getTextFromComponent(loreEntry));
            });
        } else {
            PlayerItemUpdater.playerUpdate(player, skyBlockItem.getItemStack()).build().get(DataComponents.LORE).forEach(loreEntry -> {
                toReturn.add(StringUtility.getTextFromComponent(loreEntry));
            });
        }

        toReturn.add("§8§m----------------------");
        toReturn.add("§7Seller: " + SkyBlockPlayer.getDisplayName(auctionItem.getOriginator()));

        if (auctionItem.isBin()) {
            toReturn.add("§7Buy it now: §6" + auctionItem.getStartingPrice() + " coins");
        } else {
            if (auctionItem.getBids().isEmpty()) {
                toReturn.add("§7Starting bid: §6" + auctionItem.getStartingPrice() + " coins");
            } else {
                toReturn.add("§7Bids: §a" + auctionItem.getBids().size() + " bid" + (auctionItem.getBids().size() == 1 ? "" : "s"));
                toReturn.add(" ");

                AuctionItem.Bid topBid = auctionItem.getBids().stream().max(Comparator.comparing(AuctionItem.Bid::value)).orElse(null);

                toReturn.add("§7Top bid: §6" + topBid.value() + " coins");
                toReturn.add("§7Bidder: " + SkyBlockPlayer.getDisplayName(topBid.uuid()));
            }
        }

        if (player != null) {
            if (auctionItem.getOriginator().equals(player.getUuid())) {
                toReturn.add(" ");
                toReturn.add("§aThis is your own auction!");
            } else {
                CoopDatabase.Coop viewerCoop = CoopDatabase.getFromMember(player.getUuid());
                if (viewerCoop != null && viewerCoop.members().contains(auctionItem.getOriginator())) {
                    toReturn.add(" ");
                    toReturn.add("§aThis is a coop member's auction!");
                }
            }
        }

        toReturn.add(" ");
        if (auctionItem.isBin() && !auctionItem.getBids().isEmpty()) {
            toReturn.add("§7Status: §aPurchased");
        } else if (auctionItem.getEndTime() > System.currentTimeMillis()) {
            toReturn.add("§7Ends in: §e" + StringUtility.formatTimeLeft(auctionItem.getEndTime() - System.currentTimeMillis()));
        } else {
            toReturn.add("§7Status: §aEnded!");
        }
        toReturn.add(" ");

        toReturn.add("§eClick to inspect!");
        return toReturn;
    }
}
