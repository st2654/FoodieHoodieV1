package com.foodiehoodie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.foodiehoodie.util.database.QueryProcessor;

public class Controller extends javax.servlet.http.HttpServlet {
	
	
	// Method to handle the post request to add or delete the ingredients
    
	protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String[] main = request.getParameterValues("main");
        try {
            if(main!= null && main.length>0){
            	for (String mainStr : main) {
            		String changedName=request.getParameter(mainStr);
    
            		// If delete keyword presend delete the ingredients with their mappings
            		if(!changedName.equals("delete")){

                		String ingredientID=QueryProcessor.getIngredientID(changedName);
                	
                		//Get the list of the document ids
                		ArrayList<Integer> i=QueryProcessor.getDocIdsNewIngredients(mainStr);
                		
                		// Check if given ingredient present in the database or not 
                		if(ingredientID.length()>0){
                			for(int docID:i){
                				QueryProcessor.addDocIDToIngredientRelationship(docID, ingredientID);
                			}
                		}
                		else{
                		
                			// create new ingredient and add mappings to the database
                			QueryProcessor.addAsMainIngredient(changedName);
                			String newID=QueryProcessor.getIngredientID(changedName);
                			for(int docID:i){
                				QueryProcessor.addDocIDToIngredientRelationship(docID, newID);
                			}
                		}
                		QueryProcessor.deleteNewIngredient(mainStr);
            		}
            		else{
            			QueryProcessor.deleteNewIngredient(mainStr);
            		}
            	}
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        response.sendRedirect("index.jsp");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
    doPost(request,response);
    }
}
