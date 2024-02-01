<%@include file="common/header.jspf" %>
<%@include file="common/navigation.jspf" %>

<div class="container">
    <h1>List Todos</h1>
    <p>${name}, your todos are:</p>
    <table class="table">
        <thead>
        <tr>
            <th>Id</th>
            <th>Description</th>
            <th>Target Date</th>
            <th>Is Done?</th>
            <th>Delete</th>
            <th>Update</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${todos}" var="todo">
            <tr>
                <td>${todo.id}</td>
                <td>${todo.description}</td>
                <td>${todo.targetDate}</td>
                <td>${todo.done}</td>
                <td><a href="deleteTodo?id=${todo.id}" class="btn btn-warning">Delete</a></td>
                <td><a href="updateTodo?id=${todo.id}" class="btn btn-danger">Update</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="addTodo" class="btn btn-success">Add Todo</a>
</div>
<%@include file="common/footer.jspf" %>


