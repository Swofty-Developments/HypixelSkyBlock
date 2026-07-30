import { NextRequest, NextResponse } from "next/server";
import Stripe from "stripe";
import {
  markOrderExpiredFromCheckoutSession,
  markOrderPaidFromCheckoutSession,
  markOrderRefundedFromCharge,
  markStripeEventFailed,
  markStripeEventProcessed,
  tryAcquireStripeEvent,
} from "@/lib/server/orders";
import { stripeClient, stripeWebhookSecret } from "@/lib/server/stripe";

export const runtime = "nodejs";

export async function POST(request: NextRequest) {
  const signature = request.headers.get("stripe-signature");
  if (!signature) {
    return NextResponse.json({ error: "Missing Stripe signature." }, { status: 400 });
  }

  let event: Stripe.Event;
  const payload = await request.text();

  try {
    event = stripeClient().webhooks.constructEvent(payload, signature, stripeWebhookSecret());
  } catch (error) {
    return NextResponse.json(
      { error: error instanceof Error ? error.message : "Invalid Stripe webhook." },
      { status: 400 },
    );
  }

  const acquired = await tryAcquireStripeEvent(event);
  if (!acquired) {
    return NextResponse.json({ received: true, duplicate: true });
  }

  try {
    switch (event.type) {
      case "checkout.session.completed":
      case "checkout.session.async_payment_succeeded":
        await markOrderPaidFromCheckoutSession(event.data.object as Stripe.Checkout.Session, event.id);
        break;
      case "checkout.session.expired":
        await markOrderExpiredFromCheckoutSession(event.data.object as Stripe.Checkout.Session, event.id);
        break;
      case "charge.refunded":
        await markOrderRefundedFromCharge(event.data.object as Stripe.Charge, event.id);
        break;
      default:
        break;
    }

    await markStripeEventProcessed(event.id);
    return NextResponse.json({ received: true });
  } catch (error) {
    await markStripeEventFailed(event.id, error);
    throw error;
  }
}
