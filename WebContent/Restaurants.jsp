<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="FoodWeb_Pckg.Restaurant" %>
<%-- <%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%> --%>

<%@ page import="java.util.ArrayList" language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Restaurant page</title>
<link rel="stylesheet" href="css/Restaurants.css" />
</head>
<body>
<% 
	ArrayList<Restaurant> rest_list = (ArrayList<Restaurant>) request.getAttribute("restaurant_list");
   String cuisine = (String)request.getAttribute("cuisine_type");
%>

	<div id="navibar">
		<ul>
			<li><a href="Home.jsp">Home</a></li>
			<li><a href="Query.jsp">Query Page</a></li>
		</ul>
	</div>

 	
 	<div id="leftpanel">
 	<% for(Restaurant r:rest_list){  %>
		<table border="0" class="alpha" align="left"
				style="width: 80%;  
				margin-bottom: 15px; margin-left: 40px;background-color: #ff9933;">
			<tr>
				<th>
					<div style='float: left; padding-left: 0px; text-align: left'>
					<font size="5">	<%out.print(r.getName());%> </font> </div>
						
						<div style='float: right; padding-right: 0px; text-align: right'>
						Rating: <% out.print(r.getScore()); %> </div>
				</th>
			</tr>
			
			<div style='float: left; padding-left: 0px; text-align: left'>
			<tr>
				<th style='padding-top: 20px;' align="left"> 
				 Address </th>
			</tr>
			<tr><td><%out.print(r.getBuilding_no()); %> <%out.print(r.getStreet()); %></td></tr>
			<tr>	<td><%out.print(r.getBoro()); %> <%out.print(r.getZipcode()); %></td></tr>
			<tr>	<td style='padding-top: 10px;'> <b>Phone</b> :<%out.print(r.phone); %></td></tr>	
			</div>
		</table>
		<% } %>
	</div>
</body>
</html>