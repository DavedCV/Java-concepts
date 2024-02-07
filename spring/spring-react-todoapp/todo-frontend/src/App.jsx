//import "./App.css";
import "./TodoApp.css";
import Login from "./components/login/Login";
import Welcome from "./components/welcome/Welcome";
import ErrorPage from "./components/error/Error";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    children: [
      { path: "login", element: <Login /> },
      { path: "welcome", element: <Welcome /> },
    ],
    errorElement: <ErrorPage />,
  },
]);

export default function TodoApp() {
  return (
    <div className="TodoApp">
      <RouterProvider router={router} />
    </div>
  );
}
