package com.foodiehoodie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.foodiehoodie.dto.DocIngredientRel;
import com.foodiehoodie.util.database.QueryProcessor;

public class ProcessQueue {

    public void processQueue(){
        try {
        	
            //Get List of all available ingredient
            Set<String> availableIngredient = QueryProcessor.getListofAvailableIngredient();

            //Get the list of new docIds and ingredients to get indexed
            ArrayList<DocIngredientRel> list =  QueryProcessor.getProcessQueueDocIngredientIds();
            for(DocIngredientRel temp:list){
                String[] recipeIngredientArray = temp.getIngredientList().split(",");

                //if(!availableIngredient.isEmpty()){
                   
                Set<String> difference =new HashSet<>(Arrays.asList(recipeIngredientArray));
                Set<String> restOfAvailableIngredient=new HashSet<>(Arrays.asList(recipeIngredientArray));
                difference.removeAll(availableIngredient);
                System.out.println("New Ingredient: " + difference);
                
                QueryProcessor.insertIntoNewIngredient(temp.getDocId(), difference);
                QueryProcessor.deleteDocIngredientIdsFromQueue(Integer.parseInt(temp.getDocId()));

                restOfAvailableIngredient.removeAll(difference);
                System.out.println("Already Available Ingredient : " + restOfAvailableIngredient);

                for (String ingredient:restOfAvailableIngredient){
                    QueryProcessor.ingredientWeightPlus(ingredient); //Increase weight by 1 for available Ingredient
                    //Add DocId and Ingredient id relationship
                    QueryProcessor.addDocidAndIngredientRelationship(Integer.parseInt(temp.getDocId()),ingredient);
                }
                restOfAvailableIngredient.clear();
                difference.clear();
                
            }
            list.clear();
            availableIngredient.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
