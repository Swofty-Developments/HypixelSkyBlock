import { randomUUID } from "crypto";
import Stripe from "stripe";
import { Product, StoreEntitlement } from "@/data/store";
import { minestomDb } from "./mongodb";

export type PurchaseStatus =
  | "CREATED"
  | "PAID"
  | "FULFILLING"
  | "FULFILLED"
  | "FAILED"
  | "EXPIRED"
  | "REFUNDED";

export type CheckoutOrder = {
  _id: string;
  playerUuid: string;
  playerName: string;
  productId: string;
  productName: string;
  quantity: number;
  currency: string;
  amountSubtotal: number;
  amountTotal: number;
  status: PurchaseStatus;
  entitlements: StoreEntitlement[];
  createdAt: Date;
  updatedAt: Date;
  paidAt?: Date;
  fulfilledAt?: Date;
  stripeSessionId?: string;
  stripePaymentIntentId?: string;
  stripeCustomerId?: string;
  stripeSubscriptionId?: string;
  stripeEventIds?: string[];
  attempts?: number;
  nextAttemptAt?: Date;
  lastError?: string;
};

type StripeEventDocument = {
  _id: string;
  type: string;
  status: "PROCESSING" | "PROCESSED" | "FAILED";
  livemode: boolean;
  createdAt: Date;
  updatedAt: Date;
  attempts: number;
  error?: string;
};

export function priceToMinorUnits(price: string) {
  if (!/^\d+(\.\d{1,2})?$/.test(price)) {
    throw new Error(`Invalid product price: ${price}`);
  }

  const [major, minor = ""] = price.split(".");
  return Number(major) * 100 + Number(minor.padEnd(2, "0"));
}

export async function createCheckoutOrder(player: { uuid: string; name: string }, product: Product) {
  const now = new Date();
  const amount = priceToMinorUnits(product.price);
  const order: CheckoutOrder = {
    _id: randomUUID(),
    playerUuid: player.uuid,
    playerName: player.name,
    productId: product.id,
    productName: product.name,
    quantity: 1,
    currency: "usd",
    amountSubtotal: amount,
    amountTotal: amount,
    status: "CREATED",
    entitlements: product.entitlements.map((entitlement) => ({ ...entitlement })),
    createdAt: now,
    updatedAt: now,
    attempts: 0,
    nextAttemptAt: now,
  };

  const db = await minestomDb();
  await db.collection<CheckoutOrder>("store_purchases").insertOne(order);
  return order;
}

export async function attachStripeSession(orderId: string, session: Stripe.Checkout.Session) {
  const db = await minestomDb();
  await db.collection<CheckoutOrder>("store_purchases").updateOne(
    { _id: orderId },
    {
      $set: {
        stripeSessionId: session.id,
        updatedAt: new Date(),
      },
    },
  );
}

export async function markOrderPaidFromCheckoutSession(
  session: Stripe.Checkout.Session,
  eventId: string,
) {
  if (session.payment_status !== "paid" && session.payment_status !== "no_payment_required") {
    return;
  }

  const orderId = session.metadata?.orderId;
  if (!orderId) {
    throw new Error(`Checkout Session ${session.id} is missing metadata.orderId.`);
  }

  const db = await minestomDb();
  const now = new Date();
  const paymentIntentId = typeof session.payment_intent === "string" ? session.payment_intent : undefined;
  const customerId = typeof session.customer === "string" ? session.customer : undefined;
  const subscriptionId = typeof session.subscription === "string" ? session.subscription : undefined;
  const setFields: Partial<CheckoutOrder> = {
    status: "PAID",
    paidAt: now,
    updatedAt: now,
    stripeSessionId: session.id,
    nextAttemptAt: now,
  };

  if (session.amount_total != null) {
    setFields.amountTotal = session.amount_total;
  }
  if (paymentIntentId) {
    setFields.stripePaymentIntentId = paymentIntentId;
  }
  if (customerId) {
    setFields.stripeCustomerId = customerId;
  }
  if (subscriptionId) {
    setFields.stripeSubscriptionId = subscriptionId;
  }

  await db.collection<CheckoutOrder>("store_purchases").updateOne(
    { _id: orderId, status: { $nin: ["FULFILLED", "REFUNDED"] } },
    {
      $set: setFields,
      $addToSet: {
        stripeEventIds: eventId,
      },
    },
  );
}

export async function markOrderExpiredFromCheckoutSession(
  session: Stripe.Checkout.Session,
  eventId: string,
) {
  const orderId = session.metadata?.orderId;
  if (!orderId) return;

  const db = await minestomDb();
  await db.collection<CheckoutOrder>("store_purchases").updateOne(
    { _id: orderId, status: "CREATED" },
    {
      $set: {
        status: "EXPIRED",
        stripeSessionId: session.id,
        updatedAt: new Date(),
      },
      $addToSet: {
        stripeEventIds: eventId,
      },
    },
  );
}

export async function markOrderRefundedFromCharge(charge: Stripe.Charge, eventId: string) {
  const paymentIntentId = typeof charge.payment_intent === "string" ? charge.payment_intent : undefined;
  if (!paymentIntentId) return;

  const db = await minestomDb();
  await db.collection<CheckoutOrder>("store_purchases").updateOne(
    { stripePaymentIntentId: paymentIntentId },
    {
      $set: {
        status: "REFUNDED",
        updatedAt: new Date(),
        lastError: "Stripe charge was refunded. Entitlement reversal requires manual review.",
      },
      $addToSet: {
        stripeEventIds: eventId,
      },
    },
  );
}

export async function tryAcquireStripeEvent(event: Stripe.Event) {
  const db = await minestomDb();
  const now = new Date();

  try {
    await db.collection<StripeEventDocument>("stripe_events").insertOne({
      _id: event.id,
      type: event.type,
      status: "PROCESSING",
      livemode: event.livemode,
      createdAt: now,
      updatedAt: now,
      attempts: 1,
    });
    return true;
  } catch (error) {
    if ((error as { code?: number }).code !== 11000) {
      throw error;
    }

    const existing = await db.collection<StripeEventDocument>("stripe_events").findOne({ _id: event.id });
    if (existing?.status === "PROCESSED") {
      return false;
    }

    await db.collection<StripeEventDocument>("stripe_events").updateOne(
      { _id: event.id, status: { $ne: "PROCESSED" } },
      {
        $set: { status: "PROCESSING", updatedAt: now },
        $inc: { attempts: 1 },
      },
    );
    return true;
  }
}

export async function markStripeEventProcessed(eventId: string) {
  const db = await minestomDb();
  await db.collection<StripeEventDocument>("stripe_events").updateOne(
    { _id: eventId },
    {
      $set: {
        status: "PROCESSED",
        updatedAt: new Date(),
      },
      $unset: {
        error: "",
      },
    },
  );
}

export async function markStripeEventFailed(eventId: string, error: unknown) {
  const db = await minestomDb();
  await db.collection<StripeEventDocument>("stripe_events").updateOne(
    { _id: eventId },
    {
      $set: {
        status: "FAILED",
        updatedAt: new Date(),
        error: error instanceof Error ? error.message : String(error),
      },
    },
  );
}
