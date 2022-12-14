package com.tvv.web.command;

import com.tvv.service.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class for servlet command. Use PEST function and GET function
 */
public abstract class Command implements Serializable {	

	public abstract void executePost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException;

	public abstract void executeGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;
	
	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}