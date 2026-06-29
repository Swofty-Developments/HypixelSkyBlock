import Stripe from "stripe";

const globalForStripe = globalThis as typeof globalThis & {
  hypixelStripe?: Stripe;
};

export function stripeClient() {
  if (!globalForStripe.hypixelStripe) {
    const secretKey = process.env.STRIPE_SECRET_KEY;
    if (!secretKey) {
      throw new Error("STRIPE_SECRET_KEY is not configured.");
    }

    globalForStripe.hypixelStripe = new Stripe(secretKey, {
      appInfo: {
        name: "Hypixel Store",
        version: "1.0.0",
      },
      typescript: true,
    });
  }

  return globalForStripe.hypixelStripe;
}

export function stripeWebhookSecret() {
  const secret = process.env.STRIPE_WEBHOOK_SECRET;
  if (!secret) {
    throw new Error("STRIPE_WEBHOOK_SECRET is not configured.");
  }
  return secret;
}
