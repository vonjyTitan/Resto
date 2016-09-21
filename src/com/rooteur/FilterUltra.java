package com.rooteur;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilterUltra implements Filter{
	 private FilterConfig config = null;

	    public void init(FilterConfig config) throws ServletException {
	        this.config = config;
	        config.getServletContext().log("Initializing SessionCheckerFilter");
	    }

	    public void doFilter(ServletRequest req, ServletResponse res,
	                         FilterChain chain)
	            throws ServletException, IOException {

	        HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) res;

	        String path=request.getServletPath();
	        try{
	        	String[] tranches=path.split("/");
	        	if(tranches.length==2)
	        	{
	        		if(!tranches[1].contains(".") && tranches[1].contains("-")){
	        			Serveur.run(request, response,tranches[1]);
		        		return;
	        		}
	        		
	        	}
	        }
	        catch(Exception ex){
	        	ex.printStackTrace();
	        }
	        
	        chain.doFilter(req, res);
	    }

	    public void destroy() {
	        config.getServletContext().log("Destroying SessionCheckerFilter");
	    }
}
