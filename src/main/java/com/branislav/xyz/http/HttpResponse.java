package com.branislav.xyz.http;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpResponse {

	public static boolean addBody(Object object, HttpServletResponse response)	{
		try {
			response.setHeader("Content-Type", "application/json; charset=UTF-8");
			response.getWriter().write(
					new ObjectMapper().writeValueAsString(object));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean addBody(List<Object> objects, HttpServletResponse response)	{
		try {
			response.setHeader("Content-Type", "application/json");
			response.getWriter().write(
					new ObjectMapper().writeValueAsString(objects));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
