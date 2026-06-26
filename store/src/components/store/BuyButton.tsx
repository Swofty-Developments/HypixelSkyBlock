"use client";

import { RefreshCcw, ShoppingCart } from "lucide-react";

export default function BuyButton({
  label = "BUY",
  onClick,
}: {
  label?: "BUY" | "SUBSCRIBE";
  onClick?: () => void;
}) {
  const Icon = label === "SUBSCRIBE" ? RefreshCcw : ShoppingCart;

  return (
    <button className="buy-button" type="button" onClick={onClick}>
      <Icon size={15} strokeWidth={3} />
      {label}
    </button>
  );
}
