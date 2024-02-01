<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Add Todo</title>
    <link href="webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h1>Enter Todo Details</h1>
        <form:form method="post" modelAttribute="todo">
            Description: <form:input type="text" name="description" path="description" required="required"/>
            <form:errors path="description" cssClass="text-warning"/>
            <form:input type="hidden" path="done"/>
            <form:input type="hidden" path="id"/>
            <input type="submit" class="btn btn-success">
        </form:form>
    </div>
    <script src="webjars/jquery/3.7.1/jquery.min.js"></script>
</body>
</html>