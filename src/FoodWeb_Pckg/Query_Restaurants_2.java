package FoodWeb_Pckg;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query_Restaurants_2
 */
@WebServlet("/Query_Restaurants_2")
public class Query_Restaurants_2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query_Restaurants_2() {
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
		String queried_cuisine = request.getParameter("cusinesubmit");		
		request.setAttribute("cuisine_type",queried_cuisine);
		
		SPARQL_MAIN obj_spaqlmain = new SPARQL_MAIN();
		ArrayList<Restaurant> restaurantlist=  obj_spaqlmain.QueryRestaurants(queried_cuisine);
		request.setAttribute("restaurant_list", restaurantlist);
		request.getRequestDispatcher("Restaurants.jsp").forward(request,response);
	}

}
