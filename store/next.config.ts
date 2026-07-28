import type {NextConfig} from "next";

const nextConfig: NextConfig = {
  reactCompiler: true,
  turbopack: {
    root: process.cwd(),
  },
  images: {
    remotePatterns: [
      { hostname: "crafthead.net" },
      {hostname: "dunb17ur4ymx4.cloudfront.net"},
      {hostname: "staticassets.hypixel.net"},
    ],
  },
};

export default nextConfig;
