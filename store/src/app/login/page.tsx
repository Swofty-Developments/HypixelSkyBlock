"use client";

import { Suspense, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

function LoginForm() {
  const [username, setUsername] = useState("");
  const router = useRouter();
  const searchParams = useSearchParams();
  const redirect = searchParams.get("redirect") || "/";

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (username.trim()) {
      localStorage.setItem("hypixel_username", username.trim());
      router.push(redirect);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
        <div style={{ display: "flex", width: "100%", maxWidth: "595px" }}>
          <input
            type="text"
            name="ign"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Enter your in-game username"
            style={{
              flex: 1,
              height: "46px",
              padding: "10px 16px",
              fontSize: "18px",
              fontFamily: "Raleway, sans-serif",
              color: "rgb(85, 85, 85)",
              border: "1px solid rgb(204, 204, 204)",
              borderRadius: "35px",
              outline: "none",
            }}
          />
        </div>
        <div style={{ width: "100%", maxWidth: "595px", marginTop: "10px" }}>
          <button
            type="submit"
            style={{
              width: "100%",
              height: "46px",
              fontSize: "18px",
              fontFamily: "Raleway, sans-serif",
              color: "rgb(78, 45, 21)",
              backgroundColor: "rgb(230, 174, 71)",
              border: "1px solid rgba(0,0,0,0.5)",
              borderRadius: "35px",
              cursor: "pointer",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "10px",
            }}
          >
            Continue
            <svg style={{ width: 14, height: 14, marginLeft: "10px" }} viewBox="0 0 320 512" fill="currentColor">
              <path d="M310.6 233.4c12.5 12.5 12.5 32.8 0 45.3l-192 192c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L242.7 256 73.4 86.6c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0l192 192z" />
            </svg>
          </button>
        </div>
      </div>
    </form>
  );
}

export default function LoginPage() {
  return (
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
      <div style={{ width: "1250px", maxWidth: "1250px", margin: "0 auto", padding: "0 15px", fontFamily: "Raleway, sans-serif", fontSize: "14px", color: "#000" }}>
        <Navbar />

        {/* Heading */}
        <div style={{ padding: "10px 15px", fontSize: "38.4px", fontWeight: 400, fontFamily: "Neuton, Raleway, serif" }}>
          Login
        </div>

        {/* Content */}
        <div style={{ display: "flex", flexDirection: "column", gap: "50px", padding: "0 15px", fontSize: "16px" }}>
          <div style={{ textAlign: "center" }}>
            <p style={{ marginBottom: "20px", fontSize: "16px" }}>Enter your Minecraft username</p>
            <Suspense fallback={<div>Loading...</div>}>
              <LoginForm />
            </Suspense>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
}
