package com.branislav.xyz.http;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

public class HttpResponseMessage {

	public static void simpleResponseMessage(String key, 
			String value, 
			HttpServletResponse response)	{
		response.setHeader("Content-Type", "applicaion/json");
		response.setHeader(key, value);
		try {
			response.getWriter().write("{\"" + key + "\":\"" + value + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void responseMessage(Map<String, String> data, 
			HttpServletResponse response)	{
		response.setHeader("Content-Type", "applicaion/json");
		if(data !=  null)	{
			String body = "{";
			int i = 0;
			for(Entry<String, String> e : data.entrySet())	{
				response.setHeader(e.getKey(), e.getValue());
				if(i != 0)	{
					body += ",\"" + e.getKey() + "\":\"" + e.getValue() + "\"";
				}else	{
					body += "\"" + e.getKey() + "\":\"" + e.getValue() + "\"";
				}
				i++;
			}
			body += "}";
			try {
				response.getWriter().write(body);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}