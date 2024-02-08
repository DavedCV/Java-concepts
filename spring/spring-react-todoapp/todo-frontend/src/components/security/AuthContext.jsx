import { createContext, useState, useContext } from "react";

export const authContext = createContext();
export const useAuth = () => useContext(authContext);

export default function AuthProvider({ children }) {
  const [isAuth, setIsAuth] = useState(false);
  return (
    <authContext.Provider value={{ isAuth, setIsAuth }}>
      {children}
    </authContext.Provider>
  );
}
