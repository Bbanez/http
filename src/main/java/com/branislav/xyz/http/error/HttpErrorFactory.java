package com.branislav.xyz.http.error;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.branislav.xyz.http.HttpResponseMessage;

public class HttpErrorFactory {

	private HttpErrorFactory()	{
		throw new AssertionError();
	}
	
	public static HttpError getInstance(Class<?> invocationClass,
			HttpServletResponse response)	{
		return new HttpError() {
			@Override
			public void occured(HttpErrorEvent e) {
				Logger logger = LoggerFactory.getLogger(invocationClass);
				logger.error(e.getMessage());
				HttpResponseMessage.simpleResponseMessage(
						"message", 
						e.getMessage(), 
						response);
				response.setStatus(e.getStatus());
			}
		};
	}
}
