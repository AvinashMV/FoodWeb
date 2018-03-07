package FoodWeb_Pckg;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query_Restaurants
 */
@WebServlet("/Query_Restaurants")
public class Query_Restaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query_Restaurants() {
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

		String cuisinestring = request.getParameter("querystring_cuisines");
		request.setAttribute("cuisine_type",cuisinestring );
		
		SPARQL_MAIN obj_spaqlmain = new SPARQL_MAIN();
		ArrayList<Restaurant> restaurantlist=  obj_spaqlmain.QueryRestaurants(cuisinestring);
		request.setAttribute("restaurant_list", restaurantlist);
		request.getRequestDispatcher("Restaurants.jsp").forward(request,response);
	
		System.out.println(restaurantlist);

	}

}
