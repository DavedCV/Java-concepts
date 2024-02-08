import { Link } from "react-router-dom";

export default function Welcome() {
  return (
    <div className="welcome container">
      <h1>Welcome Component</h1>
      <p>
        Manage your <Link to="/todos">todos!</Link>
      </p>
    </div>
  );
}
