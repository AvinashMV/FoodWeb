<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="FoodWeb_Pckg.Recipe"%>
<%@ page import="FoodWeb_Pckg.Nutrition"%>
<%@ page import="FoodWeb_Pckg.SPARQL_MAIN"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="css/Display_Recipe.css" />
</head>
<body>
	<%
		Recipe r = (Recipe) request.getAttribute("recipe");
	%>

	<div id="navibar">
		<ul>
			<li><a href="Home.jsp">Home</a></li>
			<li><a href="Query.jsp">Query Page</a></li>
		</ul>
	</div>


	<div id="leftpanel">
		<table class="alpha" align="left"
			style="width: 100%; padding: 5px 5px 15px 5px; background-color: #ff9933; margin-left: 5px; margin-bottom: 40px;">

			<tr>
				<th>
					<div style='float: left; padding-left: 12px; text-align: left'>
						<font size="5"> <%
 	out.print(r.getName());
 %>
						</font>
					</div>
					<div style='float: right; padding-right: 12px; text-align: right'>
						Rating:
						<%
						out.print(r.getRating());
					%>
					</div>
				</th>
			</tr>

			<tr>
				<th sytle='padding-top: 20px;'><div style='float: left; padding-right: 12px; padding-top: 20px;
					padding-bottom: 6px; text-align: left'>
						 <u>Directions</u>
					</div></th>
			</tr>
			<tr>
				<td>
					<%
						out.print(r.getDirection());
					%>
				</td>
			</tr>

			<tr>
				<th><div style='float: left; padding-right: 12px; padding-top: 20px; text-align: left'>
				<u>Ingredients Measurement</u> </div></th>
			</tr>
				<%java.util.Formatter formatter = new java.util.Formatter(); %>
			<tr><td>
				<ul>
					<% for(Nutrition n:r.getIngredients()){ %>
					<li><%out.print(n.getValue()); %>  <%out.print(n.getUnit()); %>  <%out.print(n.getName()); %></li>
					<%} %>
				</ul>
				</td>
			</tr>
		</table>
	</div>

	<div id="rightpanel">
		<%
			SPARQL_MAIN obj_sparqlmain = new SPARQL_MAIN();
			ArrayList<Float> nutstats = obj_sparqlmain.TotalNutrition(r);
		%>

		<table class="alpha" align="left"
			style="height: 60px; width: 100%; padding: 5px 5px 15px 5px; background-color: #ff9933; margin-left: 30px; margin-bottom: 40px;">

			<tr>
				<th align="center" colspan="2" style='padding-bottom: 20px;'><font size="5">Nutrition stats </font></th>
			</tr>
			<tr>
				<td>Water(g):</td>
				<td>
					<%
						out.print(nutstats.get(0));
					%>
				</td>
			</tr>
			<tr>
				<td>Energy(kcal):</td>
				<td>
					<%
						out.print(nutstats.get(1));
					%>
				</td>
			</tr>
			<tr>
				<td>Protein(g):</td>
				<td>
					<%
						out.print(nutstats.get(2));
					%>
				</td>
			</tr>
			<tr>
				<td>Total Lipid(g):</td>
				<td>
					<%
						out.print(nutstats.get(3));
					%>
				</td>
			</tr>
			<tr>
				<td>Carbohydrates(g):</td>
				<td>
					<%
						out.print(nutstats.get(4));
					%>
				</td>
			</tr>
			<tr>
				<td>Fiber(g):</td>
				<td>
					<%
						out.print(nutstats.get(5));
					%>
				</td>
			</tr>
			<tr>
				<td>Sugar(g):</td>
				<td>
					<%
						out.print(nutstats.get(6));
					%>
				</td>
			</tr>
			<tr>
				<td>Calcium(mg):</td>
				<td>
					<%out.print(nutstats.get(7)); %>
				</td>
			</tr>
		</table>
	</div>


</body>
</html>