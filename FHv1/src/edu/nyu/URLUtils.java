package edu.nyu;

import java.net.URI;
import java.net.URL;

public class URLUtils {

	
	public static String normalizeURL(String URL){
		String normalizedURL="";
		System.out.println(URL);
		try{
			URL url = new URI(URL).normalize().toURL();
			String path = url.getPath().replace("/$", "");
			normalizedURL=url.getProtocol() + "://" + url.getHost()+path;
			//System.out.println(normalizedURL);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return normalizedURL;
	}
	
	public static String getHostName(String URL){
		String hostName="";
		try{
			URL url = new URI(URL).normalize().toURL();
			hostName=url.getHost();
			System.out.println(hostName);
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		return hostName;
	}
//	
	public static void main(String[] args){
		getHostName("http://allrecipes.com/Recipe/Chantals-New-York-Cheesecake/Detail.aspx?evt19=1&referringHubId=85&video=true");
//		URLUtils u=new URLUtils();
//		//u.normalizeURL("http://allrecipes.com/Recipe/Chantals-New-York-Cheesecake/Detail.aspx?evt19=1&referringHubId=85&video=true");
//		//u.normalizeURL("http://allrecipes.com/Recipe/Chantals-New-York-Cheesecake/Detail.aspx?evt19=1&referringHubId=85");
	}
	
}
