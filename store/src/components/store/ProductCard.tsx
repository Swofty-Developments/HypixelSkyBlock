"use client";

import Image from "next/image";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Product } from "@/data/store";
import BuyButton from "./BuyButton";

export default function ProductCard({
  product,
  onBuy,
}: {
  product: Product;
  onBuy?: (product: Product) => void;
}) {
  const router = useRouter();
  const [isCheckingOut, setIsCheckingOut] = useState(false);
  const [checkoutError, setCheckoutError] = useState<string | null>(null);

  async function startCheckout() {
    if (onBuy) {
      onBuy(product);
      return;
    }

    const username = localStorage.getItem("hypixel_username")?.trim();
    if (!username) {
      router.push(`/login?redirect=${encodeURIComponent(window.location.pathname)}`);
      return;
    }

    setIsCheckingOut(true);
    setCheckoutError(null);

    try {
      const response = await fetch("/api/checkout/sessions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productId: product.id, username }),
      });
      const payload = await response.json();

      if (!response.ok || !payload.url) {
        throw new Error(payload.error ?? "Unable to start checkout.");
      }

      window.location.assign(payload.url);
    } catch (error) {
      setCheckoutError(error instanceof Error ? error.message : "Unable to start checkout.");
      setIsCheckingOut(false);
    }
  }

  return (
    <article className={`product-card ${product.sale || product.bestValue ? "product-card-sale" : ""} ${product.bestValue ? "best-value" : ""} ${product.wide ? "wide" : ""}`}>
      {(product.sale || product.bestValue) && (
        <div className="sale-ribbon">
          {product.bestValue ? "BEST VALUE!" : "🌂 20% OFF 🌂"}
        </div>
      )}

      <div className="product-body">
        <Image
          src={product.image}
          alt=""
          width={180}
          height={180}
          className="product-image"
          unoptimized
        />
        <h2>{product.name}</h2>
        {product.bonus && <strong className="bonus-line">{product.bonus}</strong>}
        <p>{product.description}</p>
        <div className="price-block">
          {product.originalPrice && (
            <span className="original-price">{product.originalPrice} USD</span>
          )}
          <strong>{product.price} USD</strong>
        </div>
        <BuyButton label={product.cta ?? "BUY"} onClick={startCheckout} loading={isCheckingOut} />
        {checkoutError && <p className="checkout-error">{checkoutError}</p>}
      </div>
    </article>
  );
}
