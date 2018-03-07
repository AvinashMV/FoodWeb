package FoodWeb_Pckg;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


public class Send_HTTP_Request2 {
	public static void main(String[] args) {
     try {
         String id = Send_HTTP_Request2.call_me("carrot");
         if(!id.equals("0"))
        	 	getIngredientDetails(id,"small"," ");
        } catch (Exception e) {
         e.printStackTrace();
       }
     }
	
	   
public static String call_me(String ing) throws Exception {
	
	if(ing.equals(" ")||ing.contains("water"))
		return "0";
	
	System.out.println(ing);
	
	String ingredient =ing +" raw";
	
	String key ="7QAyHJUDU5h00Nlr2An1ZQnq1QDXiZ07v35u9bSk";
	
     String url = "https://api.nal.usda.gov/ndb/search/?format=json&q="+ingredient.replace(" ", "%20")+"&sort=r&ds=Standard%20Reference&max=1&offset=0&api_key=" +key;
     
    String response = HttpRequest(url);
    String id="0";	//if item is not found
    try {
    
    	 System.out.println(response);
     JSONObject Response = new JSONObject(response);
     JSONObject list = Response.getJSONObject("list");
     JSONArray item = list.getJSONArray("item");
     JSONObject details = item.getJSONObject(0);
      id = details.getString("ndbno");
    }catch(Exception e) {
    		
    }
     
     System.out.println("result after Reading JSON Response: "+id);
     
     return id;
     
    
   }

public static ArrayList<Float> getIngredientDetails(String id,String unit,String quantity) throws Exception {
	
	String key ="7QAyHJUDU5h00Nlr2An1ZQnq1QDXiZ07v35u9bSk";
	
     String url = "https://api.nal.usda.gov/ndb/reports/?format=json&ndbno="+id+"&api_key=" +key;
     
     int unit_index=0;
     
     ArrayList<Nutrition> array = new ArrayList();
     ArrayList<Float> array1 = new ArrayList(); 
     
    String response = HttpRequest(url);
     
    JSONObject Response = new JSONObject(response);
    JSONObject report = Response.getJSONObject("report");
    JSONObject food = report.getJSONObject("food");
    JSONArray nutrients = food.getJSONArray("nutrients");
    JSONObject nutrition_details = nutrients.getJSONObject(0);
    
    JSONArray measures = nutrition_details.getJSONArray("measures");
    for(int i=0;i<measures.length();i++) {
   	 
   	 	JSONObject measures_deatils = measures.getJSONObject(i);
   	 	String label = measures_deatils.getString("label");
   	 	if(label.contains(unit.toLowerCase())) {
   	 		unit_index=i;
   	 		break;
   	 	}
    }
    
    JSONObject measures_deatils = measures.getJSONObject(unit_index);
    
    System.out.println("index"+unit_index);
    
    for(int i=0;i<8;i++) {
    	
    		Nutrition nutrition = new Nutrition();
    		
    		JSONObject details = nutrients.getJSONObject(i);
    		String n_name = details.getString("name");
    		String n_unit = details.getString("unit");
    		
    		JSONArray n_measures = details.getJSONArray("measures");
    		JSONObject n_details = n_measures.getJSONObject(unit_index);
    		float qty = (float) n_details.getDouble("qty");
    	    float value = Float.parseFloat(n_details.getString("value"));
    	    
    	    Float quantity_check = (float) 1;	//if quantity is not given
    	    
    	    try {
    	    		
    	    		quantity_check = Float.parseFloat(quantity);
    	    	
    	    }catch(Exception e) {
    	    	
    	    }
    	    
    	    value = (value/qty)*quantity_check;
    	    
    	    nutrition.setName(n_name);
    	    nutrition.setUnit(n_unit);
    	    nutrition.setValue(String.valueOf(value));
    	    
    	    array.add(nutrition);
    	    array1.add(value);
    		
    }
    
  
//     for(Nutrition n:array)
//    	 	System.out.println("  "+n.getName()+"  "+n.getUnit()+"  "+n.getValue());
//     System.out.println(array1);
     return array1;
    
   }

public static String HttpRequest(String url)throws Exception {

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    // optional default is GET
    con.setRequestMethod("GET");
    //add request header
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode();
    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
    	response.append(inputLine);
    }
    
    in.close();
    
    return response.toString();
}
}