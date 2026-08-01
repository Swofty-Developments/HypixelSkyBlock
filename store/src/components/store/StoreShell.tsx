import Footer from "@/components/Footer";
import Navbar from "@/components/Navbar";

export default function StoreShell({
                                       children
}: {
  children: React.ReactNode;
  username?: string;
}) {
  return (
    <div className="store-page">
      <div className="store-container">
          <Navbar/>
        {children}
      </div>
      <Footer />
    </div>
  );
}
