# Store Payments

The web store uses Stripe-hosted Checkout for payment collection and a separate `ServiceStore` process for durable
in-game fulfillment.

## Architecture

1. The store creates a local `store_purchases` document before sending the buyer to Stripe Checkout.
2. Stripe sends a signed webhook to `/api/stripe/webhook` after payment.
3. The webhook marks the purchase `PAID` in MongoDB.
4. `ServiceStore` leases paid purchases, asks the Velocity proxy to apply entitlements, and marks each purchase
   `FULFILLED`.
5. If the service or proxy is offline, the purchase stays `PAID` or `FAILED` and is retried automatically.

The proxy applies purchase IDs idempotently in `store-player-entitlements`, so replayed webhooks or restarted workers do
not grant duplicate Gold, Gems, cosmetics, boosters, or ranks.

## Store Environment

Set these variables for the Next.js store:

```bash
STRIPE_SECRET_KEY=sk_live_...
STRIPE_WEBHOOK_SECRET=whsec_...
STORE_PUBLIC_URL=https://store.example.com
MONGODB_URI=mongodb://localhost:27017
MONGODB_DATABASE=Minestom
STRIPE_AUTOMATIC_TAX=false
```

Use test-mode keys (`sk_test_...`) outside production.

## Stripe Dashboard Setup

In Stripe:

1. Create or copy the secret API key from **Developers -> API keys**.
2. Create a webhook endpoint at `https://store.example.com/api/stripe/webhook`.
3. Subscribe the endpoint to:
    - `checkout.session.completed`
    - `checkout.session.async_payment_succeeded`
    - `checkout.session.expired`
    - `charge.refunded`
4. Copy the webhook signing secret into `STRIPE_WEBHOOK_SECRET`.
5. Configure payment methods in **Settings -> Payment methods**.
6. If you set `STRIPE_AUTOMATIC_TAX=true`, configure Stripe Tax registrations and origin address first.

Stripe references:

- [Checkout Sessions](https://docs.stripe.com/payments/checkout-sessions)
- [Stripe-hosted Checkout quickstart](https://docs.stripe.com/checkout/quickstart)
- [Webhook signature verification](https://docs.stripe.com/webhooks/signature)

## Running Fulfillment

`ServiceStore` uses the same `configuration/config.yml` MongoDB and Redis settings as the other services.

```bash
java -jar ServiceStore.jar
```

For Docker deployments, the provided `docker-compose.yml` includes `service_store`.

## Local Webhook Testing

Install the Stripe CLI, then forward events to the local store:

```bash
stripe listen --forward-to http://localhost:3000/api/stripe/webhook
```

Use the printed `whsec_...` value as `STRIPE_WEBHOOK_SECRET` while testing.
