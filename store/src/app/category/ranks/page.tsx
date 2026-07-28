"use client";

import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/navigation";
import {useState} from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import BuyButton from "@/components/store/BuyButton";

// Easter theme colors
const saleColor = "rgb(230, 174, 71)";
const grayBorder = "rgb(51, 51, 51)";
const checkColor = "rgb(120, 174, 50)";

const ranks = [
    {
        name: "VIP Rank",
        productId: "rank-vip",
        icon: "https://dunb17ur4ymx4.cloudfront.net/packages/images/9f9aef1a97dd7a7061958a2d2dc72238e06a455c.png",
        prefix: "https://staticassets.hypixel.net/store/perks/vip-prefix.png",
        prefixAlt: "A green VIP prefix",
        originalPrice: "7.69",
        salePrice: "6.15",
        sale: false
    },
    {
        name: "VIP+ Rank",
        productId: "rank-vip-plus",
        icon: "https://dunb17ur4ymx4.cloudfront.net/packages/images/573d597eebcbfdc8c5485d6bf044abb60175c2c5.png",
        prefix: "https://staticassets.hypixel.net/store/perks/vip-plus-prefix.png",
        prefixAlt: "A green VIP prefix followed by a golden plus",
        originalPrice: "16.49",
        salePrice: "13.19",
        sale: false
    },
    {
        name: "MVP Rank",
        productId: "rank-mvp",
        icon: "https://dunb17ur4ymx4.cloudfront.net/packages/images/1b734bae59b7cef0dbbfc1b43d1ceed19b9cfebf.png",
        prefix: "https://staticassets.hypixel.net/store/perks/mvp-prefix.png",
        prefixAlt: "An aqua MVP prefix",
        originalPrice: "32.99",
        salePrice: "26.39",
        sale: false
    },
    {
        name: "MVP+ Rank",
        productId: "rank-mvp-plus",
        icon: "https://dunb17ur4ymx4.cloudfront.net/packages/images/be813e0264e79d198cc076ef686e4b6d1ed44c89.png",
        prefix: "https://staticassets.hypixel.net/store/perks/mvp-plus-prefix.png",
        prefixAlt: "An aqua MVP prefix followed by a red plus",
        originalPrice: "49.49",
        salePrice: "39.59",
        sale: false
    },
    {
        name: "MVP++ Rank",
        icon: "https://staticassets.hypixel.net/store/icons/ranks/mvp-plus-plus.png",
        prefix: "https://staticassets.hypixel.net/store/perks/mvp-plus-plus-prefix.png",
        prefixAlt: "A golden MVP prefix followed by two red plusses",
        originalPrice: null,
        salePrice: null,
        sale: false,
        isSub: true
    },
];

type FVal = boolean | string;
const features: { name: string; values: FVal[] }[] = [
    {name: "Game Replays (/games) Command", values: [true, true, true, true, true]},
    {name: "No chat cooldown", values: [true, true, true, true, true]},
    {name: "Rank-specific Cosmetics", values: [true, true, true, true, true]},
    {name: "Event Silver Multiplier", values: ["1.5x", "2x", "2.5x", "3x", "3.5x"]},
    {name: "Housing House Slots", values: ["2", "2", "3", "3", "3"]},
    {name: "Priority Queue", values: [true, true, true, true, true]},
    {
        name: "Extra SkyBlock Wardrobe Slots",
        values: ["+3 (5 total)", "+7 (9 total)", "+11 (13 total)", "+16 (18 total)", "+16 (18 total)"]
    },
    {name: "SkyBlock Quick Crafting", values: [true, true, true, true, true]},
    {name: "Access Seasonal SkyBlock Salesman", values: [true, true, true, true, true]},
    {name: "Extended Replay Access", values: ["1 day", "2 days", "3 days", "4 days", "4 days"]},
    {name: "Create Guilds", values: [false, true, true, true, true]},
    {name: 'Change your plus ("+") color', values: [false, false, false, true, true]},
    {name: "Game Map Selectors", values: [false, false, false, true, true]},
    {name: "Achievements Tracking", values: [false, false, false, true, true]},
    {name: "SkyBlock Epic Warp Scrolls", values: [false, false, false, true, true]},
    {name: "Free Monthly SkyBlock Gems", values: [false, false, false, false, "500 Monthly SkyBlock Gems"]},
    {name: "Statuses", values: [false, false, false, false, true]},
    {name: "Private Games (/party private) Command", values: [false, false, false, false, true]},
    {name: "Nickname (/nick) Command", values: [false, false, false, false, true]},
    {name: "Golden Guild Tag", values: [false, false, false, false, true]},
    {name: "Exclusive In-Game Emojis", values: [false, false, false, false, true]},
];

