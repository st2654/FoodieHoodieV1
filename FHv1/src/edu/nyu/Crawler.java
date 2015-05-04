package edu.nyu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	HashMap<String,Integer> setOfURLs;
	ArrayList<String> seeds;
	int docID;
	
	// Constructor to initialize the Crawler
	
	public Crawler(){
		setOfURLs=new HashMap<String,Integer>();
		seeds=new ArrayList<String>();
		ArrayList<String> seedURLs=getSeeds();
		for(String url:seedURLs){
			seeds.add(url);
			setOfURLs.put(url, 0);
		}
		RecipeDAO r=new RecipeDAO();
		ArrayList<String> crawledURLs=r.getCrawledURLs();
		for(String url:crawledURLs){
			seeds.add(url);
			setOfURLs.put(url, 1);
		}
		int temp=r.getAutoIncrementNumber("foodiehoodie", "recipe-table");
		docID=temp==0?1:temp;
		System.out.println(docID);
	}

	
	// Method to add URLs to the current pool
	
	public void addURLsToPool(ArrayList<String> urls){
		for(String url:urls){
			
			String normalizedURL= url; //URLUtils.normalizeURL(url);
			if(!setOfURLs.keySet().contains(normalizedURL)){
				setOfURLs.put(url, 0);
				seeds.add(url);
			}
		}
	}
	
	// Method to crawl the given url
	
	public Document crawlURL(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	// Method to start the main Crawling
	
	public void mainCrawler() throws Exception{
		
		while(seeds.size()>0){
			String URLToCrawl=seeds.get(0);
			seeds.remove(0);
			int status=setOfURLs.get(URLToCrawl);
			if(status==0){
				System.out.println("Crawling :"+URLToCrawl);
				try{
					Document doc=crawlURL(URLToCrawl);
					ArrayList<String> urlsFromCrawledPage=getURLs(doc,URLToCrawl);					
					setOfURLs.put(URLToCrawl, 1);
					addURLsToPool(urlsFromCrawledPage);
					System.out.println("size:"+setOfURLs.size());
					processPage(doc,URLToCrawl);
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println("Error in processing : "+URLToCrawl);
				}
			}
		}
	}
	
	// Method to parse the page and save it for future use
	
	public void processPage(Document doc,String URL){
		if(checkDocumentData(doc)){
			try{
				RecipeDAO r=new RecipeDAO();
				int currentDocID=r.getAutoIncrementNumber("foodiehoodie", "recipe-table");
				writeToFile(doc, currentDocID);
				writeDocIDToURLMapping(currentDocID, URL);
				Recipe recipe=new Recipe();
				recipe.setId(currentDocID);
				recipe.setURL(URL);
				recipe.setAuthor(URLUtils.getHostName(URL));
				RecipeParser parser=new RecipeParser();
				recipe=parser.parsePage(doc, recipe);
				r.insertRecipe(recipe);
				r.insertRecipeInQueue(recipe);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	// Method to write the downloaded page to the file
	
	public void writeToFile(Document doc,int docID){
		try{
			File fout = new File("Downloads/Doc_"+docID+".html");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write(doc.outerHtml());
			bw.close(); 
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Method to check page is recipe page or not
	
	public boolean checkDocumentData(Document doc){
		boolean returnValue=false;
		
		Elements yieldList1=doc.getElementsByAttributeValue("itemprop", "recipeyield");
        Elements yieldList2=doc.getElementsByAttributeValue("itemprop", "yield");
        Elements instructionsList1=doc.getElementsByAttributeValue("itemprop", "recipeinstructions");
        Elements instructionsList2=doc.getElementsByAttributeValue("itemprop", "instructions");
        Elements ingedientsList1=doc.getElementsByAttributeValue("itemprop", "ingredients");
        Elements ingedientsList2=doc.getElementsByAttributeValue("itemprop", "ingredient");
        boolean yieldFlag=false;
        boolean instructionFlag=false;
        boolean ingedientsFlag=false;
        if(yieldList1.size()>0 || yieldList2.size()>0)
        	yieldFlag=true;
        if(instructionsList1.size()>0||instructionsList2.size()>0)
        	instructionFlag=true;
        if(ingedientsList1.size()>0 || ingedientsList2.size()>0)
        	ingedientsFlag=true;
		
        if(yieldFlag || instructionFlag || ingedientsFlag)
        	returnValue=true;
        return returnValue;
	}
	
	// Method to write docID to URL mapping to the file
	
	public void writeDocIDToURLMapping(int docID,String URL){
		try{
			File fout = new File("Downloads/DocID to URL Mappings.txt");
			FileWriter fileWritter = new FileWriter(fout.getAbsolutePath(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(docID+","+URL+"");
			bufferWritter.newLine();
			bufferWritter.close();
			System.out.println("Writing file..  DocID:"+docID+"    URL : "+URL );
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	// Method to get URLs from the downloaded page
	
	public ArrayList<String> getURLs(Document doc,String currentURL) throws Exception{
		ArrayList<String> urls=new ArrayList<String>();
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String url=link.attr("abs:href").trim();
			if(url!=null && url.length()>0 && getDomainName(currentURL).equalsIgnoreCase(getDomainName(url))){
				urls.add(url);
			}
        }
		return urls;
	}
	
	// Method to get the domain name
	
	public static String getDomainName(String url) throws MalformedURLException{
	    if(!url.startsWith("http") && !url.startsWith("https")){
	         url = "http://" + url;
	    }        
	    URL netUrl = new URL(url);
	    String host = netUrl.getHost();
	    if(host.startsWith("www")){
	        host = host.substring("www".length()+1);
	    }
	    return host;
	}
	
	// Method to get the list of URL
	
	public ArrayList<String> getSeeds(){
		ArrayList<String> seeds=new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader("resources/seeds.txt"));
		    String line;
		    while ((line=br.readLine()) != null) {
		    	seeds.add(line);
		    	System.out.println(""+line);
	        }
		    br.close();
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return seeds;
	}
}