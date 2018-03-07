package FoodWeb_Pckg;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.jena.query.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.system.Txn;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

//types of searches: ing to recipe; name to recipe; cuisine to restaurants
public class SPARQL_MAIN {
	
	static String pre = "PREFIX rdf: <http://w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX fw: <http://www.semanticweb.org/bhangal/ontologies/2017/9/FoodWeb#>";	

public static void main(String args[]){

	ArrayList<String> ArrayIngredients = new ArrayList();
	
	ArrayIngredients.add("romaine lettuce");
	ArrayIngredients.add("black olive");
	ArrayIngredients.add("grape tomatoes");
	//QueryCuisine(GenerateCuisineQuery(ArrayIngredients));		//get cuisine from ingredients
	
	
	QueryRestaurants("greek");									//get restaurant from cuisine
	
	ArrayIngredients.clear();
	ArrayIngredients.add("chocolate");
	ArrayIngredients.add("whipping cream");
	ArrayList<String> recipeId = QueryIngredients(GenerateRecipeQuery(ArrayIngredients));		//get recipe id from ingredients
	QueryRecipeIngredients1(recipeId);															//get recipes from id

//	//recipe details as nutrition stats
//	ArrayList<String> recipeId = new ArrayList();
//	recipeId.add("11119");
//	ArrayList<Recipe> recipeDetails= QueryRecipeIngredients1(recipeId);
//	
//	//TotalNutrition(recipeDetails.get(0));														//get total nutrition stats
//	
	
	QueryRecipe1("apple pie");		//get recipe from recipe name
	
}


public static String GenerateRecipeQuery(ArrayList<String> array) {
	
	String Query= pre + " SELECT ?id (count(?id)as ?ncount) WHERE {?Res fw:has_id ?id. ?Res fw:has_name ?recipe. ?Res fw:has_rating ?rating. ?Res fw:has_ingredient ?ing. FILTER( ";
	
	for(String n: array) {
		//Query = Query + "contains(str(?ing)," + "'" +n + "'" + ")||";
		Query = Query + "regex(?ing, '"+ n +"', 'i')||";
	}
	
	Query = Query.substring(0, Query.length() - 2) + ") } group by ?id order by  desc (?ncount) limit 20";

	return Query;
}

public static String GenerateCuisineQuery(ArrayList<String> array) {
	
	String Query= pre + " SELECT ?name (count(?name) as ?ncount)  WHERE { ?Res fw:has_ingredient ?ing. ?Res fw:has_cuisine ?name. FILTER(";
	
	for(String n: array) {
		Query = Query + "regex(?ing, '"+ n +"', 'i')||";//"contains(str(?ing)," + "'" +n + "'" + ")||";
	}
	
	Query = Query.substring(0, Query.length() - 2) + ") } group by ?name order by desc (?ncount) LIMIT 5";

	return Query;
}

public static ArrayList<Restaurant> QueryRestaurants(String cuisine) {
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/abc3-csv.ttl").toString());
	
	String strQuery1 = pre + " SELECT ?name ?boro ?building ?phone ?street ?zipcode ?date ?flag ?voilation ?score"
			+ " WHERE { ?Res fw:has_name ?name. ?Res fw:has_cuisine ?cuisine. ?Res fw:has_boro ?boro. "
			+ "?Res fw:has_building_no ?building. ?Res fw:has_street ?street. ?Res fw:has_zipcode ?zipcode. "
			+ "?Res fw:has_phone ?phone. OPTIONAL { ?Res fw:has_score ?score.  ?Res fw:has_violation_discription ?voilation. "
			+ "?Res fw:has_inspection_date ?date. ?Res fw:has_violation_flag ?flag.} "
			+ "FILTER (lcase(str(?cuisine)) ='"+cuisine+"')} LIMIT 10";
	
	Query query = QueryFactory.create(strQuery1);

	QueryExecution queryExe = QueryExecutionFactory.create(query,model);
	
	ArrayList<Restaurant> array =new ArrayList();

	try{


	ResultSet resultset =  queryExe.execSelect();

	System.out.println(resultset);

	while(resultset.hasNext()){
		
	Restaurant r = new Restaurant();	

	QuerySolution soln = resultset.nextSolution();

	Literal name = soln.getLiteral("?name");
	Literal boro = soln.getLiteral("?boro");
	Literal building = soln.getLiteral("?building");
	Literal phone = soln.getLiteral("?phone");
	Literal street = soln.getLiteral("?street");
	Literal zipcode = soln.getLiteral("?zipcode");
	Literal date = soln.getLiteral("?date");
	Literal flag = soln.getLiteral("?flag");
	Literal violation = soln.getLiteral("?voilation");
	Literal score = soln.getLiteral("?score");
	
	String s_name="",s_boro ="",s_building="",s_phone="",s_street="",s_zipcode="",s_date="",s_flag="",s_violation="",s_score="";
	
	if(name!=null)
		s_name = name.toString();
	if(boro!=null)
		s_boro = boro.toString();
	if(building!=null)
		s_building = building.toString();
	if(phone!=null)
		s_phone = phone.toString();
	if(street!=null)
		s_street = street.toString();
	if(zipcode!=null)
		s_zipcode = zipcode.toString();
	if(date!=null)
		s_date = date.toString();
	if(flag!=null)
		s_flag = flag.toString();
	if(violation!=null)
		s_violation = violation.toString();
	if(score!=null)
		s_score = score.toString();
	
	r.setBoro(s_boro);
	r.setBuilding_no(s_building);
	r.setDate(s_date);
	r.setName(s_name);
	r.setPhone(s_phone);
	r.setScore(s_score);
	r.setStreet(s_street);
	r.setViolation_details(s_violation);
	r.setViolation_flag(s_flag);
	r.setZipcode(s_zipcode);
	
	array.add(r);

		}

	}catch(Exception e){

		e.printStackTrace();

	}finally{

	queryExe.close();

	}
	for(Restaurant n:array)
		System.out.println(n.getName()+" "+n.getBoro()+" "+n.getScore());
	
	return array;
	
}


public static ArrayList<String> QueryIngredients(String Recipequery) {
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/ingredients_combined.ttl").toString());

	Query query = QueryFactory.create(Recipequery);

	QueryExecution queryExe = QueryExecutionFactory.create(query,model);
	
	ArrayList<String> recipeId = new ArrayList();

	try{

	ResultSet resultset =  queryExe.execSelect();

	//System.out.println(resultset);
	

	while(resultset.hasNext()){

	QuerySolution soln = resultset.nextSolution();

	Literal name = soln.getLiteral("?id");
	Literal count = soln.getLiteral("?ncount");

	//System.out.println(name + " "+ count);
	recipeId.add(name.toString());

		}
	

	}catch(Exception e){

		e.printStackTrace();

	}finally{

	queryExe.close();

	}
	
	return recipeId;
}

public static ArrayList<String> QueryCuisine(String myquery) {
	//Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/abc3-csv.ttl").toString());

	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/cuisine.ttl").toString());
	//System.out.println("passed the model part..line 219");
	
	Query query = QueryFactory.create(myquery);

	QueryExecution queryExe = QueryExecutionFactory.create(query,model);

	ArrayList<String> arrayCuisine =new ArrayList();
	
	try{

	ResultSet resultset =  queryExe.execSelect();

	while(resultset.hasNext()){

	QuerySolution soln = resultset.nextSolution();

	Literal name = soln.getLiteral("?name");

	Literal count = soln.getLiteral("?ncount");

	System.out.println(name);
	arrayCuisine.add(name.toString());
		}

	}catch(Exception e){

		e.printStackTrace();

	}finally{

	queryExe.close();

	}

	return arrayCuisine;
}

public static ArrayList<Recipe> QueryRecipe1(String search) {
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/ingredients_combined.ttl").toString());

	String strQuery_recipe2= pre+ "SELECT ?ing0 ?ing1 ?ing2 ?ing3 ?ing4 ?ing5 ?ing6 ?ing7 ?ing8 ?ing9 ?ing10 ?ing11 ?ing12 ?ing13 ?ing14 ?ing15 "
			+ "?unit0 ?unit1 ?unit2 ?unit3 ?unit4 ?unit5 ?unit6 ?unit7 ?unit8 ?unit9 ?unit10 ?unit11 ?unit12 ?unit13 ?unit14 ?unit15"
			+ "?quant0 ?quant1 ?quant2 ?quant3 ?quant4 ?quant5 ?quant6 ?quant7 ?quant8 ?quant9 ?quant10 ?quant11 ?quant12 ?quant13 ?quant14 ?quant15 "
			+ "?name ?rating ?direction ?id WHERE { ?Res fw:has_name ?name. ?Res fw:has_rating ?rating. ?Res fw:has_id ?id. ?Res fw:has_direction ?direction."
			+ "OPTIONAL{ ?Res fw:has_ingredient0 ?ing0. ?Res fw:has_unit0 ?unit0. ?Res fw:has_quantity0 ?quant0.}OPTIONAL{ ?Res fw:has_ingredient1 ?ing1. ?Res fw:has_unit1 ?unit1. ?Res fw:has_quantity1 ?quant1.}OPTIONAL{ ?Res fw:has_ingredient2 ?ing2. ?Res fw:has_unit2 ?unit2. ?Res fw:has_quantity2 ?quant2. }OPTIONAL{?Res fw:has_ingredient3 ?ing3. ?Res fw:has_unit3 ?unit3. ?Res fw:has_quantity3 ?quant3. }OPTIONAL{?Res fw:has_ingredient4 ?ing4. ?Res fw:has_unit4 ?unit4. ?Res fw:has_quantity4 ?quant4. }"
			+ "OPTIONAL{?Res fw:has_ingredient5 ?ing5. ?Res fw:has_unit5 ?unit5. ?Res fw:has_quantity5 ?quant5.}OPTIONAL{?Res fw:has_ingredient6 ?ing6. ?Res fw:has_unit6 ?unit6. ?Res fw:has_quantity6 ?quant6."
			+ "}OPTIONAL{?Res fw:has_ingredient7 ?ing7. ?Res fw:has_unit7 ?unit7. ?Res fw:has_quantity7 ?quant7.}OPTIONAL{ ?Res fw:has_ingredient8 ?ing8. ?Res fw:has_unit8 ?unit8. ?Res fw:has_quantity8 ?quant8.}OPTIONAL{ ?Res fw:has_ingredient9 ?ing9. ?Res fw:has_unit9 ?unit9. ?Res fw:has_quantity9 ?quant9.}OPTIONAL{ ?Res fw:has_ingredient10 ?ing10. ?Res fw:has_unit10 ?unit10. ?Res fw:has_quantity10 ?quant10.}OPTIONAL{ ?Res fw:has_ingredient11 ?ing11. ?Res fw:has_unit11 ?unit11. ?Res fw:has_quantity11 ?quant11. }OPTIONAL{?Res fw:has_ingredient12 ?ing12. ?Res fw:has_unit12 ?unit12. ?Res fw:has_quantity12 ?quant12.}OPTIONAL{ ?Res fw:has_ingredient13 ?ing13. ?Res fw:has_unit13 ?unit13. ?Res fw:has_quantity13 ?quant13.}OPTIONAL{ ?Res fw:has_ingredient14 ?ing14. ?Res fw:has_unit14 ?unit14. ?Res fw:has_quantity14 ?quant14.}OPTIONAL{ ?Res fw:has_ingredient15 ?ing15. ?Res fw:has_unit15 ?unit15. ?Res fw:has_quantity15 ?quant15.}"
			+ "FILTER regex(?name, '"+ search +"', 'i') } order by desc (?rating) limit 50" ;

	Query query = QueryFactory.create(strQuery_recipe2);

	QueryExecution queryExe = QueryExecutionFactory.create(query,model);
	
	ArrayList<Recipe> array= new ArrayList();

	try{

	ResultSet resultset =  queryExe.execSelect();
	

	while(resultset.hasNext()){

	QuerySolution soln = resultset.nextSolution();
	
	Recipe recipe = new Recipe();

	ArrayList<Nutrition> ArrayIngredients =new ArrayList();

	Literal name = soln.getLiteral("?name");
	Literal rating = soln.getLiteral("?rating");
	Literal direction = soln.getLiteral("?direction");
	Literal id = soln.getLiteral("?id");
	
	for(int i=0;i<=15;i++) {
		
		Nutrition nutrition = new Nutrition();
		
		Literal ing = soln.getLiteral("?ing"+i);
		Literal unit = soln.getLiteral("?unit"+i);
		Literal quant = soln.getLiteral("?quant"+i);
		
		String s_ing, s_unit, s_quant;
		
		if(ing==null)	
			//s_ing =" ";
			continue;
		else
			s_ing =ing.toString();
		
		if(unit==null)	
			//s_unit =" ";
			continue;
		else
			s_unit =unit.toString();
		
		if(quant==null)	
			//s_quant =" ";
			continue;
		else
			s_quant =quant.toString();
		
		
		nutrition.setName(s_ing);
		nutrition.setUnit(s_unit);
		nutrition.setValue(s_quant);
		
		ArrayIngredients.add(nutrition);
		
			
	}
	
	recipe.setName(name.toString());
	recipe.setIngredients(ArrayIngredients);
	recipe.setRating(rating.toString());
	recipe.setDirection(direction.toString());
	recipe.setId(id.toString());
	
	array.add(recipe);
	
	//for(Nutrition n:ArrayIngredients)
	//System.out.println(n.getValue()+" "+n.getUnit()+" "+n.getName());
	
	//System.out.println(name);
	//System.out.println(direction);

		}

	}catch(Exception e){

		e.printStackTrace();

	}finally{

	queryExe.close();

	}

	return array;
}


public static ArrayList<Recipe> QueryRecipeIngredients1(ArrayList<String> recipeId) {
	
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/recipe_quantity_decimal.ttl").toString());
	
	System.out.println(recipeId);

	ArrayList<Recipe> Arrayrecipe = new ArrayList();
	
	for(String id: recipeId) {
		
	
		String Query_Recipe2= pre + "SELECT ?ing0 ?ing1 ?ing2 ?ing3 ?ing4 ?ing5 ?ing6 ?ing7 ?ing8 ?ing9 ?ing10 ?ing11 ?ing12 ?ing13 ?ing14 ?ing15 "
				+ "?unit0 ?unit1 ?unit2 ?unit3 ?unit4 ?unit5 ?unit6 ?unit7 ?unit8 ?unit9 ?unit10 ?unit11 ?unit12 ?unit13 ?unit14 ?unit15"
				+ "?quant0 ?quant1 ?quant2 ?quant3 ?quant4 ?quant5 ?quant6 ?quant7 ?quant8 ?quant9 ?quant10 ?quant11 ?quant12 ?quant13 ?quant14 ?quant15 "
				+ "?name ?rating ?direction WHERE { ?Res fw:has_name ?name. ?Res fw:has_rating ?rating. ?Res fw:has_id ?id. ?Res fw:has_direction ?direction."
				+ "OPTIONAL{ ?Res fw:has_ingredient0 ?ing0. ?Res fw:has_unit0 ?unit0. ?Res fw:has_quantity0 ?quant0.}OPTIONAL{ ?Res fw:has_ingredient1 ?ing1. ?Res fw:has_unit1 ?unit1. ?Res fw:has_quantity1 ?quant1.}OPTIONAL{ ?Res fw:has_ingredient2 ?ing2. ?Res fw:has_unit2 ?unit2. ?Res fw:has_quantity2 ?quant2. }OPTIONAL{?Res fw:has_ingredient3 ?ing3. ?Res fw:has_unit3 ?unit3. ?Res fw:has_quantity3 ?quant3. }OPTIONAL{?Res fw:has_ingredient4 ?ing4. ?Res fw:has_unit4 ?unit4. ?Res fw:has_quantity4 ?quant4. }"
				+ "OPTIONAL{?Res fw:has_ingredient5 ?ing5. ?Res fw:has_unit5 ?unit5. ?Res fw:has_quantity5 ?quant5.}OPTIONAL{?Res fw:has_ingredient6 ?ing6. ?Res fw:has_unit6 ?unit6. ?Res fw:has_quantity6 ?quant6."
				+ "}OPTIONAL{?Res fw:has_ingredient7 ?ing7. ?Res fw:has_unit7 ?unit7. ?Res fw:has_quantity7 ?quant7.}OPTIONAL{ ?Res fw:has_ingredient8 ?ing8. ?Res fw:has_unit8 ?unit8. ?Res fw:has_quantity8 ?quant8.}OPTIONAL{ ?Res fw:has_ingredient9 ?ing9. ?Res fw:has_unit9 ?unit9. ?Res fw:has_quantity9 ?quant9.}OPTIONAL{ ?Res fw:has_ingredient10 ?ing10. ?Res fw:has_unit10 ?unit10. ?Res fw:has_quantity10 ?quant10.}OPTIONAL{ ?Res fw:has_ingredient11 ?ing11. ?Res fw:has_unit11 ?unit11. ?Res fw:has_quantity11 ?quant11. }OPTIONAL{?Res fw:has_ingredient12 ?ing12. ?Res fw:has_unit12 ?unit12. ?Res fw:has_quantity12 ?quant12.}OPTIONAL{ ?Res fw:has_ingredient13 ?ing13. ?Res fw:has_unit13 ?unit13. ?Res fw:has_quantity13 ?quant13.}OPTIONAL{ ?Res fw:has_ingredient14 ?ing14. ?Res fw:has_unit14 ?unit14. ?Res fw:has_quantity14 ?quant14.}OPTIONAL{ ?Res fw:has_ingredient15 ?ing15. ?Res fw:has_unit15 ?unit15. ?Res fw:has_quantity15 ?quant15.}"
				+ "FILTER (regex(?id, '^"+id+"$'))} LIMIT 1";
	
		Query query = QueryFactory.create(Query_Recipe2);

		QueryExecution queryExe = QueryExecutionFactory.create(query,model);
		
		try{

			ResultSet resultset =  queryExe.execSelect();

			while(resultset.hasNext()){

			QuerySolution soln = resultset.nextSolution();
			
			Recipe recipe = new Recipe();
			
			ArrayList<Nutrition> ArrayIngredients =new ArrayList();

			Literal name = soln.getLiteral("?name");
			Literal rating = soln.getLiteral("?rating");
			Literal direction = soln.getLiteral("?direction");
			
			for(int i=0;i<=15;i++) {
				
				Nutrition nutrition = new Nutrition();
				
				Literal ing = soln.getLiteral("?ing"+i);
				Literal unit = soln.getLiteral("?unit"+i);
				Literal quant = soln.getLiteral("?quant"+i);
				
				String s_ing, s_unit, s_quant;
				
				if(ing==null)	
//					s_ing =" ";
					continue;
				else
					s_ing =ing.toString();
				
				if(unit==null)	
//					s_unit =" ";
					continue;
				
				else
					s_unit =unit.toString();
				
				if(quant==null)	
//					s_quant =" ";
					continue;
				else
					s_quant =quant.toString();
				
				
				nutrition.setName(s_ing);
				nutrition.setUnit(s_unit);
				nutrition.setValue(s_quant);
				
				ArrayIngredients.add(nutrition);
				
					
			}
			
			recipe.setName(name.toString());
			recipe.setIngredients(ArrayIngredients);
			recipe.setRating(rating.toString());
			recipe.setDirection(direction.toString());
			recipe.setId(id);
			
			Arrayrecipe.add(recipe);
			
			//for(Nutrition n:ArrayIngredients)
			//System.out.println(n.getValue()+" "+n.getUnit()+" "+n.getName());
			
			System.out.println(name);
			}
			
			}catch(Exception e){

				e.printStackTrace();

			}finally{

			queryExe.close();

			}
	}
	
	/*for(Recipe i:Arrayrecipe) {
		System.out.println(i.getName());
	}*/
	
	return Arrayrecipe;

	
}

public static ArrayList<Float> TotalNutrition(Recipe recipe){
	
	ArrayList<Nutrition> ingredients = recipe.getIngredients();
	
	ArrayList<Float> totalstats = new ArrayList();
	totalstats.add((float) 0.0);totalstats.add((float) 0.0);totalstats.add((float) 0.0);totalstats.add((float) 0.0);totalstats.add((float) 0.0);totalstats.add((float) 0.0);totalstats.add((float) 0.0);totalstats.add((float) 0.0);
	
	ArrayList<Float> stats = new ArrayList();
	
	for(Nutrition n: ingredients) {
		
		try {
			String id = Send_HTTP_Request2.call_me(n.getName());
	         if(!id.equals("0")) {
	        	 	 stats = Send_HTTP_Request2.getIngredientDetails(id,n.getUnit(),n.getValue());
	        	 	 totalstats = merge(totalstats,stats);
	         }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 	System.out.println(totalstats);
 
	
	return totalstats;
	
}

public static ArrayList<Float> merge(ArrayList<Float> totalstats, ArrayList<Float> stats) {
	
	for(int i=0;i<8;i++) {
		float value = totalstats.get(i) + stats.get(i);
		totalstats.set(i, value);
	}
	
	return totalstats;
	
}



public static Recipe QueryRecipeName(String r_name) {
	
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/recipe_quantity_decimal.ttl").toString());
	
		String Query_Recipe2= pre + "SELECT ?ing0 ?ing1 ?ing2 ?ing3 ?ing4 ?ing5 ?ing6 ?ing7 ?ing8 ?ing9 ?ing10 ?ing11 ?ing12 ?ing13 ?ing14 ?ing15 "
				+ "?unit0 ?unit1 ?unit2 ?unit3 ?unit4 ?unit5 ?unit6 ?unit7 ?unit8 ?unit9 ?unit10 ?unit11 ?unit12 ?unit13 ?unit14 ?unit15"
				+ "?quant0 ?quant1 ?quant2 ?quant3 ?quant4 ?quant5 ?quant6 ?quant7 ?quant8 ?quant9 ?quant10 ?quant11 ?quant12 ?quant13 ?quant14 ?quant15 "
				+ "?name ?rating ?direction WHERE { ?Res fw:has_name ?name. ?Res fw:has_rating ?rating. ?Res fw:has_id ?id. ?Res fw:has_direction ?direction."
				+ "OPTIONAL{ ?Res fw:has_ingredient0 ?ing0. ?Res fw:has_unit0 ?unit0. ?Res fw:has_quantity0 ?quant0.}OPTIONAL{ ?Res fw:has_ingredient1 ?ing1. ?Res fw:has_unit1 ?unit1. ?Res fw:has_quantity1 ?quant1.}OPTIONAL{ ?Res fw:has_ingredient2 ?ing2. ?Res fw:has_unit2 ?unit2. ?Res fw:has_quantity2 ?quant2. }OPTIONAL{?Res fw:has_ingredient3 ?ing3. ?Res fw:has_unit3 ?unit3. ?Res fw:has_quantity3 ?quant3. }OPTIONAL{?Res fw:has_ingredient4 ?ing4. ?Res fw:has_unit4 ?unit4. ?Res fw:has_quantity4 ?quant4. }"
				+ "OPTIONAL{?Res fw:has_ingredient5 ?ing5. ?Res fw:has_unit5 ?unit5. ?Res fw:has_quantity5 ?quant5.}OPTIONAL{?Res fw:has_ingredient6 ?ing6. ?Res fw:has_unit6 ?unit6. ?Res fw:has_quantity6 ?quant6."
				+ "}OPTIONAL{?Res fw:has_ingredient7 ?ing7. ?Res fw:has_unit7 ?unit7. ?Res fw:has_quantity7 ?quant7.}OPTIONAL{ ?Res fw:has_ingredient8 ?ing8. ?Res fw:has_unit8 ?unit8. ?Res fw:has_quantity8 ?quant8.}OPTIONAL{ ?Res fw:has_ingredient9 ?ing9. ?Res fw:has_unit9 ?unit9. ?Res fw:has_quantity9 ?quant9.}OPTIONAL{ ?Res fw:has_ingredient10 ?ing10. ?Res fw:has_unit10 ?unit10. ?Res fw:has_quantity10 ?quant10.}OPTIONAL{ ?Res fw:has_ingredient11 ?ing11. ?Res fw:has_unit11 ?unit11. ?Res fw:has_quantity11 ?quant11. }OPTIONAL{?Res fw:has_ingredient12 ?ing12. ?Res fw:has_unit12 ?unit12. ?Res fw:has_quantity12 ?quant12.}OPTIONAL{ ?Res fw:has_ingredient13 ?ing13. ?Res fw:has_unit13 ?unit13. ?Res fw:has_quantity13 ?quant13.}OPTIONAL{ ?Res fw:has_ingredient14 ?ing14. ?Res fw:has_unit14 ?unit14. ?Res fw:has_quantity14 ?quant14.}OPTIONAL{ ?Res fw:has_ingredient15 ?ing15. ?Res fw:has_unit15 ?unit15. ?Res fw:has_quantity15 ?quant15.}"
				+ "FILTER (regex(?name, '^"+r_name+"$'))} LIMIT 1";
	
		Query query = QueryFactory.create(Query_Recipe2);

		QueryExecution queryExe = QueryExecutionFactory.create(query,model);
		
		Recipe recipe = new Recipe();
		
		try{

			ResultSet resultset =  queryExe.execSelect();

			while(resultset.hasNext()){

			QuerySolution soln = resultset.nextSolution();

			ArrayList<Nutrition> ArrayIngredients =new ArrayList();

			Literal name = soln.getLiteral("?name");
			Literal rating = soln.getLiteral("?rating");
			Literal direction = soln.getLiteral("?direction");
			
			for(int i=0;i<=15;i++) {
				
				Nutrition nutrition = new Nutrition();
				
				Literal ing = soln.getLiteral("?ing"+i);
				Literal unit = soln.getLiteral("?unit"+i);
				Literal quant = soln.getLiteral("?quant"+i);
				
				String s_ing, s_unit, s_quant;
				
				if(ing==null)	
					s_ing =" ";
				else
					s_ing =ing.toString();
				
				if(unit==null)	
					s_unit =" ";
				else
					s_unit =unit.toString();
				
				if(quant==null)	
					s_quant =" ";
				else
					s_quant =quant.toString();
				
				
				nutrition.setName(s_ing);
				nutrition.setUnit(s_unit);
				nutrition.setValue(s_quant);
				
				ArrayIngredients.add(nutrition);
				
					
			}
			
			recipe.setName(name.toString());
			recipe.setIngredients(ArrayIngredients);
			recipe.setRating(rating.toString());
			recipe.setDirection(direction.toString());

			System.out.println(name);
			}
			
			}catch(Exception e){

				e.printStackTrace();

			}finally{

			queryExe.close();

			}
	
	
	return recipe;

	
}


/*
public static ArrayList<Recipe> QueryRecipeIngredients(ArrayList<String> recipeId) {
	
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/recipe_saperated_ingredient.ttl").toString());
	
	System.out.println(recipeId);

	ArrayList<Recipe> Arrayrecipe = new ArrayList();
	
	for(String id: recipeId) {
		
	
		String Query_Recipe2= pre + "SELECT ?ing0 ?ing1 ?ing2 ?ing3 ?ing4 ?ing5 ?ing6 ?ing7 ?ing8 ?ing9 ?ing10 ?ing11 ?ing12 ?ing13 ?ing14 ?ing15 ?name ?rating WHERE { ?Res fw:has_name ?name. ?Res fw:has_rating ?rating. ?Res fw:has_id ?id. OPTIONAL{ ?Res fw:has_ingredient0 ?ing0.}OPTIONAL{ ?Res fw:has_ingredient1 ?ing1.}OPTIONAL{ ?Res fw:has_ingredient2 ?ing2. }OPTIONAL{?Res fw:has_ingredient3 ?ing3. }OPTIONAL{?Res fw:has_ingredient4 ?ing4. }OPTIONAL{?Res fw:has_ingredient5 ?ing5. }OPTIONAL{?Res fw:has_ingredient6 ?ing6."
				+ "}OPTIONAL{?Res fw:has_ingredient7 ?ing7.}OPTIONAL{ ?Res fw:has_ingredient8 ?ing8.}OPTIONAL{ ?Res fw:has_ingredient9 ?ing9.}OPTIONAL{ ?Res fw:has_ingredient10 ?ing10.}OPTIONAL{ ?Res fw:has_ingredient11 ?ing11. }OPTIONAL{?Res fw:has_ingredient12 ?ing12.}OPTIONAL{ ?Res fw:has_ingredient13 ?ing13.}OPTIONAL{ ?Res fw:has_ingredient14 ?ing14.}OPTIONAL{ ?Res fw:has_ingredient15 ?ing15. }"
				//+ "FILTER(contains(str(?id),'"+id+"' ))} LIMIT 1";
				+ "FILTER (regex(?id, '^"+id+"$'))}";
	
		Query query = QueryFactory.create(Query_Recipe2);

		QueryExecution queryExe = QueryExecutionFactory.create(query,model);
		
		try{

			ResultSet resultset =  queryExe.execSelect();

			while(resultset.hasNext()){

			QuerySolution soln = resultset.nextSolution();
			
			Recipe recipe = new Recipe();
			ArrayList<String> ArrayIngredients =new ArrayList();

			Literal name = soln.getLiteral("?name");
			Literal rating = soln.getLiteral("?rating");
			
			for(int i=0;i<=15;i++) {
				
				if(soln.getLiteral("?ing"+i)==null)
					break;
				else
					ArrayIngredients.add(soln.getLiteral("?ing"+i).toString());
					
			}
			
			recipe.setName(name.toString());
			recipe.setIngredients(ArrayIngredients);
			recipe.setRating(rating.toString());
			
			Arrayrecipe.add(recipe);

			System.out.println(ArrayIngredients);
			System.out.println(name);

			}
			
			}catch(Exception e){

				e.printStackTrace();

			}finally{

			queryExe.close();

			}
	}
	
	return Arrayrecipe;

	
}


 
 public static ArrayList<String> QueryRecipe(String search) {
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/recipe_from_ingredients.ttl").toString());

	String strQuery_recipe2= pre+ "SELECT ?recipe WHERE { ?Res fw:has_name ?recipe. ?Res fw:has_rating ?rating. FILTER regex(?recipe, '"+ search +"', 'i') } order by desc (?rating) limit 20" ;

	Query query = QueryFactory.create(strQuery_recipe2);

	QueryExecution queryExe = QueryExecutionFactory.create(query,model);
	
	ArrayList<String> array= new ArrayList();

	try{

	ResultSet resultset =  queryExe.execSelect();

	System.out.println(resultset);

	while(resultset.hasNext()){

	QuerySolution soln = resultset.nextSolution();

	Literal name = soln.getLiteral("?recipe");

	System.out.println(name);
	array.add(name.toString());

		}

	}catch(Exception e){

		e.printStackTrace();

	}finally{

	queryExe.close();

	}

	return array;
}

 
 */

public static Recipe QueryRecipeFromId(String id) {
	
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/ingredients_saperate.ttl").toString());
		
	
		String Query_Recipe2= pre + "SELECT ?ing0 ?ing1 ?ing2 ?ing3 ?ing4 ?ing5 ?ing6 ?ing7 ?ing8 ?ing9 ?ing10 ?ing11 ?ing12 ?ing13 ?ing14 ?ing15 "
				+ "?unit0 ?unit1 ?unit2 ?unit3 ?unit4 ?unit5 ?unit6 ?unit7 ?unit8 ?unit9 ?unit10 ?unit11 ?unit12 ?unit13 ?unit14 ?unit15"
				+ "?quant0 ?quant1 ?quant2 ?quant3 ?quant4 ?quant5 ?quant6 ?quant7 ?quant8 ?quant9 ?quant10 ?quant11 ?quant12 ?quant13 ?quant14 ?quant15 "
				+ "?name ?rating ?direction WHERE { ?Res fw:has_name ?name. ?Res fw:has_rating ?rating. ?Res fw:has_id ?id. ?Res fw:has_direction ?direction."
				+ "OPTIONAL{ ?Res fw:has_ingredient0 ?ing0. ?Res fw:has_unit0 ?unit0. ?Res fw:has_quantity0 ?quant0.}OPTIONAL{ ?Res fw:has_ingredient1 ?ing1. ?Res fw:has_unit1 ?unit1. ?Res fw:has_quantity1 ?quant1.}OPTIONAL{ ?Res fw:has_ingredient2 ?ing2. ?Res fw:has_unit2 ?unit2. ?Res fw:has_quantity2 ?quant2. }OPTIONAL{?Res fw:has_ingredient3 ?ing3. ?Res fw:has_unit3 ?unit3. ?Res fw:has_quantity3 ?quant3. }OPTIONAL{?Res fw:has_ingredient4 ?ing4. ?Res fw:has_unit4 ?unit4. ?Res fw:has_quantity4 ?quant4. }"
				+ "OPTIONAL{?Res fw:has_ingredient5 ?ing5. ?Res fw:has_unit5 ?unit5. ?Res fw:has_quantity5 ?quant5.}OPTIONAL{?Res fw:has_ingredient6 ?ing6. ?Res fw:has_unit6 ?unit6. ?Res fw:has_quantity6 ?quant6."
				+ "}OPTIONAL{?Res fw:has_ingredient7 ?ing7. ?Res fw:has_unit7 ?unit7. ?Res fw:has_quantity7 ?quant7.}OPTIONAL{ ?Res fw:has_ingredient8 ?ing8. ?Res fw:has_unit8 ?unit8. ?Res fw:has_quantity8 ?quant8.}OPTIONAL{ ?Res fw:has_ingredient9 ?ing9. ?Res fw:has_unit9 ?unit9. ?Res fw:has_quantity9 ?quant9.}OPTIONAL{ ?Res fw:has_ingredient10 ?ing10. ?Res fw:has_unit10 ?unit10. ?Res fw:has_quantity10 ?quant10.}OPTIONAL{ ?Res fw:has_ingredient11 ?ing11. ?Res fw:has_unit11 ?unit11. ?Res fw:has_quantity11 ?quant11. }OPTIONAL{?Res fw:has_ingredient12 ?ing12. ?Res fw:has_unit12 ?unit12. ?Res fw:has_quantity12 ?quant12.}OPTIONAL{ ?Res fw:has_ingredient13 ?ing13. ?Res fw:has_unit13 ?unit13. ?Res fw:has_quantity13 ?quant13.}OPTIONAL{ ?Res fw:has_ingredient14 ?ing14. ?Res fw:has_unit14 ?unit14. ?Res fw:has_quantity14 ?quant14.}OPTIONAL{ ?Res fw:has_ingredient15 ?ing15. ?Res fw:has_unit15 ?unit15. ?Res fw:has_quantity15 ?quant15.}"
				+ "FILTER (regex(?id, '^"+id+"$'))} LIMIT 1";
	
		Query query = QueryFactory.create(Query_Recipe2);

		QueryExecution queryExe = QueryExecutionFactory.create(query,model);
		Recipe recipe = new Recipe();
		
		try{

			ResultSet resultset =  queryExe.execSelect();

			while(resultset.hasNext()){

			QuerySolution soln = resultset.nextSolution();
			
			ArrayList<Nutrition> ArrayIngredients =new ArrayList();

			Literal name = soln.getLiteral("?name");
			Literal rating = soln.getLiteral("?rating");
			Literal direction = soln.getLiteral("?direction");
			
			for(int i=0;i<=15;i++) {
				
				Nutrition nutrition = new Nutrition();
				
				Literal ing = soln.getLiteral("?ing"+i);
				Literal unit = soln.getLiteral("?unit"+i);
				Literal quant = soln.getLiteral("?quant"+i);
				
				String s_ing, s_unit, s_quant;
				
				if(ing==null)	
					continue;//s_ing =" ";
				else
					s_ing =ing.toString();
				
				if(unit==null)	
					continue;//s_unit =" ";
				else
					s_unit =unit.toString();
				
				if(quant==null)	
					continue;//s_quant =" ";
				else
					s_quant =quant.toString();
				
				
				//Float f = Float.parseFloat(s_quant);
				nutrition.setName(s_ing);
				nutrition.setUnit(s_unit);
				nutrition.setValue(s_quant);
				
				ArrayIngredients.add(nutrition);
				
					
			}
			
			recipe.setName(name.toString());
			recipe.setIngredients(ArrayIngredients);
			recipe.setRating(rating.toString());
			recipe.setDirection(direction.toString());
			recipe.setId(id);
			
			System.out.println("\n"+name);
			for(Nutrition n:ArrayIngredients)
			System.out.println(n.getValue()+" "+n.getUnit()+" "+n.getName());
			

			}
			
			}catch(Exception e){

				e.printStackTrace();

			}finally{

			queryExe.close();

			}
	
	return recipe;

	
}

public static ArrayList<Recipe> RemoveRepetition(ArrayList<Recipe> array) {
	
	String a="";
	
	ArrayList<Recipe> newArray = new ArrayList();
	
	System.out.println("---------------");
	
	for(Recipe r: array) {
		if(!a.equals(r.getName())) {
			a = r.getName();
			newArray.add(r);
		}
			
	}
	
	return newArray;
	
}


public static ArrayList<Recipe> QueryRecipeIngredientsabc(ArrayList<String> recipeId) {
	
	
	Model model = FileManager.get().loadModel(SPARQL_MAIN.class.getResource("/Ontologies/ingredients_combined.ttl").toString());

	ArrayList<Recipe> Arrayrecipe = new ArrayList();
	
	for(String id: recipeId) {
		
	
		String Query_Recipe2= pre + "SELECT ?ing"
				+ "?name ?rating ?direction WHERE { ?Res fw:has_name ?name. ?Res fw:has_rating ?rating. ?Res fw:has_id ?id."
				+ "?Res fw:has_direction ?direction."
				+ "FILTER (regex(?id, '^"+id+"$'))} LIMIT 1";
	
		Query query = QueryFactory.create(Query_Recipe2);

		QueryExecution queryExe = QueryExecutionFactory.create(query,model);
		
		try{

			ResultSet resultset =  queryExe.execSelect();

			while(resultset.hasNext()){

			QuerySolution soln = resultset.nextSolution();
			
			Recipe recipe = new Recipe();
			
			ArrayList<Nutrition> ArrayIngredients =new ArrayList();

			Literal name = soln.getLiteral("?name");
			Literal rating = soln.getLiteral("?rating");
			Literal direction = soln.getLiteral("?direction");		
			
			recipe.setName(name.toString());
			recipe.setRating(rating.toString());
			recipe.setDirection(direction.toString());
			recipe.setId(id);
			
			Arrayrecipe.add(recipe);
			System.out.println(name);
			if(name.toString().length()>20)
			System.out.println(name.toString().substring(0,20));
			}
			
			}catch(Exception e){

				e.printStackTrace();

			}finally{

			queryExe.close();

			}
	}
	
	return Arrayrecipe;

	
}

}

