"use client";

import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/navigation";
import {useState} from "react";
import Footer from "@/components/Footer";
import Navbar from "@/components/Navbar";
import BuyButton from "@/components/store/BuyButton";
import {directProducts} from "@/data/store";

const upgradeProducts = directProducts
    .filter((product) => product.id.startsWith("rank-mvp-plus-plus"))
    .map((product, index) => ({
        ...product,
        name: ["Monthly MVP++ Subscription", "Quarterly MVP++ Subscription", "Annual MVP++ Subscription"][index],
        renewal: ["every 30 days", "every 3 months", "every year"][index],
        image: "https://dunb17ur4ymx4.cloudfront.net/packages/images/47d4241a58953038edba484c04b94d98c6db8960.png",
    }));

const features = [
    {
        title: "New Rank Prefix and Color",
        paragraphs: ["The new MVP++ rank prefix will make you stand out even more with your name and prefix displayed in gold."],
        image: "https://staticassets.hypixel.net/store/perks/mvp-plus-plus-prefix.png",
        alt: "A golden MVP prefix followed by two red pluses",
    },
    {
        title: "Monthly 500 SkyBlock Gems Included",
        paragraphs: ["Every month you're subscribed to MVP++, you'll automatically receive 500 SkyBlock Gems!"],
    },
    {
        title: "Access to Nickname (/nick) Command",
        paragraphs: [
            "Using the /nick command will let you disguise your name and skin in all Hypixel games.",
            "Please note you can only change your name up to 3 times each day and all reporting commands work with nicknames. Your nickname is a randomly generated name and cannot be customized. Nicknames can be activated in lobbies but will only display in games.",
        ],
        image: "https://staticassets.hypixel.net/store/perks/nick-book.png",
        alt: "Minecraft nickname setup book",
    },
    {
        title: "Access to /stream Command",
        paragraphs: [
            "The /stream command lets you create a party that anyone can join, which is very useful if you are live-streaming, as anyone watching the stream can join in without you having to invite them.",
            "The command also lets you set a maximum number of players, disable party chat, and more.",
        ],
        image: "https://staticassets.hypixel.net/store/perks/public-party-book.png",
        alt: "Minecraft public party settings book",
    },
    {
        title: "Access to Private Games (/party private) Command",
        paragraphs: [
            "The Private Party settings let you create private games in which only you and your party will play. During a private game, stats and other achievements are not earned to prevent boosting.",
            "Currently supported games include Bed Wars, Duels, SkyWars, Arcade, Build Battle, Murder Mystery, TNT Games, Blitz Survival Games, UHC Champions, and Warlords. Some modes require a minimum party size.",
        ],
        image: "https://staticassets.hypixel.net/store/perks/private-party-message.png",
        alt: "Hypixel private game chat message",
    },
    {
        title: "Golden Guild Tag",
        paragraphs: ["As a Guild Master, MVP++ has the option to turn on the Golden Guild Tag for you and your guild members with the command /guild tagcolor."],
        image: "https://staticassets.hypixel.net/store/perks/gold-tag-color.png",
        alt: "Minecraft guild tag color menu",
    },
    {
        title: "Animated Forum Frame and Private Forum",
        paragraphs: ["As MVP++, your forum frame is now gold and animated. You’ll also gain access to a private MVP++ only subforum."],
        image: "https://staticassets.hypixel.net/store/perks/mvp-plus-plus-forums-border.gif",
        alt: "Animated golden forum avatar frame",
    },
    {
        title: "Auto-Tip Enabled",
        paragraphs: ["Don’t want to manually tip players with boosters all the time? With Auto-Tip, you don’t have to anymore – active boosters will be automatically tipped every 10 minutes."],
    },
    {
        title: "New Unique Join Message and Particle Effect on Lobby Join",
        paragraphs: ["For that extra special feeling, your presence will be announced to the whole lobby, together with a hail of particles."],
        image: "https://staticassets.hypixel.net/store/perks/special-join-message.png",
        alt: "Hypixel special lobby join message",
    },
    {
        title: "Exclusive In-Game Emojis",
        paragraphs: ["Express yourself even more with exclusive emojis you can use in-game by typing their shortcuts in chat."],
        image: "https://staticassets.hypixel.net/store/perks/emotes.png",
        alt: "List of Hypixel MVP++ chat emojis",
    },
    {
        title: 'Golden "GG" at the End of Games',
        paragraphs: ["Feeling like you enjoyed yourself in the last game? Let everyone know with your new and shiny golden GG!"],
        image: "https://staticassets.hypixel.net/store/perks/golden-gg.png",
        alt: "Golden GG Minecraft chat message",
    },
];

