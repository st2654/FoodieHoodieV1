package edu.nyu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RecipeParser {
	

	// Method to parse the page and extract the data
	
    public Recipe parsePage(Document doc,Recipe recipe) throws IOException{
        recipe.setRecipeName(parseRecipeTitle(doc));
        recipe.setRecipeYield(""+parseServing(doc));
        recipe.setRecipeIngredients(parseIngredients(doc));
        recipe.setRecipeSteps(parseRecipeSteps(doc));
        String[] time=parseTime(doc).split(",");
        if(time.length==2){
        	recipe.setRecipePrepTime(time[0]);
        	recipe.setRecipeCookTime(time[1]);
        }
        return recipe;

    }

    // Method to parse the recipe servings
    
    public int parseServing(Document doc){
        String totalServing="";
        int serving=0;
        try{
        	Elements list1=doc.getElementsByAttributeValue("itemprop", "recipeyield");
        	if(list1.size()>0)
        		totalServing=list1.get(0).text().replaceAll("\\D+","");
        	else{
        		Elements list=doc.getElementsByAttributeValue("itemprop", "yield");
        		totalServing=list.get(0).text().replaceAll("\\D+","");
        	}
        	serving=Integer.parseInt(totalServing);
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return serving;
    }
    
    // Method to parse the cook and preparation time
    
    public String parseTime(Document doc){
        String totalTime="";
        String str=doc.text();
        Elements list1=doc.getElementsByAttributeValue("itemprop", "preptime");
        Elements list2=doc.getElementsByAttributeValue("itemprop", "cooktime");
        if(list1.size()>0 && list2.size()>0){
        	String s1=list1.get(0).attr("datetime").replaceAll("\\D+", "");
        	String s2=list2.get(0).attr("datetime").replaceAll("\\D+", "");
        	totalTime=s1+","+s2;
        	
        }
        else{
        Pattern p=Pattern.compile("(?i)\\d+\\s(mins|hrs|minutes|hours|hr|min)");
        Matcher m = p.matcher(str);
        	if(m.find()){
        		totalTime="0,"+m.group().replaceAll("\\D+", "");
        	}
        }
        System.out.println(totalTime);
        return totalTime;

    }
    
    // Method to parse the recipe steps 
    
    public ArrayList<String> parseRecipeSteps(Document doc){
        ArrayList<String> recipeSteps=new ArrayList<String>();
        Elements list1=doc.getElementsByAttributeValue("itemprop", "recipeinstructions");
        for(Element e : list1){
            Elements child=e.getElementsByTag("li");
            if(child.size()==0){
                System.out.println(e.text());
                recipeSteps.add(e.text());
            }
            else {
                for (Element e2 : child){
                    recipeSteps.add(e.text());
                    System.out.println(e2.text());
                }

            }
        }
        Elements list2=doc.getElementsByAttributeValue("itemprop", "instructions");
        for(Element e : list2){
            Elements child=e.getElementsByTag("li");
            if(child.size()==0){
                recipeSteps.add(e.text());
            }
            else {
                for (Element e2 : child){
                    recipeSteps.add(e2.text());
                }
            }
        }
        return recipeSteps;

    }

    
    // Method to parse the ingredients from the page
    
    public ArrayList<Ingredient> parseIngredients(Document doc){
        ArrayList<Ingredient> ingredientList=new ArrayList<Ingredient>();
        Elements list2=doc.getElementsByAttributeValue("itemprop", "ingredients");
        Elements list1=doc.getElementsByAttributeValue("itemprop", "ingredient");
        for(Element j:list1){
            ingredientList.add(getIngredientFromString(j.text()));
        }
        for(Element j:list2){
            ingredientList.add(getIngredientFromString(j.text()));
        }
        return ingredientList;
    }

    // Method to parse the ingredient from the string
    
    public Ingredient getIngredientFromString(String str){
    	Ingredient i=new Ingredient();
          String pattern1="(\\d+(/\\d+)?\\s[a-z]+)\\s(.+)";
          String pattern2="(.+)\\s((\\d+(/\\d+)?)\\s(.+)$)";
          Pattern r = Pattern.compile(pattern1);
          Pattern r2 = Pattern.compile(pattern2);
          Matcher m = r.matcher(str);
          Matcher m2=r2.matcher(str);
          if (m.find( )) {
              if(m.group(1)!=null){
                  i.setUnit(m.group(1));
              }
              if(m.group(3)!=null){
                  i.setName(m.group(3));
              }

          }
          else if(m2.find()){
              if(m.group(2)!=null){
                  i.setUnit(m.group(2));
              }
              if(m.group(2)!=null){
                  i.setName(m.group(1));
              }
          }
          else{
              i.setName(str);
          }
          return i;
    }
    
    // Method to parse the title of the recipe
    
    public String parseRecipeTitle(Document doc){
        String recipeName="";
        Elements title=doc.getElementsByTag("title");
        recipeName=title.get(0).text().split("- |\\.|\\|")[0];
        return recipeName;
    }

}