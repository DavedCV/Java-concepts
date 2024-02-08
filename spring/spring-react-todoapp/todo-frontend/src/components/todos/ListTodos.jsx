export default function ListTodos() {
  const todo = [
    {
      id: 1,
      description: "Learn React",
      done: false,
      targetDate: new Date(2025, 1),
    },
    {
      id: 2,
      description: "Become an expert at React",
      done: false,
      targetDate: new Date(2025, 1),
    },
    {
      id: 3,
      description: "Visit Hawai",
      done: false,
      targetDate: new Date(2025, 1),
    },
  ];

  return (
    <div className="container">
      <h1>Things you want to do!</h1>
      <div>
        <table className="table">
          <thead>
            <tr>
              <td>id</td>
              <td>description</td>
              <td>done</td>
              <td>date</td>
            </tr>
          </thead>
          <tbody>
            {todo.map((todo) => (
              <tr key={todo.id}>
                <td>{todo.id}</td>
                <td>{todo.description}</td>
                <td>{todo.done.toString()}</td>
                <td>{todo.targetDate.toDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
