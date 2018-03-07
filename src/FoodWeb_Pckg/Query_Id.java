package FoodWeb_Pckg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.andrewoma.dexx.collection.ArrayList;

/**
 * Servlet implementation class Query_Id
 */
@WebServlet("/Query_Id")
public class Query_Id extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query_Id() {
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
		
		String recipeid= request.getParameter("recipeid");
		System.out.println("recipe id is "+ recipeid);
		SPARQL_MAIN obj_sparqlmain = new SPARQL_MAIN();
		Recipe r=obj_sparqlmain.QueryRecipeFromId(recipeid);
		
		System.out.println(r.getName());
		request.setAttribute("recipe", r);
			
		request.getRequestDispatcher("Display_Recipe.jsp").forward(request, response);
		
	}

}
