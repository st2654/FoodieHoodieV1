package edu.nyu;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

public class RecipeUtils {

	
	
	public String getMethod(Recipe recipe){
    	StringBuilder sb=new StringBuilder();
    	for(String step:recipe.getRecipeSteps()){
    		sb.append(step);
    		sb.append(",");
    	}
    	if(sb.length()>0)
    		sb.deleteCharAt(sb.length()-1);
    	return sb.toString();
    }
    public String getIngredientList(Recipe recipe){
    	StringBuilder sb=new StringBuilder();
    	for(Ingredient ingredient:recipe.getRecipeIngredients()){
    		sb.append(ingredient.getUnit()+" "+ingredient.getName());
    		sb.append(",");
    	}
    	if(sb.length()>0)
    		sb.deleteCharAt(sb.length()-1);
    	return sb.toString();
    }
    public String getIngredientListWOUnit(Recipe recipe){
    	StringBuilder sb=new StringBuilder();
    	for(Ingredient ingredient:recipe.getRecipeIngredients()){
    		sb.append(ingredient.getName());
    		sb.append(",");
    	}
    	if(sb.length()>0)
    		sb.deleteCharAt(sb.length()-1);
    	return sb.toString(); 	
    }
    public ArrayList<Ingredient> getIngredientsFromString(String str){
    	ArrayList<Ingredient> ingredientList=new ArrayList<Ingredient>();
    	String[] array=str.split(",");
    	for(int i=0;i<array.length;i++){
    		Ingredient ingredient=new Ingredient();
    		ingredient.setName(array[i]);
    		ingredientList.add(ingredient);
    	}
    	return ingredientList;
    	
    }
    
    public ArrayList<String> getStepsFromString(String str){
    	ArrayList<String> steps=new ArrayList<String>();
    	BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
    	iterator.setText(str);
    	int start = iterator.first();
    	for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) {
    	  steps.add(str.substring(start,end));
    	}
    	
    	return steps;
    }
}
