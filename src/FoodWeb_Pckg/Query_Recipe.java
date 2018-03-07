package FoodWeb_Pckg;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jsonldjava.utils.Obj;

/**
 * Servlet implementation class Query_Recipe
 */
@WebServlet("/Query_Recipe")
public class Query_Recipe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query_Recipe() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String allergies_text = request.getParameter("querystring_allergies");
		String ingredients_text=request.getParameter("querystring_ingredients");
		//System.out.println(ingredients_text);
		
		ArrayList<String> ingredients_list = new ArrayList<String>();
	
		String[] ingredients_array= ingredients_text.split(",");
		
		for(int i=0;i<ingredients_array.length;i++) {
			ingredients_list.add(ingredients_array[i]);
		}
		
		System.out.println(ingredients_list.size());
		
		SPARQL_MAIN obj_sparqlmain= new SPARQL_MAIN();
	
		String query_cuisine=obj_sparqlmain.GenerateCuisineQuery(ingredients_list);
		System.out.println(query_cuisine);
		
		ArrayList<String> cuisine_list= obj_sparqlmain.QueryCuisine(query_cuisine);
		System.out.println("cuisine list is "+cuisine_list);
		
		String recipequeryString = obj_sparqlmain.GenerateRecipeQuery(ingredients_list);
		ArrayList<String> recipeId =obj_sparqlmain.QueryIngredients(recipequeryString);
		ArrayList<Recipe> Recipelist = obj_sparqlmain.QueryRecipeIngredientsabc(recipeId);
		
		//getting the nutrition information. 
		
		request.setAttribute("recipelist", Recipelist);
		request.setAttribute("cuisine_list", cuisine_list);
		
		request.getRequestDispatcher("RecipeCuisine.jsp").forward(request,response);
	}
}

	
