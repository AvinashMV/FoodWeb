package FoodWeb_Pckg;

import java.util.ArrayList;

public class Recipe {
	
	public String name;
	public ArrayList<Nutrition> ingredients;
	public String rating;
	public String direction;
	public String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Nutrition> getIngredients() {
		return ingredients;
	}
	public void setIngredients(ArrayList<Nutrition> ingredients) {
		this.ingredients = ingredients;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
