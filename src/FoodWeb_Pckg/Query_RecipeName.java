package FoodWeb_Pckg;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query_RecipeName
 */
@WebServlet("/Query_RecipeName")
public class Query_RecipeName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query_RecipeName() {
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
		doGet(request, response);
		String queried_recipename= request.getParameter("recipe_name");
		System.out.println("recipe name is "+queried_recipename);
		
		//passing this to sparql main and getting arraylist of recipe to display. 
		
		SPARQL_MAIN obj_sparqlmain = new SPARQL_MAIN();
		ArrayList<Recipe>recipelists =  obj_sparqlmain.QueryRecipe1(queried_recipename);
		
		//removing repititio of reicpe. 
		ArrayList<Recipe>recipelists_cleaned = obj_sparqlmain.RemoveRepetition(recipelists);
		request.setAttribute("recipeList_3", recipelists_cleaned);
		request.getRequestDispatcher("Full_Recipes.jsp").forward(request, response);
		
		
	}

}
