package com.branislav.xyz.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpSender {

	private final String USER_AGENT = "Mozilla/5.0";
	private String response;
	
	private int status;

	public boolean get(String url, HttpHeaders headers,
			HttpQueries queries)	{
		URL endpoint = null;
		if(queries != null && queries.get().size() > 0)	{
			url += "?";
			int i = 0;
			for(HttpQuery q : queries.get())	{
				if(i != 0)	url += "&";
				try {
					url += URLEncoder.encode(q.getKey(), "UTF-8") + "=" 
							+ URLEncoder.encode(q.getValue(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
		try {
			endpoint = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) endpoint.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", USER_AGENT);
			if(headers != null && headers.get().size() > 0)	{
				for(HttpHeader h : headers.get())	{
					conn.setRequestProperty(h.getKey(), h.getValue());
				}
			}
			int responseCode = conn.getResponseCode();
			status = responseCode;
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(conn.getInputStream(), "UTF8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			this.response = response.toString();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean post(String url, HttpHeaders headers,
			HttpQueries queries, String body)	{
		URL endpoint = null;
		if(queries != null && queries.get().size() > 0)	{
			url += "?";
			int i = 0;
			for(HttpQuery q : queries.get())	{
				if(i != 0)	url += "&";
				try {
					url += URLEncoder.encode(q.getKey(), "UTF-8") + "=" 
							+ URLEncoder.encode(q.getValue(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
		try {
			endpoint = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) endpoint.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", USER_AGENT);
			if(headers != null && headers.get().size() > 0)	{
				for(HttpHeader h : headers.get())	{
					conn.setRequestProperty(h.getKey(), h.getValue());
				}
			}
			if( body != null)	{
				OutputStream os = conn.getOutputStream();
				os.write(body.getBytes());
				os.flush();
			}
			int responseCode = conn.getResponseCode();
			status = responseCode;
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(conn.getInputStream(), "UTF8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			this.response = response.toString();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendPost(String urlConnection, String dataAsJson) 
			throws Exception	{
		try {

			URL url = new URL(urlConnection);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(dataAsJson.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream()), "UTF8"));

			this.response = "";
			System.out.println("Output from Server .... \n");
			System.out.println("Code: " + conn.getResponseCode());
			while ((this.response += br.readLine()) != null);
			
			System.out.println(this.response);

			status = conn.getResponseCode();
			conn.disconnect();
			
			if(status == 200)	{
				return true;
			}else	{
				return false;
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
			
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			
			return false;
		 }
	}
	
	public int getStatus()	{
		return status;
	}
	
	public String getResponse()	{
		return response;
	}
}
