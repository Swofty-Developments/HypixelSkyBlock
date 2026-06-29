"use client";

import { RefreshCcw, ShoppingCart } from "lucide-react";

export default function BuyButton({
  label = "BUY",
  onClick,
  disabled = false,
  loading = false,
}: {
  label?: "BUY" | "SUBSCRIBE";
  onClick?: () => void;
  disabled?: boolean;
  loading?: boolean;
}) {
  const Icon = label === "SUBSCRIBE" ? RefreshCcw : ShoppingCart;

  return (
    <button className="buy-button" type="button" onClick={onClick} disabled={disabled || loading}>
      <Icon size={15} strokeWidth={3} />
      {loading ? "WAIT" : label}
    </button>
  );
}
