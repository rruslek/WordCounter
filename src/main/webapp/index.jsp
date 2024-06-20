<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"  language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
        <h1 class="fs-4 font-weight-bold ">WordChecker</h1>
    </header>
</div>
<div class="container">
    <form action="/files" method="get" style="margin: 10px">
        <div class="form-group row">
            <div class="col-lg">
                <input class="form-control" name="word"
                       placeholder="Enter your word">
            </div>
            <div class="col-xs">
                <button type="submit" class="btn btn-primary">Find</button>
            </div>
        </div>
    </form>
    <table id="files" class="table table-striped">
        <caption>Results for word: <c:out value="\"${word}\"" default="" /></caption>
        <thead>
        <tr>
            <th scope="col">File Path</th>
            <th scope="col">Count</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${requestScope.files}">
            <tr>
                <td align="center"><a href="/download/${item.key}"><c:out value="${item.key}"/></a></td>
                <td align="center"><c:out value="${item.value}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br />
</div>
</body>
</html>