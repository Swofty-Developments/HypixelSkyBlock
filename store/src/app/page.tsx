"use client";

import Link from "next/link";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import {categories} from "@/data/store";

export default function Home() {
  return (
    <div className="store-page">
      <div className="store-container">
        <Navbar />

        {/* Panel Heading - exact: 38.4px, Neuton, padding 10px 15px */}
        <div style={{ padding: "10px 15px", fontSize: "38.4px", fontWeight: 400, fontFamily: "Neuton, Raleway, serif" }}>
          Hypixel Store
        </div>

        <div className="home-content">

          <ul className="home-category-grid">
            {categories.map((cat) => (
              <li
                key={cat.slug}
                className="home-category-card"
              >
                <Link
                  href={`/category/${cat.slug}`}
                  title={cat.name}
                >
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
              Welcome to the unofficial Hypixel Store! This is the place for you to
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
