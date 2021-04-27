package org.gl.ceir.interfacepackage;

import org.springframework.http.ResponseEntity;

public interface HeaderInterface {
	public ResponseEntity<?> headers(String role);
	
}
