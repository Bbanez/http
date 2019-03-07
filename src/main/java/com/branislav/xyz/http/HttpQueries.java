package com.branislav.xyz.http;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpQueries {

	private List<HttpQuery> entities;
	
	public HttpQueries()	{
		entities = new ArrayList<>();
	}

	public HttpQueries(List<HttpQuery> entities) {
		super();
		this.entities = entities;
	}
	
	public HttpQueries(HttpServletRequest request)	{
		entities = new ArrayList<>();
		if(request.getQueryString() != null)	{
			String[] parts = request.getQueryString().split("&");
			for(String part : parts)	{
				String[] query = part.split("=");
				if(query.length == 2)	{
					try {
						query[0] = URLDecoder.decode(query[0], "UTF-8");
						query[1] = URLDecoder.decode(query[1], "UTF-8");
						entities.add(new HttpQuery(query[0], query[1]));
					} catch (Exception e) {
            e.printStackTrace();
					}
				}
			}
		}
	}
	
	public List<HttpQuery> get()	{
		return entities;
	}
	
	public Optional<HttpQuery> get(String key)	{
		if(entities != null)	{
			return entities.stream().filter(e -> e.getKey().equals(key)).findFirst();
		}
		return Optional.ofNullable(null);
	}
	
	public void add(HttpQuery entity)	{
		if(entities == null)	{
			entities = new ArrayList<>();
		}
		entities.add(entity);
	}
	
	public String toUrl()	{
		String[] s = new String[entities.size()];
		int i = 0;
		for(HttpQuery e : entities)	{
			s[i] = e.getKey() + "=" + e.getValue();
			i++;
		}
		return ("?" + String.join("&", s));
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
