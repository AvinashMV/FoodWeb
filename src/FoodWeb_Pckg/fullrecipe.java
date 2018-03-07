package FoodWeb_Pckg;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class fullrecipe
 */
@WebServlet("/fullrecipe")
public class fullrecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fullrecipe() {
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
		
		System.out.println("inside dopost of fullrecipe method");
		
		String jsprecipename = request.getParameter("typeofrecipe");
		System.out.println("Retrieved recipe name is "+jsprecipename);
		
		SPARQL_MAIN obj_sparqlmain = new SPARQL_MAIN();
		Recipe r=obj_sparqlmain.QueryRecipeName(jsprecipename);
		
		
		
		
	}

}
