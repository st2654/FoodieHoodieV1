package com.foodiehoodie.util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.foodiehoodie.dto.DocIngredientRel;

public class QueryProcessor {

	// Method to get the list of pending ingredients and document id to generate the index
    public static ArrayList<DocIngredientRel> getProcessQueueDocIngredientIds() throws SQLException {
        Connection dbConn = null;
        ArrayList<DocIngredientRel> docIngredientRels = new ArrayList<>();
        docIngredientRels.clear();
        dbConn = new DatabaseConnection().getConnection();
        String sql = "select * from `doc-ingredient-process-queue`";
        Statement st = dbConn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            DocIngredientRel temp = new DocIngredientRel();
            temp.setDocId(rs.getString("docid"));
            temp.setIngredientList(rs.getString("ingredients").toLowerCase());
            docIngredientRels.add(temp);
        }
        rs.close();
        st.close();
        dbConn.close();

        return docIngredientRels;
    }

    
    // Method to get ingredient id from the ingredient name

    public static String getIngredientID(String ingredient) throws SQLException{
    	String ingredientID="";
    	Connection dbConn = null;
        dbConn = new DatabaseConnection().getConnection();
        String sql = "select id from `ingredients-main` where name=?";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1, ingredient);
        ResultSet rs=pst.executeQuery();
        if(rs!=null){
        	while(rs.next()){
        		ingredientID=rs.getString(1);
        		break;
        	}
        }
        pst.close();
        dbConn.close();
        return ingredientID;
    }
    
    // Method to delete the processed document id from the pending queue
    
    public static void deleteDocIngredientIdsFromQueue(int docId) throws SQLException {
        Connection dbConn = null;
        dbConn = new DatabaseConnection().getConnection();
        String sql = "delete from `doc-ingredient-process-queue` where docid=?";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setInt(1, docId);
        pst.executeUpdate();
        pst.close();
        dbConn.close();
    }

    
    //  Method to delete the given ingredient from the new ingredient mapping table
    
    public static void deleteNewIngredient(String ingredient) throws SQLException {
        Connection dbConn = null;
        dbConn = new DatabaseConnection().getConnection();
        String sql = "delete from `new-ingredient-w-doc` where ingredient=?";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1, ingredient.toLowerCase());
        pst.executeUpdate();
        pst.close();
        dbConn.close();
    }

    // Method to get the set of ingredients
    
    public static Set<String> getListofAvailableIngredient(String[] ingredientList) throws SQLException {
        Connection dbConn = null;
        Set<String> list = new HashSet<>();
        list.clear();
        dbConn = new DatabaseConnection().getConnection();
        StringBuilder inQuery = new StringBuilder();
        for(String ingredient:ingredientList)
            inQuery.append("'").append(ingredient.trim().toLowerCase()).append("'").append(",");
        inQuery.deleteCharAt(inQuery.length()-1); // removing last comma
        String sql = "select il.name as ingredient from `ingredients-main` as il where il.name in ("+inQuery.toString()+")";
        Statement st = dbConn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            list.add(rs.getString("ingredient").toLowerCase());
        }
        rs.close();
        st.close();
        dbConn.close();

        return list;
    }
    
    //Method to get all available ingredient
    
    public static Set<String> getListofAvailableIngredient() throws SQLException {
        Connection dbConn = null;
        Set<String> list = new HashSet<>();
        list.clear();
        dbConn = new DatabaseConnection().getConnection();
        String sql = "select name as ingredient from `ingredients-main`";
        Statement st = dbConn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            list.add(rs.getString("ingredient").toLowerCase());
        }
        rs.close();
        st.close();
        dbConn.close();

        return list;
    }

    //Get List of only popular ingredient
    
    public static HashMap<Integer,String> getAllIngredient() throws SQLException {
        Connection dbConn = null;
        HashMap<Integer,String> list = new HashMap<>();
        list.clear();
        dbConn = new DatabaseConnection().getConnection();
        String sql = "select il.id as id,il.name as ingredient from `ingredients-main` as il where same_name_id=0";
        Statement st = dbConn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            list.put(rs.getInt("id"), rs.getString("ingredient").toLowerCase());
        }
        rs.close();
        st.close();
        dbConn.close();

        return list;
    }

    //Key: Ingredient and Value is CSV of docIds
    
    public static HashMap<String,String> getListofNewIngredients() throws SQLException {
        Connection dbConn = null;
        HashMap<String,String> newIngredients = new HashMap<>();
        newIngredients.clear();
        dbConn = new DatabaseConnection().getConnection();
        String sql = "select docid,ingredient from `new-ingredient-w-doc` order by ingredient limit 100";
        Statement st = dbConn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
                String ingredientName = rs.getString("ingredient").toLowerCase();
                if(newIngredients.containsKey(ingredientName)){
                    String docId = newIngredients.get(ingredientName)+","+rs.getString("docid");
                    newIngredients.put(ingredientName,docId);
                }else{
                    newIngredients.put(ingredientName,rs.getString("docid"));
                }
        }
        rs.close();
        st.close();
        dbConn.close();

        return newIngredients;
    }

    //method to return docIds of the Ingredient
    
    public static ArrayList<Integer> getDocIdsNewIngredients(String ingredient) throws SQLException {
        Connection dbConn = null;
        ArrayList<Integer> docIds = new ArrayList<>();
        docIds.clear();
        dbConn = new DatabaseConnection().getConnection();
        String sql = "select docid from `new-ingredient-w-doc` where ingredient='"+ingredient+"'";
        Statement st = dbConn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            docIds.add(rs.getInt("docid"));
        }
        rs.close();
        st.close();
        dbConn.close();

        return docIds;
    }


    // Method to insert the ingredients which are not found in the main list
    
    public static void insertIntoNewIngredient(String docId, Set<String> difference) throws SQLException {
        Connection dbConn = null;
        dbConn = new DatabaseConnection().getConnection();
        String sql = "insert into `new-ingredient-w-doc`(docid,ingredient) VALUES (?,?)";
        for(String temp:difference){
            PreparedStatement pst = dbConn.prepareStatement(sql);
            pst.setString(1, docId);
            pst.setString(2, temp.toLowerCase());
            pst.executeUpdate();
            pst.close();
        }
        dbConn.close();
    }

    
    // Method to add weight to given ingredient
    
    public static void ingredientWeightPlus(String ingredient) throws SQLException {
        Connection dbConn = null;
        dbConn = new DatabaseConnection().getConnection();
        String sql = "update foodiehoodie.`ingredients-main` \n" +
                "set weight=weight+1\n" +
                "where name =?";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1,ingredient);
        pst.executeUpdate();
        pst.close();
        dbConn.close();
    }

    // Method to add new ingredient and process the list of the documents 
   
    public static void addAsMainIngredient(String ingredient) throws  SQLException{
        Connection dbConn = new DatabaseConnection().getConnection();
        String sql = "insert into `ingredients-main`(name) values(?) ";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1, ingredient.toLowerCase());
        pst.execute();
        pst.close();
        dbConn.close();
        ArrayList<Integer> docIds = getDocIdsNewIngredients(ingredient);
        for(int docId:docIds)
            addDocidAndIngredientRelationship(docId,ingredient);
        docIds.clear();
    }

    // Method to add new ingredient in the main ingredient table
    
    public static void addNewIngredient(String ingredient) throws SQLException{
    	Connection dbConn = new DatabaseConnection().getConnection();
        String sql = "insert into `ingredients-main`(name) values(?) ";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1, ingredient.toLowerCase());
        pst.execute();
        pst.close();
        dbConn.close();
    }
    
    
    // Method to ass synonym to the main ingredient table  
    
    public static void addAsSynonymIngredient(String synonymIngredient,int mainIngredientId) throws  SQLException{
        Connection dbConn = new DatabaseConnection().getConnection();
        String sql = "insert into `ingredients-main`(name,same_name_id,weight) values(?,?,?) ";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1,synonymIngredient.toLowerCase());
        pst.setInt(2, mainIngredientId);
        pst.setInt(3, 1);
        pst.execute();
        pst.close();
        dbConn.close();
        addDocidAndIngredientRelationship(mainIngredientId,synonymIngredient);

    }
    
    // Method to new docid and ingredient  relationship to the table 
    
    public static int addDocIDToIngredientRelationship(int docID,String ingredientID) throws SQLException{
    	Connection dbConn = new DatabaseConnection().getConnection();
        String sql="insert into `doc-ingredient-relationship`(docid,ingredientId) values(?,?) ";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setInt(1, docID);
        pst.setInt(2,Integer.parseInt(ingredientID));
        return pst.executeUpdate();
    }

    // Method to add new relatuion in the table
    
    public static void addDocidAndIngredientRelationship(int docId,String ingredient) throws  SQLException{
        Connection dbConn = new DatabaseConnection().getConnection();
        String sql="select id from `ingredients-main` as il where il.name=?";
        PreparedStatement pst = dbConn.prepareStatement(sql);
        pst.setString(1, ingredient);
        ResultSet rs = pst.executeQuery();
        int ingredientId=0;
        if(rs.next()) ingredientId=rs.getInt("id");
        rs.close();
        pst.close();
        sql = "insert into `doc-ingredient-relationship`(docid,ingredientId) values(?,?) ";
        pst = dbConn.prepareStatement(sql);
        pst.setInt(1, docId);
        pst.setInt(2, ingredientId);
        pst.execute();
        pst.close();
        dbConn.close();
    }

}
