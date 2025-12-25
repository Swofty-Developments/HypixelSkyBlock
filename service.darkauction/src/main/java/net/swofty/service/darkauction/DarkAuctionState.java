package net.swofty.service.darkauction;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.service.darkauction.loot.DarkAuctionBookPool;
import net.swofty.service.darkauction.loot.DarkAuctionItemPool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
public class DarkAuctionState {
    public static final long STARTING_BID = 50_000;

    private final UUID auctionId = UUID.randomUUID();
    private final List<ItemType> roundItems;
    private int currentRound = 0;
    private long currentBid = STARTING_BID;
    private UUID highestBidderId = null;
    private String highestBidderName = null;
    private DarkAuctionPhase phase = DarkAuctionPhase.QUEUE;

    // Escrow tracking - coins held from bidders
    private final Map<UUID, Long> escrowedCoins = new ConcurrentHashMap<>();
    // Pending refunds to broadcast
    private final Queue<RefundEntry> pendingRefunds = new ConcurrentLinkedQueue<>();
    // Bid history for reconciliation - ordered from most recent to oldest
    private final List<BidEntry> bidHistory = Collections.synchronizedList(new ArrayList<>());

    public record RefundEntry(UUID playerId, long amount) {}
    public record BidEntry(UUID playerId, String playerName, long bidAmount) {}

    public DarkAuctionState() {
        // Pre-select all items at auction creation
        DarkAuctionItemPool pool = new DarkAuctionItemPool();
        List<ItemType> items = new ArrayList<>();

        items.add(pool.selectAndRemove());           // Round 1: Item Pool
        items.add(DarkAuctionBookPool.selectRandom()); // Round 2: Book Pool
        items.add(pool.selectAndRemove());           // Round 3: Item Pool
        items.add(pool.selectAndRemove());           // Round 4: Item Pool
        items.add(ItemType.FLOWER_MINION);           // Round 5: Always Flower Minion

        this.roundItems = Collections.unmodifiableList(items);
    }

    public ItemType getCurrentItem() {
        if (currentRound < 0 || currentRound >= roundItems.size()) {
            return null;
        }
        return roundItems.get(currentRound);
    }

    public void setEscrowedAmount(UUID playerId, long amount) {
        escrowedCoins.put(playerId, amount);
    }

    public void queueRefund(UUID playerId, long amount) {
        pendingRefunds.add(new RefundEntry(playerId, amount));
        escrowedCoins.remove(playerId);
    }

    public List<RefundEntry> drainPendingRefunds() {
        List<RefundEntry> refunds = new ArrayList<>();
        RefundEntry entry;
        while ((entry = pendingRefunds.poll()) != null) {
            refunds.add(entry);
        }
        return refunds;
    }

    public void clearEscrowForRound() {
        // Called when round ends - winner's escrow is "finalized"
        escrowedCoins.clear();
        pendingRefunds.clear();
    }

    public void resetForNextRound() {
        currentBid = STARTING_BID;
        highestBidderId = null;
        highestBidderName = null;
        clearEscrowForRound();
        bidHistory.clear();
    }

    /**
     * Records a bid in the history for reconciliation purposes.
     */
    public void recordBid(UUID playerId, String playerName, long bidAmount) {
        // Add to front of list (most recent first)
        bidHistory.add(0, new BidEntry(playerId, playerName, bidAmount));
    }

    /**
     * Finds the next highest bidder after excluding a specific player.
     * Used when the highest bidder leaves and we need to promote the runner-up.
     *
     * @param excludePlayerId the player to exclude (who left)
     * @return the next highest bid entry, or null if no other bidders
     */
    public BidEntry findNextHighestBidder(UUID excludePlayerId) {
        synchronized (bidHistory) {
            for (BidEntry entry : bidHistory) {
                if (!entry.playerId().equals(excludePlayerId)) {
                    return entry;
                }
            }
        }
        return null;
    }

    /**
     * Removes all bids by a specific player from the history.
     */
    public void removeBidsFromPlayer(UUID playerId) {
        synchronized (bidHistory) {
            bidHistory.removeIf(entry -> entry.playerId().equals(playerId));
        }
    }

    public boolean hasMoreRounds() {
        return currentRound < roundItems.size() - 1;
    }

    public void advanceToNextRound() {
        currentRound++;
        resetForNextRound();
    }

    public List<String> getRoundItemNames() {
        return roundItems.stream().map(ItemType::name).toList();
    }
}
