import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("test");
  const [password, setPassword] = useState("test");
  const [showSucessMessage, setShowSucessMessage] = useState(false);
  const navigate = useNavigate();

  function handleSubmit() {
    if (username === "test" && password === "test") {
      setShowSucessMessage(true);
      navigate("/welcome");
    }
  }

  return (
    <div className="Login">
      <p>Login Component</p>
      <div className="LoginForm">
        <div className="message">
          {showSucessMessage && (
            <div className="errorMessage">
              Authentication Failed. Please Check Your Credentials.
            </div>
          )}
        </div>
        <form>
          <div>
            <label htmlFor="name">User Name</label>
            <input
              type="text"
              name="name"
              id="name"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="pass">Password</label>
            <input
              type="password"
              name="pass"
              id="pass"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <div>
            <button
              type="button"
              onClick={() => handleSubmit(username, password)}
            >
              Login
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
