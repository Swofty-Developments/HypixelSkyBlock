import Image from "next/image";
import Link from "next/link";

const paymentMethods = [
  { name: "MasterCard", file: "mastercard.webp" },
  { name: "Visa", file: "visa.webp" },
  { name: "AmEx", file: "amex.webp" },
  { name: "PayPal", file: "paypal.webp" },
  { name: "iDEAL", file: "ideal.webp" },
  { name: "Paysafecard", file: "paysafecard.webp" },
  { name: "Apple Pay", file: "apple-pay.webp" },
  { name: "Google Pay", file: "gpay.webp" },
];

const footerLinks = [
  { label: "FAQ", href: "/faq" },
  { label: "Support", href: "https://support.hypixel.net/" },
  { label: "Cancel subscription", href: "https://subscriptions.hypixel.net/" },
  { label: "Terms", href: "/terms/checkout" },
  { label: "Privacy", href: "/terms/privacy" },
  { label: "Impressum", href: "/terms/impressum" },
];

export default function Footer() {
  return (
    <>
      {/* Store Footer - bg: rgb(6,25,40), padding 30px 0, margin-top 30px */}
      <footer style={{
        backgroundColor: "rgb(6, 25, 40)",
        color: "#fff",
        padding: "30px 0",
        marginTop: "30px",
        fontFamily: "Raleway, sans-serif",
        fontSize: "14px",
        lineHeight: "19.6px",
      }}>
        {/* Footer Inner - max-width 1160px, centered, flex column, gap 15px, justify space-between */}
        <div style={{
          maxWidth: "1160px",
          width: "1160px",
          margin: "0 auto",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          gap: "15px",
        }}>
          {/* Footer Inner Child - flex row, gap 15px */}
          <div style={{
            display: "flex",
            flexDirection: "row",
            gap: "15px",
            width: "1160px",
          }}>
            {/* Legal Notice - width ~847px, takes most space */}
            <div style={{ flex: 1 }}>
              <h4 style={{
                fontSize: "18px",
                fontWeight: 100,
                fontFamily: "Neuton, Raleway, serif",
                textTransform: "uppercase",
                color: "#fff",
                marginBottom: "10px",
                letterSpacing: "normal",
              }}>
                Legal Notice
              </h4>
              <p style={{ fontSize: "12px", color: "rgb(40, 82, 118)", lineHeight: "16.8px", marginBottom: "0" }}>
                The Hypixel server is in no way affiliated with Mojang Studios,
                nor should it be considered a company endorsed by Mojang Studios.
                Any contributions or purchases made on this store goes to the
                Hypixel team.
              </p>
              <p style={{ fontSize: "12px", color: "rgb(40, 82, 118)", lineHeight: "16.8px", marginBottom: "0" }}>
                For support or a purchase history, please send us a ticket on the{" "}
                <Link href="https://support.hypixel.net" style={{ color: "#fff", textDecoration: "none" }}>
                  Hypixel Support Help Desk
                </Link>
                . To{" "}
                <Link href="https://subscriptions.hypixel.net/" style={{ color: "#fff", textDecoration: "none" }}>
                  manage or cancel your subscriptions, visit our subscription portal
                </Link>
                .
              </p>
              <p style={{ fontSize: "12px", color: "rgb(40, 82, 118)", lineHeight: "16.8px", fontStyle: "italic" }}>
                Minecraft &reg;/TM, Mojang Studios / Notch &copy; 2009-2026
              </p>
            </div>

            {/* Payment Methods - text-align right, ~313px */}
            <div style={{ textAlign: "right", flexShrink: 0 }}>
              <h4 style={{
                fontSize: "18px",
                fontWeight: 100,
                fontFamily: "Neuton, Raleway, serif",
                textTransform: "uppercase",
                color: "#fff",
                marginBottom: "10px",
              }}>
                Payment methods we accept
              </h4>
              <div style={{ display: "flex", flexWrap: "wrap", gap: "4px", justifyContent: "flex-end", marginBottom: "5px" }}>
                {paymentMethods.map((pm) => (
                  <div
                    key={pm.name}
                    style={{
                      width: "48px",
                      height: "32px",
                      backgroundColor: "rgb(255, 255, 255)",
                      backgroundImage: `url(/images/payments/${pm.file})`,
                      backgroundSize: "contain",
                      backgroundPosition: "center",
                      backgroundRepeat: "no-repeat",
                      borderRadius: "3px",
                    }}
                    role="img"
                    aria-label={pm.name}
                  />
                ))}
              </div>
              <p style={{ fontSize: "12px", color: "rgb(40, 82, 118)", lineHeight: "16.8px" }}>
                All payments are handled and secured by Tebex.
              </p>
            </div>
          </div>

          {/* Footer Links - flex row, font-size 15px, height 30px, flex-wrap */}
          <ul style={{
            display: "flex",
            flexDirection: "row",
            flexWrap: "wrap",
            alignItems: "center",
            gap: "0",
            listStyle: "none",
            margin: 0,
            padding: 0,
            fontSize: "15px",
            lineHeight: "21px",
            height: "30px",
          }}>
            <li style={{ marginRight: "15px" }}>
              <span style={{ color: "#fff" }}>
                Server IP: <b style={{ textTransform: "uppercase", userSelect: "text" }}>mc.hypixel.net</b>
              </span>
            </li>
            <li style={{ marginRight: "15px" }}>
              <button style={{ background: "none", border: "none", color: "#fff", cursor: "pointer", padding: 0, fontSize: "inherit", display: "inline-flex", alignItems: "center", gap: "4px" }}>
                USD
                <svg style={{ width: 10, height: 10 }} viewBox="0 0 320 512" fill="currentColor">
                  <path d="M137.4 374.6c12.5 12.5 32.8 12.5 45.3 0l128-128c9.2-9.2 11.9-22.9 6.9-34.9s-16.6-19.8-29.6-19.8L32 192c-12.9 0-24.6 7.8-29.6 19.8s-2.2 25.7 6.9 34.9l128 128z" />
                </svg>
              </button>
            </li>
            {footerLinks.map((link) => (
              <li key={link.label} style={{ marginRight: "15px" }}>
                <Link href={link.href} style={{ color: "#fff", textDecoration: "none" }}>
                  {link.label}
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </footer>

      {/* Hypixel Footer - artwork.webp bg with dark overlay */}
      <footer style={{
        backgroundImage: "linear-gradient(rgba(26, 26, 26, 0.92), rgb(26, 26, 26)), url(/images/artwork.webp)",
        backgroundSize: "cover",
        backgroundPosition: "center",
        padding: "30px 0",
        color: "#fff",
        fontFamily: "Raleway, sans-serif",
        fontSize: "14px",
      }}>
        <div style={{
          maxWidth: "1160px",
          width: "1160px",
          margin: "0 auto",
          display: "flex",
          flexWrap: "wrap",
          justifyContent: "space-between",
        }}>
          {/* Logo + Copyright + Social */}
          <div>
            <Link href="https://hypixel.net/">
              <Image
                src="/images/hypixel-footer-logo.png"
                alt="Hypixel"
                width={200}
                height={60}
                style={{ height: "50px", width: "auto", display: "block", marginBottom: "5px" }}
              />
            </Link>
            <p style={{ fontSize: "13px", color: "rgb(128, 128, 128)", marginBottom: "5px" }}>
              Hypixel, Inc. &copy; 2026
            </p>
            <div style={{ display: "flex", gap: "6px", color: "rgb(128, 128, 128)", fontSize: "14px" }}>
              {["YouTube", "Twitter", "TikTok", "Instagram", "Threads", "Bluesky", "Facebook", "Discord"].map((s) => (
                <span key={s} style={{ cursor: "pointer" }} title={s}>{s.charAt(0)}</span>
              ))}
            </div>
          </div>

          {/* Link columns */}
          <div style={{ display: "flex", gap: "80px" }}>
            <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
              {["News & Announcements", "Community Forums", "Official Wiki", "Rules"].map((l) => (
                <Link key={l} href="#" style={{ fontSize: "13px", color: "rgb(128, 128, 128)", textDecoration: "none" }}>{l}</Link>
              ))}
            </div>
            <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
              {["Support", "Store", "Status", "Jobs"].map((l) => (
                <Link key={l} href="#" style={{ fontSize: "13px", color: "rgb(128, 128, 128)", textDecoration: "none" }}>{l}</Link>
              ))}
            </div>
            <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
              <Link href="#" style={{ fontSize: "13px", color: "rgb(128, 128, 128)", textDecoration: "none" }}>Translate</Link>
            </div>
          </div>
        </div>
      </footer>
    </>
  );
}
