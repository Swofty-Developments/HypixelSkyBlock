package net.swofty.type.skyblockgeneric.bazaar;

import net.swofty.commons.skyblock.bazaar.BuyOrderRefundTransaction;
import net.swofty.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import net.swofty.commons.skyblock.bazaar.SuccessfulBazaarTransaction;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCompletedBazaarTransactions;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.UUID;

public class BazaarAwarder {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");

    /**
     * Process a successful bazaar transaction for a player
     * @param player The player to process for
     * @param transaction The successful transaction
     * @return true if the player was the buyer or seller and was processed
     */
    public static boolean processSuccessfulTransaction(SkyBlockPlayer player, SuccessfulBazaarTransaction transaction) {
        UUID playerUuid = player.getUuid();
        UUID currentProfile = player.getProfiles().getCurrentlySelected();

        // Check if player is the buyer
        if (playerUuid.equals(transaction.buyer()) && currentProfile.equals(transaction.buyerProfile())) {
            return processBuyer(player, transaction);
        }

        // Check if player is the seller
        if (playerUuid.equals(transaction.seller()) && currentProfile.equals(transaction.sellerProfile())) {
            return processSeller(player, transaction);
        }

        // Player is not involved in this transaction
        return false;
    }

    public static boolean processRefundTransaction(SkyBlockPlayer player, BuyOrderRefundTransaction transaction) {
        UUID playerUuid = player.getUuid();
        UUID currentProfile = player.getProfiles().getCurrentlySelected();

        if (playerUuid.equals(transaction.owner()) && currentProfile.equals(transaction.ownerProfile())) {
            return processRefund(player, transaction);
        }

        return false;
    }

    /**
     * Process an expired order transaction for a player
     * @param player The player to process for
     * @param transaction The expired order transaction
     * @return true if the player was the owner and was processed
     */
    public static boolean processExpiredOrder(SkyBlockPlayer player, OrderExpiredBazaarTransaction transaction) {
        UUID playerUuid = player.getUuid();
        UUID currentProfile = player.getProfiles().getCurrentlySelected();

        // Check if player is the owner of the expired order
        if (playerUuid.equals(transaction.owner()) && currentProfile.equals(transaction.ownerProfile())) {
            return processExpiredOrderOwner(player, transaction);
        }

        // Player is not the owner of this expired order
        return false;
    }

