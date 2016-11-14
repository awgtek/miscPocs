<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

  <head>
    <title>Basic Model Test</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" media="all" href="<s:url value='/resources/style.css' />"/>
  </head>

  <body>

    <h2>Basic Model Form</h2>

    <s:url var="formUrl" value="edit" />
    <sf:form modelAttribute="basicpojo" action="${formUrl}">

      <fieldset>

        <div>
          <label for="email"><s:message code="basicpojo.email" />: </label>
          <sf:input path="email" />
        </div>
        <div>
          <label><s:message code="basicpojo.type" />: </label>
          <ul>
            <c:forEach var="type" items="${allTypes}" varStatus="typeStatus">
              <li>
                <sf:radiobutton path="someType" value="${type}" />
                <label for="someType${typeStatus.count}">
                  <s:message code="someType.${type}" />
                </label>
              </li>
            </c:forEach>
          </ul>
        </div>

        <div class="submit">
          <button type="submit" name="save"><s:message code="basicpojo.submit" /></button>
        </div>

      </fieldset>

    </sf:form>

  </body>

</html>
