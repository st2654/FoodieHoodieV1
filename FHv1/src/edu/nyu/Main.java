package edu.nyu;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Crawler c=new Crawler();
		//c.crawl();
		try{
			c.mainCrawler();
			//c.getSeeds();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
