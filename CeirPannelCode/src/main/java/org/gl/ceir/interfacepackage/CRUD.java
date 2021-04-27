package org.gl.ceir.interfacepackage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

public interface CRUD {
	public ResponseEntity<?>  view( String role,String sourceType,Integer file,HttpServletRequest request,HttpSession session,Integer sessionFlag);
	public ResponseEntity<?> directives(String role,HttpSession session);
}
