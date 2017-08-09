package com.agyletime.rostering.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Utils {
	
	public static boolean validateUrl(String uri) {
	    final URL url;
	    try {
	        url = new URL(uri);
	    } catch (Exception e1) {
	        return false;
	    }
	    return "http".equals(url.getProtocol()) || "https".equals(url.getProtocol());
	}

	public static void sendPost(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con ; 
		if(url.startsWith("http")) {
			con = (HttpURLConnection) obj.openConnection();
		}else {
			con = (HttpsURLConnection) obj.openConnection();
		}
		

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
}