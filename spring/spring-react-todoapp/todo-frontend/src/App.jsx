import "./App.css";
import "./TodoApp.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import AuthProvider from "./components/security/AuthContext";
import Root from "./components/common/Root";
import Login from "./components/login/Login";
import Welcome from "./components/welcome/Welcome";
import ListTodos from "./components/todos/ListTodos";
import ErrorPage from "./components/common/Error";
import Logout from "./components/common/Logout";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      { path: "login", element: <Login /> },
      { path: "welcome", element: <Welcome /> },
      { path: "todos", element: <ListTodos /> },
      { path: "logout", element: <Logout /> },
    ],
    errorElement: <ErrorPage />,
  },
]);

export default function TodoApp() {
  return (
    <div className="TodoApp">
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
    </div>
  );
}
