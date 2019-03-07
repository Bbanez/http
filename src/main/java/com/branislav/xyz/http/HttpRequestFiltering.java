package com.branislav.xyz.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.branislav.xyz.http.error.HttpError;
import com.branislav.xyz.http.error.HttpErrorEvent;
import com.branislav.xyz.http.error.HttpErrorFactory;

public class HttpRequestFiltering extends GenericFilterBean {
	
private static Logger logger = LoggerFactory.getLogger(HttpRequestFiltering.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpError error = HttpErrorFactory.getInstance(HttpRequestFiltering.class, res);
		if( ! isRequestValid(req, res, error) )	{
			return;
		}
		chain.doFilter(request, response);
	}

	public static List<String> allowedDomains = new ArrayList<>(Arrays.asList(
			"localhost"
			));
	
	public static List<String> allowedUrls = new ArrayList<>(Arrays.asList(
			"http://localhost:8080"
			));
	
	public static List<String> allowedSchemes = new ArrayList<>(Arrays.asList(
			"http",
			"https"
			));
	
	public static List<Integer> allowedPorts = new ArrayList<>(Arrays.asList(
			80,
			443,
			8080
			));
	
	public static List<String> flagedChars = new ArrayList<>(Arrays.asList(
			"<", ">"
			));
	
	public static boolean isDomainAllowed(String domain)	{
		Optional<String> externalDomain = HttpRequestFiltering.allowedUrls
				.stream()
				.filter(e -> e.equals(domain))
				.findFirst();
		if( externalDomain.isPresent() )	{
			return true;
		}
		return false;
	}
	
	public static boolean isRequestValid(HttpServletRequest request, 
			HttpServletResponse response, HttpError error)	{
		logger.info(request.getMethod() + ": " + request.getServletPath());
		Optional<String> scheme = HttpRequestFiltering.allowedSchemes
				.stream()
				.filter(e -> e.equals(request.getScheme()))
				.findFirst();
		if( !scheme.isPresent() )	{
			error.occured(new HttpErrorEvent(
					HttpServletResponse.SC_FORBIDDEN,
					"Scheme `" + request.getScheme() + "` is not allowed."
					));
			return false;
		}
		Optional<String> server = HttpRequestFiltering.allowedDomains
				.stream()
				.filter(e -> e.equals(request.getServerName()))
				.findFirst();
		if(!server.isPresent())	{
			error.occured(new HttpErrorEvent(
					HttpServletResponse.SC_FORBIDDEN,
					"Server `" + request.getServerName() + "` is not on whitelist."
					));
			return false;
		}
		Optional<Integer> port = HttpRequestFiltering.allowedPorts
				.stream()
				.filter(e -> e == request.getServerPort())
				.findFirst();
		if(!port.isPresent())	{
			error.occured(new HttpErrorEvent(
					HttpServletResponse.SC_FORBIDDEN,
					"Port `" + request.getServerPort() + "` is not on whitelist."
					));
			return false;
		}
		logger.info("Connection allowed: " + scheme.get() + "://" 
				+ server.get() + ":" + port.get());
		return true;
	}
	
	public static boolean isContainingInjections(String body, HttpHeaders headers,
			HttpQueries queries, HttpServletResponse response,
			HttpError error)	{
		for(String s : HttpRequestFiltering.flagedChars)	{
			if(body != null)	{
				if(body.contains(s))	{
					error.occured(new HttpErrorEvent(
							HttpServletResponse.SC_FORBIDDEN,
							"Flagged body. Contains `" + s + "`. Request blocked."
							));
					return false;
				}
			}
			if(headers != null && headers.get() != null)	{
				for(HttpHeader h : headers.get())	{
					if(h.getValue().contains(s))	{
						error.occured(new HttpErrorEvent(
								HttpServletResponse.SC_FORBIDDEN,
								"Flagged header `" + h + "`. Request blocked."
								));
						return false;
					}
				}
			}
			if(queries != null && queries.get() != null)	{
				for(HttpQuery q : queries.get())	{
					if(q.getValue().contains(s))	{
						error.occured(new HttpErrorEvent(
								HttpServletResponse.SC_FORBIDDEN,
								"Flagged query `" + q + "`. Request blocked."
								));
						return false;
					}
				}
			}
		}
		return true;
	}
}
