import { defineConfig } from "vitepress";
import container_plugin from "markdown-it-container";

export default defineConfig({
  title: "HypixelSkyBlock",
  description: "A Minestom-based recreation of Hypixel SkyBlock",
  markdown: {
    config(md) {
      md.use(container_plugin, "alert", {
        render(tokens, idx) {
          const token = tokens[idx];
          const type = token.info.trim().split(" ")[1] || "info";
          if (token.nesting === 1) {
            return `<div class="alert alert-${type}">
                      <div class="alert-header">${type.toUpperCase()}</div>
                      <div class="alert-content">\n`;
          } else {
            return `</div></div>\n`;
          }
        },
      });
    },
  },
  head: [["meta", { name: "theme-color", content: "#4ade80" }]],
  cleanUrls: true,
  themeConfig: {
    search: {
      provider: "local",
    },
    logo: "/logo.png",
    nav: [
      { text: "Guide", link: "/docs/introduction" },
      { text: "Reference", link: "/docs/reference/server-types" },
      { text: "Javadoc", link: "https://swofty-developments.github.io/HypixelSkyBlock/" },
      { text: "Discord", link: "https://discord.gg/ZaGW5wzUJ3" },
    ],
    sidebar: {
      "/docs/": [
        {
          text: "Getting Started",
          items: [
            { text: "Introduction", link: "/docs/introduction" },
            { text: "Requirements", link: "/docs/requirements" },
          ],
        },
        {
          text: "Docker (Recommended)",
          items: [
            { text: "Quick Install", link: "/docs/docker/setup" },
            { text: "Adding Servers", link: "/docs/docker/adding-servers" },
          ],
        },
        {
          text: "Manual Setup",
          items: [
            { text: "Proxy Setup", link: "/docs/setup/proxy" },
            { text: "Game Servers", link: "/docs/setup/game-servers" },
            { text: "Services", link: "/docs/setup/services" },
            { text: "Resource Pack", link: "/docs/setup/resource-pack" },
            { text: "Forums Website", link: "/docs/setup/forums" },
          ],
        },
        {
          text: "Reference",
          items: [
            { text: "Server Types", link: "/docs/reference/server-types" },
            { text: "Services", link: "/docs/reference/services" },
            { text: "Configuration", link: "/docs/reference/configuration" },
          ],
        },
        {
          text: "Help",
          items: [
            { text: "Troubleshooting", link: "/docs/troubleshooting" },
            { text: "Credits", link: "/docs/credits" },
          ],
        },
      ],
    },
    socialLinks: [
      { icon: "github", link: "https://github.com/Swofty-Developments/HypixelSkyBlock" },
      { icon: "discord", link: "https://discord.gg/ZaGW5wzUJ3" },
    ],
    footer: {
      message: "Released under the MIT License.",
      copyright: "Copyright © 2026 Swofty Developments",
    },
  },
});
