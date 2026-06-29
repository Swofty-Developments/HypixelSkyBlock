# Hypixel Store

Next.js storefront for Stripe-hosted checkout. The store records paid purchases in MongoDB; `ServiceStore` performs
in-game fulfillment through the Velocity proxy.

## Environment

```bash
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
STORE_PUBLIC_URL=http://localhost:3000
MONGODB_URI=mongodb://localhost:27017
MONGODB_DATABASE=Minestom
STRIPE_AUTOMATIC_TAX=false
```

## Development

```bash
npm install
npm run dev
```

Forward Stripe webhooks locally:

```bash
stripe listen --forward-to http://localhost:3000/api/stripe/webhook
```

Use the printed webhook secret as `STRIPE_WEBHOOK_SECRET`.

## Production Notes

- Configure the Stripe webhook endpoint at `/api/stripe/webhook`.
- Subscribe to `checkout.session.completed`, `checkout.session.async_payment_succeeded`, `checkout.session.expired`, and
  `charge.refunded`.
- Keep `ServiceStore.jar` running with MongoDB and Redis access so paid purchases can be fulfilled.
- Fulfillment is recoverable: purchases paid while the service is offline remain in MongoDB and are retried when it
  returns.
