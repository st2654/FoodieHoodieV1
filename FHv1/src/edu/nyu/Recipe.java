package edu.nyu;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;


// Recipe class

@XmlRootElement
public class Recipe {

    private int id;
    private String recipeName;
    private String recipeCookTime;
    private String recipePrepTime;
    private String recipeType;
    private String url;
    private String author;
   

	private String recipeYield;
	private ArrayList<String> recipeSteps;
    private ArrayList<Ingredient> recipeIngredients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeCookTime() {
        return recipeCookTime;
    }

    public void setRecipeCookTime(String recipeCookTime) {
        this.recipeCookTime = recipeCookTime;
    }

    public String getRecipePrepTime() {
        return recipePrepTime;
    }

    public void setRecipePrepTime(String recipePrepTime) {
        this.recipePrepTime = recipePrepTime;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}
	public String getRecipeYield() {
		return recipeYield;
	}

	public void setRecipeYield(String recipeYield) {
		this.recipeYield = recipeYield;
	}


	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
    public ArrayList<String> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(ArrayList<String> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }
    public ArrayList<Ingredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
    
    
}
