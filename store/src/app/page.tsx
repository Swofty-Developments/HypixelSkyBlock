"use client";

import Image from "next/image";
import Link from "next/link";
import { useEffect, useState } from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { categories } from "@/data/store";

function CountdownTimer({ targetSeconds }: { targetSeconds: number }) {
  const [remaining, setRemaining] = useState(targetSeconds);
  useEffect(() => {
    const interval = setInterval(() => {
      setRemaining((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(interval);
  }, []);
  const days = Math.floor(remaining / 86400);
  const hours = Math.floor((remaining % 86400) / 3600);
  const minutes = Math.floor((remaining % 3600) / 60);
  const seconds = remaining % 60;
  return <span>{days}d {hours}h {minutes}m {seconds}s</span>;
}

export default function Home() {
  return (
    <div className="store-page">
      <div className="store-container">
        <Navbar />

        {/* Featured Image / Banner */}
        <div style={{ position: "relative", marginBottom: "35px" }}>
          <Link href="/category/ranks" style={{ position: "relative", display: "block" }}>
            <Image
              src="/images/easter-sale-banner.png"
              alt='Easter Sale Banner'
              width={3508}
              height={1168}
              style={{ width: "100%", maxWidth: "100%", height: "auto", display: "block" }}
              priority
            />
            {/* Border overlay - ::after pseudo element with border-image */}
            <span style={{
              content: '""',
              position: "absolute",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              border: "1px solid transparent",
              borderImage: "url(/images/square-border-alt.webp) 500 / 50px / 5px stretch",
              pointerEvents: "none",
            }} />
          </Link>
          {/* Countdown - exact styles from original */}
          <p style={{
            position: "absolute",
            bottom: "49.94px",
            left: 0,
            right: 0,
            textAlign: "center",
            color: "#fff",
            fontSize: "56.88px",
            fontWeight: 700,
            fontFamily: "Neuton, Raleway, serif",
            letterSpacing: "1px",
            lineHeight: "normal",
            padding: "0 37.5px 0 0",
            textShadow: "rgb(161, 186, 50) 1px 3px 0px, rgb(124, 136, 27) 4px 3px 0px",
          }}>
            Ends in <CountdownTimer targetSeconds={1672638} />
          </p>
        </div>

        {/* Panel Heading - exact: 38.4px, Neuton, padding 10px 15px */}
        <div style={{ padding: "10px 15px", fontSize: "38.4px", fontWeight: 400, fontFamily: "Neuton, Raleway, serif" }}>
          Hypixel Store
        </div>

        <div className="home-content">

          <ul className="home-category-grid">
            {categories.map((cat) => (
              <li
                key={cat.slug}
                className={`home-category-card ${cat.sale ? "sale" : ""}`}
              >
                <Link
                  href={`/category/${cat.slug}`}
                  title={cat.name}
                >
                  {cat.sale && <div className="sale-ribbon">SALE!</div>}
                  <div
                    className="home-category-icon"
                    style={{ backgroundImage: `url(${cat.icon})` }}
                    role="img"
                    aria-hidden="true"
                  />
                  <p>{cat.name}</p>
                </Link>
              </li>
            ))}
          </ul>

          <div className="copy-block">
            <h3 style={{ fontSize: "24px", fontWeight: 500, marginBottom: "10px" }}>Welcome</h3>
            <p style={{ marginBottom: "16px" }}>
              Welcome to the official Hypixel Store! This is the place for you to
              enhance your Hypixel Server experience. We offer ranks, Hypixel
              Gold, SkyBlock Gems, and more. You can choose the product category
              in the site navigation at the top or by clicking on the category
              list above.
            </p>
            <p style={{ marginBottom: "16px" }}>
              All payments are handled and secured by Stripe.
            </p>

            <h3 style={{ fontSize: "24px", fontWeight: 500, marginBottom: "10px" }}>About Hypixel</h3>
            <p style={{ marginBottom: "16px" }}>
              Starting as a YouTube channel making Minecraft Adventure Maps,
              Hypixel is now one of the world&apos;s largest and highest-quality
              Minecraft Server Networks, featuring hit games such as SkyBlock, The
              Walls, Bed Wars, Blitz Survival Games, and many more.
            </p>

            <h3 style={{ fontSize: "24px", fontWeight: 500, marginBottom: "10px" }}>Need help?</h3>
            <p>
              If you have any questions or issues related to payments,{" "}
              <Link href="https://support.hypixel.net/" style={{ color: "rgb(230, 174, 71)" }}>
                send us a ticket here
              </Link>
              , and we will reply as fast as possible.
            </p>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
}
