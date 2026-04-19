package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol.Request;
import net.swofty.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol.Response;
import net.swofty.commons.skyblock.bazaar.BazaarTransaction;
import net.swofty.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import net.swofty.commons.skyblock.bazaar.SuccessfulBazaarTransaction;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.bazaar.BazaarAwarder;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;

public class TypedBazaarTransactionHandler implements TypedServiceHandler<Request, Response> {

    private static final BazaarTransactionPushProtocol PROTOCOL = new BazaarTransactionPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        try {
            String transactionType = message.transactionType();
            JSONObject data = new JSONObject(message.transactionJson());

            Logger.info("Received bazaar transaction: " + transactionType);

            BazaarTransaction transaction = parseTransaction(transactionType, data);
            if (transaction == null) {
                Logger.error("Failed to parse bazaar transaction of type: " + transactionType);
                return Response.failure("Failed to parse transaction");
            }

            return switch (transaction) {
                case SuccessfulBazaarTransaction success -> handleSuccessfulTransaction(success);
                case OrderExpiredBazaarTransaction expired -> handleExpiredTransaction(expired);
                default -> {
                    Logger.error("Unknown transaction type: " + transaction.getClass().getSimpleName());
                    yield Response.failure("Unknown transaction type");
                }
            };

        } catch (Exception e) {
            Logger.error(e, "Failed to propagate bazaar transaction");
            return Response.failure("Exception occurred: " + e.getMessage());
        }
    }

    private BazaarTransaction parseTransaction(String type, JSONObject data) {
        try {
            return switch (type) {
                case "SuccessfulBazaarTransaction" -> new SuccessfulBazaarTransaction(
                        null, null, null, null, null, 0, 0, 0, 0, 0, UUID.randomUUID(), UUID.randomUUID(), null
                ).fromJSON(data);
                case "OrderExpiredBazaarTransaction" -> new OrderExpiredBazaarTransaction(
                        null, null, null, null, null, 0, 0, null
                ).fromJSON(data);
                default -> {
                    Logger.error("Unknown transaction type: " + type);
                    yield null;
                }
            };
        } catch (Exception e) {
            Logger.error("Error parsing transaction: " + e.getMessage());
            return null;
        }
    }

    private Response handleSuccessfulTransaction(SuccessfulBazaarTransaction transaction) {
        Logger.info("Handling successful transaction for item: " + transaction.itemName() +
                " between buyer: " + transaction.buyer() + " and seller: " + transaction.seller());

        boolean buyerHandled = false;
        boolean sellerHandled = false;

        SkyBlockPlayer buyer = SkyBlockGenericLoader.getFromUUID(transaction.buyer());
        if (buyer != null && buyer.getProfiles().getCurrentlySelected().equals(transaction.buyerProfile())) {
            buyerHandled = BazaarAwarder.processSuccessfulTransaction(buyer, transaction);
            Logger.info("Buyer " + transaction.buyer() + " processed successfully: " + buyerHandled);
        } else {
            Logger.info("Buyer " + transaction.buyer() + " is offline or on different profile");
        }

        SkyBlockPlayer seller = SkyBlockGenericLoader.getFromUUID(transaction.seller());
        if (seller != null && seller.getProfiles().getCurrentlySelected().equals(transaction.sellerProfile())) {
            sellerHandled = BazaarAwarder.processSuccessfulTransaction(seller, transaction);
            Logger.info("Seller " + transaction.seller() + " processed successfully: " + sellerHandled);
        } else {
            Logger.info("Seller " + transaction.seller() + " is offline or on different profile");
        }

        return new Response(true, buyerHandled, sellerHandled, transaction.timestamp().toString());
    }

    private Response handleExpiredTransaction(OrderExpiredBazaarTransaction transaction) {
        Logger.info("Handling expired order for: " + transaction.itemName() +
                " owner: " + transaction.owner());

        boolean ownerHandled = false;

        SkyBlockPlayer owner = SkyBlockGenericLoader.getFromUUID(transaction.owner());
        if (owner != null && owner.getProfiles().getCurrentlySelected().equals(transaction.ownerProfile())) {
            ownerHandled = BazaarAwarder.processExpiredOrder(owner, transaction);
            Logger.info("Owner " + transaction.owner() + " processed successfully: " + ownerHandled);
        } else {
            Logger.info("Owner " + transaction.owner() + " is offline or on different profile");
        }

        return new Response(true, ownerHandled, false, transaction.orderId().toString());
    }
}
