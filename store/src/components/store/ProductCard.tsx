"use client";

import Image from "next/image";
import { Product } from "@/data/store";
import BuyButton from "./BuyButton";

export default function ProductCard({
  product,
  onBuy,
}: {
  product: Product;
  onBuy?: (product: Product) => void;
}) {
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
        <BuyButton label={product.cta ?? "BUY"} onClick={() => onBuy?.(product)} />
      </div>
    </article>
  );
}
