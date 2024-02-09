import { createContext, useState, useContext } from "react";

export const authContext = createContext();
export const useAuth = () => useContext(authContext);

export default function AuthProvider({ children }) {
  const [isAuth, setIsAuth] = useState(false);

  function login(username, password) {
    if (username === "test" && password === "test") {
      setIsAuth(true);
      return true;
    } else {
      setIsAuth(false);
      return false;
    }
  }

  function logout() {
    setIsAuth(false);
  }

  return (
    <authContext.Provider value={{ isAuth, setIsAuth, login, logout }}>
      {children}
    </authContext.Provider>
  );
}
