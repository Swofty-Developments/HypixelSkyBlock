import Link from "next/link";
import StoreShell from "@/components/store/StoreShell";

export default function CheckoutSuccessPage() {
  return (
    <StoreShell>
      <main className="checkout-result">
        <h1>Payment received</h1>
        <p>Your purchase is being delivered to your account. Most purchases appear in-game within a few seconds.</p>
        <p>If the network is restarting, delivery resumes automatically when the store service comes back online.</p>
        <Link className="checkout-result-button" href="/">Return to store</Link>
      </main>
    </StoreShell>
  );
}
