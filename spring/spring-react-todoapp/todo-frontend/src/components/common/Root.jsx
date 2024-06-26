import { Outlet } from "react-router-dom";

import Header from "./Header";
import Footer from "./Footer";

export default function Root() {
  return (
    <div className="Root">
      <Header />
      <Outlet />
      <Footer />
    </div>
  );
}
