export type Product = {
  name: string;
  image: string;
  description: string;
  originalPrice?: string;
  price: string;
  sale?: boolean;
  bestValue?: boolean;
  wide?: boolean;
  bonus?: string;
  cta?: "BUY" | "SUBSCRIBE";
};

export type Category = {
  slug: string;
  title: string;
  navLabel: string;
  sale?: boolean;
  products: Product[];
  aboutTitle: string;
  about: string[];
  bullets?: string[];
  note?: string;
  features?: FeatureBlock[];
};

export type FeatureBlock = {
  title: string;
  text?: string[];
  images?: string[];
};

export const categories = [
  { name: "Ranks", slug: "ranks", icon: "/images/icons/ranks.png", sale: true },
  { name: "Gold", slug: "gold", icon: "/images/icons/gold.png" },
  { name: "SkyBlock Gems", slug: "skyblock-gems", icon: "/images/icons/skyblock-gems.png" },
  { name: "Housing+", slug: "housing-plus", icon: "/images/icons/housing-plus.png", sale: true },
  { name: "Boosters", slug: "boosters", icon: "/images/icons/boosters.png", sale: true },
  { name: "Companions", slug: "companions", icon: "/images/icons/companions.png", sale: true },
];

export const productCategories: Record<string, Category> = {
  gold: {
    slug: "gold",
    title: "Gold",
    navLabel: "Gold",
    products: [
      { name: "1,000 Gold", image: "/images/gold/1000.png", description: "Receive 1,000 Gold for use in the in-game store.", price: "12.55" },
      { name: "2,500 Gold", image: "/images/gold/2500.png", description: "Receive 2,500 Gold for use in the in-game store.", price: "31.38" },
      { name: "5,000 Gold", image: "/images/gold/5000.png", description: "Receive 5,000 Gold for use in the in-game store.", price: "62.75" },
      { name: "11,000 Gold", image: "/images/gold/11000.png", description: "Receive 11,000 Gold for use in the in-game store.", bonus: "10,000 + 1,000 Bonus Gold", price: "125.50", bestValue: true },
    ],
    aboutTitle: "About Gold",
    about: ["Gold is a currency used to purchase items from the Hypixel In-Game Store. You can find vendors that use Gold in any Hypixel lobby.", "From these vendors, you can buy:"],
    bullets: ["Seasonal items", "Bundles", "Companions", "... and more!"],
    note: "Hypixel Gold is non-refundable and cannot be earned through any in-game methods.",
  },
  "skyblock-gems": {
    slug: "skyblock-gems",
    title: "SkyBlock Gems",
    navLabel: "SkyBlock Gems",
    products: [
      { name: "700 SkyBlock Gems", image: "/images/gems/700.png", description: "Receive 700 Gems for use in Hypixel SkyBlock.", price: "7.52" },
      { name: "1,800 SkyBlock Gems", image: "/images/gems/1800.png", bonus: "1,750 + 50 Bonus Gems", description: "Receive 1,800 Gems for use in Hypixel SkyBlock.", price: "18.81" },
      { name: "3,750 SkyBlock Gems", image: "/images/gems/3750.png", bonus: "3,500 + 250 Bonus Gems", description: "Receive 3,750 Gems for use in Hypixel SkyBlock.", price: "37.64" },
      { name: "7,600 SkyBlock Gems", image: "/images/gems/7600.png", bonus: "7,000 + 600 Bonus Gems", description: "Receive 7,600 Gems for use in Hypixel SkyBlock.", price: "75.29" },
      { name: "17,000 SkyBlock Gems", image: "/images/gems/17000.png", bonus: "14,000 + 3,000 Bonus Gems", description: "Receive 17,000 Gems for use in Hypixel SkyBlock.", price: "150.59", bestValue: true },
    ],
    aboutTitle: "About SkyBlock Gems",
    about: ["SkyBlock Gems are used to purchase a variety of items in Hypixel SkyBlock. To spend your Gems, visit the Community Center in the Hypixel SkyBlock Hub.", "You can buy the following items with Gems:"],
    bullets: ["Profile and account upgrades!", "Booster Cookies!", "Exclusive SkyBlock Cosmetics from limited in-game Fire Sales!", "Cosmetics from Taylor in the Fashion Shop!", "Barn and Greenhouse Skins from SkyMart in the Garden!"],
    note: "You can only use SkyBlock Gems in Hypixel SkyBlock.",
  },
  "housing-plus": {
    slug: "housing-plus",
    title: "Housing+",
    navLabel: "Housing+",
    sale: true,
    products: [
      { name: "Housing+", image: "/images/housing/housing-plus.png", description: "Support Housing and unlock a larger plot size, higher limits, and more", originalPrice: "25.09", price: "20.07", sale: true, bestValue: true },
    ],
    aboutTitle: "About Housing+",
    about: ["Housing+ is for the Housing enthusiast looking to have some major upgrades, with features including larger plot sizes, increased stat duration, and more!"],
    features: [
      { title: "Larger Plot Size", text: ["The default plot limit for regular players is 51x51 blocks, while Housing+ increases your maximum plot size to 255x255 blocks." ] },
      { title: "Increased Limits", text: ["Furniture, holograms, regions, NPCs, stat keys, functions and inventory layouts receive increased limits for Housing+ players."] },
      { title: "Increased Stats Duration", text: ["By default, your stats are kept for 14 days. With Housing+, your stats are kept for an entire year!"] },
      { title: "Skull Packs", images: ["/images/housing/pet_skins_pack.png", "/images/housing/pet_skins_pack_2.png", "/images/housing/pet_skins_pack_3.png", "/images/housing/minion_skins_pack.png", "/images/housing/minion_skins_pack_2.png"] },
      { title: "Housing+ Chat Prefix", images: ["/images/housing/chat_prefix.png"] },
      { title: "Egg Hunt Item", images: ["/images/housing/egg_hunt.png"] },
    ],
  },
  boosters: {
    slug: "boosters",
    title: "Boosters",
    navLabel: "Boosters",
    sale: true,
    products: [
      { name: "Classic Games Network Booster", image: "/images/boosters/classic.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "12.54", price: "10.03", sale: true, wide: true },
      { name: "Blitz Survival Games Network Booster", image: "/images/boosters/blitz.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "18.81", price: "15.05", sale: true, wide: true },
      { name: "Mega Walls Network Booster", image: "/images/boosters/mega-walls.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "18.81", price: "15.05", sale: true },
      { name: "SkyWars Network Booster", image: "/images/boosters/skywars.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "18.81", price: "15.05", sale: true },
      { name: "UHC Champions Network Booster", image: "/images/boosters/uhc.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "15.05", price: "12.04", sale: true },
      { name: "Arcade Network Booster", image: "/images/boosters/arcade.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "12.54", price: "10.03", sale: true },
      { name: "TNT Games Network Booster", image: "/images/boosters/tnt.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra tokens.", originalPrice: "7.53", price: "6.01", sale: true },
      { name: "Warlords Network Booster", image: "/images/boosters/warlords.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "11.28", price: "9.02", sale: true },
      { name: "Smash Heroes Network Booster", image: "/images/boosters/smash.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "10.03", price: "8.02", sale: true },
      { name: "Cops and Crims Network Booster", image: "/images/boosters/cops.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "10.03", price: "8.02", sale: true },
    ],
    aboutTitle: "About Boosters",
    about: ["Network Boosters increase the coin multiplier for an hour by up to 3X for everyone playing that specific minigame on the Hypixel Server!", "Boosters can also be stacked, and you can only use them once!"],
    bullets: ["Regular Boosters raise the coin multiplier to 3X.", "You will also get a shoutout in game lobbies when you activate a Booster.", "If there are already ten stacked Boosters, your Booster will stack on the next available slot."],
    note: "You can only activate one booster per game each day!",
  },
  companions: {
    slug: "companions",
    title: "Companions",
    navLabel: "Companions",
    sale: true,
    products: [
      { name: "Magic Dog Companion", image: "/images/companions/magic-dog.png", description: "A loyal adventure companion for your lobby adventures.", originalPrice: "5.01", price: "4.00", sale: true },
      { name: "White Pug Companion", image: "/images/companions/white-pug.png", description: "A small, friendly companion that follows you around.", originalPrice: "5.01", price: "4.00", sale: true },
      { name: "Shibe Companion", image: "/images/companions/shibe.png", description: "Much doge. Wow. Such pet.", originalPrice: "5.01", price: "4.00", sale: true },
      { name: "Gorilla Companion", image: "/images/companions/gorilla.png", description: "A strong companion for your Hypixel lobby.", originalPrice: "8.77", price: "7.02", sale: true },
      { name: "Sloth Companion", image: "/images/companions/sloth.png", description: "A calm companion that takes life at its own speed.", originalPrice: "8.77", price: "7.02", sale: true },
      { name: "Chimp Companion", image: "/images/companions/chimp.png", description: "A playful companion with plenty of energy.", originalPrice: "8.77", price: "7.02", sale: true },
      { name: "Panda Companion", image: "/images/companions/panda.png", description: "A rare panda buddy that joins you in lobbies.", originalPrice: "5.01", price: "4.00", sale: true },
      { name: "Penguin Companion", image: "/images/companions/penguin.png", description: "A waddling Antarctic companion.", originalPrice: "12.54", price: "10.03", sale: true },
      { name: "Turtle Companion", image: "/images/companions/turtle.png", description: "A slow and steady turtle companion.", originalPrice: "12.54", price: "10.03", sale: true },
      { name: "Elephant Companion", image: "/images/companions/elephant.png", description: "A dependable elephant companion.", originalPrice: "12.54", price: "10.03", sale: true },
      { name: "Duck Companion", image: "/images/companions/duck.png", description: "A bright duck companion.", originalPrice: "5.01", price: "4.00", sale: true },
      { name: "Frog Companion", image: "/images/companions/frog.png", description: "A hoppy companion for your lobby.", originalPrice: "5.01", price: "4.00", sale: true },
      { name: "Doggo Companion Bundle", image: "/images/companions/doggo-bundle.png", description: "Contains Magic Dog, White Pug, and Shibe companions.", originalPrice: "13.75", price: "11.03", sale: true, wide: true },
      { name: "Jungle Companion Bundle", image: "/images/companions/jungle-bundle.png", description: "Contains Gorilla, Sloth, and Chimp companions.", originalPrice: "25.09", price: "20.07", sale: true, wide: true },
    ],
    aboutTitle: "About Companions",
    about: ["Companions are unique buddies to join you on your Hypixel lobby adventures! Each of them is custom-built, self-animated, and only available on Hypixel."],
    note: "Companions can only join you in lobbies, not in any Hypixel game.",
  },
};
