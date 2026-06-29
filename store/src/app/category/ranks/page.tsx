"use client";

import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import BuyButton from "@/components/store/BuyButton";
import ProductCard from "@/components/store/ProductCard";
import { directProducts } from "@/data/store";

// Easter theme colors
const saleColor = "rgb(140, 163, 45)"; // green for easter
const grayBorder = "rgb(199, 199, 199)";
const checkColor = "rgb(120, 174, 50)";

const ranks = [
  { name: "VIP Rank", productId: "rank-vip", icon: "/images/ranks/vip.png", prefix: "/images/perks/vip-prefix.png", prefixAlt: "A green VIP prefix", originalPrice: "7.69", salePrice: "6.15", sale: true },
  { name: "VIP+ Rank", productId: "rank-vip-plus", icon: "/images/ranks/vip-plus.png", prefix: "/images/perks/vip-plus-prefix.png", prefixAlt: "A green VIP prefix followed by a golden plus", originalPrice: "16.49", salePrice: "13.19", sale: true },
  { name: "MVP Rank", productId: "rank-mvp", icon: "/images/ranks/mvp.png", prefix: "/images/perks/mvp-prefix.png", prefixAlt: "An aqua MVP prefix", originalPrice: "32.99", salePrice: "26.39", sale: true },
  { name: "MVP+ Rank", productId: "rank-mvp-plus", icon: "/images/ranks/mvp-plus.png", prefix: "/images/perks/mvp-plus-prefix.png", prefixAlt: "An aqua MVP prefix followed by a red plus", originalPrice: "49.49", salePrice: "39.59", sale: true },
  { name: "MVP++ Rank", icon: "/images/ranks/mvp-plus-plus.png", prefix: "/images/perks/mvp-plus-plus-prefix.png", prefixAlt: "A golden MVP prefix followed by two red plusses", originalPrice: null, salePrice: null, sale: false, isSub: true },
];

type FVal = boolean | string;
const features: { name: string; values: FVal[] }[] = [
  { name: "Game Replays (/games) Command", values: [true, true, true, true, true] },
  { name: "No chat cooldown", values: [true, true, true, true, true] },
  { name: "Rank-specific Cosmetics", values: [true, true, true, true, true] },
  { name: "Event Silver Multiplier", values: ["1.5x", "2x", "2.5x", "3x", "3.5x"] },
  { name: "Housing House Slots", values: ["2", "2", "3", "3", "3"] },
  { name: "Priority Queue", values: [true, true, true, true, true] },
  { name: "Extra SkyBlock Wardrobe Slots", values: ["+3 (5 total)", "+7 (9 total)", "+11 (13 total)", "+16 (18 total)", "+16 (18 total)"] },
  { name: "SkyBlock Quick Crafting", values: [true, true, true, true, true] },
  { name: "Access Seasonal SkyBlock Salesman", values: [true, true, true, true, true] },
  { name: "Extended Replay Access", values: ["1 day", "2 days", "3 days", "4 days", "4 days"] },
  { name: "Create Guilds", values: [false, true, true, true, true] },
  { name: 'Change your plus ("+") color', values: [false, false, false, true, true] },
  { name: "Game Map Selectors", values: [false, false, false, true, true] },
  { name: "Achievements Tracking", values: [false, false, false, true, true] },
  { name: "SkyBlock Epic Warp Scrolls", values: [false, false, false, true, true] },
  { name: "Free Monthly SkyBlock Gems", values: [false, false, false, false, "500 Monthly SkyBlock Gems"] },
  { name: "Statuses", values: [false, false, false, false, true] },
  { name: "Private Games (/party private) Command", values: [false, false, false, false, true] },
  { name: "Nickname (/nick) Command", values: [false, false, false, false, true] },
  { name: "Golden Guild Tag", values: [false, false, false, false, true] },
  { name: "Exclusive In-Game Emojis", values: [false, false, false, false, true] },
];

const Check = () => <svg style={{ width: 22, height: 22, display: "inline", color: checkColor }} viewBox="0 0 512 512" fill="currentColor"><path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM369 209L241 337c-9.4 9.4-24.6 9.4-33.9 0l-64-64c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0l47 47L335 175c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9z" /></svg>;
const Cross = () => <svg style={{ width: 22, height: 22, display: "inline", color: "gray" }} viewBox="0 0 512 512" fill="currentColor"><path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c9.4-9.4 24.6-9.4 33.9 0l47 47 47-47c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-47 47 47 47c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-47-47-47 47c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l47-47-47-47c-9.4-9.4-9.4-24.6 0-33.9z" /></svg>;

