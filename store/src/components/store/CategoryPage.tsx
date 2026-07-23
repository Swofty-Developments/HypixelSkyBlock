"use client";

import Link from "next/link";
import { Category } from "@/data/store";
import StoreShell from "./StoreShell";
import ProductCard from "./ProductCard";

function HelpBlock() {
  return (
    <section className="copy-block">
      <h2>Need help?</h2>
      <p>
        If you have any questions or issues related to payments,{" "}
        <Link href="https://support.hypixel.net/">send us a ticket here</Link>,
        {" "}and we will reply as fast as possible.
      </p>
      <p>
        You can{" "}
        <Link href="https://subscriptions.hypixel.net/">
          manage or cancel your subscriptions by visiting our subscription portal here
        </Link>.
      </p>
    </section>
  );
}

export default function CategoryPage({ category }: { category: Category }) {
  return (
    <StoreShell>
      <main className="category-content">
        <h1>{category.title}</h1>

        <div className={`product-grid product-grid-${category.slug}`}>
          {category.products.map((product) => (
            <ProductCard key={product.name} product={product} />
          ))}
        </div>

        <section className="copy-block">
          <h2>{category.aboutTitle}</h2>
          {category.about.map((paragraph) => (
            <p key={paragraph}>{paragraph}</p>
          ))}
          {category.slug === "gold" && (
            // eslint-disable-next-line @next/next/no-img-element
            <img className="gold-about-image" src="/images/in-game-store.png" alt="Hypixel in-game store" />
          )}
          {category.bullets && (
            <ul>
              {category.bullets.map((bullet) => (
                <li key={bullet}>{bullet}</li>
              ))}
            </ul>
          )}
          {category.note && <p><strong>{category.note}</strong></p>}
        </section>

        <HelpBlock />

        {category.features && (
          <section className="features-block">
            <h2>Features</h2>
            {category.features.map((feature) => (
              <details key={feature.title} className="feature-panel">
                <summary>{feature.title}</summary>
                <div className="feature-panel-body">
                  {feature.text?.map((line) => <p key={line}>{line}</p>)}
                  {feature.images?.map((image) => (
                    // eslint-disable-next-line @next/next/no-img-element
                    <img key={image} src={image} alt="" />
                  ))}
                </div>
              </details>
            ))}
          </section>
        )}
      </main>
    </StoreShell>
  );
}
