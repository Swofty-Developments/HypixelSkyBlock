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
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
      {/* Container - matches original exactly: 1250px max, 0px 15px padding */}
      <div style={{ width: "1250px", maxWidth: "1250px", margin: "0 auto", padding: "0 15px", fontFamily: "Raleway, sans-serif", fontSize: "14px", color: "#000" }}>
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

        {/* Content - flex column, gap 50px, padding 0 15px, font-size 16px */}
        <div style={{ display: "flex", flexDirection: "column", gap: "50px", padding: "0 15px", fontSize: "16px" }}>

          {/* Blocks List - flex row, gap 15px, justify-content center, flex-wrap wrap */}
          <ul style={{ display: "flex", flexDirection: "row", gap: "15px", justifyContent: "center", flexWrap: "wrap", listStyle: "none", margin: 0, padding: 0, width: "1220px" }}>
            {categories.map((cat) => (
              <li
                key={cat.slug}
                style={{
                  display: "flex",
                  position: "relative",
                  width: "calc((100% - 30px) / 3)",
                  height: "234px",
                  backgroundColor: "#fff",
                  border: cat.sale ? "4px solid rgb(230, 174, 71)" : "1px solid rgb(199, 199, 199)",
                  borderRadius: "8px",
                  boxShadow: cat.sale ? "rgba(231, 135, 27, 0.25) 0px 0px 30px 10px" : "rgba(175, 175, 175, 0.2) 0px 2px 15px 0px",
                  textAlign: "center",
                  color: "rgb(230, 174, 71)",
                  overflow: "hidden",
                }}
              >
                <Link
                  href={`/category/${cat.slug}`}
                  title={cat.name}
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    width: "100%",
                    textDecoration: "none",
                    color: "inherit",
                    padding: "14px",
                  }}
                >
                  {cat.sale && (
                    <div style={{
                      position: "absolute",
                      top: 0,
                      left: 0,
                      right: 0,
                      backgroundColor: "rgb(230, 174, 71)",
                      color: "rgb(78, 45, 21)",
                      fontSize: "13px",
                      fontWeight: 700,
                      padding: "3px 0",
                      textAlign: "center",
                    }}>
                      SALE!
                    </div>
                  )}
                  <div
                    style={{
                      width: "100%",
                      height: "120px",
                      backgroundImage: `url(${cat.icon})`,
                      backgroundSize: "contain",
                      backgroundPosition: "center",
                      backgroundRepeat: "no-repeat",
                      margin: cat.sale ? "30px 0 10px" : "20px 0 10px",
                    }}
                    role="img"
                    aria-hidden="true"
                  />
                  <p style={{ fontSize: "22px", fontWeight: 400, color: "rgb(230, 174, 71)", margin: 0 }}>
                    {cat.name}
                  </p>
                </Link>
              </li>
            ))}
          </ul>

          {/* Panel Description - width 854px, margin 0 183px */}
          <div style={{ width: "854px", margin: "0 183px", fontSize: "16px", lineHeight: "normal" }}>
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
            <p><br /></p>

            <h3 style={{ fontSize: "24px", fontWeight: 500, marginBottom: "10px" }}>About Hypixel</h3>
            <p style={{ marginBottom: "16px" }}>
              Starting as a YouTube channel making Minecraft Adventure Maps,
              Hypixel is now one of the world&apos;s largest and highest-quality
              Minecraft Server Networks, featuring hit games such as SkyBlock, The
              Walls, Bed Wars, Blitz Survival Games, and many more.
            </p>
            <p><br /></p>

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
