package com.branislav.xyz.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Branislav Vajagic - www.branislav.xyz
 * 
 * Store instances of Servlet Cookie and allow manipulation.
 */
public class HttpCookies {

	private List<Cookie> entities;
	
	/**
	 * Create an instance of an object with empty Cookie list.
	 */
	public HttpCookies()	{
		entities = new ArrayList<>();
	}

	/**
	 * Create an instance of an object and initialize Cookie list.
	 * @param entities - List of Cookie.
	 */
	public HttpCookies(List<Cookie> entities) {
		super();
		if(entities == null)	this.entities = new ArrayList<>();
		else this.entities = entities;
	}
	
	/**
	 * Create an instance of an object from the request.
	 * @param request - Servlet Request.
	 */
	public HttpCookies(HttpServletRequest request)	{
		entities = new ArrayList<>();
		if(request.getCookies() != null)	{
			for(Cookie e : request.getCookies())	{
				entities.add(e);
			}
		}
	}
	
	/**
	 * @return All available cookies.
	 */
	public List<Cookie> get()	{
		return entities;
	}
	
	/**
	 * Get a Cookie by name if exists.
	 * @param name - Wanted Cookie name/key.
	 * @return Optional Cookie.
	 */
	public Optional<Cookie> get(String name)	{
		if(entities != null)	{
			return entities.stream().filter(e -> e.getName().equals(name)).findFirst();
		}
		return Optional.ofNullable(null);
	}
	
	/**
	 * Add a new Cookie to a list.
	 * @param entity - Of type Cookie.
	 */
	public void add(Cookie entity)	{
		if(entities == null)	{
			entities = new ArrayList<>();
		}
		entities.add(entity);
	}
	
	/**
	 * Add all Cookies from the list to a Servlet response.
	 * @param response - Servlet Response
	 */
	public void add(HttpServletResponse response)	{
		if(entities != null)	
			entities.forEach(e -> response.addCookie(e));
	}
	
	/**
	 * @return JSON string of a Cookie list.
	 */
	public Optional<String> toJson()	{
		try {
			return Optional.of(
					new ObjectMapper().writeValueAsString(
							entities
							)
					);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(null);
	}

	@Override
	public String toString() {
		return "{entities:" + entities + "}";
	}

	
}
