<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Food web home page</title>
<link rel="stylesheet" href="css/Query.css" />
 </head>
<body>
	<div id="navibar">
		<ul>
			<li><a href="Home.jsp">Home</a></li>
		</ul>
	</div>

	<div id="ingredient_body" class="ingredient_body">
			<table class="ingredient_table" align="left"
				style="height: 60px; width: 65%; padding: 5px 5px 15px 5px; 
				background-color: #ff9933; margin-left: 180px;margin-bottom: 40px;" 
				>
				<!-- form 1 -->
				<form method="post" action="Query_Recipe">
					<tr>
						<th align="center" colspan="3"><font size="5">Search Recipe by Ingredients</font></th>
					</tr>
					<tr>
						<td align="center">Enter Ingredients</td>
						<td><input type="text" class="minitextbox" name="querystring_ingredients"></td>
								
						<td colspan="2" align="center">
						<input type="submit" value="Search" id="sub_button1"></td>
						
					</tr>
				</form>
			</table>
				
			<table class="ingredient_table" align="left"
				style="height: 60px; width: 65%; padding: 5px 5px 15px 5px; 
				background-color: #ff9933; margin-left: 180px; margin-bottom: 40px;">
				<!-- form 2 -->
				<form method="post" action="Query_Restaurants">
					<tr>
					<th align="center" colspan="3"><font size="5">Search Restaurants By Cuisine</font> </th>
					</tr>
					
					<tr>
					<td align="center"> Enter Cuisines </td>
					<td><input type="text" name="querystring_cuisines"
						class="minitextbox"></td>
						
					<td colspan="2" align="center"><input type="submit"
						value="Search" id="sub_button2"></td>
						
					</tr>
				</form>
			</table>
			
			<table class="ingredient_table" align="left"
				style="height: 60px; width: 65%; padding: 5px 5px 15px 5px; 
				background-color: #ff9933; margin-left: 180px;margin-bottom: 20px;">
				<!-- form 3 -->
				<form method="post" action="Query_RecipeName">
				
					<tr>
						<th align="center" colspan="3"><font size="5">Search Recipe By Name</font> </th>
					</tr>
					<tr>
						<td align="center">Enter Name </td>
						<td><input type="text" name="recipe_name" class="minitextbox"> </td>
						<td colspan="2" align="center"> <input type="submit" value="Search" id="sub_button3">
						</td>
					</tr>	
				</form>
			</table>
		
	</div>
</body>

</html>