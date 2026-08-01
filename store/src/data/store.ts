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

const HYPIXEL_ASSETS = "https://staticassets.hypixel.net/store";
const TEBEX_PACKAGE_IMAGES = "https://dunb17ur4ymx4.cloudfront.net/packages/images";

export const directProducts: Product[] = [
    {
        id: "rank-vip",
        name: "VIP Rank",
        image: `${TEBEX_PACKAGE_IMAGES}/9f9aef1a97dd7a7061958a2d2dc72238e06a455c.png`,
        description: "Receive the VIP Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "7.69",
        entitlements: [{type: "RANK", key: "VIP", amount: 1}],
    },
    {
        id: "rank-vip-plus",
        name: "VIP+ Rank",
        image: `${TEBEX_PACKAGE_IMAGES}/573d597eebcbfdc8c5485d6bf044abb60175c2c5.png`,
        description: "Receive the VIP+ Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "16.49",
        entitlements: [{type: "RANK", key: "VIP_PLUS", amount: 1}],
    },
    {
        id: "rank-mvp",
        name: "MVP Rank",
        image: `${TEBEX_PACKAGE_IMAGES}/1b734bae59b7cef0dbbfc1b43d1ceed19b9cfebf.png`,
        description: "Receive the MVP Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "32.99",
        entitlements: [{type: "RANK", key: "MVP", amount: 1}],
    },
    {
        id: "rank-mvp-plus",
        name: "MVP+ Rank",
        image: `${TEBEX_PACKAGE_IMAGES}/be813e0264e79d198cc076ef686e4b6d1ed44c89.png`,
        description: "Receive the MVP+ Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "49.49",
        entitlements: [{type: "RANK", key: "MVP_PLUS", amount: 1}],
    },
    {
        id: "rank-mvp-plus-plus-30",
        name: "30 Days MVP++",
        image: `${HYPIXEL_ASSETS}/icons/ranks/mvp-plus-plus.png`,
        description: "Receive 30 days of MVP++ on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "10.03",
        entitlements: [{type: "RANK", key: "MVP_PLUS_PLUS", amount: 1, durationDays: 30}],
    },
    {
        id: "rank-mvp-plus-plus-90",
        name: "90 Days MVP++",
        image: `${HYPIXEL_ASSETS}/icons/ranks/mvp-plus-plus.png`,
        description: "Receive 90 days of MVP++ on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "27.60",
        entitlements: [{type: "RANK", key: "MVP_PLUS_PLUS", amount: 1, durationDays: 90}],
    },
    {
        id: "rank-mvp-plus-plus-365",
        name: "365 Days MVP++",
        image: `${HYPIXEL_ASSETS}/icons/ranks/mvp-plus-plus.png`,
        description: "Receive 365 days of MVP++ on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel Discord.",
        price: "89.09",
        bestValue: true,
        entitlements: [{type: "RANK", key: "MVP_PLUS_PLUS", amount: 1, durationDays: 365}],
    },
];

export const categories = [
    {name: "Ranks", slug: "ranks", icon: `${HYPIXEL_ASSETS}/icons/ranks/mvp-plus.png`},
    {name: "Gold", slug: "gold", icon: `${HYPIXEL_ASSETS}/icons/gold/4.png`},
    {name: "SkyBlock Gems", slug: "skyblock-gems", icon: `${HYPIXEL_ASSETS}/icons/gems/5.png`},
    {name: "Housing+", slug: "housing-plus", icon: `${HYPIXEL_ASSETS}/icons/housing-plus/icon.png`},
    {name: "Boosters", slug: "boosters", icon: `${HYPIXEL_ASSETS}/icons/boosters/uhc.png`},
    {name: "Companions", slug: "companions", icon: `${HYPIXEL_ASSETS}/icons/companions/icon.png`},
];