    /**
     * Process a pending transaction from JSON data
     * @param player The player to process for
     * @param transactionType The type of transaction
     * @param transactionData The transaction data as JSON
     * @return true if the transaction was processed successfully
     */
    public static boolean processPendingTransaction(SkyBlockPlayer player, String transactionType, JSONObject transactionData) {
        try {
            switch (transactionType) {
                case "SuccessfulBazaarTransaction" -> {
                    var transaction = parseSuccessfulTransaction(transactionData);
                    return processSuccessfulTransaction(player, transaction);
                }
                case "BuyOrderRefundTransaction" -> {
                    var transaction = parseRefundTransaction(transactionData);
                    return processRefundTransaction(player, transaction);
                }
                case "OrderExpiredBazaarTransaction" -> {
                    var transaction = parseExpiredOrderTransaction(transactionData);
                    return processExpiredOrder(player, transaction);
                }
                default -> {
                    System.err.println("Unknown pending transaction type: " + transactionType);
                    return false;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to process pending transaction: " + e.getMessage());
            return false;
        }
    }

    private static boolean processBuyer(SkyBlockPlayer player, SuccessfulBazaarTransaction transaction) {
        try {
            String itemName = transaction.itemName();
            double quantity = transaction.quantity();
            double actualPrice = transaction.actualPricePerUnit();
            double originalBid = transaction.originalBuyerBid();
            double priceImprovement = transaction.priceImprovement();

            String displayName = getItemDisplayName(itemName);

            var completedTransaction = DatapointCompletedBazaarTransactions.CompletedBazaarTransaction
                    .createBuyTransaction(itemName, quantity, actualPrice, originalBid, transaction.buyerOrderId());

            var completedTransactions = player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                    DatapointCompletedBazaarTransactions.class
            ).getValue();

            completedTransactions.addTransaction(completedTransaction);

            if (priceImprovement > 0) {
                player.sendMessage("§6[Bazaar] §eYour §aBuy Order §ewas partially filled! §e" + (int)quantity +
                        "x " + displayName + " §afor §6" + FORMATTER.format(actualPrice) + " coins §aeach!");
                player.sendMessage("§6[Bazaar] §7You saved §a" + FORMATTER.format(priceImprovement) +
                        " coins §7by getting a better price than your §6" + FORMATTER.format(originalBid) + " §7bid!");
            } else {
                player.sendMessage("§6[Bazaar] §eYour §aBuy Order §ewas partially filled! §e" + (int)quantity +
                        "x " + displayName + " §afor §6" + FORMATTER.format(actualPrice) + " coins §aeach!");
            }

            player.sendMessage("§6[Bazaar] §7Items are ready to collect! Head to the Bazaar for collection.");
            player.playSuccessSound();
            return true;

        } catch (Exception e) {
            System.err.println("Failed to process buyer transaction: " + e.getMessage());
            return false;
        }
    }

    private static boolean processRefund(SkyBlockPlayer player, BuyOrderRefundTransaction transaction) {
        try {
            String displayName = getItemDisplayName(transaction.itemName());

            var completedTransaction = DatapointCompletedBazaarTransactions.CompletedBazaarTransaction
                    .createRefundTransaction(transaction.itemName(), transaction.refundAmount(),
                            transaction.reason(), transaction.orderId());

            var completedTransactions = player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                    DatapointCompletedBazaarTransactions.class
            ).getValue();

            completedTransactions.addTransaction(completedTransaction);

            String reasonMsg = switch (transaction.reason()) {
                case "COMPLETED" -> "Your buy order was completed with price improvements!";
                case "EXPIRED" -> "Your buy order expired!";
                case "CANCELLED" -> "Your buy order was cancelled!";
                default -> "Refund processed!";
            };

            player.sendMessage("§6[Bazaar] §7" + reasonMsg);
            player.sendMessage("§6[Bazaar] §7Refund of §a" + FORMATTER.format(transaction.refundAmount()) +
                    " coins §7is ready to collect!");
            player.sendMessage("§6[Bazaar] §7Head to the Bazaar to claim your refund.");

            player.playSuccessSound();
            return true;

        } catch (Exception e) {
            System.err.println("Failed to process refund transaction: " + e.getMessage());
            return false;
        }
    }

