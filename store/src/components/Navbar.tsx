"use client";

import Image from "next/image";
import Link from "next/link";
import {HelpCircle} from "lucide-react";
import {categories} from "@/data/store";
import CurrencySelector from "@/components/CurrencySelector";
import {useLocalStorageValue} from "@/lib/use-local-storage";

const borderImageStyle = "url(https://staticassets.hypixel.net/store/borders/classic-border.webp) 500 / 18px / 6px stretch";
const separatorColor = "rgb(7, 11, 54)";
export default function Navbar() {
    const username = useLocalStorageValue("hypixel_username").trim();

  return (
      <nav className="store-navbar">
      {/* Logo */}
          <div className="store-navbar-logo">
        <Link href="/" title="Hypixel Store Homepage">
          <Image
              src="https://dunb17ur4ymx4.cloudfront.net/webstore/logos/6c9b0cbd5c2f0ceef98f01068102b0d056c04b7b.png"
            alt="Hypixel Logo"
            width={103}
            height={55}
              className="store-logo-image"
            priority
          />
        </Link>
      </div>

      {/* Category Links */}
      <div style={{
        backgroundColor: "rgb(35, 52, 88)",
        border: "1px solid rgb(231, 231, 231)",
        borderImage: borderImageStyle,
        height: "38px",
        display: "flex",
        alignItems: "center",
        overflow: "visible",
      }}>
        <ul style={{ display: "flex", margin: 0, padding: 0, listStyle: "none", height: "36px" }}>
          {categories.map((cat, i) => (
            <li key={cat.name} style={{
              position: "relative",
              display: "block",
              borderRight: i < categories.length - 1 ? `1px solid ${separatorColor}` : "none",
              borderLeft: i > 0 ? `1px solid ${separatorColor}` : "none",
            }}>
              <Link
                href={`/category/${cat.slug}`}
                style={{
                  display: "inline-block",
                  color: "rgb(255, 255, 255)",
                  fontSize: "16px",
                  fontWeight: 500,
                  fontFamily: "Raleway, sans-serif",
                  padding: "8px 12px",
                    paddingRight: "12px",
                  lineHeight: "20px",
                  height: "36px",
                  textDecoration: "none",
                  whiteSpace: "nowrap",
                }}
              >
                {cat.name}
              </Link>
            </li>
          ))}
        </ul>
      </div>

      {/* User Controls */}
      <div style={{
        backgroundColor: "rgb(35, 52, 88)",
        border: "1px solid rgb(231, 231, 231)",
        borderImage: borderImageStyle,
        height: "38px",
        display: "flex",
        alignItems: "center",
        overflow: "visible",
      }}>
        <ul style={{ display: "flex", margin: 0, padding: 0, listStyle: "none", height: "36px", alignItems: "center" }}>
          {/* Login / Username */}
          <li style={{ borderRight: `1px solid ${separatorColor}` }}>
            <Link
              href="/login"
              className={`nav-user-button${username ? " nav-user-button-logged-in" : ""}`}
              style={{
                display: "inline-block",
                color: "white",
                fontSize: "16px",
                fontWeight: 500,
                padding: "8px 12px 8px 54px",
                height: "36px",
                lineHeight: "20px",
                textDecoration: "none",
                whiteSpace: "nowrap",
                backgroundImage: username
                  ? `url(https://crafthead.net/helm/${username})`
                  : "url(https://crafthead.net/helm/c06f89064c8a49119c29ea1dbd1aab82)",
                backgroundSize: "26px",
                backgroundPosition: "14px 5px",
                backgroundRepeat: "no-repeat",
              }}
            >
              {username || "Login"}
            </Link>
          </li>

          {/* Currency */}
          <li style={{ borderRight: `1px solid ${separatorColor}`, borderLeft: `1px solid ${separatorColor}` }}>
              <CurrencySelector/>
          </li>

          {/* Support */}
          <li style={{ borderLeft: `1px solid ${separatorColor}` }}>
            <Link
              href="https://support.hypixel.net"
              style={{
                display: "inline-flex",
                alignItems: "center",
                justifyContent: "center",
                padding: "8px 12px",
                color: "white",
                height: "36px",
                textDecoration: "none",
              }}
              title="Support"
            >
              <HelpCircle fill="currentColor" color="currentColor" size={19} />
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}