const moreFeatures: { name: string; values: FVal[] }[] = [
    {name: "Flying in lobbies", values: [true, true, true, true, true]},
    {name: "Click Effects", values: [true, true, true, true, true]},
    {name: "Housing Mailboxes", values: [true, true, true, true, true]},
    {name: "Ability to gift ranks", values: [true, true, true, true, true]},
    {name: "Particle Packs", values: [false, false, false, true, true]},
    {name: "Ride & Control Lobby Pets", values: [false, false, false, true, true]},
    {name: "Ability to change color overlay", values: [false, false, false, false, true]},
    {name: "/stream Command", values: [false, false, false, false, true]},
    {name: "Golden \"GG\" at the end of games", values: [false, false, false, false, true]},
];

const Check = () => <svg style={{width: 22, height: 22, display: "inline", color: checkColor}} viewBox="0 0 512 512"
                         fill="currentColor">
    <path
        d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM369 209L241 337c-9.4 9.4-24.6 9.4-33.9 0l-64-64c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0l47 47L335 175c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9z"/>
</svg>;
const Cross = () => <svg style={{width: 22, height: 22, display: "inline", color: "gray"}} viewBox="0 0 512 512"
                         fill="currentColor">
    <path
        d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c9.4-9.4 24.6-9.4 33.9 0l47 47 47-47c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-47 47 47 47c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-47-47-47 47c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l47-47-47-47c-9.4-9.4-9.4-24.6 0-33.9z"/>
</svg>;

// Cell border helper
const valBorder = (sale: boolean) => sale ? `2px solid ${saleColor}` : `2px solid ${grayBorder}`;
const valBorderBottom = (sale: boolean) => sale ? `1px solid ${saleColor}` : `1px solid ${grayBorder}`;

