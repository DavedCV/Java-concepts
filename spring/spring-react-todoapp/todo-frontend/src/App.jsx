import "./App.css";
import "./TodoApp.css";
import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
} from "react-router-dom";

import AuthProvider, { useAuth } from "./components/security/AuthContext";
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
      {
        path: "welcome",
        element: (
          <AuthenticatedRoute>
            <Welcome />
          </AuthenticatedRoute>
        ),
      },
      {
        path: "todos",
        element: (
          <AuthenticatedRoute>
            <ListTodos />
          </AuthenticatedRoute>
        ),
      },
      {
        path: "logout",
        element: (
          <AuthenticatedRoute>
            <Logout />
          </AuthenticatedRoute>
        ),
      },
    ],
    errorElement: <ErrorPage />,
  },
]);

function AuthenticatedRoute({ children }) {
  const authContext = useAuth();

  if (authContext.isAuth) return children;
  return <Navigate to="/login" />;
}

export default function TodoApp() {
  return (
    <div className="TodoApp">
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
    </div>
  );
}
