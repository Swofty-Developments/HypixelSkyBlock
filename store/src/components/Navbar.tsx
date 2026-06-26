"use client";

import Image from "next/image";
import Link from "next/link";
import { HelpCircle, Moon, Sun, Tag } from "lucide-react";
import { categories } from "@/data/store";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

const borderImageStyle = "url(/images/classic-border.webp) 500 / 18px / 6px stretch";
const separatorColor = "rgb(7, 11, 54)";
const currencies = ["AUD", "BRL", "CAD", "DKK", "EUR", "NOK", "NZD", "PLN", "GBP", "SEK", "USD"];

export default function Navbar({ username }: { username?: string }) {

  return (
    <nav style={{
      display: "flex",
      alignItems: "center",
      justifyContent: "space-between",
      height: "90px",
      width: "1220px",
      maxWidth: "1220px",
      margin: "0 auto 15px",
      gap: "15px",
      position: "relative",
    }}>
      {/* Logo */}
      <div style={{ padding: "15px 0", height: "90px", display: "flex", alignItems: "center" }}>
        <Link href="/" title="Hypixel Store Homepage">
          <Image
            src="/images/hypixel-logo.png"
            alt="Hypixel Logo"
            width={103}
            height={55}
            style={{ height: "55px", width: "auto", display: "block" }}
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
                  paddingRight: cat.sale ? "36px" : "12px",
                  lineHeight: "20px",
                  height: "36px",
                  textDecoration: "none",
                  whiteSpace: "nowrap",
                }}
              >
                {cat.name}
              </Link>
              {cat.sale && (
                <span style={{
                  position: "absolute",
                  right: "10px",
                  top: "8px",
                  width: "20px",
                  height: "20px",
                }}>
                  <Tag fill="rgb(230, 174, 71)" color="rgb(230, 174, 71)" size={18} />
                </span>
              )}
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
            <DropdownMenu>
              <DropdownMenuTrigger className="nav-button">USD ▾</DropdownMenuTrigger>
              <DropdownMenuContent align="start">
                {currencies.map((currency) => (
                  <DropdownMenuItem key={currency}>{currency}</DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
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