const mvpPlusPlusProducts = directProducts.filter((product) => product.id.startsWith("rank-mvp-plus-plus"));

const mvpFeatures = [
  "New Rank Prefix and Color",
  "Monthly 500 SkyBlock Gems Included",
  "Access to Nickname (/nick) Command",
  "Access to /stream Command",
  "Access to Private Games (/party private) Command",
  "Golden Guild Tag",
  "Animated Forum Frame and Private Forum",
  "Auto-Tip Enabled",
  "New Unique Join Message and Particle Effect on Lobby Join",
  "Exclusive In-Game Emojis",
  'Golden "GG" at the End of Games',
];

// Cell border helper
const valBorder = (sale: boolean) => sale ? `2px solid ${saleColor}` : `2px solid ${grayBorder}`;
const valBorderBottom = (sale: boolean) => sale ? `1px solid ${saleColor}` : `1px solid ${grayBorder}`;

export default function RanksPage() {
  const router = useRouter();
  const [showMore, setShowMore] = useState(false);
  const [mvpMenu, setMvpMenu] = useState(false);
  const [loadingProductId, setLoadingProductId] = useState<string | null>(null);
  const [checkoutError, setCheckoutError] = useState<string | null>(null);

  async function startCheckout(productId: string) {
    const username = localStorage.getItem("hypixel_username")?.trim();
    if (!username) {
      router.push(`/login?redirect=${encodeURIComponent(window.location.pathname)}`);
      return;
    }

    setCheckoutError(null);
    setLoadingProductId(productId);

    try {
      const response = await fetch("/api/checkout/sessions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productId, username }),
      });
      const payload = await response.json();

      if (!response.ok || !payload.url) {
        throw new Error(payload.error ?? "Unable to start checkout.");
      }

      window.location.assign(payload.url);
    } catch (error) {
      setCheckoutError(error instanceof Error ? error.message : "Unable to start checkout.");
      setLoadingProductId(null);
    }
  }

  if (mvpMenu) {
    return (
      <div className="store-page">
        <div className="store-container">
          <Navbar username="ArikSquad" />
          <main className="category-content">
            <h1>Rank Upgrades</h1>
            <div className="product-grid" style={{ gridTemplateColumns: "repeat(3, minmax(0, 1fr))", maxWidth: 980 }}>
              {mvpPlusPlusProducts.map((product) => (
                <ProductCard key={product.name} product={product} />
              ))}
            </div>
            <button type="button" className="rank-back-button" onClick={() => setMvpMenu(false)}>‹ GO BACK TO ALL RANKS</button>
            <section className="copy-block">
              <h2>About Rank Upgrades</h2>
              <p>MVP++ is an exclusive rank upgrade to your existing MVP+ rank. This rank upgrade allows you to access some very useful commands and is the best way to support the Hypixel Network.</p>
              <p>MVP++ is a recurring subscription that will be charged every 30 days, 3 months, or 1 year depending on the option you choose. Any prepaid days you already have will be paused if you choose to subscribe.</p>
              <p>MVP++ is not a lifetime rank and will expire after the subscription is cancelled or the prepaid time has passed. Once it expires, you will automatically go back to MVP+.</p>
            </section>
            <section className="copy-block">
              <h2>Need help?</h2>
              <p>If you have any questions or issues related to payments, <Link href="https://support.hypixel.net/" style={{ color: "rgb(230, 174, 71)" }}>send us a ticket here</Link>, and we will reply as fast as possible.</p>
              <p>You can <Link href="https://subscriptions.hypixel.net/" style={{ color: "rgb(230, 174, 71)" }}>manage or cancel your subscriptions by visiting our subscription portal here</Link>.</p>
            </section>
            <section className="features-block">
              <h2>Features</h2>
              {mvpFeatures.map((feature) => (
                <div className="feature-panel" key={feature}>
                  <summary>{feature}</summary>
                </div>
              ))}
            </section>
          </main>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="store-page">
      <div style={{ width: "1250px", maxWidth: "1250px", margin: "0 auto", padding: "0 15px", fontFamily: "Raleway, sans-serif", fontSize: "14px", color: "var(--text-color)" }}>
        <Navbar username="ArikSquad" />

        <div style={{ padding: "10px 15px", fontSize: "38.4px", fontWeight: 400, fontFamily: "Neuton, Raleway, serif" }}>
          Ranks
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: "50px", padding: "0 15px", fontSize: "16px" }}>
          {checkoutError && <p className="checkout-error">{checkoutError}</p>}
          {/* Ranks Table */}
          <table style={{ width: "1232px", borderCollapse: "separate", borderSpacing: "6px 0px", fontFamily: "Raleway, serif", fontSize: "16px", fontWeight: 700 }}>
            <thead>
              {/* Icon Row */}
              <tr>
                <th style={{ padding: "10px 25px", textAlign: "center", verticalAlign: "middle", minWidth: "170px", border: "2px solid transparent" }} />
                {ranks.map((r) => (
                  <th key={r.name} style={{
                    padding: "45px 25px 10px", textAlign: "center", verticalAlign: "top",
                    backgroundColor: "#fff", borderRadius: "8px 8px 0 0",
                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                    borderTop: `2px solid ${r.sale ? saleColor : grayBorder}`,
                    borderBottom: "0px none",
                    position: "relative", minWidth: "170px",
                  }}>
                    {r.sale && (
                      <div style={{
                        position: "absolute", top: "-1px", left: "-1px", right: "-1px",
                        backgroundColor: saleColor, color: "#000",
                        fontSize: "16px", fontWeight: 700, height: "34px",
                        lineHeight: "34px", textAlign: "center",
                        borderRadius: "8px 8px 0 0",
                      }}>
                        20% Off!
                      </div>
                    )}
                    <Image src={r.icon} alt={`${r.name} icon`} width={144} height={144} unoptimized style={{ width: "144px", height: "144px", objectFit: "contain" }} />
                  </th>
                ))}
              </tr>
              {/* Name Row */}
              <tr id="header-row">
                <th style={{ padding: "10px 25px", border: "2px solid transparent" }} />
                {ranks.map((r) => (
                  <th key={r.name} style={{
                    padding: "10px 25px", textAlign: "center", backgroundColor: "#fff",
                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                    borderTop: "0px none", borderBottom: "0px none",
                  }}>
                    <div style={{ color: "rgb(230, 174, 71)", fontSize: "22px", fontWeight: 700 }}>{r.name}</div>
                  </th>
                ))}
              </tr>
              {/* Description Row */}
              <tr>
                <th style={{ padding: "10px 25px", border: "2px solid transparent" }} />
                {ranks.map((r) => (
                  <th key={r.name} style={{
                    padding: "10px 20px", textAlign: "center", backgroundColor: "#fff",
                    fontSize: "13px", fontWeight: 400, color: "#555",
                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                    borderTop: "0px none", borderBottom: "0px none",
                  }}>
                    Receive the {r.name} on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.
                  </th>
                ))}
              </tr>
              {/* Price + Buy Row */}
              <tr>
                <th style={{ padding: "10px 25px", border: "2px solid transparent" }} />
                {ranks.map((r) => (
                  <th key={r.name} style={{
                    padding: "10px 25px 15px", textAlign: "center", backgroundColor: "#fff",
                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                    borderTop: "0px none", borderBottom: "0px none",
                  }}>
                    {r.isSub ? (
                      <div style={{ marginBottom: "8px" }}>
                        <Link href="#" style={{ color: "rgb(230, 174, 71)", fontSize: "13px", fontStyle: "italic" }}>Manage subscription</Link>
                      </div>
                    ) : (
                      <div style={{ marginBottom: "8px" }}>
                        {r.sale && r.originalPrice && (
                          <span style={{ color: "red", fontSize: "14px", textDecoration: "line-through" }}>{r.originalPrice} USD</span>
                        )}
                        <br />
                        <span style={{ fontSize: "21.6px", fontWeight: 700 }}>{r.salePrice} USD</span>
                      </div>
                    )}
                    <BuyButton
                      label={r.isSub ? "SUBSCRIBE" : "BUY"}
                      onClick={() => r.isSub ? setMvpMenu(true) : startCheckout(r.productId!)}
                      loading={Boolean(r.productId && loadingProductId === r.productId)}
                    />
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {/* Chat Prefix Row */}
              <tr>
                <td style={{
                  padding: "10px 25px", textAlign: "left", verticalAlign: "middle",
                  backgroundColor: "rgb(242, 242, 242)",
                  borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                  borderTop: `2px solid ${grayBorder}`, borderBottom: `1px solid ${grayBorder}`,
                }}>Chat Prefix</td>
                {ranks.map((r) => (
                  <td key={r.name} style={{
                    padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                    backgroundColor: "rgb(242, 242, 242)",
                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                    borderTop: `1px solid ${r.sale ? saleColor : grayBorder}`,
                    borderBottom: valBorderBottom(r.sale),
                  }}>
                    {/* eslint-disable-next-line @next/next/no-img-element */}
                    <img src={r.prefix} alt={r.prefixAlt} style={{ height: "28px", width: "auto", imageRendering: "pixelated" }} />
                  </td>
                ))}
              </tr>
              {/* Feature Rows */}
              {features.map((feat, idx) => (
                <tr key={feat.name}>
                  <td style={{
                    padding: "10px 25px", textAlign: "left", verticalAlign: "middle",
                    backgroundColor: idx % 2 === 0 ? "#fff" : "rgb(242, 242, 242)",
                    borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                    borderBottom: `1px solid ${grayBorder}`, borderTop: "0px none",
                  }}>{feat.name}</td>
                  {feat.values.map((val, i) => (
                    <td key={i} style={{
                      padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                      backgroundColor: idx % 2 === 0 ? "#fff" : "rgb(242, 242, 242)",
                      borderLeft: valBorder(ranks[i].sale), borderRight: valBorder(ranks[i].sale),
                      borderBottom: valBorderBottom(ranks[i].sale), borderTop: "0px none",
                    }}>
                      {val === true ? <Check /> : val === false ? <Cross /> : <span style={{ fontWeight: 400 }}>{val}</span>}
                    </td>
                  ))}
                </tr>
              ))}
              {/* See More */}
              <tr>
                <td colSpan={6} style={{
                  padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                  backgroundColor: "rgb(242, 242, 242)",
                  borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                  borderBottom: `1px solid ${grayBorder}`, borderTop: "0px none",
                  cursor: "pointer", fontWeight: 400,
                }} onClick={() => setShowMore(!showMore)}>
                  <svg style={{ display: "inline", width: 12, height: 12, marginRight: 4, transform: showMore ? "rotate(180deg)" : undefined }} viewBox="0 0 320 512" fill="currentColor"><path d="M137.4 374.6c12.5 12.5 32.8 12.5 45.3 0l128-128c9.2-9.2 11.9-22.9 6.9-34.9s-16.6-19.8-29.6-19.8L32 192c-12.9 0-24.6 7.8-29.6 19.8s-2.2 25.7 6.9 34.9l128 128z" /></svg>
                  Click here to see more features
                  <svg style={{ display: "inline", width: 12, height: 12, marginLeft: 4, transform: showMore ? "rotate(180deg)" : undefined }} viewBox="0 0 320 512" fill="currentColor"><path d="M137.4 374.6c12.5 12.5 32.8 12.5 45.3 0l128-128c9.2-9.2 11.9-22.9 6.9-34.9s-16.6-19.8-29.6-19.8L32 192c-12.9 0-24.6 7.8-29.6 19.8s-2.2 25.7 6.9 34.9l128 128z" /></svg>
                </td>
              </tr>
              {/* Bottom Buy Row */}
              <tr>
                <td style={{ padding: "10px 25px", border: "2px solid transparent" }} />
                {ranks.map((r) => (
                  <td key={r.name} style={{
                    padding: "10px 25px", textAlign: "center", backgroundColor: "#fff",
                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                    borderBottom: `2px solid ${r.sale ? saleColor : grayBorder}`,
                    borderTop: "0px none",
                    borderRadius: "0 0 8px 8px",
                  }}>
                    <BuyButton
                      label={r.isSub ? "SUBSCRIBE" : "BUY"}
                      onClick={() => r.isSub ? setMvpMenu(true) : startCheckout(r.productId!)}
                      loading={Boolean(r.productId && loadingProductId === r.productId)}
                    />
                  </td>
                ))}
              </tr>
            </tbody>
          </table>

          {/* About Ranks */}
          <div style={{ width: "854px", margin: "0 183px", fontSize: "16px" }}>
            <h3 style={{ fontSize: "24px", fontWeight: 500, marginBottom: "10px" }}>About Ranks</h3>
            <p style={{ marginBottom: "16px" }}>
              A Hypixel Rank is a fun way to help support the Hypixel Server! With different perks, commands, and rank colors, this table above is to help you figure out which rank best suits you.
            </p>
            <p style={{ marginBottom: "16px" }}>
              If you already have a rank, the value of your current rank is deducted from your future rank upgrades. This upgrade price will be displayed automatically as you browse this page.
            </p>
            <h3 style={{ fontSize: "24px", fontWeight: 500, marginBottom: "10px" }}>Need help?</h3>
            <p style={{ marginBottom: "8px" }}>
              If you have any questions or issues related to payments, <Link href="https://support.hypixel.net/" style={{ color: "rgb(230, 174, 71)" }}>send us a ticket here</Link>, and we will reply as fast as possible.
            </p>
            <p>
              You can <Link href="https://subscriptions.hypixel.net/" style={{ color: "rgb(230, 174, 71)" }}>manage or cancel your subscriptions by visiting our subscription portal here</Link>.
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}
