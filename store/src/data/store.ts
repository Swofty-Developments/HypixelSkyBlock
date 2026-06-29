export type Product = {
  id: string;
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
  entitlements: StoreEntitlement[];
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

export type StoreEntitlement = {
  type: "RANK" | "STORE_CURRENCY" | "SKYBLOCK_GEMS" | "FEATURE" | "BOOSTER" | "COSMETIC";
  key: string;
  amount: number;
  durationDays?: number;
};

export const directProducts: Product[] = [
  {
    id: "rank-vip",
    name: "VIP Rank",
    image: "/images/ranks/vip.png",
    description: "Receive the VIP Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    originalPrice: "7.69",
    price: "6.15",
    sale: true,
    entitlements: [{ type: "RANK", key: "VIP", amount: 1 }],
  },
  {
    id: "rank-vip-plus",
    name: "VIP+ Rank",
    image: "/images/ranks/vip-plus.png",
    description: "Receive the VIP+ Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    originalPrice: "16.49",
    price: "13.19",
    sale: true,
    entitlements: [{ type: "RANK", key: "VIP_PLUS", amount: 1 }],
  },
  {
    id: "rank-mvp",
    name: "MVP Rank",
    image: "/images/ranks/mvp.png",
    description: "Receive the MVP Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    originalPrice: "32.99",
    price: "26.39",
    sale: true,
    entitlements: [{ type: "RANK", key: "MVP", amount: 1 }],
  },
  {
    id: "rank-mvp-plus",
    name: "MVP+ Rank",
    image: "/images/ranks/mvp-plus.png",
    description: "Receive the MVP+ Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    originalPrice: "49.49",
    price: "39.59",
    sale: true,
    entitlements: [{ type: "RANK", key: "MVP_PLUS", amount: 1 }],
  },
  {
    id: "rank-mvp-plus-plus-30",
    name: "30 Days MVP++",
    image: "/images/ranks/mvp-plus-plus.png",
    description: "Receive 30 days of MVP++ on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    price: "10.03",
    entitlements: [{ type: "RANK", key: "MVP_PLUS_PLUS", amount: 1, durationDays: 30 }],
  },
  {
    id: "rank-mvp-plus-plus-90",
    name: "90 Days MVP++",
    image: "/images/ranks/mvp-plus-plus.png",
    description: "Receive 90 days of MVP++ on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    price: "27.60",
    entitlements: [{ type: "RANK", key: "MVP_PLUS_PLUS", amount: 1, durationDays: 90 }],
  },
  {
    id: "rank-mvp-plus-plus-365",
    name: "365 Days MVP++",
    image: "/images/ranks/mvp-plus-plus.png",
    description: "Receive 365 days of MVP++ on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
    price: "89.09",
    bestValue: true,
    entitlements: [{ type: "RANK", key: "MVP_PLUS_PLUS", amount: 1, durationDays: 365 }],
  },
];

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
      { id: "gold-1000", name: "1,000 Gold", image: "/images/gold/1000.png", description: "Receive 1,000 Gold for use in the in-game store.", price: "12.55", entitlements: [{ type: "STORE_CURRENCY", key: "GOLD", amount: 1000 }] },
      { id: "gold-2500", name: "2,500 Gold", image: "/images/gold/2500.png", description: "Receive 2,500 Gold for use in the in-game store.", price: "31.38", entitlements: [{ type: "STORE_CURRENCY", key: "GOLD", amount: 2500 }] },
      { id: "gold-5000", name: "5,000 Gold", image: "/images/gold/5000.png", description: "Receive 5,000 Gold for use in the in-game store.", price: "62.75", entitlements: [{ type: "STORE_CURRENCY", key: "GOLD", amount: 5000 }] },
      { id: "gold-11000", name: "11,000 Gold", image: "/images/gold/11000.png", description: "Receive 11,000 Gold for use in the in-game store.", bonus: "10,000 + 1,000 Bonus Gold", price: "125.50", bestValue: true, entitlements: [{ type: "STORE_CURRENCY", key: "GOLD", amount: 11000 }] },
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
      { id: "skyblock-gems-700", name: "700 SkyBlock Gems", image: "/images/gems/700.png", description: "Receive 700 Gems for use in Hypixel SkyBlock.", price: "7.52", entitlements: [{ type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 700 }] },
      { id: "skyblock-gems-1800", name: "1,800 SkyBlock Gems", image: "/images/gems/1800.png", bonus: "1,750 + 50 Bonus Gems", description: "Receive 1,800 Gems for use in Hypixel SkyBlock.", price: "18.81", entitlements: [{ type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 1800 }] },
      { id: "skyblock-gems-3750", name: "3,750 SkyBlock Gems", image: "/images/gems/3750.png", bonus: "3,500 + 250 Bonus Gems", description: "Receive 3,750 Gems for use in Hypixel SkyBlock.", price: "37.64", entitlements: [{ type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 3750 }] },
      { id: "skyblock-gems-7600", name: "7,600 SkyBlock Gems", image: "/images/gems/7600.png", bonus: "7,000 + 600 Bonus Gems", description: "Receive 7,600 Gems for use in Hypixel SkyBlock.", price: "75.29", entitlements: [{ type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 7600 }] },
      { id: "skyblock-gems-17000", name: "17,000 SkyBlock Gems", image: "/images/gems/17000.png", bonus: "14,000 + 3,000 Bonus Gems", description: "Receive 17,000 Gems for use in Hypixel SkyBlock.", price: "150.59", bestValue: true, entitlements: [{ type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 17000 }] },
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
      { id: "housing-plus", name: "Housing+", image: "/images/housing/housing-plus.png", description: "Support Housing and unlock a larger plot size, higher limits, and more", originalPrice: "25.09", price: "20.07", sale: true, bestValue: true, entitlements: [{ type: "FEATURE", key: "HOUSING_PLUS", amount: 1 }] },
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
      { id: "booster-classic", name: "Classic Games Network Booster", image: "/images/boosters/classic.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "12.54", price: "10.03", sale: true, wide: true, entitlements: [{ type: "BOOSTER", key: "CLASSIC", amount: 1 }] },
      { id: "booster-blitz", name: "Blitz Survival Games Network Booster", image: "/images/boosters/blitz.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "18.81", price: "15.05", sale: true, wide: true, entitlements: [{ type: "BOOSTER", key: "BLITZ", amount: 1 }] },
      { id: "booster-mega-walls", name: "Mega Walls Network Booster", image: "/images/boosters/mega-walls.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "18.81", price: "15.05", sale: true, entitlements: [{ type: "BOOSTER", key: "MEGA_WALLS", amount: 1 }] },
      { id: "booster-skywars", name: "SkyWars Network Booster", image: "/images/boosters/skywars.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "18.81", price: "15.05", sale: true, entitlements: [{ type: "BOOSTER", key: "SKYWARS", amount: 1 }] },
      { id: "booster-uhc", name: "UHC Champions Network Booster", image: "/images/boosters/uhc.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "15.05", price: "12.04", sale: true, entitlements: [{ type: "BOOSTER", key: "UHC", amount: 1 }] },
      { id: "booster-arcade", name: "Arcade Network Booster", image: "/images/boosters/arcade.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "12.54", price: "10.03", sale: true, entitlements: [{ type: "BOOSTER", key: "ARCADE", amount: 1 }] },
      { id: "booster-tnt", name: "TNT Games Network Booster", image: "/images/boosters/tnt.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra tokens.", originalPrice: "7.53", price: "6.01", sale: true, entitlements: [{ type: "BOOSTER", key: "TNT", amount: 1 }] },
      { id: "booster-warlords", name: "Warlords Network Booster", image: "/images/boosters/warlords.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "11.28", price: "9.02", sale: true, entitlements: [{ type: "BOOSTER", key: "WARLORDS", amount: 1 }] },
      { id: "booster-smash", name: "Smash Heroes Network Booster", image: "/images/boosters/smash.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "10.03", price: "8.02", sale: true, entitlements: [{ type: "BOOSTER", key: "SMASH", amount: 1 }] },
      { id: "booster-cops", name: "Cops and Crims Network Booster", image: "/images/boosters/cops.png", description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.", originalPrice: "10.03", price: "8.02", sale: true, entitlements: [{ type: "BOOSTER", key: "COPS_AND_CRIMS", amount: 1 }] },
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
      { id: "companion-magic-dog", name: "Magic Dog Companion", image: "/images/companions/magic-dog.png", description: "A loyal adventure companion for your lobby adventures.", originalPrice: "5.01", price: "4.00", sale: true, entitlements: [{ type: "COSMETIC", key: "MAGIC_DOG_COMPANION", amount: 1 }] },
      { id: "companion-white-pug", name: "White Pug Companion", image: "/images/companions/white-pug.png", description: "A small, friendly companion that follows you around.", originalPrice: "5.01", price: "4.00", sale: true, entitlements: [{ type: "COSMETIC", key: "WHITE_PUG_COMPANION", amount: 1 }] },
      { id: "companion-shibe", name: "Shibe Companion", image: "/images/companions/shibe.png", description: "Much doge. Wow. Such pet.", originalPrice: "5.01", price: "4.00", sale: true, entitlements: [{ type: "COSMETIC", key: "SHIBE_COMPANION", amount: 1 }] },
      { id: "companion-gorilla", name: "Gorilla Companion", image: "/images/companions/gorilla.png", description: "A strong companion for your Hypixel lobby.", originalPrice: "8.77", price: "7.02", sale: true, entitlements: [{ type: "COSMETIC", key: "GORILLA_COMPANION", amount: 1 }] },
      { id: "companion-sloth", name: "Sloth Companion", image: "/images/companions/sloth.png", description: "A calm companion that takes life at its own speed.", originalPrice: "8.77", price: "7.02", sale: true, entitlements: [{ type: "COSMETIC", key: "SLOTH_COMPANION", amount: 1 }] },
      { id: "companion-chimp", name: "Chimp Companion", image: "/images/companions/chimp.png", description: "A playful companion with plenty of energy.", originalPrice: "8.77", price: "7.02", sale: true, entitlements: [{ type: "COSMETIC", key: "CHIMP_COMPANION", amount: 1 }] },
      { id: "companion-panda", name: "Panda Companion", image: "/images/companions/panda.png", description: "A rare panda buddy that joins you in lobbies.", originalPrice: "5.01", price: "4.00", sale: true, entitlements: [{ type: "COSMETIC", key: "PANDA_COMPANION", amount: 1 }] },
      { id: "companion-penguin", name: "Penguin Companion", image: "/images/companions/penguin.png", description: "A waddling Antarctic companion.", originalPrice: "12.54", price: "10.03", sale: true, entitlements: [{ type: "COSMETIC", key: "PENGUIN_COMPANION", amount: 1 }] },
      { id: "companion-turtle", name: "Turtle Companion", image: "/images/companions/turtle.png", description: "A slow and steady turtle companion.", originalPrice: "12.54", price: "10.03", sale: true, entitlements: [{ type: "COSMETIC", key: "TURTLE_COMPANION", amount: 1 }] },
      { id: "companion-elephant", name: "Elephant Companion", image: "/images/companions/elephant.png", description: "A dependable elephant companion.", originalPrice: "12.54", price: "10.03", sale: true, entitlements: [{ type: "COSMETIC", key: "ELEPHANT_COMPANION", amount: 1 }] },
      { id: "companion-duck", name: "Duck Companion", image: "/images/companions/duck.png", description: "A bright duck companion.", originalPrice: "5.01", price: "4.00", sale: true, entitlements: [{ type: "COSMETIC", key: "DUCK_COMPANION", amount: 1 }] },
      { id: "companion-frog", name: "Frog Companion", image: "/images/companions/frog.png", description: "A hoppy companion for your lobby.", originalPrice: "5.01", price: "4.00", sale: true, entitlements: [{ type: "COSMETIC", key: "FROG_COMPANION", amount: 1 }] },
      { id: "companion-doggo-bundle", name: "Doggo Companion Bundle", image: "/images/companions/doggo-bundle.png", description: "Contains Magic Dog, White Pug, and Shibe companions.", originalPrice: "13.75", price: "11.03", sale: true, wide: true, entitlements: [
        { type: "COSMETIC", key: "MAGIC_DOG_COMPANION", amount: 1 },
        { type: "COSMETIC", key: "WHITE_PUG_COMPANION", amount: 1 },
        { type: "COSMETIC", key: "SHIBE_COMPANION", amount: 1 },
      ] },
      { id: "companion-jungle-bundle", name: "Jungle Companion Bundle", image: "/images/companions/jungle-bundle.png", description: "Contains Gorilla, Sloth, and Chimp companions.", originalPrice: "25.09", price: "20.07", sale: true, wide: true, entitlements: [
        { type: "COSMETIC", key: "GORILLA_COMPANION", amount: 1 },
        { type: "COSMETIC", key: "SLOTH_COMPANION", amount: 1 },
        { type: "COSMETIC", key: "CHIMP_COMPANION", amount: 1 },
      ] },
    ],
    aboutTitle: "About Companions",
    about: ["Companions are unique buddies to join you on your Hypixel lobby adventures! Each of them is custom-built, self-animated, and only available on Hypixel."],
    note: "Companions can only join you in lobbies, not in any Hypixel game.",
  },
};

export const allProducts = [
  ...directProducts,
  ...Object.values(productCategories).flatMap((category) => category.products),
];

export const productById = new Map(allProducts.map((product) => [product.id, product]));
