export default function Login() {
  return (
    <div className="Login">
      <p>Login Component</p>
      <div className="LoginForm">
        <form>
          <div>
            <label htmlFor="name">User Name</label>
            <input type="text" name="name" id="name" />
          </div>
          <div>
            <label htmlFor="pass">Password</label>
            <input type="password" name="pass" id="pass" />
          </div>
          <div>
            <button type="button">Login</button>
          </div>
        </form>
      </div>
    </div>
  );
}