    private static boolean processSeller(SkyBlockPlayer player, SuccessfulBazaarTransaction transaction) {
        try {
            String itemName = transaction.itemName();
            double quantity = transaction.quantity();
            double pricePerUnit = transaction.actualPricePerUnit();
            double grossEarnings = pricePerUnit * quantity;
            double taxTaken = transaction.taxTaken();
            double netEarnings = grossEarnings - taxTaken;

            // Get display name for the item
            String displayName = getItemDisplayName(itemName);

            // Store the completed transaction in player data (unclaimed)
            DatapointCompletedBazaarTransactions.CompletedBazaarTransaction completedTransaction =
                    DatapointCompletedBazaarTransactions.CompletedBazaarTransaction.createSellTransaction(
                            itemName, quantity, pricePerUnit, taxTaken, transaction.sellerOrderId());

            var completedTransactions = player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                    DatapointCompletedBazaarTransactions.class
            ).getValue();

            completedTransactions.addTransaction(completedTransaction);

            // Send notification messages
            player.sendMessage("§6[Bazaar] §aYour §6Sell Offer §awas filled! §e" + (int)quantity + "x " + displayName +
                    " §afor §6" + FORMATTER.format(pricePerUnit) + " coins §aeach!");
            player.sendMessage("§6[Bazaar] §7You earned §6" + FORMATTER.format(netEarnings) +
                    " coins §7(§c-" + FORMATTER.format(taxTaken) + " tax§7)");
            player.sendMessage("§6[Bazaar] §7Coins are ready to collect! Head to the Bazaar to claim them.");

            player.playSuccessSound();
            return true;

        } catch (Exception e) {
            System.err.println("Failed to process seller transaction: " + e.getMessage());
            return false;
        }
    }

    private static boolean processExpiredOrderOwner(SkyBlockPlayer player, OrderExpiredBazaarTransaction transaction) {
        try {
            String itemName = transaction.itemName();
            String side = transaction.side();
            double remainingQty = transaction.remainingQty();
            boolean isBuyOrder = "BUY".equals(side);

            // Get display name for the item
            String displayName = getItemDisplayName(itemName);

            DatapointCompletedBazaarTransactions.CompletedBazaarTransaction completedTransaction;

            if (isBuyOrder) {
                double originalPricePerUnit = transaction.originalPricePaid();
                double totalRefund = originalPricePerUnit * remainingQty;

                completedTransaction = DatapointCompletedBazaarTransactions.CompletedBazaarTransaction
                        .createExpiredBuyOrder(itemName, remainingQty, originalPricePerUnit, transaction.orderId());

                player.sendMessage("§6[Bazaar] §7Your buy order for §e" + (int)remainingQty +
                        "x " + displayName + " §7expired!");
                player.sendMessage("§6[Bazaar] §7Refund of §6" + FORMATTER.format(totalRefund) +
                        " coins §7is ready to collect!");

            } else {
                // Expired sell order - return items
                completedTransaction = DatapointCompletedBazaarTransactions.CompletedBazaarTransaction
                        .createExpiredSellOrder(itemName, remainingQty, transaction.orderId());

                player.sendMessage("§6[Bazaar] §7Your sell order for §e" + (int)remainingQty +
                        "x " + displayName + " §7expired!");
                player.sendMessage("§6[Bazaar] §7Items are ready to collect!");
            }

            // Store the completed transaction in player data (unclaimed)
            var completedTransactions = player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                    DatapointCompletedBazaarTransactions.class
            ).getValue();

            completedTransactions.addTransaction(completedTransaction);

            return true;
        } catch (Exception e) {
            System.err.println("Failed to process expired order: " + e.getMessage());
            return false;
        }
    }

    private static SuccessfulBazaarTransaction parseSuccessfulTransaction(JSONObject data) {
        return new SuccessfulBazaarTransaction(
                data.getString("itemName"),
                UUID.fromString(data.getString("buyer")),
                UUID.fromString(data.getString("buyerProfile")),
                UUID.fromString(data.getString("seller")),
                UUID.fromString(data.getString("sellerProfile")),
                data.getDouble("actualPricePerUnit"),
                data.getDouble("originalBuyerBid"),
                data.getDouble("quantity"),
                data.getDouble("taxTaken"),
                data.getDouble("priceImprovement"),
                UUID.fromString(data.getString("buyerOrderId")),
                UUID.fromString(data.getString("sellerOrderId")),
                java.time.Instant.parse(data.getString("timestamp"))
        );
    }

    private static BuyOrderRefundTransaction parseRefundTransaction(JSONObject data) {
        return new BuyOrderRefundTransaction(
                UUID.fromString(data.getString("orderId")),
                data.getString("itemName"),
                UUID.fromString(data.getString("owner")),
                UUID.fromString(data.getString("ownerProfile")),
                data.getDouble("refundAmount"),
                data.getString("reason"),
                java.time.Instant.parse(data.getString("timestamp"))
        );
    }

    private static OrderExpiredBazaarTransaction parseExpiredOrderTransaction(JSONObject data) {
        return new OrderExpiredBazaarTransaction(
                UUID.fromString(data.getString("orderId")),
                data.getString("itemName"),
                UUID.fromString(data.getString("owner")),
                UUID.fromString(data.getString("ownerProfile")),
                data.getString("side"),
                data.getDouble("originalPricePaid"),
                data.getDouble("remainingQty"),
                java.time.Instant.parse(data.getString("expiredAt"))
        );
    }

    private static String getItemDisplayName(String itemName) {
        try {
            SkyBlockItem item = new SkyBlockItem(ItemType.valueOf(itemName));
            return item.getDisplayName();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown item type: " + itemName);
        }
    }
}