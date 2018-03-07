<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@page import="java.util.ArrayList"%>
<%@ page import="FoodWeb_Pckg.Recipe" %>
<%@ page import="FoodWeb_Pckg.Nutrition" %>
<%@ page import="FoodWeb_Pckg.SPARQL_MAIN" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recipe list page</title>
  <link rel="stylesheet" href="css/RecipeCuisines.css" />
</head>


<body>
<%
	ArrayList<Recipe> recipelist = (ArrayList<Recipe>) request.getAttribute("recipelist"); 
	ArrayList<String> cuisineList= (ArrayList<String>) request.getAttribute("cuisine_list");
%>
	<div id="navibar">
		<ul>
			<li><a href="Home.jsp">Home</a></li>
			<li><a href="Query.jsp">Query page</a>
		</ul>
	</div>
	
	<form method="post" action="fullrecipe">
	<div id="leftpanel">
	<%for(Recipe i:recipelist){ 
	  SPARQL_MAIN obj_sparqlmain=new SPARQL_MAIN();
	  //ArrayList<Float>nutstats= (ArrayList<Float>)obj_sparqlmain.TotalNutrition(i);
	%>
		<table class="alpha" align="left"
				style="height: 60px; width: 100%; padding: 5px 5px 15px 5px; 
				background-color: #ff9933; margin-left: 5px;margin-bottom: 40px;">
			
			<tr>
				<th>
					<div style='float: left; padding-left: 12px; text-align: left'>
					<font size="5"> <% out.print(i.getName()); %> </font>
					</div>
					<div style='float: right; padding-right: 12px; text-align: right'>Rating:
						 <%out.print(i.getRating()); %></div>
				</th>
			</tr>

			<tr>
				<td style='padding-top: 15px;'>
					<% if (i.getDirection().length() >300){
						out.print(i.getDirection().substring(0,300)+"..........");
					}
					else{
						out.print(i.getDirection());
					}%>
				</td>
			</tr>
			<tr>
				<td align="right">
					<form method="post" action="Query_Id">
						<input type="hidden" name="recipeid" value=<%out.print(i.getId());%> >
						<input style="font-size: 25px; height:100px; padding-right: 12px;" type="submit" value="View Full Recipe">
					</form>
				</td>
			
			</tr>
		</table>
		<%} %>
		</div>
	</form>
		
	<%-- <footer  style="position: fixed; bottom: 0px; width: 100%; height: 60px;">
		 <div id="cuisinebar">
		 <form method="post" action="Query_Restaurants_2">
			<ul>
			<%for(String i:cuisineList){ %>
				<li><input type="submit" name="cusinesubmit" value=<%out.print(i);%> 
					style="font-size:12pt;color:white; background-color:red; border:2px solid #336600;" > </li>
			<%} %>
			</ul>
		</form>
		</div> 
	</footer> --%>
	
	
	<div id="rightpanel">
	 <form method="post" action="Query_Restaurants_2">
	<table class="alpha" align="center"
				style="height: 60px; width: 100%; padding: 5px 5px 15px 5px; 
				background-color: #ff9933; margin-left: 60px;margin-bottom: 40px;">
		<tr>
			<th style='padding-bottom: 15px;'><font size="4">Top suggested cuisines</font></th>
		</tr>
		<%for(String i:cuisineList){ %>
		<tr>
			<td align="center" style='padding-bottom: 10px;'>
			<input type="submit" name="cusinesubmit" value=<%out.print(i);%> 
					style="font-size:12pt;color:white; background-color:red; border:2px solid #336600;" >
			</td>
		</tr>
		<%}%>
	
	</table>
	</form>
	</div>

	
</body>
</html>