import { NextRequest, NextResponse } from "next/server";
import { productById } from "@/data/store";
import { attachStripeSession, createCheckoutOrder, priceToMinorUnits } from "@/lib/server/orders";
import { findPlayerByUsername } from "@/lib/server/mongodb";
import { stripeClient } from "@/lib/server/stripe";

export const runtime = "nodejs";

type CheckoutRequest = {
  productId?: string;
  username?: string;
};

function publicOrigin(request: NextRequest) {
  return (
    process.env.STORE_PUBLIC_URL ??
    process.env.NEXT_PUBLIC_STORE_URL ??
    request.nextUrl.origin
  ).replace(/\/$/, "");
}

function cleanPlayerName(raw: string) {
  return raw.trim().replace(/[^A-Za-z0-9_]/g, "").slice(0, 16);
}

export async function POST(request: NextRequest) {
  let body: CheckoutRequest;
  try {
    body = await request.json();
  } catch {
    return NextResponse.json({ error: "Invalid checkout request." }, { status: 400 });
  }

  const productId = body.productId?.trim();
  const username = body.username ? cleanPlayerName(body.username) : "";
  const product = productId ? productById.get(productId) : null;

  if (!product) {
    return NextResponse.json({ error: "Unknown store product." }, { status: 404 });
  }

  if (!username) {
    return NextResponse.json({ error: "Enter a valid Minecraft username before checkout." }, { status: 400 });
  }

  const player = await findPlayerByUsername(username);
  if (!player) {
    return NextResponse.json(
      { error: "That player has not joined this server yet." },
      { status: 404 },
    );
  }

  const playerName = typeof player.ign === "string"
    ? player.ign.replace(/^"|"$/g, "")
    : username;
  const order = await createCheckoutOrder({ uuid: player._id, name: playerName }, product);
  const origin = publicOrigin(request);
  const metadata = {
    orderId: order._id,
    playerUuid: order.playerUuid,
    playerName: order.playerName,
    productId: product.id,
    productName: product.name,
  };

  const session = await stripeClient().checkout.sessions.create(
    {
      mode: "payment",
      client_reference_id: order.playerUuid,
      customer_creation: "always",
      line_items: [
        {
          quantity: 1,
          price_data: {
            currency: order.currency,
            unit_amount: priceToMinorUnits(product.price),
            product_data: {
              name: product.name,
              description: product.description,
              metadata: {
                productId: product.id,
              },
            },
          },
        },
      ],
      metadata,
      payment_intent_data: {
        metadata,
      },
      automatic_tax: {
        enabled: process.env.STRIPE_AUTOMATIC_TAX === "true",
      },
      success_url: `${origin}/checkout/success?session_id={CHECKOUT_SESSION_ID}`,
      cancel_url: `${origin}/checkout/cancelled?order_id=${order._id}`,
    },
    {
      idempotencyKey: `checkout:${order._id}`,
    },
  );

  await attachStripeSession(order._id, session);

  return NextResponse.json({
    orderId: order._id,
    url: session.url,
  });
}
