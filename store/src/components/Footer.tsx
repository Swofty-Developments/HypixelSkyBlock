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

const communityColumns = [
  ["News & Announcements", "Community Forums", "Official Wiki", "Rules"],
  ["Support", "Store", "Status", "Jobs"],
  ["Translate"],
];

export default function Footer() {
  return (
    <>
      <footer className="footer-store">
        <div className="footer-inner">
          <div className="footer-top">
            <section className="footer-legal">
              <h4>Legal Notice</h4>
              <p>
                The Hypixel server is in no way affiliated with Mojang Studios, nor should it be
                considered a company endorsed by Mojang Studios. Any contributions or purchases
                made on this store goes to the Hypixel team.
              </p>
              <p>
                For support or a purchase history, please send us a ticket on the{" "}
                <Link href="https://support.hypixel.net/">Hypixel Support Help Desk</Link>. To
                manage or cancel your subscriptions, visit our subscription portal.
              </p>
              <p><em>Minecraft &reg;/TM, Mojang Studios / Notch &copy; 2009-2026</em></p>
            </section>

            <section className="footer-payments">
              <h4>Payment Methods We Accept</h4>
              <div className="payment-grid">
                {paymentMethods.map((method) => (
                  <span
                    key={method.name}
                    className="payment-method"
                    role="img"
                    aria-label={method.name}
                    style={{ backgroundImage: `url(/images/payments/${method.file})` }}
                  />
                ))}
              </div>
              <p>All payments are handled and secured by Tebex.</p>
            </section>
          </div>

          <ul className="footer-links">
            <li>Server IP: <strong>mc.hypixel.net</strong></li>
            <li>USD ▾</li>
            {footerLinks.map((link) => (
              <li key={link.label}>
                <Link href={link.href}>{link.label}</Link>
              </li>
            ))}
          </ul>
        </div>
      </footer>

      <footer className="footer-community">
        <div className="footer-inner footer-community-inner">
          <section>
            <Link href="https://hypixel.net/">
              <Image
                src="/images/hypixel-footer-logo.png"
                alt="Hypixel"
                width={200}
                height={60}
                className="footer-community-logo"
              />
            </Link>
            <p>Hypixel, Inc. &copy; 2026</p>
            <div className="footer-socials" aria-label="Social links">
              {["Y", "T", "T", "I", "B", "F", "D"].map((label, index) => (
                <span key={`${label}-${index}`}>{label}</span>
              ))}
            </div>
          </section>

          <nav className="footer-columns" aria-label="Hypixel links">
            {communityColumns.map((column, index) => (
              <div className="footer-column" key={index}>
                {column.map((label) => (
                  <Link href="#" key={label}>{label}</Link>
                ))}
              </div>
            ))}
          </nav>
        </div>
      </footer>
    </>
  );
}