export default function RankUpgradesPage() {
    const router = useRouter();
    const [loading, setLoading] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    async function subscribe(productId: string) {
        const username = localStorage.getItem("hypixel_username")?.trim();
        if (!username) {
            router.push("/login?redirect=%2Fcategory%2Frank-upgrades");
            return;
        }
        setLoading(productId);
        setError(null);
        try {
            const response = await fetch("/api/checkout/sessions", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({productId, username}),
            });
            const payload = await response.json();
            if (!response.ok || !payload.url) throw new Error(payload.error ?? "Unable to start checkout.");
            window.location.assign(payload.url);
        } catch (checkoutError) {
            setError(checkoutError instanceof Error ? checkoutError.message : "Unable to start checkout.");
            setLoading(null);
        }
    }

    return (
        <div className="store-page">
            <div className="store-container">
                <Navbar/>
                <main className="category-content rank-upgrades">
                    <h1>Rank Upgrades</h1>
                    {error && <p className="checkout-error">{error}</p>}
                    <div className="upgrade-grid">
                        {upgradeProducts.map((product) => (
                            <article className="upgrade-card" key={product.id}>
                                <Image src={product.image} alt={`${product.name} icon`} width={144} height={144}
                                       unoptimized/>
                                <h2>{product.name}</h2>
                                <p>Receive the MVP++ Rank on the Hypixel Minecraft Server, Hypixel Forums, and Hypixel
                                    Discord.</p>
                                <p className="upgrade-renewal">Subscription
                                    renews <strong>{product.renewal}</strong> and can be cancelled at any time.</p>
                                <strong className="upgrade-price">{product.price} USD</strong>
                                <BuyButton label="SUBSCRIBE" onClick={() => subscribe(product.id)}
                                           loading={loading === product.id}/>
                            </article>
                        ))}
                    </div>

                    <Link className="rank-back-button" href="/category/ranks">‹ GO BACK TO ALL RANKS</Link>
                    <section className="copy-block">
                        <h2>About Rank Upgrades</h2>
                        <p>MVP++ is an exclusive rank upgrade to your existing MVP+ rank. This rank upgrade allows you
                            to access some very useful commands and is the best way to support the Hypixel Network.</p>
                        <p>MVP++ is a recurring subscription that will be charged <strong>every 30 days (monthly), 3
                            months (quarterly), or 1 year (annual)</strong> depending on the option you choose, and only
                            one subscription can be active at a time. If you want to purchase prepaid days without a
                            subscription, you can do so using Gold in the in-game store. <strong>Any prepaid days you
                                already have will be paused if you choose to subscribe and will continue after your
                                subscription ends.</strong></p>
                        <p>MVP++ is not a lifetime rank and will expire after the subscription is cancelled or the
                            prepaid time has passed. Once it expires, you will automatically go back to MVP+.</p>
                    </section>
                    <section className="copy-block">
                        <h2>Need help?</h2>
                        <p>If you have any questions or issues related to payments, <Link
                            href="https://support.hypixel.net/">send us a ticket here</Link>, and we will reply as fast
                            as possible.</p>
                        <p>You can <Link href="https://portal.tebex.io/">manage or cancel your subscriptions by visiting
                            our subscription portal here</Link>.</p>
                    </section>
                    <section className="features-block">
                        <h2>Features</h2>
                        {features.map((feature) => (
                            <details className="feature-panel" key={feature.title}>
                                <summary>{feature.title}</summary>
                                <div className="feature-panel-body">
                                    {feature.paragraphs.map((paragraph) => <p key={paragraph}>{paragraph}</p>)}
                                    {feature.image &&
                                        <Image src={feature.image} alt={feature.alt ?? ""} width={700} height={430}
                                               unoptimized/>}
                                </div>
                            </details>
                        ))}
                    </section>
                </main>
            </div>
            <Footer/>
        </div>
    );
}
