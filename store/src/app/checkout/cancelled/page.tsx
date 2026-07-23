import Link from "next/link";
import StoreShell from "@/components/store/StoreShell";

export default function CheckoutCancelledPage() {
  return (
    <StoreShell>
      <main className="checkout-result">
        <h1>Checkout cancelled</h1>
        <p>No payment was completed. You can return to the store whenever you are ready.</p>
        <Link className="checkout-result-button" href="/">Return to store</Link>
      </main>
    </StoreShell>
  );
}