export default function RanksPage() {
    const router = useRouter();
    const [showMore, setShowMore] = useState(false);
    const [loadingProductId, setLoadingProductId] = useState<string | null>(null);
    const [checkoutError, setCheckoutError] = useState<string | null>(null);

    async function startCheckout(productId: string) {
        const username = localStorage.getItem("hypixel_username")?.trim();
        if (!username) {
            router.push(`/login?redirect=${encodeURIComponent(window.location.pathname)}`);
            return;
        }

        setCheckoutError(null);
        setLoadingProductId(productId);

        try {
            const response = await fetch("/api/checkout/sessions", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({productId, username}),
            });
            const payload = await response.json();

            if (!response.ok || !payload.url) {
                throw new Error(payload.error ?? "Unable to start checkout.");
            }

            window.location.assign(payload.url);
        } catch (error) {
            setCheckoutError(error instanceof Error ? error.message : "Unable to start checkout.");
            setLoadingProductId(null);
        }
    }

    return (
        <div className="store-page">
            <div className="store-container">
                <Navbar/>

                <div style={{
                    padding: "10px 15px",
                    fontSize: "38.4px",
                    fontWeight: 400,
                    fontFamily: "Neuton, Raleway, serif"
                }}>
                    Ranks
                </div>

                <div className="ranks-content">
                    {checkoutError && <p className="checkout-error">{checkoutError}</p>}
                    {/* Ranks Table */}
                    <table className={`ranks-table ${showMore ? "show-more" : ""}`} style={{
                        width: "100%",
                        borderCollapse: "separate",
                        borderSpacing: "6px 0px",
                        fontFamily: "Raleway, serif",
                        fontSize: "16px",
                        fontWeight: 700
                    }}>
                        <thead>
                        {/* Icon Row */}
                        <tr>
                            <th
                                style={{
                                    padding: "10px 25px",
                                    textAlign: "center",
                                    verticalAlign: "middle",
                                    minWidth: "170px",
                                    border: "none",
                                    background: "transparent",
                                    visibility: "hidden",
                                }}
                            />

                            {ranks.map((r) => (
                                <th
                                    key={r.name}
                                    className={r.sale ? "rank-sale-cell" : ""}
                                    style={{
                                        padding: "45px 25px 10px",
                                        textAlign: "center",
                                        verticalAlign: "top",
                                        backgroundColor: "#fff",
                                        borderRadius: "8px 8px 0 0",
                                        borderLeft: valBorder(r.sale),
                                        borderRight: valBorder(r.sale),
                                        borderTop: `2px solid ${r.sale ? saleColor : grayBorder}`,
                                        borderBottom: "0px none",
                                        position: "relative",
                                        minWidth: "170px",
                                    }}
                                >
                                    {r.sale && (
                                        <div
                                            style={{
                                                position: "absolute",
                                                top: "-1px",
                                                left: "-1px",
                                                right: "-1px",
                                                backgroundColor: saleColor,
                                                color: "#000",
                                                fontSize: "16px",
                                                fontWeight: 700,
                                                height: "34px",
                                                lineHeight: "34px",
                                                textAlign: "center",
                                                borderRadius: "8px 8px 0 0",
                                            }}
                                        >
                                            20% Off!
                                        </div>
                                    )}

                                    <Image
                                        src={r.icon}
                                        alt={`${r.name} icon`}
                                        width={144}
                                        height={144}
                                        unoptimized
                                        style={{
                                            width: "144px",
                                            height: "144px",
                                            objectFit: "contain",
                                        }}
                                    />
                                </th>
                            ))}
                        </tr>

                        {/* Name Row */}
                        <tr id="header-row">
                            <th
                                style={{
                                    padding: "10px 25px",
                                    border: "none",
                                    background: "transparent",
                                    visibility: "hidden",
                                }}
                            />

                            {ranks.map((r) => (
                                <th
                                    key={r.name}
                                    className={r.sale ? "rank-sale-cell" : ""}
                                    style={{
                                        padding: "10px 25px",
                                        textAlign: "center",
                                        backgroundColor: "#fff",
                                        borderLeft: valBorder(r.sale),
                                        borderRight: valBorder(r.sale),
                                        borderTop: "0px none",
                                        borderBottom: "0px none",
                                    }}
                                >
                                    <div
                                        className="rank-name"
                                        style={{
                                            color: "rgb(230, 174, 71)",
                                            fontSize: "22px",
                                            fontWeight: 700,
                                        }}
                                    >
                                        {r.name}
                                    </div>
                                </th>
                            ))}
                        </tr>

                        {/* Description Row */}
                        <tr>
                            <th
                                style={{
                                    padding: "10px 25px",
                                    border: "none",
                                    background: "transparent",
                                    visibility: "hidden",
                                }}
                            />

                            {ranks.map((r) => (
                                <th
                                    key={r.name}
                                    className={`rank-description ${
                                        r.sale ? "rank-sale-cell" : ""
                                    }`}
                                    style={{
                                        padding: "10px 20px",
                                        textAlign: "center",
                                        backgroundColor: "#fff",
                                        fontSize: "13px",
                                        fontWeight: 400,
                                        color: "#555",
                                        borderLeft: valBorder(r.sale),
                                        borderRight: valBorder(r.sale),
                                        borderTop: "0px none",
                                        borderBottom: "0px none",
                                    }}
                                >
                                    Receive the {r.name} on the Hypixel Minecraft Server, Hypixel
                                    Forums, and Hypixel Discord.
                                </th>
                            ))}
                        </tr>

                        {/* Price + Buy Row */}
                        <tr>
                            <th
                                style={{
                                    padding: "10px 25px",
                                    border: "none",
                                    background: "transparent",
                                    visibility: "hidden",
                                }}
                            />

                            {ranks.map((r) => (
                                <th
                                    key={r.name}
                                    className={r.sale ? "rank-sale-cell" : ""}
                                    style={{
                                        padding: "10px 25px 15px",
                                        textAlign: "center",
                                        backgroundColor: "#fff",
                                        borderLeft: valBorder(r.sale),
                                        borderRight: valBorder(r.sale),
                                        borderTop: "0px none",
                                        borderBottom: "0px none",
                                    }}
                                >
                                    {r.isSub ? (
                                        <div style={{marginBottom: "8px"}}>
                                            <Link
                                                href="#"
                                                style={{
                                                    color: "rgb(230, 174, 71)",
                                                    fontSize: "13px",
                                                    fontStyle: "italic",
                                                }}
                                            >
                                                Manage subscription
                                            </Link>
                                        </div>
                                    ) : (
                                        <div style={{marginBottom: "8px"}}>
                                            {r.sale && r.originalPrice && (
                                                <span
                                                    style={{
                                                        color: "red",
                                                        fontSize: "14px",
                                                        textDecoration: "line-through",
                                                    }}
                                                >
                {r.originalPrice} USD
              </span>
                                            )}

                                            <br/>

                                            <span
                                                style={{
                                                    fontSize: "21.6px",
                                                    fontWeight: 700,
                                                }}
                                            >
              {r.sale ? r.salePrice : r.originalPrice} USD
            </span>
                                        </div>
                                    )}

                                    <BuyButton
                                        label="BUY"
                                        onClick={() =>
                                            r.isSub
                                                ? router.push("/category/rank-upgrades")
                                                : startCheckout(r.productId!)
                                        }
                                        loading={Boolean(
                                            r.productId && loadingProductId === r.productId
                                        )}
                                    />
                                </th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {/* Chat Prefix Row */}
                        <tr>
                            <td style={{
                                padding: "10px 25px", textAlign: "left", verticalAlign: "middle",
                                backgroundColor: "rgb(242, 242, 242)",
                                borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                                borderTop: `2px solid ${grayBorder}`, borderBottom: `1px solid ${grayBorder}`,
                            }}>Chat Prefix
                            </td>
                            {ranks.map((r) => (
                                <td key={r.name} className={r.sale ? "rank-sale-cell" : ""} style={{
                                    padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                                    backgroundColor: "rgb(242, 242, 242)",
                                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                                    borderTop: `1px solid ${r.sale ? saleColor : grayBorder}`,
                                    borderBottom: valBorderBottom(r.sale),
                                }}>
                                    {/* eslint-disable-next-line @next/next/no-img-element */}
                                    <img src={r.prefix} alt={r.prefixAlt}
                                         style={{height: "28px", width: "auto", imageRendering: "pixelated"}}/>
                                </td>
                            ))}
                        </tr>
                        {/* Feature Rows */}
                        {features.map((feat, idx) => (
                            <tr key={feat.name}>
                                <td style={{
                                    padding: "10px 25px", textAlign: "left", verticalAlign: "middle",
                                    backgroundColor: idx % 2 === 0 ? "#fff" : "rgb(242, 242, 242)",
                                    borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                                    borderBottom: `1px solid ${grayBorder}`, borderTop: "0px none",
                                }}>{feat.name}</td>
                                {feat.values.map((val, i) => (
                                    <td key={i} className={ranks[i].sale ? "rank-sale-cell" : ""} style={{
                                        padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                                        backgroundColor: idx % 2 === 0 ? "#fff" : "rgb(242, 242, 242)",
                                        borderLeft: valBorder(ranks[i].sale), borderRight: valBorder(ranks[i].sale),
                                        borderBottom: valBorderBottom(ranks[i].sale), borderTop: "0px none",
                                    }}>
                                        {val === true ? <Check/> : val === false ? <Cross/> :
                                            <span style={{fontWeight: 400}}>{val}</span>}
                                    </td>
                                ))}
                            </tr>
                        ))}
                        {/* See More */}
                        <tr className="see-more-row">
                            <td colSpan={6} style={{
                                padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                                backgroundColor: "rgb(242, 242, 242)",
                                borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                                borderBottom: `1px solid ${grayBorder}`, borderTop: "0px none",
                                cursor: "pointer", fontWeight: 400,
                            }} onClick={() => setShowMore(!showMore)}>
                                <span className="rank-toggle-icon" aria-hidden="true"/>
                                Click here to see more features
                                <span className="rank-toggle-icon" aria-hidden="true"/>
                            </td>
                        </tr>
                        {showMore && moreFeatures.map((feat, idx) => (
                            <tr key={feat.name} className="more-feature">
                                <td style={{
                                    padding: "10px 25px", textAlign: "left", verticalAlign: "middle",
                                    backgroundColor: idx % 2 === 0 ? "#fff" : "rgb(242, 242, 242)",
                                    borderLeft: `2px solid ${grayBorder}`, borderRight: `2px solid ${grayBorder}`,
                                    borderBottom: `1px solid ${grayBorder}`, borderTop: "0px none",
                                }}>{feat.name}</td>
                                {feat.values.map((val, i) => (
                                    <td key={i} className={ranks[i].sale ? "rank-sale-cell" : ""} style={{
                                        padding: "10px 25px", textAlign: "center", verticalAlign: "middle",
                                        backgroundColor: idx % 2 === 0 ? "#fff" : "rgb(242, 242, 242)",
                                        borderLeft: valBorder(ranks[i].sale), borderRight: valBorder(ranks[i].sale),
                                        borderBottom: valBorderBottom(ranks[i].sale), borderTop: "0px none",
                                    }}>
                                        {val === true ? <Check/> : val === false ? <Cross/> :
                                            <span style={{fontWeight: 400}}>{val}</span>}
                                    </td>
                                ))}
                            </tr>
                        ))}
                        {/* Bottom Buy Row */}
                        <tr>
                            <td style={{padding: "10px 25px", border: "2px solid transparent"}}/>
                            {ranks.map((r) => (
                                <td key={r.name} className={r.sale ? "rank-sale-cell" : ""} style={{
                                    padding: "10px 25px", textAlign: "center", backgroundColor: "#fff",
                                    borderLeft: valBorder(r.sale), borderRight: valBorder(r.sale),
                                    borderBottom: `2px solid ${r.sale ? saleColor : grayBorder}`,
                                    borderTop: "0px none",
                                    borderRadius: "0 0 8px 8px",
                                }}>
                                    <BuyButton
                                        label="BUY"
                                        onClick={() => r.isSub ? router.push("/category/rank-upgrades") : startCheckout(r.productId!)}
                                        loading={Boolean(r.productId && loadingProductId === r.productId)}
                                    />
                                </td>
                            ))}
                        </tr>
                        </tbody>
                    </table>

                    {/* About Ranks */}
                    <div className="ranks-about">
                        <h3 style={{fontSize: "24px", fontWeight: 500, marginBottom: "10px"}}>About Ranks</h3>
                        <p style={{marginBottom: "16px"}}>
                            A Hypixel Rank is a fun way to help support the Hypixel Server! With different perks,
                            commands, and rank colors, this table above is to help you figure out which rank best suits
                            you.
                        </p>
                        <p style={{marginBottom: "16px"}}>
                            If you already have a rank, the value of your current rank is deducted from your future rank
                            upgrades. This upgrade price will be displayed automatically as you browse this page.
                        </p>
                        <h3 style={{fontSize: "24px", fontWeight: 500, marginBottom: "10px"}}>Need help?</h3>
                        <p style={{marginBottom: "8px"}}>
                            If you have any questions or issues related to payments, <Link
                            href="https://support.hypixel.net/" style={{color: "rgb(230, 174, 71)"}}>send us a ticket
                            here</Link>, and we will reply as fast as possible.
                        </p>
                        <p>
                            You can <Link href="https://subscriptions.hypixel.net/"
                                          style={{color: "rgb(230, 174, 71)"}}>manage or cancel your subscriptions by
                            visiting our subscription portal here</Link>.
                        </p>
                    </div>
                </div>
            </div>
            <Footer/>
        </div>
    );
}
