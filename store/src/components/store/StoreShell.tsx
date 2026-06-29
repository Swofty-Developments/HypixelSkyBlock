import Footer from "@/components/Footer";
import Navbar from "@/components/Navbar";

export default function StoreShell({
  children,
  username = "ArikSquad",
}: {
  children: React.ReactNode;
  username?: string;
}) {
  return (
    <div className="store-page">
      <div className="store-container">
        <Navbar username={username} />
        {children}
      </div>
      <Footer />
    </div>
  );
}
