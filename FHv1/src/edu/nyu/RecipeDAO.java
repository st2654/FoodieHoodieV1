package edu.nyu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RecipeDAO {
	
	final int RESULT_SIZE=20; 
	
	// Method to insert the parsed recipe in the database
	
	void insertRecipe(Recipe recipe){
		try{

			Connection dbConnection=DatabaseConnection.getConnection();
			String insertQuery="insert into foodiehoodie.`recipe-table` (name,cooktime,ingredientlist,method,url,preptime,AUTHOR,yield) values(?,?,?,?,?,?,?,?)";
			PreparedStatement ps=dbConnection.prepareStatement(insertQuery);
			RecipeUtils ru=new RecipeUtils();
			ps.setString(1, recipe.getRecipeName());
			ps.setString(2, recipe.getRecipeCookTime());
			ps.setString(3, ru.getIngredientList(recipe));
			ps.setString(4, ru.getMethod(recipe));
			ps.setString(5, recipe.getURL());
			ps.setString(6, recipe.getRecipePrepTime());
			ps.setString(7, recipe.getAuthor());
			ps.setString(8, recipe.getRecipeYield());
			System.out.println(ps.toString());
			ps.executeUpdate();
			
		}
		catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	// Method to insert the recipe ingredient to the processing queue
	
	void insertRecipeInQueue(Recipe recipe){
		try{

			Connection dbConnection=DatabaseConnection.getConnection();
			String insertQuery="insert into foodiehoodie.`doc-ingredient-process-queue` (docid,ingredients) values(?,?)";
			PreparedStatement ps=dbConnection.prepareStatement(insertQuery);
			RecipeUtils ru=new RecipeUtils();
			ps.setInt(1, recipe.getId());
			ps.setString(2, ru.getIngredientListWOUnit(recipe));
			ps.executeUpdate();
			
		}
		catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	// Method to get the auto increment number for recipe table
	
	int getAutoIncrementNumber(String db,String table){
		int seqNumber=0;
		try{
			Connection dbConnection=DatabaseConnection.getConnection();
			String query="SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA =? AND TABLE_NAME=?";
			PreparedStatement ps=dbConnection.prepareStatement(query);
			ps.setString(1, db);
			ps.setString(2, table);
			System.out.println(ps.toString());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				seqNumber=rs.getInt("AUTO_INCREMENT");
				//System.out.println("**"+seqNumber);
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		return seqNumber;
	}
	
	// Method to get the list of the previously crawled URLs
	
	ArrayList<String> getCrawledURLs(){
		ArrayList<String> crawledURLs=new ArrayList<String>();
		try{
			Connection dbConnection=DatabaseConnection.getConnection();
			String query="SELECT url FROM  foodiehoodie.`recipe-table`";
			PreparedStatement ps=dbConnection.prepareStatement(query);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				crawledURLs.add(rs.getString("url"));
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		
		return crawledURLs;
		
	}
	
	// Method to get the ingredient id list form the ingredient name
	
	ArrayList<String> getIngredientsID(ArrayList<String> ingredients){
		ArrayList<String> ingredientsIDs=new ArrayList<String>();
		try{

			Connection dbConnection=DatabaseConnection.getConnection();
			String query="select id from `ingredients-main` where `ingredients-main`.name in "+generateINClause(ingredients.size());
			PreparedStatement ps=dbConnection.prepareStatement(query);
			for(int i=1;i<=ingredients.size();i++){
				ps.setString(i, ingredients.get(i-1));
			}
			System.out.println(ps.toString());
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				ingredientsIDs.add(""+rs.getInt(1));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return ingredientsIDs;
	}
	
	// Method to get the string of ? for prepared statement
	
	String generateINClause(int size){
		StringBuilder sb=new StringBuilder();
		sb.append("(");
    	for(int i=0;i<size;i++){
    		sb.append("?,");
    	}
		if(sb.length()>0)
    		sb.deleteCharAt(sb.length()-1);
    	sb.append(")");
    	return sb.toString();
		
	}
	
	// Method to get the docids for the given ingredient id
	
	ArrayList<String> getDocIDsFromIngredients(int ingredientID){
		ArrayList<String> docIDS=new ArrayList<String>();
		try{
			Connection dbConnection=DatabaseConnection.getConnection();
			String query="select docid from `doc-ingredient-relationship` where ingredientId=?";
			PreparedStatement ps=dbConnection.prepareStatement(query);
			ps.setInt(1, ingredientID);
			System.out.println(ps.toString());
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				docIDS.add(rs.getString(1));
				System.out.println(""+rs.getString(1));
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return docIDS;
	}
	
	
	// Get the list of the recipes form the given docIDs
	
	ArrayList<Recipe> getRecipesForDocIDs(ArrayList<String> docIDs){
		ArrayList<Recipe> recipeList=new  ArrayList<Recipe>();
		try{
			Connection dbConnection=DatabaseConnection.getConnection();
			String query="select * from `recipe-table` where id in "+generateINClause(docIDs.size())+" limit 100";
			PreparedStatement ps=dbConnection.prepareStatement(query);
			for(int i=1;i<=docIDs.size();i++){
				ps.setString(i, docIDs.get(i-1));
			}
			System.out.println(ps.toString());
			ResultSet rs=ps.executeQuery();
			RecipeUtils ru=new RecipeUtils();
			while(rs.next()){
				Recipe r=new Recipe();
				r.setId(rs.getInt(1));
				r.setRecipeName(rs.getString(2));
				r.setRecipeCookTime(rs.getString(3));
				r.setRecipePrepTime(rs.getString(4));
				r.setRecipeIngredients(ru.getIngredientsFromString(rs.getString(6)));
				r.setRecipeSteps(ru.getStepsFromString(rs.getString(7)));
				r.setAuthor(rs.getString(8));
				r.setURL(rs.getString(9)); 
				r.setRecipeYield(rs.getString(11));
				recipeList.add(r);
			}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return recipeList;
		
	}
	
	//  Method to parse the REST request and generate the recipe list
	
	public ArrayList<Recipe> getRecipeFromQuery(String mobileQuery){
		ArrayList<Recipe> recipeList=new ArrayList<Recipe>();
		
		String[] ingredients=mobileQuery.split(",");
		HashMap<String,ArrayList<Recipe>> ingredientRecipeMap=new HashMap<String,ArrayList<Recipe>>();
		ArrayList<String> ingredientNameList=new ArrayList<String>();
		for(int i=0;i<ingredients.length;i++){
			ingredientNameList.add(ingredients[i]);
		}
		ArrayList<String> ingredientsIDList=this.getIngredientsID(ingredientNameList);
		HashSet<String> finalDocIDs=new HashSet<String>();
		
		for(String ingredientID:ingredientsIDList){
			int id=Integer.parseInt(ingredientID);
			ArrayList<String> docIDs=this.getDocIDsFromIngredients(id);
			if(finalDocIDs.size()==0){
				finalDocIDs.addAll(docIDs);
			}
			finalDocIDs.retainAll(docIDs);	
			ArrayList<Recipe> tempRecipeList=this.getRecipesForDocIDs(docIDs);
			ingredientRecipeMap.put(ingredientID, tempRecipeList);
		}
		
		ArrayList<String> docIDList=new ArrayList<String>();
		docIDList.addAll(finalDocIDs);
		ArrayList<Recipe> commonRecipeList=this.getRecipesForDocIDs(docIDList);
		if(docIDList.size()<RESULT_SIZE){
			for(int i=0;i<commonRecipeList.size();i++){
				recipeList.add(commonRecipeList.get(i));
			}
			//int remaining=RESULT_SIZE-recipeList.size();
		}
		else{		
			for(int i=0;i<RESULT_SIZE;i++){
				recipeList.add(commonRecipeList.get(i));
			}
		}		
		
		
		return recipeList;
	}

	
}
