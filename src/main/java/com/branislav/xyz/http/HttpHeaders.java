package com.branislav.xyz.http;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpHeaders {

	private List<HttpHeader> entities;
	
	public HttpHeaders()	{
		entities = new ArrayList<>();
	}

	public HttpHeaders(List<HttpHeader> entities) {
		super();
		this.entities = entities;
	}
	
	public HttpHeaders(HttpServletRequest request)	{
		entities = new ArrayList<>();
		if(request.getHeaderNames() != null)	{
			Enumeration<String> headerKeys = request.getHeaderNames();
			while(headerKeys.hasMoreElements())	{
				String headerKey = headerKeys.nextElement();
				if( ! headerKey.equals("cookie") ) {
					entities.add(new HttpHeader(
							headerKey, 
							request.getHeader(headerKey)));
				}
			}
		}
	}
	
	public List<HttpHeader> get()	{
		return entities;
	}
	
	public Optional<HttpHeader> get(String key)	{
		if(entities != null)	{
			return entities.stream().filter(e -> e.getKey().equals(key)).findFirst();
		}
		return Optional.ofNullable(null);
	}
	
	public void add(HttpHeader entity)	{
		if(entities == null)	{
			entities = new ArrayList<>();
		}
		entities.add(entity);
	}
	
	public void add(HttpServletResponse response)	{
		if(entities != null)	
			entities.forEach(e -> response.setHeader(e.getKey(), e.getValue()));
	}
	
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
