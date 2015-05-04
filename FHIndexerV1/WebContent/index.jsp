<%@ page import="java.util.HashMap" %>
<%@ page import="com.foodiehoodie.util.database.QueryProcessor" %>
<%@ page import="java.util.Set" %>
<%--
  Created by IntelliJ IDEA.
  User: Sourabh
  Date: 4/25/2015
  Time: 3:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  HashMap<String,String> newIngredientList = QueryProcessor.getListofNewIngredients();
  Set<String> ingredientSet=newIngredientList.keySet();
%>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <form action="Controller" name="mainform" method="post">
  <table>
    <tr><th>Main</th><th>Synonym</th><th>Popular Ingredient</th><th>Name</th><th>DocIds</th></tr>

    <%for(String ingredient:ingredientSet){%>
    <tr><td><input type="checkbox" name="main" value="<%=ingredient%>"  /></td><td><input type="checkbox" name="synonym" value="<%=ingredient%>"/></td>
      <td><input type="text" name="<%=ingredient%>_synonymid" value="0" size="4"/></td><td><input type="text" name="<%=ingredient%>" value="<%=ingredient%>"></td><td><%=newIngredientList.get(ingredient)%></td></tr>
<%}%>
  </table>
    <input type="submit">
  </form>
  </body>
</html>
