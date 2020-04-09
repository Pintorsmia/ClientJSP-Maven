<%--
  Created by IntelliJ IDEA.
  User: djanin
  Date: 18/02/2020
  Time: 11:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body><!--
      <form action="/Client_JSP_war_exploded/ServletTrain" method="post">
        LatA:<input type="text" name="latA"/><br/>
        LonA:<input type="text" name="lonA"/><br/>
        LatB:<input type="text" name="latB"/><br/>
        LonB:<input type="text" name="lonB"/><br/>
        devise:<input type="text" name="devise"/><br/>
        <input type="submit" value="Calculer"/>
      </form>
-->
      <form action="/Client_JSP_war/ServletTrain" method="post">
        Départ:<input type="text" name="villeDpt"/><br/>
        Destination:<input type="text" name="villeDst"/><br/>
        devise:<input type="text" name="devise"/><br/>
        <input type="submit" value="Calculer"/>
      </form>
  </body>
</html>
