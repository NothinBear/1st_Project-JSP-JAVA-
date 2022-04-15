package com.estimulo.system.common.servlet.controller;


import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

import com.estimulo.system.common.servlet.ModelAndView;

public abstract class AbstractController implements Controller {	//공통된? 실행 시 계속 작업하는 건 여기에 작성

	 public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response){
		 
		  System.out.println("		@ AbstractController handleRequest");
	      String controllerFullName = this.getClass().getName();
//	    	 	 	풀네임 = this.getClass().getName();	
// 					패키지명까지 컨트롤러에 다구해오는로직.
	  
	      String controllerShortName = controllerFullName.substring(controllerFullName.lastIndexOf(".")+1);	  
	  
	  	  System.out.println("		@ ControllerFullName : "+controllerFullName);
	      System.out.println("		@ ControllerShortName : "+controllerShortName);
	    
	      
	      response.setHeader("Pragma", "no-cache");
	      response.setHeader("Cache-Control", "no-cache");
	      response.addHeader("Cache-Control", "no-store");
	      System.out.println("		@ set & add header");
	      System.out.println("		@ call handleRequestInternal");
	    
	      
	      ModelAndView modelAndView  = handleRequestInternal(request, response);
		
	      //UrlFilenameViewController	 .     handleRequestInternal();
	 
	      return modelAndView;


	 }
	 public abstract ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response);
}
