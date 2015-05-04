package edu.nyu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {
	String url="";

    public void parsePage() throws IOException{
    	
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Recipe r=new Recipe();
        //System.out.println(doc.text()+"\n\n\n\n\n\n\n\n");
        r.setURL(url);
        System.out.println("------------------------------------------------");
        r.setRecipeName(parseRecipeTitle(doc));
        System.out.println("Name : "+r.getRecipeName());
        //r.setRecipeSteps(parseRecipeSteps(doc));
        r.setRecipeYield(""+parseServing(doc));
        System.out.println("------------------------------------------------");
        System.out.println("Yield : "+r.getRecipeYield());
        
        System.out.println("------------------------------------------------");
        r.setRecipeIngredients(parseIngredients(doc));
        System.out.println("------------------------------------------------");
        r.setRecipeSteps(parseRecipeSteps(doc));
        System.out.println("------------------------------------------------");
        String[] time=parseTime(doc).split(",");
        if(time.length==2){
        	r.setRecipePrepTime(time[0]);
        	r.setRecipeCookTime(time[1]);
        }
        

    }

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
    public String parseTime(Document doc){
        String totalTime="";
        String str=doc.text();
        Elements list1=doc.getElementsByAttributeValue("itemprop", "preptime");
        Elements list2=doc.getElementsByAttributeValue("itemprop", "cooktime");
        if(list1.size()>0 && list2.size()>0){
        	String s1=list1.get(0).attr("datetime").replaceAll("\\D+", "");
        	String s2=list2.get(0).attr("datetime").replaceAll("\\D+", "");
        	String s3=list1.get(0).attr("content").replaceAll("\\D+", "");
        	String s4=list2.get(0).attr("content").replaceAll("\\D+", "");
        	if(s1.length()>0 && s2.length()>0)
        		totalTime=s1+","+s2;
        	else{
        		totalTime=s3+","+s4;
        	}
        	
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
    public ArrayList<String> parseRecipeSteps(Document doc){
        ArrayList<String> recipeSteps=new ArrayList<String>();
        //Elements list1=doc.getElementsByTag("ul");
        //Elements list2=doc.getElementsByTag("ol");
        Elements list1=doc.getElementsByAttributeValue("itemprop", "recipeinstructions");
        //System.out.println(list1.size());
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
            //System.out.println(e.text()+"***");
        }
        Elements list2=doc.getElementsByAttributeValue("itemprop", "instructions");
        //System.out.println(list2.size());
        for(Element e : list2){
            Elements child=e.getElementsByTag("li");
            if(child.size()==0){
                System.out.println(e.text());
                recipeSteps.add(e.text());
            }
            else {
                for (Element e2 : child){
                    System.out.println(e2.text());
                    recipeSteps.add(e2.text());
                }
            }
        }
        return recipeSteps;

    }

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

    public Ingredient getIngredientFromString(String str){
    	Ingredient i=new Ingredient();
        /*  String[] list=j.text().split("\\d+(/\\d+)?\\s[a-z]+");
          System.out.println(j.text());
          if(list.length==1) {
              i.setName(j.text());
              System.out.println("&&");
          }
          else if (list.length==2){
              i.setName(list[1].trim());
              System.out.println("**"+list[0]);
              System.out.println("##"+list[1]);

          }
          */
          String pattern1="(\\d+(/\\d+)?\\s[a-z]+)\\s(.+)";
          String pattern2="(.+)\\s((\\d+(/\\d+)?)\\s(.+)$)";
          Pattern r = Pattern.compile(pattern1);
          Pattern r2 = Pattern.compile(pattern2);
          // Now create matcher object.
          Matcher m = r.matcher(str);
          Matcher m2=r2.matcher(str);
          if (m.find( )) {
              if(m.group(1)!=null){
                  i.setUnit(m.group(1));
              }
              if(m.group(3)!=null){
                  i.setName(m.group(3));
                  System.out.println(i.getName());
              }

          }
          else if(m2.find()){
              if(m.group(2)!=null){
                  i.setUnit(m.group(2));
              }
              if(m.group(2)!=null){
                  i.setName(m.group(1));
                  System.out.println(i.getName());
              }
          }
          else{
              i.setName(str);
          }
          return i;
    	
    }
    
    public String parseRecipeTitle(Document doc){
        String recipeName="";
        Elements title=doc.getElementsByTag("title");
        recipeName=title.get(0).text().split("- |\\.|\\|")[0];
        return recipeName;
    }


    

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }


}


//private static String trim(String s, int width) {
//if (s.length() > width)
//  return s.substring(0, width-1) + ".";
//else
//  return s;
//}

/*
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        Elements list1=doc.getElementsByTag("ul");
        Elements list2=doc.getElementsByTag("ol");
        //System.out.println( doc.text());

 */
/* for(Element e : list1){
                Elements e2 = e.getElementsByTag("li");
                for(Element e3:e2){
                    System.out.println(e3.text());
                }
                //System.out.println(e.text()+"***");
            }

            for(Element e:list2){
                Elements e2 = e.getElementsByTag("li");
                for(Element e3:e2){
                    System.out.println(e3.text());
                }
                //System.out.println(e.text()+"***");
            }


            print("\nMedia: (%d)", media.size());
            for (Element src : media) {
                if (src.tagName().equals("img"))
                    print(" * %s: <%s> %sx%s (%s)",
                            src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                            trim(src.attr("alt"), 20));
                else
                    print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
            }

            print("\nImports: (%d)", imports.size());
            for (Element link : imports) {
                print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
            }

            print("\nLinks: (%d)", links.size());
            for (Element link : links) {
                print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
            }*/