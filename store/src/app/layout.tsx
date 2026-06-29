import type { Metadata } from "next";
import { Neuton, Raleway } from "next/font/google";
import "./globals.css";

const raleway = Raleway({
  subsets: ["latin"],
  weight: ["100", "300", "400", "500", "600", "700"],
  variable: "--font-raleway",
});

const neuton = Neuton({
  subsets: ["latin"],
  weight: ["200", "300", "400", "700"],
  variable: "--font-neuton",
});

export const metadata: Metadata = {
  title: "Hypixel Store",
  description: "Hypixel Store Clone",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${raleway.variable} ${neuton.variable} font-sans`}>{children}</body>
    </html>
  );
}