export const productCategories: Record<string, Category> = {
    gold: {
        slug: "gold",
        title: "Gold",
        navLabel: "Gold",
        products: [
            {
                id: "gold-1000",
                name: "1,000 Gold",
                image: `${TEBEX_PACKAGE_IMAGES}/c6e8cde2a5a147fd6d6e9b7506a62fab195e09e0.png`,
                description: "Receive 1,000 Gold for use in the in-game store.",
                price: "12.55",
                entitlements: [{type: "STORE_CURRENCY", key: "GOLD", amount: 1000}]
            },
            {
                id: "gold-2500",
                name: "2,500 Gold",
                image: `${TEBEX_PACKAGE_IMAGES}/f7593a650ba2a2c289e9d9c1563162993a988783.png`,
                description: "Receive 2,500 Gold for use in the in-game store.",
                price: "31.38",
                entitlements: [{type: "STORE_CURRENCY", key: "GOLD", amount: 2500}]
            },
            {
                id: "gold-5000",
                name: "5,000 Gold",
                image: `${TEBEX_PACKAGE_IMAGES}/6a4d3ae6d8c154bd593730ca635bb97db3bb0b96.png`,
                description: "Receive 5,000 Gold for use in the in-game store.",
                price: "62.75",
                entitlements: [{type: "STORE_CURRENCY", key: "GOLD", amount: 5000}]
            },
            {
                id: "gold-11000",
                name: "11,000 Gold",
                image: `${TEBEX_PACKAGE_IMAGES}/1ba5948d998c8120b97e3631568096f871f09e7d.png`,
                description: "Receive 11,000 Gold for use in the in-game store.",
                bonus: "10,000 + 1,000 Bonus Gold",
                price: "125.50",
                bestValue: true,
                entitlements: [{type: "STORE_CURRENCY", key: "GOLD", amount: 11000}]
            },
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
            {
                id: "skyblock-gems-700",
                name: "700 SkyBlock Gems",
                image: `${TEBEX_PACKAGE_IMAGES}/954a049bd96b83d1b382ebf879a5d0ad9950bf7b.png`,
                description: "Receive 700 Gems for use in Hypixel SkyBlock.",
                price: "7.52",
                entitlements: [{type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 700}]
            },
            {
                id: "skyblock-gems-1800",
                name: "1,800 SkyBlock Gems",
                image: `${TEBEX_PACKAGE_IMAGES}/0a6ab622da84b3d5cf58d7da6eae32c858e9a5aa.png`,
                bonus: "1,750 + 50 Bonus Gems",
                description: "Receive 1,800 Gems for use in Hypixel SkyBlock.",
                price: "18.81",
                entitlements: [{type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 1800}]
            },
            {
                id: "skyblock-gems-3750",
                name: "3,750 SkyBlock Gems",
                image: `${TEBEX_PACKAGE_IMAGES}/595f2e53789958dbf6d88e816ae9d94df62a640b.png`,
                bonus: "3,500 + 250 Bonus Gems",
                description: "Receive 3,750 Gems for use in Hypixel SkyBlock.",
                price: "37.64",
                entitlements: [{type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 3750}]
            },
            {
                id: "skyblock-gems-7600",
                name: "7,600 SkyBlock Gems",
                image: `${TEBEX_PACKAGE_IMAGES}/da84c94d439b95fc5b27a18e3ebecc55485e32d4.png`,
                bonus: "7,000 + 600 Bonus Gems",
                description: "Receive 7,600 Gems for use in Hypixel SkyBlock.",
                price: "75.29",
                entitlements: [{type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 7600}]
            },
            {
                id: "skyblock-gems-17000",
                name: "17,000 SkyBlock Gems",
                image: `${TEBEX_PACKAGE_IMAGES}/f7c6103a133d1fd12fe4ade63694556d35c52055.png`,
                bonus: "14,000 + 3,000 Bonus Gems",
                description: "Receive 17,000 Gems for use in Hypixel SkyBlock.",
                price: "150.59",
                bestValue: true,
                entitlements: [{type: "SKYBLOCK_GEMS", key: "SKYBLOCK_GEMS", amount: 17000}]
            },
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
        products: [
            {
                id: "housing-plus",
                name: "Housing+",
                image: `${TEBEX_PACKAGE_IMAGES}/b397c6e80bffdf2906e70e5737fdf1940c4bf86a.png`,
                description: "Support Housing and unlock a larger plot size, higher limits, and more",
                originalPrice: "25.09",
                price: "20.07",
                bestValue: true,
                entitlements: [{type: "FEATURE", key: "HOUSING_PLUS", amount: 1}]
            },
        ],
        aboutTitle: "About Housing+",
        about: ["Housing+ is for the Housing enthusiast looking to have some major upgrades, with features including larger plot sizes, increased stat duration, and more!"],
        features: [
            {
                title: "Larger Plot Size",
                text: ["The default plot limit for regular players is 51x51 blocks, while Housing+ increases your maximum plot size to 255x255 blocks."]
            },
            {
                title: "Increased Limits",
                text: ["Furniture, holograms, regions, NPCs, stat keys, functions and inventory layouts receive increased limits for Housing+ players."]
            },
            {
                title: "Increased Stats Duration",
                text: ["By default, your stats are kept for 14 days. With Housing+, your stats are kept for an entire year!"]
            },
            {
                title: "Skull Packs",
                images: [`${HYPIXEL_ASSETS}/perks/pet-skins-1.png`, `${HYPIXEL_ASSETS}/perks/pet-skins-2.png`, `${HYPIXEL_ASSETS}/perks/pet-skins-3.png`, `${HYPIXEL_ASSETS}/perks/minion-skins-1.png`, `${HYPIXEL_ASSETS}/perks/minion-skins-2.png`]
            },
            {title: "Housing+ Chat Prefix", images: [`${HYPIXEL_ASSETS}/perks/housing-plus-prefix.png`]},
            {title: "Egg Hunt Item", images: [`${HYPIXEL_ASSETS}/perks/egg-hunt-item.png`]},
        ],
    },
    boosters: {
        slug: "boosters",
        title: "Boosters",
        navLabel: "Boosters",
        products: [
            {
                id: "booster-classic",
                name: "Classic Games Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/dae0c3bf16ba97e6cd6061edaf0703a0a2097b96.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "12.54",
                price: "10.03",
                wide: true,
                entitlements: [{type: "BOOSTER", key: "CLASSIC", amount: 1}]
            },
            {
                id: "booster-blitz",
                name: "Blitz Survival Games Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/f39a76ae65be2306ad3a437ffc5e2f4c2137da2d.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "18.81",
                price: "15.05",
                wide: true,
                entitlements: [{type: "BOOSTER", key: "BLITZ", amount: 1}]
            },
            {
                id: "booster-mega-walls",
                name: "Mega Walls Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/b2e449f88d0d5572ad669d0a6fea337eb9cb2f15.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "18.81",
                price: "15.05",
                entitlements: [{type: "BOOSTER", key: "MEGA_WALLS", amount: 1}]
            },
            {
                id: "booster-skywars",
                name: "SkyWars Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/87cf0af9060aae9c62f7f36d356361dc2749c76f.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "18.81",
                price: "15.05",
                entitlements: [{type: "BOOSTER", key: "SKYWARS", amount: 1}]
            },
            {
                id: "booster-uhc",
                name: "UHC Champions Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/c3e12d9dc120b9f20f44a189711bd9a384038a06.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                price: "15.05",
                entitlements: [{type: "BOOSTER", key: "UHC", amount: 1}]
            },
            {
                id: "booster-arcade",
                name: "Arcade Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/b7cf9000b10007dcefed2b96b2034268b600e766.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                price: "12.54",
                entitlements: [{type: "BOOSTER", key: "ARCADE", amount: 1}]
            },
            {
                id: "booster-tnt",
                name: "TNT Games Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/0ad707dfa4f375428c067b1f1deb1e9a447fa8f7.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra tokens.",
                price: "7.53",
                entitlements: [{type: "BOOSTER", key: "TNT", amount: 1}]
            },
            {
                id: "booster-warlords",
                name: "Warlords Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/5793cc5e5a1a8ea12d55ba5aaeb0b7a4627db232.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "11.28",
                price: "9.02",
                entitlements: [{type: "BOOSTER", key: "WARLORDS", amount: 1}]
            },
            {
                id: "booster-smash",
                name: "Smash Heroes Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/48fffd7bb7ae4f236366b277ca198f76259329bf.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "10.03",
                price: "8.02",
                entitlements: [{type: "BOOSTER", key: "SMASH", amount: 1}]
            },
            {
                id: "booster-cops",
                name: "Cops and Crims Network Booster",
                image: `${TEBEX_PACKAGE_IMAGES}/ff64b72493c5b89443ea8f1a86a3d40eacf133a5.png`,
                description: "This Booster increases the current coin multiplier by 2X, giving all players extra coins.",
                originalPrice: "10.03",
                price: "8.02",
                entitlements: [{type: "BOOSTER", key: "COPS_AND_CRIMS", amount: 1}]
            },
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
        products: [
            {
                id: "companion-magic-dog",
                name: "Magic Dog Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/68a7f516f16c1c2c8edc903c0f0bc143520578db.png`,
                description: "A loyal adventure companion for your lobby adventures.",
                originalPrice: "5.01",
                price: "4.00",
                entitlements: [{type: "COSMETIC", key: "MAGIC_DOG_COMPANION", amount: 1}]
            },
            {
                id: "companion-white-pug",
                name: "White Pug Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/c39defc58ee9623b3479d6d40d4c3943769d22e0.png`,
                description: "A small, friendly companion that follows you around.",
                price: "5.01",
                entitlements: [{type: "COSMETIC", key: "WHITE_PUG_COMPANION", amount: 1}]
            },
            {
                id: "companion-shibe",
                name: "Shibe Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/4e6d1e3e2fb21daa8b3cc2d0de20ff5cc61dbd94.png`,
                description: "Much doge. Wow. Such pet.",
                price: "5.01",
                entitlements: [{type: "COSMETIC", key: "SHIBE_COMPANION", amount: 1}]
            },
            {
                id: "companion-gorilla",
                name: "Gorilla Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/7044663db5c1e9b5f8b87bd804b48ec62a8ebddb.png`,
                description: "A strong companion for your Hypixel lobby.",
                originalPrice: "8.77",
                price: "7.02",

                entitlements: [{type: "COSMETIC", key: "GORILLA_COMPANION", amount: 1}]
            },
            {
                id: "companion-sloth",
                name: "Sloth Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/db008f495fad370ba924b5db8cac24491e816900.png`,
                description: "A calm companion that takes life at its own speed.",
                originalPrice: "8.77",
                price: "7.02",

                entitlements: [{type: "COSMETIC", key: "SLOTH_COMPANION", amount: 1}]
            },
            {
                id: "companion-chimp",
                name: "Chimp Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/a82974ebf07695e6666d55ad105076123091f1c5.png`,
                description: "A playful companion with plenty of energy.",
                originalPrice: "8.77",
                price: "7.02",

                entitlements: [{type: "COSMETIC", key: "CHIMP_COMPANION", amount: 1}]
            },
            {
                id: "companion-panda",
                name: "Panda Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/6c9b8d73d94bc7ff1f30af4d7b04a11b2e04db37.png`,
                description: "A rare panda buddy that joins you in lobbies.",
                originalPrice: "5.01",
                price: "4.00",

                entitlements: [{type: "COSMETIC", key: "PANDA_COMPANION", amount: 1}]
            },
            {
                id: "companion-penguin",
                name: "Penguin Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/b8e850e4966c52ef7521c602bfe20ce8e58f034a.png`,
                description: "A waddling Antarctic companion.",
                originalPrice: "12.54",
                price: "10.03",

                entitlements: [{type: "COSMETIC", key: "PENGUIN_COMPANION", amount: 1}]
            },
            {
                id: "companion-turtle",
                name: "Turtle Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/1af595991388dc617bbe294bfaa0e51d6f0fa2b3.png`,
                description: "A slow and steady turtle companion.",
                originalPrice: "12.54",
                price: "10.03",

                entitlements: [{type: "COSMETIC", key: "TURTLE_COMPANION", amount: 1}]
            },
            {
                id: "companion-elephant",
                name: "Elephant Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/ebcde7caeeef1eda74e3681636907d3f647ffcad.png`,
                description: "A dependable elephant companion.",
                originalPrice: "12.54",
                price: "10.03",

                entitlements: [{type: "COSMETIC", key: "ELEPHANT_COMPANION", amount: 1}]
            },
            {
                id: "companion-duck",
                name: "Duck Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/9c22a9a565bbbaa52038b79293e87d260c3f8316.png`,
                description: "A bright duck companion.",
                originalPrice: "5.01",
                price: "4.00",

                entitlements: [{type: "COSMETIC", key: "DUCK_COMPANION", amount: 1}]
            },
            {
                id: "companion-frog",
                name: "Frog Companion",
                image: `${TEBEX_PACKAGE_IMAGES}/434943877fb2f9bc42efcc402a59f858c9f390c5.png`,
                description: "A hoppy companion for your lobby.",
                originalPrice: "5.01",
                price: "4.00",

                entitlements: [{type: "COSMETIC", key: "FROG_COMPANION", amount: 1}]
            },
            {
                id: "companion-doggo-bundle",
                name: "Doggo Companion Bundle",
                image: `${TEBEX_PACKAGE_IMAGES}/9a31b24aabb3cbab8ffd906a430bb6fb2ee4d590.png`,
                description: "Contains Magic Dog, White Pug, and Shibe companions.",
                originalPrice: "13.75",
                price: "11.03",

                wide: true,
                entitlements: [
                    {type: "COSMETIC", key: "MAGIC_DOG_COMPANION", amount: 1},
                    {type: "COSMETIC", key: "WHITE_PUG_COMPANION", amount: 1},
                    {type: "COSMETIC", key: "SHIBE_COMPANION", amount: 1},
                ]
            },
            {
                id: "companion-jungle-bundle",
                name: "Jungle Companion Bundle",
                image: `${TEBEX_PACKAGE_IMAGES}/ee07be1b7915dd99586ecc1528801c9a0f9b78c0.png`,
                description: "Contains Gorilla, Sloth, and Chimp companions.",
                originalPrice: "25.09",
                price: "20.07",

                wide: true,
                entitlements: [
                    {type: "COSMETIC", key: "GORILLA_COMPANION", amount: 1},
                    {type: "COSMETIC", key: "SLOTH_COMPANION", amount: 1},
                    {type: "COSMETIC", key: "CHIMP_COMPANION", amount: 1},
                ]
            },
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
